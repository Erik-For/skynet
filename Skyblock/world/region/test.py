import os
import sys
import gzip
import shutil
import struct
from io import BytesIO
from pathlib import Path
from nbt import nbt  # Requires the 'NBT' package: pip install NBT

def process_region_file(region_path):
    """Process a single region file to remove all entities."""
    backup_path = region_path.with_suffix(region_path.suffix + '.backup')
    
    # Create backup first
    shutil.copy2(region_path, backup_path)
    print(f"Created backup: {backup_path}")
    
    # Open the region file
    with open(region_path, 'rb') as f:
        region_data = bytearray(f.read())
    
    modified = False
    
    # Parse the region file
    # First 4096 bytes: chunk location table (4 bytes per chunk)
    # Second 4096 bytes: chunk timestamp table (4 bytes per chunk)
    for chunk_x in range(32):
        for chunk_z in range(32):
            # Calculate the position in the location table
            loc_pos = 4 * (chunk_x + chunk_z * 32)
            
            # Get the offset and sector count
            offset = struct.unpack('>I', region_data[loc_pos:loc_pos+4])[0]
            if offset == 0:
                # Chunk doesn't exist
                continue
            
            # Calculate actual byte offset
            sector_offset = (offset >> 8) * 4096
            sector_count = offset & 0xFF
            
            # Get chunk data length and compression type
            chunk_length = struct.unpack('>I', region_data[sector_offset:sector_offset+4])[0]
            compression_type = region_data[sector_offset+4]
            
            # Read the chunk data
            chunk_data = region_data[sector_offset+5:sector_offset+5+chunk_length-1]
            
            # Decompress the chunk data
            if compression_type == 1:  # GZIP
                with gzip.GzipFile(fileobj=BytesIO(chunk_data)) as gz:
                    nbt_data = BytesIO(gz.read())
            elif compression_type == 2:  # ZLIB
                # NBT library handles zlib decompression
                nbt_data = BytesIO(chunk_data)
            else:
                print(f"Unknown compression type {compression_type} in chunk ({chunk_x}, {chunk_z})")
                continue
            
            # Parse NBT data
            try:
                nbt_file = nbt.NBTFile(buffer=nbt_data)
                
                # Check if the chunk has entities
                if "Level" in nbt_file and "Entities" in nbt_file["Level"]:
                    if len(nbt_file["Level"]["Entities"]) > 0:
                        # Remove all entities by creating an empty TAG_List
                        nbt_file["Level"]["Entities"] = nbt.TAG_List(name="Entities", type=nbt.TAG_Compound)
                        modified = True
                        
                        # Write the modified NBT data
                        new_nbt_data = BytesIO()
                        nbt_file.write_file(buffer=new_nbt_data)
                        new_nbt_data.seek(0)
                        
                        # Compress the new data
                        if compression_type == 1:  # GZIP
                            new_compressed = BytesIO()
                            with gzip.GzipFile(fileobj=new_compressed, mode='wb') as gz:
                                gz.write(new_nbt_data.read())
                            new_compressed.seek(0)
                            new_chunk_data = new_compressed.read()
                        elif compression_type == 2:  # ZLIB
                            # The NBT library will handle ZLIB compression when writing
                            new_chunk_data = new_nbt_data.read()
                        
                        # Calculate new length and update region file
                        new_length = len(new_chunk_data) + 1  # +1 for compression type byte
                        
                        # Update length field
                        region_data[sector_offset:sector_offset+4] = struct.pack('>I', new_length)
                        
                        # Update chunk data
                        region_data[sector_offset+5:sector_offset+5+len(new_chunk_data)] = new_chunk_data
                        
                        print(f"Removed entities from chunk ({chunk_x}, {chunk_z})")
            except Exception as e:
                print(f"Error processing chunk ({chunk_x}, {chunk_z}): {e}")
                continue
    
    if modified:
        # Write the modified region file
        with open(region_path, 'wb') as f:
            f.write(region_data)
        print(f"Updated region file: {region_path}")
    else:
        print(f"No entities found in {region_path}")
        # Remove the backup if no changes were made
        os.remove(backup_path)

def main():
    if len(sys.argv) != 2:
        print(f"Usage: {sys.argv[0]} <region_directory>")
        sys.exit(1)
    
    region_dir = Path(sys.argv[1])
    
    if not region_dir.exists() or not region_dir.is_dir():
        print(f"Error: {region_dir} is not a valid directory")
        sys.exit(1)
    
    # Process all .mca files in the directory
    region_files = list(region_dir.glob("r.*.*.mca"))
    
    if not region_files:
        print(f"No region files found in {region_dir}")
        sys.exit(1)
    
    print(f"Found {len(region_files)} region files to process")
    
    for region_file in region_files:
        print(f"Processing {region_file}...")
        process_region_file(region_file)
    
    print("All region files processed successfully!")

if __name__ == "__main__":
    main()

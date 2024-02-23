#!/bin/bash

# Check if exactly 4 arguments are provided
if [ "$#" -ne 4 ]; then
    echo "Usage: $0 groupId artifactId version jarFilePath"
    exit 1
fi

# Assign arguments to variables for better readability
GROUP_ID=$1
ARTIFACT_ID=$2
VERSION=$3
JAR_FILE_PATH=$4

# Installing the JAR into local Maven repository
mvn install:install-file \
   -Dfile="$JAR_FILE_PATH" \
   -DgroupId="$GROUP_ID" \
   -DartifactId="$ARTIFACT_ID" \
   -Dversion="$VERSION" \
   -Dpackaging=jar \
   -DgeneratePom=true

echo "Artifact installed successfully.";
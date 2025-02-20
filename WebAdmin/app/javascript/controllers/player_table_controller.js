// app/javascript/controllers/player_table_controller.js
import { Controller } from "@hotwired/stimulus"

export default class extends Controller {
    static targets = ["table", "searchInput"]
    
    connect() {
        this.rows = Array.from(this.tableTarget.querySelector("tbody").rows)
        this.currentSort = { column: null, asc: true }
    }
    
    search() {
        const query = this.searchInputTarget.value.toLowerCase()
        
        this.rows.forEach(row => {
            const text = Array.from(row.cells)
                .map(cell => cell.textContent.toLowerCase())
                .join(" ")
            
            row.style.display = text.includes(query) ? "" : "none"
        })
    }
    
    sort(event) {
        const column = event.currentTarget.dataset.sort
        const columnIndex = Array.from(event.currentTarget.parentElement.children)
            .indexOf(event.currentTarget)
            
        // Toggle sort direction if clicking the same column
        if (this.currentSort.column === column) {
            this.currentSort.asc = !this.currentSort.asc
        } else {
            this.currentSort = { column: column, asc: true }
        }
        
        const tbody = this.tableTarget.querySelector("tbody")
        const sortedRows = [...this.rows].sort((a, b) => {
            const aValue = a.cells[columnIndex].textContent
            const bValue = b.cells[columnIndex].textContent
            
            if (this.currentSort.asc) {
                return aValue.localeCompare(bValue)
            } else {
                return bValue.localeCompare(aValue)
            }
        })
        
        // Clear and re-append sorted rows
        tbody.innerHTML = ""
        sortedRows.forEach(row => tbody.appendChild(row))
    }
}
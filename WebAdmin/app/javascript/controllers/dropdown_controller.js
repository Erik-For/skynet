import { Controller } from "@hotwired/stimulus"

export default class extends Controller {
  static targets = ["input", "menu"]

  toggle(event) {
    event.stopPropagation();    
    if (this.menuTarget.classList.contains("hidden")) {
        this.menuTarget.classList.remove("hidden")
    } else {
        this.menuTarget.classList.add("hidden")
    }
  }

  clickOutside(event) {    
    if(!this.menuTarget.contains(event.target)) {
        this.menuTarget.classList.add("hidden")
    }
  }

  add(event) {
    const rank = event.target.textContent.trim()
    const currentValue = this.inputTarget.value
    const ranks = currentValue ? currentValue.split(',').map(r => r.trim()) : []
    
    if (!ranks.includes(rank)) {
      ranks.push(rank)
      this.inputTarget.value = ranks.join(', ')
      this.renderCards()
      event.target.classList.add("hidden")
      this.update()
    }
  }

  remove(event) {
    event.preventDefault()
    const rank = event.currentTarget.dataset.rankValue
    const currentValue = this.inputTarget.value
    const ranks = currentValue.split(',').map(r => r.trim()).filter(r => r !== rank)
    
    this.inputTarget.value = ranks.join(', ')
    this.renderCards()

    // Show the option back in the menu
    const menuButtons = this.menuTarget.querySelectorAll('button')
    menuButtons.forEach(button => {
      if (button.textContent.trim() === rank) {
        button.classList.remove('hidden')
      }
    })

    this.update()
  }

  renderCards() {
    const ranks = this.inputTarget.value.split(',').map(r => r.trim()).filter(r => r)
    const cards = ranks.map(rank => {
      return `
        <div class="inline-flex items-center gap-2 bg-blue-100 text-blue-800 px-3 py-1 rounded-full mr-2">
          <span>${rank}</span>
          <button 
            data-action="click->dropdown#remove"
            data-rank-value="${rank}"
            class="text-blue-600 hover:text-blue-800 h-auto"
          >
            <svg class="w-2 h-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
            </svg>
          </button>
        </div>
      `
    }).join('')
    
    this.inputTarget.innerHTML = cards + '<span class="text-gray-400">Click to add more ranks</span>'
  }

  update() {
    const url = new URL(window.location.href)
    url.searchParams.set('ranks', Array.from(this.inputTarget.children).map(e => e.innerText).join(","))
    console.log(url.searchParams.get("ranks"));
    
    if(this.inputTarget.children.length <= 1) {
        url.searchParams.delete("ranks")
    }

    console.log(url.toString())
    
    fetch(url, {
        headers: {
          'Accept': 'text/vnd.turbo-stream.html'
        }
      })
      .then(response => response.text())
      .then(html => Turbo.renderStreamMessage(html))
    history.pushState({}, null, url.toString())
  }

  connect() {
    document.addEventListener("click", this.clickOutside.bind(this))
    this.renderCards()
  }
}

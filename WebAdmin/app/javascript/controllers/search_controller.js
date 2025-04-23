import { Controller } from "@hotwired/stimulus"

export default class extends Controller {
  connect() { 
    this.element.addEventListener('input', () => {
      const url = new URL(window.location.href)
      url.searchParams.set('search', this.element.value)
      
      history.pushState({}, null, url.toString())      
      
      fetch(url, {
        headers: {
          'Accept': 'text/vnd.turbo-stream.html'
        }
      })
      .then(response => response.text())
      .then(html => Turbo.renderStreamMessage(html))
      history.pushState({}, null, url.toString())
    })
  }
}

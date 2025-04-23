import { Controller } from "@hotwired/stimulus"

export default class extends Controller {
  static values = {
    sort: String,
    direction: String
  }

  connect() {
    this.element.addEventListener('click', this.handleClick.bind(this))
  }

  handleClick() {
    const url = new URL(window.location.href)
    url.searchParams.set('sort', this.sortValue)
    url.searchParams.set('direction', this.directionValue)



    fetch(url, {
      headers: {
        'Accept': 'text/vnd.turbo-stream.html'
      }
    })
    .then(response => response.text())
    .then(html => Turbo.renderStreamMessage(html))
    history.pushState({}, null, url.toString())
  }
}

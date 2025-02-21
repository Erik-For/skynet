import { Controller } from "@hotwired/stimulus"

export default class extends Controller {
  static values = { 
    sort: String, 
    direction: String 
  }

  connect() {
    this.element.addEventListener('click', this.toggleSort.bind(this));
  }

  toggleSort() {
    const newDirection = this.directionValue === 'asc' ? 'desc' : 'asc';
    const url = new URL(window.location.href);
    
    url.searchParams.set('sort', this.sortValue);
    url.searchParams.set('direction', newDirection);
    
    Turbo.visit(url, { 
      action: "replace", 
      frame: "players" 
    });
    
    this.directionValue = newDirection;
  }
}
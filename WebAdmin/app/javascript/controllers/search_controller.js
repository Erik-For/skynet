import { Controller } from "@hotwired/stimulus"

export default class extends Controller {
  connect() { 
    this.element.addEventListener('input', () => {
      if(window.location.href.includes('search=')){
        const url = new URL(window.location.href);
        const searchParams = new URLSearchParams(url.search);
        searchParams.set('search', this.element.value);
        window.location.href = `${url.origin}${url.pathname}?${searchParams.toString()}`;
      } else {
        let mark = '';
        if(window.location.href.includes('?')){
          mark = '&';
        } else {
          mark = '?';
        }
          
        window.location.href
        = `${window.location.href}${mark}search=${this.element.value}`;
      }
    });
  }
}

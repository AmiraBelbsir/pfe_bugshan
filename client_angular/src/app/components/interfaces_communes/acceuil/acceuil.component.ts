import { Component } from '@angular/core';

@Component({
  selector: 'app-acceuil',
  templateUrl: './acceuil.component.html',
  styleUrl: './acceuil.component.css'
})
export class AcceuilComponent {
  scrollToCatalogue() {
    const element = document.getElementById('catalogue');
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    }
  }

}

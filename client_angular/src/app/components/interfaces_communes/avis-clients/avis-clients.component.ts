import {Component, OnInit} from "@angular/core";
import {AvisService} from "../../../services/avis.service";
import {Avis} from "../../../models/avis";

@Component({
  selector: 'app-avis-clients',
  templateUrl: './avis-clients.component.html',
  styleUrl: './avis-clients.component.css'
})
export class AvisClientsComponent implements OnInit{
  avis: Avis[] = [];
  errorMessage: string='';
  constructor(private avisService: AvisService) {
  }
  ngOnInit() {
    this.fetchVehicles();
  }

  getSatisfactionRate(): number {
    if (!this.avis || this.avis.length === 0) {
      return 0;
    }
    const satisfiedCount = this.avis.filter(a => a.note >= 4).length;
    return Math.round((satisfiedCount / this.avis.length) * 100);
  }

  getAverageRating(): number {
    if (!this.avis || this.avis.length === 0) {
      return 0;
    }
    const total = this.avis.reduce((sum, a) => sum + a.note, 0);
    return +(total / this.avis.length).toFixed(1);
  }

  getTotalReviews(): number {
    return this.avis ? this.avis.length : 0;
  }


  fetchVehicles(): void {
    this.avisService.getAllAvis().subscribe({
      next: (data) => {
        this.avis = data;
      },
      error: (err) => {
        console.error('Error fetching vehicles:', err);
        this.errorMessage = 'Échec du chargement des véhicules';
      }
    });
  }
}

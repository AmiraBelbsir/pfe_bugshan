import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {VehiculeService} from "../../../services/vehicule.service";
import {environment} from "../../../../environments/environment";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-prendre-rendez-vous',
  templateUrl: './prendre-rendez-vous.component.html',
  styleUrl: './prendre-rendez-vous.component.css'
})
export class PrendreRendezVousComponent implements OnInit {
  vehicle: any;
  currentUser: any;

  constructor(
    private route: ActivatedRoute,
    private vehiculeService: VehiculeService,
 private router: Router, private authService: AuthService

) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getUser();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.vehiculeService.getVehicleById(+id).subscribe({
        next: (data) => this.vehicle = data,
        error: (err) => console.error('Erreur de chargement', err)
      });
    }
  }
  getImageUrl(imagePath: string | undefined): string {
    return `${environment.apiUrl}${imagePath}`;
  }
}

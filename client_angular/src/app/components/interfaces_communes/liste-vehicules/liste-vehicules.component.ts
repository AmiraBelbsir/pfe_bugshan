import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../../../services/auth.service";
import {environment} from "../../../../environments/environment";
import {VehiculeService} from "../../../services/vehicule.service";
import {Vehicule} from "../../../models/vehicule";

@Component({
  selector: 'app-liste-vehicules',
  templateUrl: './liste-vehicules.component.html',
  styleUrl: './liste-vehicules.component.css'
})
export class ListeVehiculesComponent implements OnInit {
  selectedFilterType: string = '';
  selectedFilterTransmission: string = '';
  selectedFilterFuel: string = '';
  showLogin: boolean = false;
  loginOpened: boolean = false;
  signUpOpened: boolean = false;
  vehicules: Vehicule[] = [];
  searchTerm = '';
  filteredVehicles = [...this.vehicules];
  user:any;
  errorMessage: string='';
  constructor(private vehicleService: VehiculeService, private router: Router, private authService: AuthService) {


  }


  ngOnInit() {
    this.fetchVehicles();
    this.user = this.authService.getUser();}
  fetchVehicles(): void {
    this.vehicleService.getAllVehicles().subscribe({
      next: (data) => {
        this.vehicules = data;
        this.filteredVehicles = data;

      },
      error: (err) => {
        console.error('Error fetching vehicles:', err);
        this.errorMessage = 'Échec du chargement des véhicules';
      }
    });
  }



  getImageUrl(imagePath: string | undefined): string {
    return `${environment.apiUrl}${imagePath}`;
  }

  handleFiche(vehicleId: number) {

      this.router.navigate(['/fiche', vehicleId]);

  }

  currentIndex = 0;
  itemsPerPage = 6; // 2 rows * 3 columns

  get pagedVehicles() {
    return this.vehicules.slice(this.currentIndex, this.currentIndex + this.itemsPerPage);
  }

  next() {
    if (this.currentIndex + this.itemsPerPage < this.vehicules.length) {
      this.currentIndex += this.itemsPerPage;
    }
  }

  prev() {
    if (this.currentIndex - this.itemsPerPage >= 0) {
      this.currentIndex -= this.itemsPerPage;
    }
  }
  filterVehicles(): void {
    this.filteredVehicles = this.vehicules.filter(vehicle => {
      const matchesType = !this.selectedFilterType || vehicle.typeVehicule === this.selectedFilterType;
      const matchesTransmission = !this.selectedFilterTransmission || vehicle.typeTransmission === this.selectedFilterTransmission;
      const matchesFuel = !this.selectedFilterFuel || vehicle.typeCarburant === this.selectedFilterFuel;
      const matchesSearch = !this.searchTerm || (
        vehicle.marque.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        vehicle.modele.toLowerCase().includes(this.searchTerm.toLowerCase())
      );


      return matchesType && matchesTransmission && matchesFuel && matchesSearch;
    });
  }


  reserverVehicule(v: Vehicule) {
    // Logique réservation, navigation, modal, etc.
    console.log("Réserver :", v);
  }

  ouvrirDetails(v: Vehicule) {
    // Ouvre modal ou page détail pour infos complètes
    console.log("Détails :", v);
  }
  openLogin() {
    this.showLogin = false;
    this.loginOpened=true;
  }

  openRegister() {
    this.showLogin = false;
    this.signUpOpened=true;
  }

  resetFilters() {
    // Implement your filter reset logic here
    this.searchTerm = '';
    this.selectedFilterType = '';
    this.selectedFilterTransmission = '';
    this.selectedFilterFuel = '';
    this.filterVehicles();
  }

}



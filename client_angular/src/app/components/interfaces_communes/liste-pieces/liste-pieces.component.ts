import { Component, OnInit } from '@angular/core';
import { Piece } from "../../../models/piece";
import { PieceService } from "../../../services/piece.service";
import {environment} from "../../../../environments/environment";
import {Router} from "@angular/router";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-liste-pieces',
  templateUrl: './liste-pieces.component.html',
  styleUrls: ['./liste-pieces.component.css']
})
export class ListePiecesComponent implements OnInit {
  pieces: Piece[] = [];
  filteredPieces: Piece[] = [];
  uniqueVehicles: string[] = [];
  showLogin: boolean = false;
  loginOpened: boolean = false;
  signUpOpened: boolean = false;
  searchTerm: string = '';
  selectedCategory: string = '';
  selectedVehicle: string = '';

  constructor(private pieceService: PieceService, private router: Router, private authService: AuthService) {}


  ngOnInit(): void {
    this.loadPieces();
  }

  loadPieces(): void {
    this.pieceService.getPieces().subscribe({
      next: (data) => {
        this.pieces = data;
        this.filteredPieces = data;
        this.extractUniqueVehicles();
      },
      error: (err) => {
        console.error('Erreur de chargement des pièces', err);
      }
    });
  }

  extractUniqueVehicles(): void {
    const allVehicles: string[] = [];
    this.pieces.forEach(piece => {
      allVehicles.push(...piece.vehiculeModeles);
    });
    this.uniqueVehicles = [...new Set(allVehicles)].sort();
  }



  filterPieces(): void {
    this.filteredPieces = this.pieces.filter(piece => {
      const matchesSearch = piece.nom.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        piece.reference.toLowerCase().includes(this.searchTerm.toLowerCase());


      const matchesVehicle = !this.selectedVehicle ||
        piece.vehiculeModeles.includes(this.selectedVehicle);

      return matchesSearch && matchesVehicle;
    });
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.selectedCategory = '';
    this.selectedVehicle = '';
    this.filterPieces();
  }

  getImageUrl(imagePath: string | undefined): string {
    return `${environment.apiUrl}${imagePath}`;
  }

  openLogin() {
    this.showLogin = false;
    this.loginOpened=true;
  }

  handleReservation(pieceId: number) {
    const user = this.authService.getUser();
    if (user) {
      this.router.navigate(['/pieces/ajouterPanier', pieceId]);
    } else {
      this.showLogin=true;
    }
  }

  openRegister() {
    this.showLogin = false;
    this.signUpOpened=true;
  }

}

import {Component, OnInit} from '@angular/core';

import {Piece} from "../../../models/piece";
import {PieceService} from "../../../services/piece.service";

@Component({
  selector: 'app-liste-pieces',
  templateUrl: './liste-pieces.component.html',
  styleUrl: './liste-pieces.component.css'
})
export class ListePiecesComponent implements OnInit {

  pieces: Piece[] = [];
  filteredPieces: Piece[] = [];

  searchTerm: string = '';


  errorMessage: string = '';


  constructor(
    private pieceService: PieceService,

  ) {
  }

  ngOnInit(): void {
    this.loadPieces();

  }
  loadPieces(): void {
    this.pieceService.getPieces().subscribe({
      next: (data) => {
        this.pieces = data;
        this.filteredPieces = data;
      },
      error: (err) => {
        console.error('Erreur de chargement des pièces', err);
      }
    });
  }
}

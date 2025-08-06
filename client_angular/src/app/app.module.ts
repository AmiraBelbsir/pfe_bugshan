import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { UsersListComponent } from './components/admin/users-list/users-list.component';
import { VehiclesListComponent } from './components/admin/vehicles-list/vehicles-list.component';
import { HttpClientModule } from '@angular/common/http';
import { NavigationMenuComponent } from './components/navigation/navigation-menu/navigation-menu.component';
import {FormsModule} from "@angular/forms";
import { SignUpComponent } from './components/interfaces_communes/sign-up/sign-up.component';
import { PieceListComponent } from './components/admin/piece-list/piece-list.component';
import { MagasinListComponent } from './components/admin/magasin-list/magasin-list.component';

import {DropdownModule} from "primeng/dropdown";
import {TableModule} from "primeng/table";
import {FooterComponent} from "./components/interfaces_communes/footer/footer.component";
import {AcceuilComponent} from "./components/interfaces_communes/acceuil/acceuil.component";
import {ListeVehiculesComponent} from "./components/interfaces_communes/liste-vehicules/liste-vehicules.component";
import {AProposComponent} from "./components/interfaces_communes/a-propos/a-propos.component";
import {ContactComponent} from "./components/interfaces_communes/contact/contact.component";
import {AvisClientsComponent} from "./components/interfaces_communes/avis-clients/avis-clients.component";
import {LoginComponent} from "./components/interfaces_communes/login/login.component";
import {
  PrendreRendezVousComponent
} from "./components/interfaces_client/prendre-rendez-vous/prendre-rendez-vous.component";
import {ListePiecesComponent} from "./components/interfaces_communes/liste-pieces/liste-pieces.component";
import {
  NavigationGeneraleComponent
} from "./components/interfaces_communes/navigation-generale/navigation-generale.component";

@NgModule({
    declarations: [
        AppComponent,
        UsersListComponent,
        VehiclesListComponent,
        SignUpComponent,
        MagasinListComponent,
        NavigationGeneraleComponent,
        FooterComponent,
        AcceuilComponent,
        ListeVehiculesComponent,
        ContactComponent,
        AProposComponent,
        AvisClientsComponent,
      LoginComponent,
      PrendreRendezVousComponent,
      ListePiecesComponent
    ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    PieceListComponent,
    NavigationMenuComponent,
    FormsModule,
    DropdownModule,
    TableModule
  ],
    providers: [
        provideClientHydration()
    ],
    exports: [
        SignUpComponent
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }

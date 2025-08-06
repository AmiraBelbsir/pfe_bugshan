import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import {UsersListComponent} from "./components/admin/users-list/users-list.component";
import {AuthGuard} from "./guards/auth.guard";
import {VehiclesListComponent} from "./components/admin/vehicles-list/vehicles-list.component";
import {SignUpComponent} from "./components/interfaces_communes/sign-up/sign-up.component";
import {PieceListComponent} from "./components/admin/piece-list/piece-list.component";
import {MagasinListComponent} from "./components/admin/magasin-list/magasin-list.component";

import {AcceuilComponent} from "./components/interfaces_communes/acceuil/acceuil.component";

import {PrendreRendezVousComponent} from "./components/interfaces_client/prendre-rendez-vous/prendre-rendez-vous.component";
import {LoginComponent} from "./components/interfaces_communes/login/login.component";
import {ListeVehiculesComponent} from "./components/interfaces_communes/liste-vehicules/liste-vehicules.component";
import {ContactComponent} from "./components/interfaces_communes/contact/contact.component";
import {AProposComponent} from "./components/interfaces_communes/a-propos/a-propos.component";
import {AvisClientsComponent} from "./components/interfaces_communes/avis-clients/avis-clients.component";
import {RendezVousListComponent} from "./components/admin/rendez-vous-list/rendez-vous-list.component";
import {
  HistoriqueRendezVousListComponent
} from "./components/admin/historique-rendez-vous-list/historique-rendez-vous-list.component";

const routes: Routes = [
  { path: '', redirectTo: '/accueil', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignUpComponent },
  { path: 'utilisateurs/liste', component: UsersListComponent, canActivate: [AuthGuard], data: { roles: ['ADMINISTRATEUR'] } },
  { path: 'vehicules/liste', component: VehiclesListComponent, canActivate: [AuthGuard], data: { roles: ['ADMINISTRATEUR'] } },
  { path: 'magasins/liste', component: MagasinListComponent, canActivate: [AuthGuard], data: { roles: ['ADMINISTRATEUR'] } },
  { path: 'pieces/liste', component: PieceListComponent, canActivate: [AuthGuard], data: { roles: ['ADMINISTRATEUR'] } },
  { path: 'rendezvous/liste', component: RendezVousListComponent, canActivate: [AuthGuard], data: { roles: ['ADMINISTRATEUR'] } },
  { path: 'rendezvous/historique/liste', component: HistoriqueRendezVousListComponent, canActivate: [AuthGuard], data: { roles: ['ADMINISTRATEUR'] } },
  { path: 'accueil', component: AcceuilComponent},
  { path: 'vehicules', component: ListeVehiculesComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'apropos', component: AProposComponent },
  { path: 'avis', component: AvisClientsComponent },
  { path: 'reservation/:id', component: PrendreRendezVousComponent}

]
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

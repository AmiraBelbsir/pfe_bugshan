import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {UsersListComponent} from "./components/users/users-list/users-list.component";
import {AuthGuard} from "./guards/auth.guard";
import {VehiclesListComponent} from "./components/vehicles/vehicles-list/vehicles-list.component";
import {SignUpComponent} from "./components/sign-up/sign-up.component";
import {PieceListComponent} from "./components/pieces/piece-list/piece-list.component";
import {MagasinListComponent} from "./components/admin/magasin-list/magasin-list.component";

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignUpComponent },
  { path: 'utilisateurs/liste', component: UsersListComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN'] } },
  { path: 'vehicules/liste', component: VehiclesListComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN'] } },
  { path: 'magasins/liste', component: MagasinListComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN'] } },
  { path: 'pieces/liste', component: PieceListComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN'] } },

]
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {UsersListComponent} from "./components/users/users-list/users-list.component";
import {AuthGuard} from "./guards/auth.guard";
import {VehiclesListComponent} from "./components/vehicles/vehicles-list/vehicles-list.component";
import {SignUpComponent} from "./components/sign-up/sign-up.component";

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignUpComponent },
  { path: 'utilisateurs/liste', component: UsersListComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN'] } },
  { path: 'vehicules/liste', component: VehiclesListComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN'] } },

]
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

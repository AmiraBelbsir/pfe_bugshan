import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { UsersListComponent } from './components/users/users-list/users-list.component';
import { VehiclesListComponent } from './components/vehicles/vehicles-list/vehicles-list.component';
import { HttpClientModule } from '@angular/common/http';
import { NavigationMenuComponent } from './components/navigation/navigation-menu/navigation-menu.component';
import {FormsModule} from "@angular/forms";
import { SignUpComponent } from './components/sign-up/sign-up.component';

@NgModule({
  declarations: [
    AppComponent,
    UsersListComponent,
    VehiclesListComponent,
    SignUpComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        LoginComponent,
        NavigationMenuComponent,
        FormsModule
    ],
  providers: [
    provideClientHydration()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../../services/auth.service";

interface ParameterItem {
  key: string;
  label: string;
  icon: string;
  description?: string;
}

@Component({
  selector: 'app-parametres-generaux',
  templateUrl: './parametres-generaux.component.html',
  styleUrls: ['./parametres-generaux.component.css']
})
export class ParametresGenerauxComponent implements OnInit {
  selectedParam: string = '';
  defaultParam = 'compte'; // Default selected parameter
  user: any;
  parameterItems: ParameterItem[] = [
    {
      key: 'compte',
      label: 'Compte',
      icon: 'fas fa-user-cog',
      description: 'Gérez vos informations personnelles et préférences de compte'
    },
    {
      key: 'securite',
      label: 'Sécurité',
      icon: 'fas fa-lock',
      description: 'Modifiez votre mot de passe et paramètres de sécurité'
    },
    // Add more parameters as needed
  ];

  constructor( private authService: AuthService) { }

  ngOnInit(): void {
    // Set default selected parameter on init
    this.selectedParam = this.defaultParam;
    this.user = this.authService.getUser();}

  selectParameter(key: string): void {
    this.selectedParam = key;
    // You could add analytics tracking here if needed
    // this.trackParameterSelection(key);
  }

  getSelectedParamTitle(): string {
    const item = this.parameterItems.find(p => p.key === this.selectedParam);
    return item ? item.label : 'Paramètres';
  }

  getSelectedParamDescription(): string {
    const item = this.parameterItems.find(p => p.key === this.selectedParam);
    return item?.description || 'Gérez vos préférences et paramètres';
  }

  // Optional: Track parameter selection for analytics
  private trackParameterSelection(key: string): void {
    console.log(`Parameter selected: ${key}`);
    // Implement your analytics tracking here
  }
}

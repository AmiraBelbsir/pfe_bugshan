import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AjouterAuPanierComponent } from './ajouter-au-panier.component';

describe('AjouterAuPanierComponent', () => {
  let component: AjouterAuPanierComponent;
  let fixture: ComponentFixture<AjouterAuPanierComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AjouterAuPanierComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AjouterAuPanierComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

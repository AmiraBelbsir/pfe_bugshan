import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoriqueRendezVousListComponent } from './historique-rendez-vous-list.component';

describe('HistoriqueRendezVousListComponent', () => {
  let component: HistoriqueRendezVousListComponent;
  let fixture: ComponentFixture<HistoriqueRendezVousListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HistoriqueRendezVousListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HistoriqueRendezVousListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

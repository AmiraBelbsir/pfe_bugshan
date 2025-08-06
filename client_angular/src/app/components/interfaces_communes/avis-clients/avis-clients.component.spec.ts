import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvisClientsComponent } from './avis-clients.component';

describe('AvisClientsComponent', () => {
  let component: AvisClientsComponent;
  let fixture: ComponentFixture<AvisClientsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AvisClientsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AvisClientsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

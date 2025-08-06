import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListePiecesComponent } from './liste-pieces.component';

describe('ListePiecesComponent', () => {
  let component: ListePiecesComponent;
  let fixture: ComponentFixture<ListePiecesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListePiecesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListePiecesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

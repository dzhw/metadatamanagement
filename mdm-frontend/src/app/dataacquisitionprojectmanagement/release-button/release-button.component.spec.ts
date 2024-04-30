import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReleaseButtonComponent } from './release-button.component';

describe('ReleaseButtonComponent', () => {
  let component: ReleaseButtonComponent;
  let fixture: ComponentFixture<ReleaseButtonComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReleaseButtonComponent]
    });
    fixture = TestBed.createComponent(ReleaseButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

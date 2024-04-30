import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReleaseStatusBadgeComponent } from './release-status-badge.component';

describe('ReleaseStatusBadgeComponent', () => {
  let component: ReleaseStatusBadgeComponent;
  let fixture: ComponentFixture<ReleaseStatusBadgeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReleaseStatusBadgeComponent]
    });
    fixture = TestBed.createComponent(ReleaseStatusBadgeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

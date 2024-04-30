import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssigneeStatusBadgeComponent } from './assignee-status-badge.component';

describe('AssigneeStatusBadgeComponent', () => {
  let component: AssigneeStatusBadgeComponent;
  let fixture: ComponentFixture<AssigneeStatusBadgeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssigneeStatusBadgeComponent]
    });
    fixture = TestBed.createComponent(AssigneeStatusBadgeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

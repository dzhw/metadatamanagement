import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectCockpitComponent } from './project-cockpit.component';

describe('ProjectCockpitComponent', () => {
  let component: ProjectCockpitComponent;
  let fixture: ComponentFixture<ProjectCockpitComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProjectCockpitComponent]
    });
    fixture = TestBed.createComponent(ProjectCockpitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

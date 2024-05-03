import { Component, Inject, OnInit } from '@angular/core';
import { DataAcquisitionProject } from '../data/dataacquisitionprojectmanagement.data';

@Component({
  selector: 'fdz-release-button',
  templateUrl: './release-button.component.html',
  styleUrls: ['./release-button.component.css']
})
export class ReleaseButtonComponent implements OnInit {

  project! : DataAcquisitionProject | undefined;

  constructor(@Inject("CurrentProjectService") private currentProjectService: any,) {

  }

  ngOnInit(){
    this.project = this.currentProjectService.getCurrentProject(); // todo get current project
  }

  hasRelease(): boolean {
    return this.project && this.project.release ? true : false;
  }

  /**
   * 
   */
  isAssignedPublisher(): boolean {
    return true;
  }

  /**
   * Method to toggle the release status of a project.
   * If the project is not released yet it will release it.
   * If the project is currently released but as a pre-release it will fully release it.
   * If the project already is released it will unrelease it.
  */
  toggleReleaseProject() {
    console.log("Releasing/Unreleasing project")
    // if (this.project.release) {
    //   if (ctrl.isPreReleased()) {
    //     ProjectReleaseService.releaseProject(ctrl.project);
    //   } else {
    //     ProjectReleaseService.unreleaseProject(ctrl.project);
    //   }
    // } else {
    //   ProjectReleaseService.releaseProject(ctrl.project);
    // }
  };

}

import { Component, Input, OnInit } from '@angular/core';
import { DataAcquisitionProject } from '../data/dataacquisitionprojectmanagement.data';

@Component({
  selector: 'fdz-release-status-badge',
  templateUrl: './release-status-badge.component.html',
  styleUrls: ['./release-status-badge.component.css']
})
export class ReleaseStatusBadgeComponent {

  @Input() project! : DataAcquisitionProject;

  /**
   * 
   * @returns 
   */
  isReleased(): boolean {
    return this.project && this.project.release ? true : false;
  }

}

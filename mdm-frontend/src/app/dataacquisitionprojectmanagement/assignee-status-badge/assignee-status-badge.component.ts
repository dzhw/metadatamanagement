import { Component, Input, OnInit } from '@angular/core';
import { DataAcquisitionProject } from '../data/dataacquisitionprojectmanagement.data';

@Component({
  selector: 'fdz-assignee-status-badge',
  templateUrl: './assignee-status-badge.component.html',
  styleUrls: ['./assignee-status-badge.component.css']
})
export class AssigneeStatusBadgeComponent implements OnInit {

  @Input() assigneeGroup! : string;

  publishersAssigned = false;

  ngOnInit(): void {
    if(this.assigneeGroup && this.assigneeGroup === "PUBLISHERS") {
      this.publishersAssigned = true;
    } else {
      this.publishersAssigned = false;
    }
  }


  // getAssigneeGroup(): string {
  //   return this.project && this.project.assigneeGroup && this.project.assigneeGroup === "PUBLISHER" ? "PUBLISHER" : "DATAPROVIDER"
  // }

}

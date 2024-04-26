import { Component, OnInit, Input } from '@angular/core';
import { downgradeComponent, getAngularJSGlobal } from '@angular/upgrade/static';

@Component({
  selector: 'fdz-project-cockpit',
  templateUrl: './project-cockpit.component.html',
  styleUrls: ['./project-cockpit.component.css']
})
export class ProjectCockpitComponent implements OnInit {

  // @Input({ required: true }) project!: DataAcquisitionProject;

  // todo input for project
  project = {
    id: "dummy"
  }

  selectedTabIndex = 1

  constructor() {
    console.log("constructing")
  }

  ngOnInit(): void {
      console.log("I am here-")
  }

  onSelectedTabChanged(tabIndex: number): void {
    console.log("New Tab Index", this.selectedTabIndex)
  }
}

// necessary for using service in AngularJS
getAngularJSGlobal()
    .module('metadatamanagementApp')
    .directive('fdzProjectCockpit', downgradeComponent({component: ProjectCockpitComponent}));

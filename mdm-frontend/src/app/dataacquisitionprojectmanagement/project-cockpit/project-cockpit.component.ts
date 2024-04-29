import { Component, OnInit, Input, Inject } from '@angular/core';
import { downgradeComponent, getAngularJSGlobal } from '@angular/upgrade/static';
import { DataacquisitionprojectDataService } from '../dataacquisitionproject.data.service';

@Component({
  selector: 'fdz-project-cockpit',
  templateUrl: './project-cockpit.component.html',
  styleUrls: ['./project-cockpit.component.css']
})
export class ProjectCockpitComponent implements OnInit {
  @Input({ required: true }) id!: string;

  // todo input for project
  project = {
    id: "dummy"
  }

  selectedTabIndex = 1

  constructor(private dataService: DataacquisitionprojectDataService) {
    console.log("constructing")
  }

  ngOnInit(): void {
      // todo: get project data
      try {
        let current;
        this.dataService.fetchProjectById(this.id).then(project => {
          current = project;
        console.log("CURRENT",current);
        });
    } catch (error) {
        console.error("Unable to fetch project data for id " + this.id);
    }
      
  }

  onSelectedTabChanged(tabIndex: number): void {
    console.log("New Tab Index", this.selectedTabIndex)
  }
}

// necessary for using component in AngularJS
getAngularJSGlobal()
    .module('metadatamanagementApp')
    .directive('fdzProjectCockpit', downgradeComponent({component: ProjectCockpitComponent}));

import { Component, OnInit, Input, Inject } from '@angular/core';
import { downgradeComponent, getAngularJSGlobal } from '@angular/upgrade/static';
import { DataacquisitionprojectDataService } from '../dataacquisitionproject.data.service';
import { CurrentProjectService } from '../current-project.service';
import { DataAcquisitionProject } from '../data/dataacquisitionprojectmanagement.data';

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

  current! : DataAcquisitionProject;

  selectedTabIndex = 0;

  constructor(private dataService: DataacquisitionprojectDataService, 
    private currentProjectService: CurrentProjectService) {
      
  }

  ngOnInit(): void {
      try {
        if(this.id) {
          this.dataService.fetchProjectById(this.id).then(project => {
            this.current = project;
            console.log("CURRENT",this.current.assigneeGroup);
            // todo: prüfen ob das an der Stelle Sinn macht
            this.currentProjectService.setCurrentProject(this.current)
          });
        }
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

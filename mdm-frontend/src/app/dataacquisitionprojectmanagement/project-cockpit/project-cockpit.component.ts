import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { downgradeComponent, getAngularJSGlobal } from '@angular/upgrade/static';
import * as angular from 'angular';

@Component({
  selector: 'fdz-project-cockpit',
  templateUrl: './project-cockpit.component.html',
  styleUrls: ['./project-cockpit.component.css']
})
export class ProjectCockpitComponent implements OnInit {

  constructor() {
    console.log("constructing")
  }

  ngOnInit(): void {
      console.log("I am here-")
  }
}

// necessary for using service in AngularJS
getAngularJSGlobal()
    .module('metadatamanagementApp')
    .directive('fdzProjectCockpit', downgradeComponent({component: ProjectCockpitComponent}));

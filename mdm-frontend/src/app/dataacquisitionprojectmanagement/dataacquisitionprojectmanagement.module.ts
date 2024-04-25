import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DataacquisitionprojectSearchService } from './dataacquisitionproject.search.service';
import { UpgradeModule } from '@angular/upgrade/static';
import { ProjectCockpitComponent } from './project-cockpit/project-cockpit.component';



@NgModule({
  declarations: [
    ProjectCockpitComponent
  ],
  imports: [
    CommonModule,
    UpgradeModule
  ],
  providers: [
    DataacquisitionprojectSearchService
  ]
})
export class DataacquisitionprojectmanagementModule {}
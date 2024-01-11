import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DataacquisitionprojectSearchService } from './dataacquisitionproject.search.service';
import { UpgradeModule } from '@angular/upgrade/static';
import { RouterModule } from '@angular/router';



@NgModule({
  declarations: [
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
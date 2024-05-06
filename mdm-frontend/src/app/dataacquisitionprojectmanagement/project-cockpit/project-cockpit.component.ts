import { Component, OnInit, Input, Inject } from '@angular/core';
import { downgradeComponent, getAngularJSGlobal } from '@angular/upgrade/static';
import { DataacquisitionprojectDataService } from '../dataacquisitionproject.data.service';
import { DataAcquisitionProject } from '../data/dataacquisitionprojectmanagement.data';
import { Router } from '@angular/router';
import { IRootScopeService } from 'angular';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'fdz-project-cockpit',
  templateUrl: './project-cockpit.component.html',
  styleUrls: ['./project-cockpit.component.css']
})
export class ProjectCockpitComponent implements OnInit {
  @Input({ required: true }) id!: string;

  current! : DataAcquisitionProject;

  selectedTabIndex = 0;

  counts : {key: string, count: number}[] = [];
  count: number | undefined;

  requiredParts : string[] = [];


  constructor(
    private dataService: DataacquisitionprojectDataService, 
    private router: Router,
    // private currentProjectService: CurrentProjectService,
    @Inject("CurrentProjectService") private currentProjectService: any,
    @Inject("CommonDialogsService") private dialogsService: any,
    @Inject("SearchDao") private searchDaoService: any,
    @Inject("ProjectSaveService") private projectSaveService: any,
    @Inject("$rootScope") private _rootScope: IRootScopeService,
    private translate: TranslateService) {
      _rootScope.$on('current-project-changed', (event, project) => {
        this.current = project;
        if (this.current) {
          router.navigateByUrl("/de/cockpit/"+project.id);
        }
        
      });
      _rootScope.$on('switched-language', (event, langCode) => {
        console.log("HELLOO", langCode);
        this.translate.use(langCode);
      });
  }

  ngOnInit(): void {
      try {
        if(this.id) {
          this.dataService.fetchProjectById(this.id).then(project => {
            this.current = project;
            console.log("CURRENT",this.current);
            // todo: prüfen ob das an der Stelle Sinn macht
            this.currentProjectService.setCurrentProject(this.current);
          });
          this.fetchTypeCounts(this.id);
        }
      } catch (error) {
          console.error("Unable to fetch project data for id " + this.id);
      }
  }

  onSelectedTabChanged(tabIndex: number): void {
    console.log("New Tab Index", this.selectedTabIndex)
    // return this.dialogsService.showConfirmOnDirtyDialog();
  }

  fetchTypeCounts(projectId: string): void {
    const dataTypes = [
      'variables', 'questions', 'data_sets', 'surveys', 'instruments',
      'data_packages', 'analysis_packages', 'related_publications',
      'concepts']
    this.searchDaoService.search('', 1, projectId, {}, undefined, 0, undefined).then(
      (d: any) => {
        for (const type of dataTypes)  {
          let buckets : ({ key: any; doc_count:number}[]) = d.aggregations.countByType.buckets;
          let b = buckets.find(b => b.key === type);
          if (b) {
            this.counts.push({key: type, count: b.doc_count ? b.doc_count : 0});
          }
      }
      }
    );
  }

  getCountForType(type: string): number {
    if (!this.counts) {
      return 0;
    }
    let count = this.counts.find(c => c.key === type)?.count;
    return count ? count : 0;
  }

  /**
   * Handles changes of metadata.
   * @param project 
   */
  onProjectPartStateChanged(project: DataAcquisitionProject): void {
    console.log("CHANGES IN PROJECT")
    this.current = project;
    this.projectSaveService.prepareProjectForSave(this.current);
    this.projectSaveService.saveProject(this.current);

  }
}

// necessary for using component in AngularJS
getAngularJSGlobal()
    .module('metadatamanagementApp')
    .directive('fdzProjectCockpit', downgradeComponent({component: ProjectCockpitComponent}));

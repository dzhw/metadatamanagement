import { Component, EventEmitter, Inject, Input, OnInit, Output } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { DataAcquisitionProject } from 'src/app/dataacquisitionprojectmanagement/data/dataacquisitionprojectmanagement.data';

export interface BtnConfig {
  delete: boolean,
  edit: boolean,
  create: boolean,
  reportPublication: boolean,
  upload: boolean
}
@Component({
  selector: 'fdz-state-card',
  templateUrl: './state-card.component.html',
  styleUrls: ['./state-card.component.css']
})
export class StateCardComponent implements OnInit {
  @Input() type!: string;
  @Input() counts!: number;
  @Input() project!: DataAcquisitionProject;
  @Output() projectChange = new EventEmitter<DataAcquisitionProject>();

  constructor(@Inject("StateProvider") private _state: any) {
  }

  createState: string = "";
  searchState: string = "";
  tooltip: string = "";
  deleteTooltip: string = "";
  editTooltip: string = "";
  icon: string = "";
  limit = 1;

  dataProviderReady: boolean = false;
  publisherReady: boolean = false;  
  buttonConfig : BtnConfig = {
    delete: false,
    edit: false,
    create: false,
    reportPublication: false,
    upload: false
  }

  ngOnInit(): void {
    console.log("COUNTS:",this.type, this.counts.toString())
    switch (this.type) {
      case 'dataPackages':
        this.createState = 'dataPackageCreate';
        this.searchState = 'data_packages';
        this.tooltip = 'search-management.buttons.' +
          'create-data-package-tooltip';
        this.deleteTooltip = 'search-management.buttons.' +
          'delete-all-data-packages-tooltip';
        this.limit = 1;
        this.dataProviderReady = this.project.configuration.dataPackagesState!.dataProviderReady;
        this.publisherReady = this.project.configuration.dataPackagesState!.publisherReady;
        
        this.buttonConfig = {
          delete: this.counts > 0 ? true : false,
          edit: this.counts > 0 ? true : false,
          create: this.counts === 0 ? true : false,
          reportPublication: false,
          upload: false
        }
        break;
      case 'analysisPackages':
        this.createState = 'analysisPackageCreate';
        this.searchState = 'analysis_packages';
        this.tooltip = 'search-management.buttons.' +
          'create-analysis-package-tooltip';
        this.editTooltip = 'search-management.buttons.' +
          'edit-analysis-package-tooltip';
        this.deleteTooltip = 'search-management.buttons.' +
          'delete-all-analysis-packages-tooltip';
        this.limit = 1;
        this.dataProviderReady = this.project.configuration.analysisPackagesState!.dataProviderReady;
        this.publisherReady = this.project.configuration.analysisPackagesState!.publisherReady;
        break;
      case 'surveys':
        this.createState = 'surveyCreate';
        this.searchState = this.type;
        this.tooltip = 'search-management.buttons.create-survey-tooltip';
        this.deleteTooltip = 'search-management.buttons.' +
          'delete-all-surveys-tooltip';
        this.editTooltip = 'search-management.buttons.' +
          'edit-surveys-tooltip';
        this.dataProviderReady = this.project.configuration.surveysState!.dataProviderReady;
        this.publisherReady = this.project.configuration.surveysState!.publisherReady;
        break;
      case 'instruments':
        this.createState = 'instrumentCreate';
        this.searchState = this.type;
        this.tooltip = 'search-management.buttons.' +
          'create-instrument-tooltip';
        this.deleteTooltip = 'search-management.buttons.' +
          'delete-all-instruments-tooltip';
        this.editTooltip = 'search-management.buttons.' +
          'edit-instruments-tooltip';
        this.dataProviderReady = this.project.configuration.instrumentsState!.dataProviderReady;
        this.publisherReady = this.project.configuration.instrumentsState!.publisherReady;
        break;
      case 'questions':
        this.createState = '';
        // this.uploadFunction = function(files) {
        //   QuestionUploadService.uploadQuestions(files, this.project.id);
        // }.bind(this);
        this.searchState = this.type;
        this.tooltip = 'search-management.buttons.' +
          'upload-questions-tooltip';
        this.deleteTooltip = 'search-management.buttons.' +
          'delete-all-questions-tooltip';
        break;
      case 'dataSets':
        this.createState = 'dataSetCreate';
        this.searchState = 'data_sets';
        this.tooltip = 'search-management.buttons.' +
          'create-data-set-tooltip';
        this.deleteTooltip = 'search-management.buttons.' +
         'delete-all-data-sets-tooltip';
        this.editTooltip = 'search-management.buttons.' +
         'edit-data-sets-tooltip';
        this.dataProviderReady = this.project.configuration.datasetsState ? this.project.configuration.datasetsState.dataProviderReady : false;
        this.publisherReady = this.project.configuration.datasetsState ? this.project.configuration.datasetsState.publisherReady : false;
        break;
      case 'variables':
        this.createState = '';
        // this.uploadFunction = function(files) {
        //   VariableUploadService.uploadVariables(files, this.project.id);
        // }.bind(this);
        this.searchState = this.type;
        this.tooltip = 'search-management.buttons.' +
          'upload-variables-tooltip';
        this.deleteTooltip = 'search-management.buttons.' +
         'delete-all-variables-tooltip';
        break;
      case 'publications':
        this.icon = 'assets/images/icons/related-publication.svg';
        this.createState = '';
        this.searchState = 'related_publications';
        this.tooltip = 'search-management.buttons.' +
         'edit-publications-tooltip';
        break;
      case 'concepts':
        this.icon = 'assets/images/icons/related-publication.svg';
        this.createState = 'conceptCreate';
        this.searchState = 'concepts';
        this.tooltip = 'search-management.buttons.' +
          'create-concept-tooltip';
        this.editTooltip = 'search-management.buttons.' +
         'edit-concepts-tooltip';
        break;
      case 'fake1':
        break;
      default:
        throw Error('wrong argument for group');
    }
  }

  onDataProviderStateChanged(value: MatCheckboxChange) {
    console.log(value);
    switch (this.type) {
      case 'dataPackages':
        this.project.configuration.dataPackagesState!.dataProviderReady = value.checked;
        break;
      case 'analysisPackages':
        this.project.configuration.analysisPackagesState!.dataProviderReady = value.checked;
        break;
      case 'surveys':
        this.project.configuration.surveysState!.dataProviderReady = value.checked;
        break;
      case 'instruments':
        this.project.configuration.instrumentsState!.dataProviderReady = value.checked;
        break;  
      case 'questions':
        console.log("TODO")
        // this.project.configuration.!.dataProviderReady = value.checked;
        break;
      case 'dataSets':
        this.project.configuration.datasetsState!.dataProviderReady = value.checked;
        break;
      case 'variables':
        console.log("TODO")
        // this.project.configuration.!.dataProviderReady = value.checked;
        break;
      case 'publications':
        console.log("TODO")
        // this.project.configuration.!.dataProviderReady = value.checked;
        break;
      case 'concepts':
        console.log("TODO")
        // this.project.configuration.!.dataProviderReady = value.checked;
        break;
      default:
        console.error("Case not implemented")        
    } 
    this.dataProviderReady = value.checked;
    this.projectChange.emit(this.project);
  }

  onPublisherStateChanged(value: MatCheckboxChange) {
    console.log(value);
    switch (this.type) {
      case 'dataPackages':
        this.project.configuration.dataPackagesState!.publisherReady = value.checked;
        break;
      case 'analysisPackages':
        this.project.configuration.analysisPackagesState!.publisherReady = value.checked;
        break;
      case 'surveys':
        this.project.configuration.surveysState!.publisherReady = value.checked;
        break;
      case 'instruments':
        this.project.configuration.instrumentsState!.publisherReady = value.checked;
        break;  
      case 'questions':
        console.log("TODO")
        // this.project.configuration.!.dataProviderReady = value.checked;
        break;
      case 'dataSets':
        this.project.configuration.datasetsState!.publisherReady = value.checked;
        break;
      case 'variables':
        console.log("TODO")
        // this.project.configuration.!.dataProviderReady = value.checked;
        break;
      case 'publications':
        console.log("TODO")
        // this.project.configuration.!.dataProviderReady = value.checked;
        break;
      case 'concepts':
        console.log("TODO")
        // this.project.configuration.!.dataProviderReady = value.checked;
        break;
      default:
        console.error("Case not implemented")        
    } 
    this.publisherReady = value.checked;
    this.projectChange.emit(this.project);
  }

  /**
   * Navigates to the search page of the corresponding project part.
   */
  goToSearch() {
    this._state.go("search", {type: this.searchState});
  }

  /**
   * If the delete button should be shown or not.
   * @returns true if it should be shown else false
   */
  shouldShowDeleteButton(): boolean {
    return true;
  }

  /**
   * If the edit button should be shown or not.
   * @returns true if it should be shown else false
   */
  shouldShowEditButton(): boolean {
    return true;
  }

  /**
   * If the button to create new objects should be shown or not.
   * @returns true if it should be shown else false
   */
  shouldShowNewButton(): boolean {
    return true;
  }

  /**
   * If the upload button should be shown or not.
   * @returns true if it should be shown else false
   */
  shouldShowUploadButton(): boolean {
    return true;
  }

  /**
   * If the button to report publications should be shown or not.
   * @returns true if it should be shown else false
   */
  shouldShowPublicationButton(): boolean {
    return true;
  }
}

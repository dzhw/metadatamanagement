import { EventEmitter, Injectable, Output } from '@angular/core';
import { DataAcquisitionProject } from './data/dataacquisitionprojectmanagement.data';

/**
 * Service to get and set the current project
 */
@Injectable({
  providedIn: 'root'
})
export class CurrentProjectService {

  @Output() currentProjectChanged = new EventEmitter<DataAcquisitionProject>();

  private currentProject : DataAcquisitionProject | undefined;

  constructor() { }

  /**
   * Getter for current project
   * @returns the current project
   */
  getCurrentProject() {
    return this.currentProject;
  }

  /**
   * Setter for current project
   * @param chosenProject 
   */
  setCurrentProject(chosenProject: DataAcquisitionProject) {
    if (this.currentProject && this.currentProject!.id != chosenProject.id) {
      this.currentProject = chosenProject;
      if (this.currentProject) {
        localStorage.setItem("currentProject", JSON.stringify(this.currentProject));
      } else {
        localStorage.removeItem("currentProject")
      }
      this.currentProjectChanged.emit(this.currentProject);
    }
  }
}

/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('ProjectSaveService', ['SimpleMessageToastService', 'DataAcquisitionProjectResource', 'CurrentProjectService', '$q', 
  function(
    SimpleMessageToastService,
    DataAcquisitionProjectResource,
    CurrentProjectService,
    $q
  ) {

    var setSaving = function(saving) {
      if (saving === undefined) {
        saving = true;
      }
      this.saving = saving;
    }.bind(this);
    var getSaving = function() {
      return this.saving;
    }.bind(this);

    var prepareProjectForSave = function(project,
                                         assigneeGroupMessage,
                                         newAssigneeGroup) {
      if (!project) {
        return null;
      }
      if (!project.configuration) {
        project.configuration = {};
      }
      if (assigneeGroupMessage) {
        project.lastAssigneeGroupMessage = assigneeGroupMessage;
      }
      if (newAssigneeGroup) {
        project.assigneeGroup = newAssigneeGroup;
      }
      return project;
    };

    var saveProject = function(project) {
        if (!project) {
          return $q.resolve();
        }
        setSaving();
        return $q(function(resolve, reject) {
            return DataAcquisitionProjectResource.save(
              project,
              //Success
              function() {
                  setSaving(false);
                  CurrentProjectService.setCurrentProject(project);
                  resolve(project);
                  SimpleMessageToastService
                    .openSimpleMessageToast(
                    'data-acquisition-project-management.log-messages.' +
                    'data-acquisition-project.saved', {
                        id: project.id
                      });
                },
              //Server Error
              function(response) {
                  var errors = _.get(response, 'data.errors');
                  setSaving(false);
                  reject(errors);
                  if (errors) {
                    errors.forEach(function(error) {
                      SimpleMessageToastService.
                        openAlertMessageToast(error.message);
                    });
                  }
                });
          });
      };

    return {
        saveProject: saveProject,
        prepareProjectForSave: prepareProjectForSave,
        setSaving: setSaving,
        getSaving: getSaving
      };
  }]);


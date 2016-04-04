'use strict';

angular.module('metadatamanagementApp').service('JobLog',
function($translate) {
  var state = {};
  var hasFinished = function() {
    if (state.progress === state.itemsToUpload) {
      state.hasFinished = true;
      state.disableButton = false;
    }
  };
  var start = function(jobName, numberOfSteps, domainObjectName) {
    state.jobName = jobName;
    state.numberOfSteps = numberOfSteps;
    state.currentProgressStep = 100 / numberOfSteps;
    state.domainObjectName = domainObjectName;
    state.errors = 0;
    state.successes = 0;
    state.progress = 0;
    state.disableButton = false;
    state.logMessages = [{
            message: $translate.instant('metadatamanagementApp.' +
                'dataAcquisitionProject.detail.logMessages.intro') +
              '\n'
          }];
  };
  var error = function(error) {
    state.errors++;
    state.progress++;
    hasFinished();
    state.logMessages.push({
       message: '\n'
     });
    if (typeof error === 'string' || error instanceof String) {
      state.logMessages.push({
         message: error + '\n',
         type: 'error'
       });
    }
    if (error.config && error.config.data && error.config.data.id) {
      state.logMessages.push({
         message: $translate.instant('metadatamanagementApp' +
           '.dataAcquisitionProject.detail.' +
           'logMessages.dataSetNotSaved', {
             id: error.config.data.id
           }) + '\n',
         type: 'error'
       });
    }
    if (error.data && error.data.errors) {
      error.data.errors.forEach(function(error) {
         state.logMessages.push({
           message: error.message + '\n',
           type: 'error'
         });
       });
    } else if (error.data && error.data.status === 500) {
      state.logMessages.push({
         message: $translate.instant('metadatamanagementApp' +
           '.dataAcquisitionProject.detail.logMessages.' +
           'internalServerError') + '\n',
         type: 'error'
       });
    } else if (error.data && error.data.message) {
      state.logMessages.push({
         message: error.data.message +
           '\n',
         type: 'error'
       });
    }
    return state;
  };
  var success = function() {
    state.successes++;
    state.progress++;
    hasFinished();
    return state;
  };
  var cancel = function() {

  };
  return {
    start: start,
    error: error,
    success: success,
    cancel: cancel
  };
});

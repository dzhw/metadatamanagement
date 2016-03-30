'use strict';

angular.module('metadatamanagementApp').service('JobLog',
function($translate) {
  var uploadState = {};
  var initUploadState = function() {
    uploadState.errors = 0;
    uploadState.successes = 0;
    uploadState.hasFinished = false;
    uploadState.itemsToUpload = 0;
    uploadState.progress = 0;
    uploadState.uploadedDomainObject = '';
    uploadState.disableButton = false;
    uploadState.logMessages = [{
            message: $translate.instant('metadatamanagementApp.' +
                'dataAcquisitionProject.detail.logMessages.intro') +
              '\n'
          }];
    uploadState.checkState = function() {
      if (uploadState.progress >= uploadState.itemsToUpload) {
        uploadState.hasFinished = true;
        uploadState.disableButton = false;
      }
    };
    uploadState.getResult = function() {
      if (uploadState.errors > 0) {
        return 'danger';
      }
      return 'success';
    };
    uploadState.getProgressState = function() {
      return uploadState.progress + '/' + uploadState.itemsToUpload;
    };
    uploadState.pushSuccess = function() {
      uploadState.progress++;
      uploadState.successes++;
    };
    uploadState.pushError = function(error) {
      //add an empty line
      uploadState.logMessages.push({
         message: '\n'
       });
      // if the error is already a string simply display it
      if (typeof error === 'string' || error instanceof String) {
        uploadState.logMessages.push({
           message: error + '\n',
           type: 'error'
         });
      }
      // log the dataset id
      if (error.config && error.config.data && error.config.data.id) {
        uploadState.logMessages.push({
           message: $translate.instant('metadatamanagementApp' +
             '.dataAcquisitionProject.detail.' +
             'logMessages.dataSetNotSaved', {
               id: error.config.data.id
             }) + '\n',
           type: 'error'
         });
      }
      //create additional information for the unsaved dataset
      if (error.data && error.data.errors) {
        error.data.errors.forEach(function(error) {
           uploadState.logMessages.push({
             message: error.message + '\n',
             type: 'error'
           });
         });
      } else if (error.data && error.data.status === 500) {
        uploadState.logMessages.push({
           message: $translate.instant('metadatamanagementApp' +
             '.dataAcquisitionProject.detail.logMessages.' +
             'internalServerError') + '\n',
           type: 'error'
         });
      } else if (error.data && error.data.message) {
        uploadState.logMessages.push({
           message: error.data.message +
             '\n',
           type: 'error'
         });
      }
    };
  };
  return {
    initUploadState: initUploadState,
    uploadState: uploadState
  };
});

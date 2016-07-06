'use strict';
/*
  Service for the post validation. It send the errors from the backend
  to the log output console (JobLoggingService) and it uses the resource.
  @author Daniel Katzberg
*/

angular.module('metadatamanagementApp').service(
  'DataAcquisitionProjectPostValidationService',
  function($translate, JobLoggingService,
    DataAcquisitionProjectPostValidationResource) {

    var postValidate = function(id) {
      JobLoggingService.start('postValidation');
      DataAcquisitionProjectPostValidationResource.postValidate({
        id: id
      }, function(result) {
        // got errors by post validation
        if (result.errors.length > 0) {
          for (var i = 0; i < result.errors.length; i++) {
            JobLoggingService.error(result.errors[i]);
          }
          //no errors by post validation
        } else {
          JobLoggingService.success();
        }

        // After sending errors or success, the process is finished.
        JobLoggingService.finish($translate.instant(
          'metadatamanagementApp.dataAcquisitionProject.detail.' +
          'logMessages.postValidationTerminated', {}));
      }, function(error) {
        // something went wrong
        JobLoggingService.cancel(error.data.error);
      });
    };
    //public, global methods definitions.
    return {
      postValidate: postValidate
    };
  });

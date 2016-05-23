'use strict';

angular.module('metadatamanagementApp').service(
  'DataAcquisitionProjectPostValidationService',
  function($translate, JobLoggingService,
    DataAcquisitionProjectPostValidationResource) {

    var postValidate = function(id) {
      // To be removed
      JobLoggingService.start('postValidation');
      DataAcquisitionProjectPostValidationResource.postValidate({
        id: id
      }, function(result) {
        // everything is okay.
        if (result.errors.length > 0) {
          for (var i = 0; i < result.errors.length; i++) {
            JobLoggingService.error(result.errors[i]);
          }
        } else {
          JobLoggingService.success();
        }
        JobLoggingService.finish($translate.instant(
          'metadatamanagementApp.dataAcquisitionProject.detail.' +
          'logMessages.postValidationTerminated', {}));
      });
    };

    return {
      postValidate: postValidate
    };
  });

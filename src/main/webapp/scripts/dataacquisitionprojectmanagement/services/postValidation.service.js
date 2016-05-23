'use strict';

angular.module('metadatamanagementApp').service(
  'DataAcquisitionProjectPostValidationService',
  function($translate, JobLoggingService,
    DataAcquisitionProjectPostValidationResource,
    ErrorMessageResolverService) {

    var postValidate = function(id) {
      // To be removed
      console.log(ErrorMessageResolverService);
      DataAcquisitionProjectPostValidationResource.postValidate({
        id: id
      });
    };
    return {
      postValidate: postValidate
    };
  });

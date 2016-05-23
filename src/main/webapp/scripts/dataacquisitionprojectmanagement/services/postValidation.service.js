'use strict';

angular.module('metadatamanagementApp').service(
  'DataAcquisitionProjectPostValidationService',
  function($translate, JobLoggingService,
    DataAcquisitionProjectPostValidationResource,
    ErrorMessageResolverService) {

    var postValidate = function(id) {
      DataAcquisitionProjectPostValidationResource.postValidate({
        id: id
      });
    };
    return {
      postValidate: postValidate
    };
  });

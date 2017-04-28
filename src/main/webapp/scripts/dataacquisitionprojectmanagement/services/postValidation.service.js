'use strict';
/*
  Service for the post validation. It send the errors from the backend
  to the log output console (JobLoggingService) and it uses the resource.
  @author Daniel Katzberg
*/

angular.module('metadatamanagementApp').service(
  'DataAcquisitionProjectPostValidationService',
  function(JobLoggingService,
    DataAcquisitionProjectPostValidationResource, $q) {

    // Convert array to object
    var convertArrayToObject = function(array) {
      var objectElement = {};
      if (typeof array === 'object') {
        for (var i in array) {
          var elementValue = convertArrayToObject(array[i]);
          objectElement[i] = elementValue;
        }
      } else {
        objectElement = array;
      }
      return objectElement;
    };

    var postValidate = function(id) {
      var deferred = $q.defer();
      JobLoggingService.start('postValidation');
      DataAcquisitionProjectPostValidationResource.postValidate({
        id: id
      }, function(result) {
        // got errors by post validation
        if (result.errors.length > 0) {
          for (var i = 0; i < result.errors.length; i++) {
            var messageParameter = {
              id: result.errors[i].messageParameter[0],
              toBereferenzedId: result.errors[i].messageParameter[1],
              additionalId: result.errors[i].messageParameter[2]
            };
            JobLoggingService.error({
              message: result.errors[i].messageId,
              messageParams: messageParameter
            });
          }
          deferred.reject(result);
          //no errors by post validation
        } else {
          JobLoggingService
            .success();
          deferred.resolve();
        }

        // After sending errors or success, the process is finished.
        JobLoggingService.finish(
          'global.log-messages.post-validation-terminated', {
            successes: JobLoggingService.getCurrentJob().successes,
            errors: JobLoggingService.getCurrentJob().errors
          });
      }, function(error) {
        // something went wrong
        JobLoggingService.cancel(error.data.error);
        deferred.reject(error);
      });
      return deferred.promise;
    };
    //public, global methods definitions.
    return {
      postValidate: postValidate
    };
  });

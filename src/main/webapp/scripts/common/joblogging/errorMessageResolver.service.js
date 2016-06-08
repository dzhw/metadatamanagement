'use strict';

angular.module('metadatamanagementApp').service('ErrorMessageResolverService',
  function($translate) {
    var getErrorMessage = function(messageObj, jobId) {
      var message = '';
      if (messageObj.config &&
        messageObj.config.data && messageObj.config.data.id) {
        message = $translate.instant('metadatamanagementApp' +
          '.dataAcquisitionProject.detail.logMessages.' + jobId +
          '.notSaved', {
            id: messageObj.config.data.id
          }) + '\n';
      }
      if (messageObj.data && messageObj.data.errors) {
        messageObj.data.errors.forEach(function(messageObj) {
          message = message + messageObj.message + '\n';
        });
      } else if (messageObj.data && messageObj.data.status === 500) {
        message = message + $translate.instant('metadatamanagementApp' +
          '.dataAcquisitionProject.detail.logMessages.' +
          'internalServerError') + '\n';
      } else if (messageObj.data && messageObj.data.message) {
        message = message + messageObj.data.message + '\n';
      }
      return message;
    };
    return {
      getErrorMessage: getErrorMessage
    };
  });

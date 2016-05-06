'use strict';

angular.module('metadatamanagementApp').service('ValidationExtractorService',
function($translate) {
  var addValidationMessage = function(jobId, logMessages, messageObj, type) {
    if (typeof messageObj === 'string' || messageObj instanceof String) {
      logMessages.push({
        message: '\n' + messageObj + '\n',
        type: type
      });
    }else {
      if (messageObj.config &&
        messageObj.config.data && messageObj.config.data.id) {
        logMessages.push({
        message: '\n' + $translate.instant('metadatamanagementApp' +
          '.dataAcquisitionProject.detail.' +
          'logMessages.' + jobId + '.notSaved', {
            id: messageObj.config.data.id
          }) + '\n',
        type: type
      });
      }
      if (messageObj.data && messageObj.data.errors) {
        messageObj.data.errors.forEach(function(messageObj) {
        logMessages.push({
          message: messageObj.message + '\n',
          type: type
        });
      });
      } else if (messageObj.data && messageObj.data.status === 500) {
        logMessages.push({
        message: $translate.instant('metadatamanagementApp' +
          '.dataAcquisitionProject.detail.logMessages.' +
          'internalServerError') + '\n',
        type: type
      });
      } else if (messageObj.data && messageObj.data.message) {
        logMessages.push({
        message: messageObj.data.message +
          '\n',
        type: type
      });
      }
    }
  };
  return {
      addValidationMessage: addValidationMessage
    };
});

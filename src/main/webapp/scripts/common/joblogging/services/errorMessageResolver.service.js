'use strict';

angular.module('metadatamanagementApp').service('ErrorMessageResolverService',
  function() {
    var getErrorMessages = function(messageObj, jobId) {
      var messages = [];
      if (messageObj.config &&
        messageObj.config.data && messageObj.config.data.id) {
        messages.push({
          message: 'metadatamanagementApp.dataAcquisitionProject.' +
            'detail.logMessages.' + jobId + '.notSaved',
          translationParams: {
            id: messageObj.config.data.id
          }
        });
      }

      if (messageObj.data && messageObj.data.errors) {
        messageObj.data.errors.forEach(function(messageObj) {
          messages.push({
            message: messageObj.message
          });
        });
      } else if (messageObj.data && messageObj.data.status === 500) {
        messages.push({
          message: 'metadatamanagementApp.dataAcquisitionProject.detail' +
            '.logMessages.internalServerError'
        });
      } else if (messageObj.data && messageObj.data.message) {
        messages.push({
          message: messageObj.data.message
        });
      }

      return messages;
    };
    return {
      getErrorMessages: getErrorMessages
    };
  });

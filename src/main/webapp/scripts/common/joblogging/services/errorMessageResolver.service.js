'use strict';

angular.module('metadatamanagementApp').service('ErrorMessageResolverService',
  function() {
    var getErrorMessages = function(messageObj, jobId) {
      var errorMessages = {};
      var subMessages = [];
      if (messageObj.config &&
        messageObj.config.data && messageObj.config.data.id) {
        errorMessages.message =
          jobId + '-management.log-messages.' + jobId + '.notSaved';
        errorMessages.translationParams = {
          id: messageObj.config.data.id
        };
      }
      if (messageObj.data && messageObj.data.errors) {
        messageObj.data.errors.forEach(function(messageObj) {
          subMessages.push({
            message: messageObj.message
          });
        });
      } else if (messageObj.data && messageObj.data.status === 500) {
        subMessages.push({
          message: 'global.log-messages.internalServerError'
        });
      } else if (messageObj.data && messageObj.data.message) {
        subMessages.push({
          message: messageObj.data.message
        });
      }
      errorMessages.subMessages = subMessages;
      return errorMessages;
    };
    return {
      getErrorMessages: getErrorMessages
    };
  });

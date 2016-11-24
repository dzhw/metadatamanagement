'use strict';

angular.module('metadatamanagementApp').service('ErrorMessageResolverService',
  function() {
    var getErrorMessage = function(messageObj, domainObjectType,
      subObjectType,
      subObjectId) {
      var errorMessage = {};
      var subMessages = [];
      if (messageObj.config &&
        messageObj.config.data && messageObj.config.data.id) {
        errorMessage.message =
          domainObjectType + '-management.log-messages.' +
          domainObjectType + '.not-saved';
        errorMessage.translationParams = {
          id: messageObj.config.data.id
        };
      } else if (subObjectType && subObjectId) {
        errorMessage.message =
          domainObjectType + '-management.log-messages.' +
          subObjectType + '.not-saved';
        errorMessage.translationParams = {
          id: subObjectId
        };
      }
      if (messageObj.data && messageObj.data.errors) {
        messageObj.data.errors.forEach(function(messageObj) {
          subMessages.push({
            message: messageObj.message,
            translationParams: {
              'property': messageObj.property,
              'invalidValue': messageObj.invalidValue,
              'entity': messageObj.entity
            }
          });
        });
      } else if (messageObj.data && messageObj.data.status === 500) {
        subMessages.push({
          message: 'global.log-messages.internal-server-error'
        });
      } else if (messageObj.data && messageObj.data.message) {
        subMessages.push({
          message: messageObj.data.message
        });
      }
      errorMessage.subMessages = subMessages;
      return errorMessage;
    };
    return {
      getErrorMessage: getErrorMessage
    };
  });

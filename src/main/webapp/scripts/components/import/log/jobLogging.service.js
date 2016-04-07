'use strict';

angular.module('metadatamanagementApp').service('JobLoggingService',
function($translate) {
  var job = {
    state: ''
  };
  var init = function() {
    return job;
  };
  var pushInfo = function(messageObj) {
    job.logMessages.push({
      message: '\n' + messageObj + '\n',
      type: 'info'
    });
  };
  var pushWarning = function(messageObj) {
    job.logMessages.push({
      message: '\n'
    });
    if (typeof messageObj === 'string' || messageObj instanceof String) {
      job.logMessages.push({
        message: messageObj + '\n',
        type: 'error'
      });
    }else {
      if (messageObj.config &&
        messageObj.config.data && messageObj.config.data.id) {
        job.logMessages.push({
        message: $translate.instant('metadatamanagementApp' +
          '.dataAcquisitionProject.detail.' +
          'logMessages.' + job.id + '.notSaved', {
            id: messageObj.config.data.id
          }) + '\n',
        type: 'error'
      });
      }
      if (messageObj.data && messageObj.data.errors) {
        messageObj.data.errors.forEach(function(messageObj) {
        job.logMessages.push({
          message: messageObj.message + '\n',
          type: 'error'
        });
      });
      } else if (messageObj.data && messageObj.data.status === 500) {
        job.logMessages.push({
        message: $translate.instant('metadatamanagementApp' +
          '.dataAcquisitionProject.detail.logMessages.' +
          'internalServerError') + '\n',
        type: 'error'
      });
      } else if (messageObj.data && messageObj.data.message) {
        job.logMessages.push({
        message: messageObj.data.message +
          '\n',
        type: 'error'
      });
      }
    }
  };
  var start = function(nameOfDomainObj) {
    job.id = nameOfDomainObj;
    job.errors = 0;
    job.successes = 0;
    job.logMessages = [];
    job.state = 'running';
    return job;
  };
  var error = function(errorMsg) {
    job.errors++;
    pushWarning(errorMsg);
  };
  var success = function(successMsg) {
    job.successes++;
    pushInfo(successMsg);
  };
  var finish = function(finishMsg) {
    job.state = 'finished';
    pushInfo(finishMsg);
  };
  var cancel = function(cancelMsg) {
    job.state = 'canceled';
    pushWarning(cancelMsg);
  };
  return {
    init: init,
    start: start,
    error: error,
    success: success,
    finish: finish,
    cancel: cancel
  };
});

'use strict';

angular.module('metadatamanagementApp').service('JobLoggingService',
function(ValidationExtractorService) {
  var job = {
    state: ''
  };
  var init = function() {
    return job;
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
    ValidationExtractorService
    .addValidationMessage(job.id, job.logMessages, errorMsg, 'error');
  };
  var success = function(successMsg) {
    job.successes++;
    ValidationExtractorService
    .addValidationMessage(job.id, job.logMessages, successMsg, 'info');
  };
  var finish = function(finishMsg) {
    job.state = 'finished';
    ValidationExtractorService
    .addValidationMessage(job.id, job.logMessages, finishMsg, 'info');
  };
  var cancel = function(cancelMsg) {
    job.state = 'cancelled';
    ValidationExtractorService
    .addValidationMessage(job.id, job.logMessages, cancelMsg, 'error');
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

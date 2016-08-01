'use strict';

angular.module('metadatamanagementApp').service('JobLoggingService',
  function(JobCompleteToastService, blockUI) {
    var job = {
      state: ''
    };
    var getCurrentJob = function() {
      return job;
    };
    var start = function(nameOfDomainObj) {
      job.id = nameOfDomainObj;
      job.errors = 0;
      job.successes = 0;
      job.logMessages = [];
      job.state = 'running';
      blockUI.start();
      return job;
    };
    var error = function(errorMsg, translationParams) {
      job.errors++;
      job.logMessages.push({
        message: errorMsg,
        translationParams: translationParams,
        type: 'error'
      });
    };
    var success = function(successMsg, translationParams) {
      job.successes++;
      if (successMsg) {
        job.logMessages.push({
          message: successMsg,
          translationParams: translationParams,
          type: 'info'
        });
      }
    };
    var finish = function(finishMsg, translationParams) {
      job.state = 'finished';
      job.logMessages.push({
        message: finishMsg,
        translationParams: translationParams,
        type: 'info'
      });
      blockUI.stop();
      JobCompleteToastService.openJobCompleteToast(finishMsg,
        translationParams);
    };
    var cancel = function(cancelMsg, translationParams) {
      job.state = 'cancelled';
      job.errors++;
      job.logMessages.push({
        message: cancelMsg,
        translationParams: translationParams,
        type: 'error'
      });
      blockUI.stop();
      JobCompleteToastService.openJobCompleteToast(cancelMsg,
        translationParams);
    };
    return {
      getCurrentJob: getCurrentJob,
      start: start,
      error: error,
      success: success,
      finish: finish,
      cancel: cancel
    };
  });

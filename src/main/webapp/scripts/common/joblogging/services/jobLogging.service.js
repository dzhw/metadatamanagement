'use strict';

angular.module('metadatamanagementApp').service('JobLoggingService',
  function(JobCompleteToastService, blockUI) {
    var job = {
      state: ''
    };
    var getCurrentJob = function() {
      return job;
    };
    var start = function(jobId) {
      job.id = jobId;
      job.errors = 0;
      job.total = 0;
      job.successes = 0;
      job.logMessages = [];
      job.state = 'running';
      blockUI.start();
      return job;
    };
    var error = function(parameters) {
      job.errors++;
      job.logMessages.push({
        message: parameters.message,
        translationParams: parameters.messageParams,
        subMessages: parameters.subMessages,
        type: 'error'
      });
      job.total++;
    };
    var success = function(parameters) {
      job.successes++;
      job.total++;
      if (parameters && parameters.message) {
        job.logMessages.push({
          message: parameters.message,
          translationParams: parameters.messageParams,
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

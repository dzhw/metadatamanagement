'use strict';

angular.module('metadatamanagementApp').service('JobLoggingService',
  function(JobCompleteToastService, blockUI, $translate) {
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
    var error = function(errorMsg) {
      job.errors++;
      job.logMessages.push({
        message: '\n' + errorMsg,
        type: 'error'
      });
    };
    var success = function(successMsg) {
      job.successes++;
      if (successMsg) {
        job.logMessages.push({
          message: '\n' + successMsg,
          type: 'info'
        });
      }
    };
    var finish = function(finishMsg, translationData) {
      job.state = 'finished';
      var message = $translate.instant(finishMsg, translationData);
      job.logMessages.push({
        message: '\n' + message,
        type: 'info'
      });
      blockUI.stop();
      JobCompleteToastService.openJobCompleteToast(finishMsg,
        translationData);
    };
    var cancel = function(cancelMsg) {
      job.state = 'cancelled';
      job.errors++;
      job.logMessages.push({
        message: '\n' + cancelMsg,
        type: 'error'
      });
      blockUI.stop();
      JobCompleteToastService.openJobCompleteToast(cancelMsg);
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

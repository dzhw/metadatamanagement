'use strict';

angular.module('metadatamanagementApp').service('JobLoggingService',
  function(JobCompleteToastService, blockUI) {
    var createNewCountsByObjectType = function() {
      return {
        successes: 0,
        errors: 0,
        total: 0
      };
    };

    var job = {
      state: '',
      getCounts: function(objectType) {
        if (objectType && this.countsByObjectType &&
            this.countsByObjectType[objectType]) {
          return this.countsByObjectType[objectType];
        } else {
          return createNewCountsByObjectType();
        }
      }
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
      job.countsByObjectType = {};
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
      if (parameters && parameters.objectType) {
        if (!job.countsByObjectType[parameters.objectType]) {
          job.countsByObjectType[parameters.objectType] =
            createNewCountsByObjectType();
        }
        job.countsByObjectType[parameters.objectType].errors++;
        job.countsByObjectType[parameters.objectType].total++;
      }
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
      if (parameters && parameters.objectType) {
        if (!job.countsByObjectType[parameters.objectType]) {
          job.countsByObjectType[parameters.objectType] =
            createNewCountsByObjectType();
        }
        job.countsByObjectType[parameters.objectType].successes++;
        job.countsByObjectType[parameters.objectType].total++;
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

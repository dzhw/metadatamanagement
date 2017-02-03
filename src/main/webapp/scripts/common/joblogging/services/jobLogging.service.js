'use strict';

angular.module('metadatamanagementApp').service('JobLoggingService',
  function(JobCompleteToastService, blockUI) {
    //init a new counts object which can be used in job.countsByObjectType
    var createNewCountsByObjectType = function() {
      return {
        successes: 0,
        warnings: 0,
        errors: 0,
        total: 0
      };
    };

    //init the job object managed by this service
    var job = {
      state: '',
      //return errors, successes and total for the given objectType
      getCounts: function(objectType) {
        if (objectType && this.countsByObjectType &&
          this.countsByObjectType[objectType]) {
          return this.countsByObjectType[objectType];
        } else {
          return createNewCountsByObjectType();
        }
      }
    };

    //return the current job managed by this services
    var getCurrentJob = function() {
      return job;
    };

    //reset the job object holding counts and log messages
    var start = function(jobId) {
      job.id = jobId;
      job.warnings = 0;
      job.errors = 0;
      job.total = 0;
      job.successes = 0;
      job.logMessages = [];
      job.state = 'running';
      //init the counts per objectType
      job.countsByObjectType = {};
      blockUI.start();
      return job;
    };

    //log warning execution of a step of the current job
    //parameters contains the message, messageParams, subMessages and objectType
    var warning = function(parameters) {
      job.warnings++;
      job.logMessages.push({
        message: parameters.message,
        translationParams: parameters.messageParams,
        subMessages: parameters.subMessages,
        type: 'warning'
      });
      //increase counters for the given object type
      if (parameters && parameters.objectType) {
        if (!job.countsByObjectType[parameters.objectType]) {
          job.countsByObjectType[parameters.objectType] =
            createNewCountsByObjectType();
        }
        job.countsByObjectType[parameters.objectType].warnings++;
        job.countsByObjectType[parameters.objectType].total++;
      }
    };

    //log failing execution of a step of the current job
    //parameters contains the message, messageParams, subMessages and objectType
    var error = function(parameters) {
      job.errors++;
      job.logMessages.push({
        message: parameters.message,
        translationParams: parameters.messageParams,
        subMessages: parameters.subMessages,
        type: 'error'
      });
      //increase counters for the given object type
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

    //log successfull execution of a step of the current job
    //parameters contains the message, messageParams and objectType
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

    //log successfull completion of the entire job
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

    //log cancellation of the entire job
    var cancel = function(cancelMsg, translationParams, objectType) {
      job.state = 'cancelled';
      job.errors++;
      job.total++;
      job.logMessages.push({
        message: cancelMsg,
        translationParams: translationParams,
        type: 'error'
      });
      if (objectType) {
        if (!job.countsByObjectType[objectType]) {
          job.countsByObjectType[objectType] =
            createNewCountsByObjectType();
        }
        job.countsByObjectType[objectType].errors++;
        job.countsByObjectType[objectType].total++;
      }
      blockUI.stop();
      JobCompleteToastService.openJobCompleteToast(cancelMsg,
        translationParams);
    };

    return {
      getCurrentJob: getCurrentJob,
      start: start,
      warning: warning,
      error: error,
      success: success,
      finish: finish,
      cancel: cancel
    };
  });

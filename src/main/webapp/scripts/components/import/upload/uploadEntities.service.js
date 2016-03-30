/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('UploadEntities',
function($translate, JobLog) {
  var upload = function(objects) {
    JobLog.uploadState.disableButton = true;
    for (var i = 0; i < objects.length; i++) {
      if (!objects[i].id || objects[i].id === '') {
        JobLog.uploadState.pushError($translate.instant(
          'metadatamanagementApp.dataAcquisitionProject.' +
          'detail.logMessages.' +
          'missingId', {
            index: i + 1
          }));
        JobLog.uploadState.progress++;
        JobLog.uploadState.errors++;
        JobLog.uploadState.checkState();
      } else {
        objects[i].$save().then(function() {
        JobLog.uploadState.pushSuccess();
        JobLog.uploadState.checkState();
      }).catch(function(error) {
        JobLog.uploadState.progress++;
        JobLog.uploadState.errors++;
        JobLog.uploadState.pushError(error);
        JobLog.uploadState.checkState();
      });
      }
    }
  };
  return {
      upload: upload
    };
});

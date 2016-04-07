/*jshint loopfunc: true */
'use strict';

angular.module('metadatamanagementApp').service('UploadEntities',
function(JobLog, $translate) {
  var upload = function(objects) {
    for (var i = 0; i < objects.length; i++) {
      if (!objects[i].id || objects[i].id === '') {
        JobLog.error($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.' +
            'detail.logMessages.' +
            'missingId', {
              index: i + 1
            }));
      } else {
        objects[i].$save().then(function() {
        JobLog.success('okk');
      }).catch(function(error) {
        JobLog.error(error);
      });
      }
    }
  };
  return {
      upload: upload
    };
});

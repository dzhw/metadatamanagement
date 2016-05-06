/* global FileReader, JSZip */
'use strict';

angular.module('metadatamanagementApp').service('ZipReaderService', function($q,
  JobLoggingService, $translate) {
  this.readZipFileAsync = function(file) {
    var deferred = $q.defer();
    var files = {};
    var fileReader = new FileReader();
    fileReader.readAsArrayBuffer(file);
    fileReader.onload = function(e) {
      try {
        files = new JSZip(e.target.result);
      }catch (ex) {
        console.log(ex);
        JobLoggingService.cancel($translate.instant(
          'metadatamanagementApp.dataAcquisitionProject.detail.' +
          'logMessages.unsupportedFile', {}));
      }
      deferred.resolve(files);
    };
    return deferred.promise;
  };
});

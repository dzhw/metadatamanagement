/* global FileReader, JSZip */
'use strict';

angular.module('metadatamanagementApp').service('ZipReaderService', function(
  $q) {
  this.readZipFileAsync = function(file) {
    var deferred = $q.defer();
    var files = {};
    var fileReader = new FileReader();
    fileReader.readAsArrayBuffer(file);
    fileReader.onload = function(e) {
      try {
        files = new JSZip(e.target.result);
      }catch (e) {
        files = e;
      }finally {
        deferred.resolve(files);
      }
    };
    return deferred.promise;
  };
});

/* global FileReader, JSZip */
'use strict';

angular.module('metadatamanagementApp').service('ZipReader', function($q) {
  this.readZipFileAsync = function(file) {
    var deferred = $q.defer();
    var fileReader = new FileReader();
    fileReader.readAsArrayBuffer(file);
    fileReader.onload = function(e) {
      var files = new JSZip(e.target.result);
      deferred.resolve(files);
    };
    return deferred.promise;
  };
});

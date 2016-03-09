/* global FileReader, JSZip */
'use strict';

angular.module('metadatamanagementApp').service('ZipParser', function($q) {
  this.readZipFileAsync = function(file) {
    var deferred = $q.defer();
    var fileReader = new FileReader();
    fileReader.readAsArrayBuffer(file);
    fileReader.onload = function(e) {
      var zipFileLoaded = new JSZip(e.target.result);
      deferred.resolve(zipFileLoaded);
    };
    return deferred.promise;
  };
});

/* global FileReader*/
'use strict';

angular.module('metadatamanagementApp').service('FileReaderService', function(
  $q) {
  this.readAsText = function(file) {
    var deferred = $q.defer();
    var fileReader = new FileReader();

    fileReader.onload = function() {
      deferred.resolve(fileReader.result);
    };

    fileReader.onerror = function(e) {
      deferred.reject(e);
    };

    fileReader.readAsText(file);

    return deferred.promise;
  };

  this.readAsArrayBuffer = function(file) {
    var deferred = $q.defer();
    var fileReader = new FileReader();

    fileReader.onload = function() {
      deferred.resolve(fileReader.result);
    };

    fileReader.onerror = function(e) {
      deferred.reject(e);
    };

    fileReader.readAsArrayBuffer(file);

    return deferred.promise;
  };
});

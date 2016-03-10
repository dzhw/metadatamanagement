/* global XLSX, FileReader */
'use strict';

angular.module('metadatamanagementApp').service('ExcelParser', function($q) {
  this.readFileAsync = function(file) {
    var deferred = $q.defer();
    var fileReader = new FileReader();
    fileReader.readAsBinaryString(file);
    fileReader.onload = function(e) {
      var data = e.target.result;
      var content = XLSX.read(data, {
        type: 'binary'
      });
      var sheetList = content.SheetNames;
      var worksheet = content.Sheets[sheetList[0]];
      // jscs:disable
      var jsonContent = XLSX.utils.sheet_to_json(worksheet);
      // jscs:enable
      deferred.resolve(jsonContent);
    };
    return deferred.promise;
  };
});

/* global XLSX, FileReader */
'use strict';

angular.module('metadatamanagementApp').service('ExcelReader', function($q) {
  this.readFileAsync = function(file) {
    var deferred = $q.defer();
    var readExcel = function(data) {
      var content = XLSX.read(data, {
        type: 'binary'
      });
      var sheetList = content.SheetNames;
      var worksheet = content.Sheets[sheetList[0]];
      // jscs:disable
      var jsonContent = XLSX.utils.sheet_to_json(worksheet);
      // jscs:enable
      deferred.resolve(jsonContent);
      console.log(jsonContent);
    };
    var fileReader = new FileReader();
    if (!FileReader.prototype.readAsBinaryString) {
      fileReader.readAsArrayBuffer(file);
      fileReader.onload = function(e) {
        var bytes = new Uint8Array(e.target.result);
        var length = bytes.byteLength;
        var binary = '';
        for (var i = 0; i < length; i++) {
          binary += String.fromCharCode(bytes[i]);
        }
        readExcel(binary);
      };
    } else {
      fileReader.readAsBinaryString(file);
      fileReader.onload = function(e) {
      var data = e.target.result;
      readExcel(data);
    };
    }
    return deferred.promise;
  };
});

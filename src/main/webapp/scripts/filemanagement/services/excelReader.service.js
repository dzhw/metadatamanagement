/* global XLSX, FileReader */
'use strict';

angular.module('metadatamanagementApp').service('ExcelReaderService',
  function($q) {
    this.readFileAsync = function(file, readAllSheets) {
      var deferred = $q.defer();
      var readExcel = function(data) {
        var jsonContent = {};
        try {
          var content = XLSX.read(data, {
            type: 'binary'
          });
          var sheetList = content.SheetNames;
          var worksheet = content.Sheets[sheetList[0]];

          if (!readAllSheets) {
            // jscs:disable
            jsonContent = XLSX.utils.sheet_to_json(worksheet);
            // jscs:enable
          } else {
            sheetList.forEach(function(sheetName) {
              // jscs:disable
              jsonContent[sheetName] = XLSX.utils.sheet_to_json(
                content.Sheets[sheetName]);
              // jscs:enable
            });
          }
          deferred.resolve(jsonContent);
        } catch (e) {
          deferred.reject(e);
        }
      };
      // check if file is compressed
      if (file._data) {
        readExcel(file.asBinary());
      } else {
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
      }
      return deferred.promise;
    };
  });

/* global XLSX, FileReader, Modernizr */
'use strict';

angular.module('metadatamanagementApp').service('ExcelReader', function($q) {
  this.readFileAsync = function(file) {
    var deferred = $q.defer();
    /*if (!FileReader.prototype.readAsBinaryString) {
      FileReader.prototype.readAsBinaryString = function(fileData) {
        var binary = '';
        var pt = this;
        var reader = new FileReader();
        //jshint unused:true
        reader.onload = function(e) {
          //jshint unused:false
          var bytes = new Uint8Array(reader.result);
          var length = bytes.byteLength;
          for (var i = 0; i < length; i++) {
            binary += String.fromCharCode(bytes[i]);
          }
          pt.result = binary;
          jQuery(pt).trigger('onload');
        };
        reader.readAsArrayBuffer(fileData);
      };
    }*/
    if (Modernizr.filereader) {
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
    }else {
      console.log('er');
      deferred.resolve({});
    }
    return deferred.promise;
  };
});

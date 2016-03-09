/*
'use strict';

angular.module('metadatamanagementApp').service('FilesReader', function($q) {
  var filesParser = {
    zip: function() {

    },
    json: function(data) {
      console.log(data);
    },
    excel: function(data) {
      var content = XLSX.read(data, {type: 'binary'});
      var sheetList = content.SheetNames;
      var worksheet = content.Sheets[sheetList[0]];
      // jscs:disable
      var jsonContent = XLSX.utils.sheet_to_json(worksheet);
      // jscs:enable
      return jsonContent;
    }
  };
  this.readFileAsync = function(file) {
    var deferred = $q.defer();
    var fileReader = new FileReader();
    fileReader.readAsArrayBuffer(file);
    fileReader.onload = function(e) {
      console.log(file);
      var content = {};
      switch (file.type) {
        case 'application/zip':
          content = filesParser.zip(e.target.result);
        break;
        case 'application/vnd.openxmlformats-officedocument' +
        '.spreadsheetml.sheet':
          console.log('sd');
          content = filesParser.excel(e.target.result);
        break;
        default: console.log('');
        break;
      }
      deferred.resolve(content);
    };

    return deferred.promise;
  };
});*/

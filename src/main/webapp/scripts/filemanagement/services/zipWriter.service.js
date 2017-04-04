/* global JSZip */
/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('ZipWriterService',
function(FileReaderService, $q) {
  var rootFolderNames;
  var allFiles;
  var createFilesObject = function(file) {
      return FileReaderService.readAsArrayBuffer(file).then(function(result) {
        var pathAsArray;
        if (file.path) {
          pathAsArray = _.split(file.path, '/');
        } else {
          if (file.webkitRelativePath) {
            pathAsArray = _.split(file.webkitRelativePath, '/');
          }
        }
        if (!pathAsArray) {
          pathAsArray = [file.name];
        }
        if (_.last(rootFolderNames) !== pathAsArray[0]) {
          rootFolderNames.push(pathAsArray[0]);
        }
        allFiles.push({'result': result, 'pathAsArray': pathAsArray});
      });
    };
  this.createZipFileAsync = function(files, rootFolderShouldBeRemoved) {
    var deferred = $q.defer();
    var zip = new JSZip();
    rootFolderNames = [];
    allFiles = [];
    var chainedFilesReader = $q.when();

    files.forEach(function(file) {
      chainedFilesReader = chainedFilesReader.then(
        function() {
          return createFilesObject(file);
        }
      );
    });
    chainedFilesReader.finally(function() {
      var pathPosition = 0;
      if (rootFolderShouldBeRemoved) {
        if (rootFolderNames.length === 1) {
          pathPosition = 1;
        }
      }
      allFiles.forEach(function(file) {
        if (rootFolderShouldBeRemoved && rootFolderNames.length === 1) {
          file.pathAsArray.shift();
        }
        var pathToFile = '';
        if (file.pathAsArray.length === 1) {
          zip.file(file.pathAsArray[0], file.result);
        } else {
          var lastPath = _.last(file.pathAsArray);
          file.pathAsArray.pop();
          file.pathAsArray.forEach(function(pathName) {
            pathToFile = pathToFile + pathName + '/';
          });
          zip.folder(pathToFile).file(lastPath, file.result);
        }
      });
      var blob = zip.generate({type: 'blob'});
      deferred.resolve(blob);
    });
    return deferred.promise;
  };
});

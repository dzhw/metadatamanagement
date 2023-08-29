/* global JSZip */
/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('ZipWriterService',
function(FileReaderService, $q, $log) {
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
      ).catch(function(error) {
        var filename;
        if (file.path) {
          filename = file.path;
        } else {
          if (file.webkitRelativePath) {
            filename = file.webkitRelativePath;
          }
        }
        $log.error('Error reading file \'' + filename + '\'');
        return $q.reject(error);
      });
    });
    chainedFilesReader.then(function() {
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
      zip.generateAsync({type: 'blob'}).then(function(blob) {
        if (rootFolderNames && rootFolderNames.length === 1) {
          blob.name =  rootFolderNames[0] + '.zip';
        }
        deferred.resolve(blob);
      }).catch(function(error) {
        $log.error('Error while creating zip file:', error);
        deferred.reject(error);
      });
    }).catch(function(error) {
      $log.error('Error while creating zip file:', error);
      deferred.reject(error);
    });
    return deferred.promise;
  };
});

/* global JSZip */
/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('ZipWriterService',
function(FileReaderService, $q) {
  this.createZipFileAsync = function(files) {
    var deferred = $q.defer();
    var zip = new JSZip();
    var i = 0;
    files.forEach(function(file) {
      FileReaderService.readAsArrayBuffer(file).then(function(result) {
        switch (file.name) {
          case 'Introduction.tex':
            zip.file(file.name, result);
          break;
          case 'Main.tex':
            zip.file(file.name, result);
          break;
          case 'Variable.tex':
            zip.folder('variables').file(file.name, result);
          break;
          case 'References.bib':
            zip.file(file.name, result);
          break;
          default:
            var pathAsArray;
            if (_.hasIn(file, 'path')) {
              pathAsArray = _.split(file.path, '/');
            } else {
              if (_.hasIn(file, 'webkitRelativePath')) {
                var path = _.split(file.webkitRelativePath, '/');
                pathAsArray = _.slice(path, 1, path.length);
              }
            }
            console.log(pathAsArray);
            var pathToFile = '';
            pathAsArray.forEach(function(pathName, index) {
              if (index === (pathAsArray.length - 1)) {
                zip.folder(pathToFile).file(file.name, result);
              } else {
                if (index === 0) {
                  pathToFile = pathName;
                } else {
                  pathToFile = pathAsArray[index - 1] + '/' + pathName;
                }
              }
            });
          break;
        }
        i++;
        if (i === files.length) {
          var blob = zip.generate({type: 'blob'});
          deferred.resolve(blob);
        }
      });
    });
    return deferred.promise;
  };
});

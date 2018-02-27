'use strict';
/*
  Service for loading the last released version of a project.
  @author Daniel Katzberg
*/
angular.module('metadatamanagementApp').service(
  'LastReleasedProjectVersionService',
  function($rootScope, $q, DataAcquisitionProjectLastReleasedVersionResource) {

    var getLastReleasedVersion = function(projectId) {
      var deferred = $q.defer();
      $rootScope.$broadcast('start-ignoring-404');
      DataAcquisitionProjectLastReleasedVersionResource
      .lastReleasedVersion({
        id: projectId
      },
      function(result) {
        var resultStr = '';
        Object.values(result).forEach(function(value) {
          if (typeof value === 'string') {
            resultStr = resultStr.concat(value);
          }
        });
        $rootScope.$broadcast('stop-ignoring-404');
        deferred.resolve(resultStr);
      },
      function() {
        $rootScope.$broadcast('stop-ignoring-404');
        deferred.resolve('0.0.0');
      });
      return deferred.promise;
    };

    //public, global methods definitions.
    return {
      getLastReleasedVersion: getLastReleasedVersion
    };
  });

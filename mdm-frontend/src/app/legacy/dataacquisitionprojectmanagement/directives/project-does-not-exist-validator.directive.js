/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .directive('projectDoesNotExist', ['DataAcquisitionProjectResource', '$q', '$rootScope',  function(DataAcquisitionProjectResource,
      $q, $rootScope) {
    return {
        require: 'ngModel',
        /* jshint -W098 */
        link: function(scope, element, attrs, ngModel) {
            /* jshint +W098 */
            var debouncedCheck = _.debounce(function(viewValue, deferred) {
                $rootScope.$broadcast('start-ignoring-404');
                DataAcquisitionProjectResource.get({id: viewValue})
                  .$promise.then(function(project) {
                    if (project) {
                      ngModel.$setValidity('project-does-not-exist', false);
                      deferred.reject();
                    } else {
                      ngModel.$setValidity('project-does-not-exist', true);
                      deferred.resolve();
                    }
                  }).catch(function(error) {
                  if (error.status === 404) {
                    ngModel.$setValidity('project-does-not-exist', true);
                    deferred.resolve();
                  } else {
                    ngModel.$setValidity('project-does-not-exist', false);
                    deferred.reject();
                  }
                }).finally(function() {
                  $rootScope.$broadcast('stop-ignoring-404');
                });
              }, 250);
            ngModel.$asyncValidators.projectDoesNotExist =
              /* jshint -W098 */
              function(modelValue, viewValue) {
                /* jshint +W098 */
                var deferred = $q.defer();
                debouncedCheck(viewValue, deferred);
                return deferred.promise;
              };
          }
      };
  }]);


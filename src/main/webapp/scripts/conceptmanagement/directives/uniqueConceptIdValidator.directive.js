'use strict';
angular.module('metadatamanagementApp').directive('fdzUniqueConceptId',
  function($q, $log, ConceptResource, $rootScope) {
    return {
      require: 'ngModel',
      link: function($scope, el, attr, ctrl) { // jshint ignore:line
        var queryConceptId = function(id) {
          $rootScope.$broadcast('start-ignoring-404');
          return ConceptResource.get({id: id}).$promise
            .then(function() {
              return $q.reject();
            }, function(error) {
              if (error.status === 404) {
                return $q.resolve();
              } else {
                $log.error('error while resolving concept id ' + id, error);
                return $q.reject(error);
              }
            }).finally(function() {
              $rootScope.$broadcast('stop-ignoring-404');
            });
        };

        var isValidationEnabled = function() {
          return attr.fdzUniqueConceptId === '' ||
            attr.fdzUniqueConceptId === 'true';
        };

        ctrl.$asyncValidators.uniqueConceptId =
          function(modelValue, viewValue) { // jshint ignore:line

          if (ctrl.$isEmpty(modelValue)) {
            return $q.resolve();
          } else if (isValidationEnabled()) {
            return queryConceptId(modelValue);
          } else {
            return $q.resolve();
          }
        };
      }
    };
  });

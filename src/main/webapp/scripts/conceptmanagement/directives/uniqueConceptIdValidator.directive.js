'use strict';
angular.module('metadatamanagementApp').directive('fdzUniqueConceptId',
  function($q, $log, ConceptResource) {
    return {
      require: 'ngModel',
      link: function($scope, el, attr, ctrl) { // jshint ignore:line
        var queryConceptId = function(id) {
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
            });
        };

        var isValidationEnabled = function() {
          return attr.fdzUniqueConceptId === '' ||
            attr.fdzUniqueConceptId === 'true';
        };

        ctrl.$asyncValidators.uniqueConceptId =
          function(modelValue, viewValue) { // jshint ignore:line

          if (ctrl.$isEmpty(viewValue)) {
            return $q.resolve();
          } else if (isValidationEnabled()) {
            return queryConceptId(viewValue);
          } else {
            return $q.resolve();
          }
        };
      }
    };
  });

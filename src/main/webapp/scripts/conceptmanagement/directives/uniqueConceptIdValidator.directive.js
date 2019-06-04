/* globals _ */
'use strict';
angular.module('metadatamanagementApp').directive('fdzUniqueConceptId',
  function ($q, $log, ConceptResource) {
    return {
      require: 'ngModel',
      link: function ($scope, el, attr, ctrl) {
        ctrl.$asyncValidators.uniqueConceptId = function (modelValue, viewValue) {
          if (ctrl.$isEmpty(viewValue)) {
            return $q.resolve();
          } else {
            return ConceptResource.query({id: viewValue}).$promise
              .then(function (result) {
                var queryResult = _.get(result, '_embedded.concepts');
                return queryResult.length === 0 ? $q.resolve() : $q.reject();
              }, function (e) {
                $log.error(e);
                return $q.reject(e);
              });
          }
        }
      }
    }
  });

/* global _ */
'use strict';

angular
  .module('metadatamanagementApp')
  .service('LocationSimplifier', ['$q',  function($q) {
    // we need to remove $ from the location for twitter autolinking
    this.removeDollarSign = function(state, stateParams, stateName) {
      if (_.endsWith(stateParams.id, '$')) {
        var stateParamsCopy = angular.copy(stateParams);
        stateParamsCopy.id = stateParamsCopy.id.slice(
          0, stateParams.id.length - 1);
        state.go(stateName, stateParamsCopy, {reload: true, supercede: true});
        return $q.reject('Redirect to state without $');
      }
      return $q.resolve();
    };

    // we need to ensure that out ids have the $ at the end
    this.ensureDollarSign = function(id) {
      if (!_.endsWith(id, '$')) {
        id = id + '$';
      }
      return id;
    };
  }]);


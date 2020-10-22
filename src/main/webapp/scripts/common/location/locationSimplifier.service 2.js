/* global _ */
'use strict';

angular
  .module('metadatamanagementApp')
  .service('LocationSimplifier', function($location) {
    // we need to remove $ from the location for twitter autolinking
    this.removeDollarSign = function() {
      var path = $location.path();
      if (_.endsWith(path, '$')) {
        $location.path(path.slice(0, path.length - 1));
      }
    };

    // we need to ensure that out ids have the $ at the end
    this.ensureDollarSign = function(id) {
      if (!_.endsWith(id, '$')) {
        id = id + '$';
      }
      return id;
    };
  });

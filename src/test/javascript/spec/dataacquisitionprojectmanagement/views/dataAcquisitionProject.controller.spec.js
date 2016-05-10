/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global mockApis */
/* global spyOn */

'use strict';

describe('Controllers Tests ', function() {
  var $scope;
  var $q;
  var DataAcquisitionProjectCollectionResource;
  var createController;
  var result = {
    'page': {
      'totalElements': 2
    },
    '_embedded': {
      'dataAcquisitionProjects': []
    }
  };
  beforeEach(mockApis);
  beforeEach(function() {
    inject(function($controller, _$rootScope_,
      _DataAcquisitionProjectCollectionResource_, _$q_) {
      $scope = _$rootScope_.$new();
      $q = _$q_;
      DataAcquisitionProjectCollectionResource = {
        query: function(param, callback) {
          var deferred = $q.defer();
          callback(result);
          return deferred.promise;
        }
      };

      var locals = {
        '$scope': $scope,
        'DataAcquisitionProjectCollectionResource':
        DataAcquisitionProjectCollectionResource
      };
      createController = function() {
        return $controller('DataAcquisitionProjectController',
          locals);
      };

    });
  });

  describe('DataAcquisitionProjectController', function() {
    beforeEach(function() {
      spyOn(DataAcquisitionProjectCollectionResource, 'query')
      .and.callThrough();
      createController();
    });
    it('should call DataAcquisitionProject.query', function() {
      DataAcquisitionProjectCollectionResource
      .query.and.returnValue($q.resolve());
      $scope.loadAll();
      expect($scope.totalItems).toEqual(2);
      expect($scope.dataAcquisitionProjects).toEqual([]);
    });
  });
});

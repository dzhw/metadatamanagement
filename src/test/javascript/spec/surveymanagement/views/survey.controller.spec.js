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
  var SurveyCollectionResource;
  var createController;
  var result = {
    'page': {
      'totalElements': 2
    },
    '_embedded': {
      'surveys': []
    }
  };
  beforeEach(mockApis);
  beforeEach(function() {
    inject(function($controller, _$rootScope_, _$q_) {
      $scope = _$rootScope_.$new();
      $q = _$q_;
      SurveyCollectionResource = {
        query: function(param, callback) {
          var deferred = $q.defer();
          callback(result);
          return deferred.promise;
        }
      };

      var locals = {
        '$scope': $scope,
        'SurveyCollectionResource': SurveyCollectionResource
      };
      createController = function() {
        return $controller('SurveyController', locals);
      };

    });
  });
  describe('SurveyController', function() {
    beforeEach(function() {
      spyOn(SurveyCollectionResource, 'query').and.callThrough();
      createController();
    });
    it('should get all surveys', function() {
      SurveyCollectionResource.query.and.returnValue($q.resolve());
      $scope.loadAll();
      expect($scope.totalItems).toEqual(2);
      expect($scope.surveys).toEqual([]);
    });
  });
});

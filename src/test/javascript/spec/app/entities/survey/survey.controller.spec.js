'use strict';

describe('Controllers Tests ', function () {
  var $scope, $q, SurveyCollection, createController;
  var result = {
    'page' : {
      'totalElements':2
    },
    '_embedded' : {
      'surveys':[]
    }
  };
  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(function() {
    inject(function($controller, _$rootScope_, _$q_) {
      $scope = _$rootScope_.$new();
      $q = _$q_;
      SurveyCollection = {
        query: function(param,callback) {
        var deferred = $q.defer();
        callback(result);
        return deferred.promise;
      }
    };

      var locals = {
        '$scope' : $scope,
        'SurveyCollection' : SurveyCollection
      };
      createController = function() {
        return $controller('SurveyController', locals);
      };

    });
   });
   describe('SurveyController',function(){
     beforeEach(function(){
         spyOn(SurveyCollection, 'query').and.callThrough();
         createController();
     });
     it('should call FdzProject.query',function(){
      SurveyCollection.query.and.returnValue($q.resolve());
      $scope.loadAll();
      expect($scope.totalItems).toEqual(2);
      expect($scope.surveys).toEqual([]);
     });
   });
});

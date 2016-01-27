'use strict';

describe('Controllers Tests ', function () {
  var $scope, $q,FdzProjectCollection, createController;
  var result = {
    'page' : {
      'totalElements':2
    },
    '_embedded' : {
      'fdzProjects':[]
    }
  };
  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(function() {
    inject(function($controller, _$rootScope_, _FdzProjectCollection_, _$q_) {
      $scope = _$rootScope_.$new();
      $q = _$q_;
       FdzProjectCollection = {
        query: function(param,callback) {
        var deferred = $q.defer();
        callback(result);
        return deferred.promise;
      }
    };

      var locals = {
        '$scope' : $scope,
        'FdzProjectCollection' : FdzProjectCollection
      };
      createController = function() {
        return $controller('FdzProjectController', locals);
      };

    });
   });
   describe('FdzProjectController',function(){
     beforeEach(function(){
         spyOn(FdzProjectCollection, 'query').and.callThrough();
         createController();
     });
     it('should call FdzProject.query',function(){
      FdzProjectCollection.query.and.returnValue($q.resolve());
      $scope.loadAll();
      expect($scope.totalItems).toEqual(2);
      expect($scope.fdzProjects).toEqual([]);
     });
   });
});

'use strict';

describe('Controllers Tests ', function () {
  var $scope, $q,DataAcquisitionProjectCollection, createController;
  var result = {
    'page' : {
      'totalElements':2
    },
    '_embedded' : {
      'dataAcquisitionProjects':[]
    }
  };
  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(function() {
    inject(function($controller, _$rootScope_, _DataAcquisitionProjectCollection_, _$q_) {
      $scope = _$rootScope_.$new();
      $q = _$q_;
       DataAcquisitionProjectCollection = {
        query: function(param,callback) {
        var deferred = $q.defer();
        callback(result);
        return deferred.promise;
      }
    };

      var locals = {
        '$scope' : $scope,
        'DataAcquisitionProjectCollection' : DataAcquisitionProjectCollection
      };
      createController = function() {
        return $controller('DataAcquisitionProjectController', locals);
      };

    });
   });
  //TODO DKatzberg activate Test
   xdescribe('DataAcquisitionProjectController',function(){
     beforeEach(function(){
         spyOn(DataAcquisitionProjectCollection, 'query').and.callThrough();
         createController();
     });
     it('should call DataAcquisitionProject.query',function(){
      DataAcquisitionProjectCollection.query.and.returnValue($q.resolve());
      $scope.loadAll();
      expect($scope.totalItems).toEqual(2);
      expect($scope.dataAcquisitionProjects).toEqual([]);
     });
   });
});

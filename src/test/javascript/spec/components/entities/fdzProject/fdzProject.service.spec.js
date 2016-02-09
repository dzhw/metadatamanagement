'use strict';

describe('Factory Tests ', function() {
  var FdzProject, $httpMock, $resource, $q, $scope;
  xdescribe('FdzProject', function() {
    /*beforeEach(module(function ($provide) {
          $httpMock = jasmine.createSpyObj('$http',['get']);
          $provide.value('$http', $httpMock);
    }));*/

    beforeEach(inject(function($injector) {
      FdzProject = $injector.get('FdzProject');
      $q = $injector.get('$q');
      $resource = $injector.get('$resource');
      $scope = $injector.get('$rootScope').$new();
    }));
    it(' should make a GET request', function() {
      var data = {
        'data': 'admin'
      };
      var defer = $q.defer();
      defer.resolve(data);
      $httpMock.get.and.returnValue(defer.promise);

      $scope.$digest();
      expect($httpMock.get).toHaveBeenCalled();
    });
  });
});

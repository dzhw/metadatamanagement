'use strict';

xdescribe('Factory Tests ', function() {
  var DataAcquisitionProject, $httpMock, $resource, $q, $scope;
  describe('DataAcquisitionProject', function() {
    /*beforeEach(module(function ($provide) {
          $httpMock = jasmine.createSpyObj('$http',['get']);
          $provide.value('$http', $httpMock);
    }));*/

    beforeEach(inject(function($injector) {
      DataAcquisitionProject = $injector.get('DataAcquisitionProject');
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

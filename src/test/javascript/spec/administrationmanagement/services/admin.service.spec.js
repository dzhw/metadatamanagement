'use strict';

describe('Factory Tests ', function () {
  var AdminService, $httpMock, $q, $scope;
    describe('AdminService', function() {
      beforeEach(module(function ($provide) {
          $httpMock = jasmine.createSpyObj('$http',['post']);
          $provide.value('$http', $httpMock);
    }));
        beforeEach(inject(function($injector) {
            AdminService = $injector.get('AdminService');
            $q = $injector.get('$q');
            $scope = $injector.get('$rootScope').$new();
        }));
        it('AdminService.recreateAllElasticsearchIndices should be defined', function() {
          expect(AdminService.recreateAllElasticsearchIndices).toBeDefined();
        });
        it(' should make a POST request', function() {
          var data = {
            'data': 'admin'
          };
          var defer = $q.defer();
          defer.resolve(data);
          $httpMock.post.and.returnValue(defer.promise);
          AdminService.recreateAllElasticsearchIndices();
          $scope.$digest();
          expect($httpMock.post).toHaveBeenCalled();
        });
      });
    });

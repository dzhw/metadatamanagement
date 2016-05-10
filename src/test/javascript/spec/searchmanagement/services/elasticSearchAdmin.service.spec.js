/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global jasmine */

'use strict';

describe('Factory Tests ', function() {
  var ElasticSearchAdminService;
  var $httpMock;
  var $q;
  var $scope;
  describe('ElasticSearchAdminService', function() {
    beforeEach(module(function($provide) {
      $httpMock = jasmine.createSpyObj('$http', ['post']);
      $provide.value('$http', $httpMock);
    }));
    beforeEach(inject(function($injector) {
          ElasticSearchAdminService = $injector
          .get('ElasticSearchAdminService');
          $q = $injector.get('$q');
          $scope = $injector.get('$rootScope').$new();
        }));
    it('ElasticSearchAdminService.recreateAllElasticsearchIndices' +
        ' should be defined', function() {
          expect(ElasticSearchAdminService.recreateAllElasticsearchIndices)
          .toBeDefined();
        });
    it(' should make a POST request', function() {
          var data = {
            'data': 'admin'
          };
          var defer = $q.defer();
          defer.resolve(data);
          $httpMock.post.and.returnValue(defer.promise);
          ElasticSearchAdminService.recreateAllElasticsearchIndices();
          $scope.$digest();
          expect($httpMock.post).toHaveBeenCalled();
        });
  });
});

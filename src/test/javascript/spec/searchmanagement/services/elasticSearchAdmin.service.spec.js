/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global mockSso */

'use strict';

describe('Factory Tests ', function() {
  var ElasticSearchAdminService;
  var $q;
  var $scope;
  var $httpBackend;
  describe('ElasticSearchAdminService', function() {
    beforeEach(mockSso);
    beforeEach(inject(function($injector) {
      ElasticSearchAdminService = $injector
        .get('ElasticSearchAdminService');
      $q = $injector.get('$q');
      $scope = $injector.get('$rootScope').$new();
      $httpBackend = $injector.get('$httpBackend');
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
      var result;
      $httpBackend.whenPOST(/api\/search\/recreate/).respond(defer.promise);
      ElasticSearchAdminService.recreateAllElasticsearchIndices().then(
        function(data) {
          result = data;
        });
      $httpBackend.flush();
      expect(result).toEqual(data);
    });
  });
});

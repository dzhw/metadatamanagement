'use strict';

describe('Factory Tests ', function() {
  var ElasticSearchClient, $scope;
  describe('ElasticSearchClient', function() {
    beforeEach(inject(function($injector) {
      ElasticSearchClient = $injector.get('ElasticSearchClient');
      $scope = $injector.get('$rootScope').$new();
    }));
    it('ElasticSearchClient should be defined', function() {
      expect(ElasticSearchClient).toBeDefined();
    });
  });
});

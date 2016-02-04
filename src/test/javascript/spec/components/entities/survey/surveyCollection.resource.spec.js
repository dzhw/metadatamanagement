'use strict';

describe('Factory Tests ', function() {
  var SurveyCollection, $scope;
  describe('FdzProject', function() {
    beforeEach(inject(function($injector) {
      SurveyCollection = $injector.get('SurveyCollection');
      $scope = $injector.get('$rootScope').$new();
    }));
    it('SurveyCollection should be defined', function() {
      expect(SurveyCollection).toBeDefined();
    });
  });
});

/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */

'use strict';

describe('Factory Tests ', function() {
  var SurveyCollectionResource;
  var $scope;
  describe('DataAcquisitionProject', function() {
    beforeEach(inject(function($injector) {
      SurveyCollectionResource = $injector.get('SurveyCollectionResource');
      $scope = $injector.get('$rootScope').$new();
    }));
    it('SurveyCollection should be defined', function() {
      expect(SurveyCollectionResource).toBeDefined();
    });
  });
});

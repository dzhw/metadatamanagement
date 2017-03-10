/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global mockApis */

'use strict';

describe('Survey', function() {
  var mockSurveyResource;
  var $httpBackend;
  var data = {
    id: 1,
    surveyname: 'test',
    fieldPeriod: {
      start: '12-12-2012',
      end: '12-12-2012'
    }
  };
  beforeEach(mockApis);
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    mockSurveyResource = $injector.get('SurveyResource');
  }));
  describe('getSurvey', function() {
    it('should call get with id', inject(function() {
      $httpBackend.expectGET(
        /api\/surveys\?projection=complete/).respond(
        data);

      var result = mockSurveyResource.get(1);
      try {
        $httpBackend.flush();
      } catch (e) {

      }
      expect(result.surveyname).toEqual('test');
    }));
    it('should put', inject(function() {
      $httpBackend.when('PUT',
        /api\/surveys\/1/).respond(
        data);

      var result = mockSurveyResource.save(data);
      $httpBackend.flush();
      expect(result.surveyname).toEqual('test');
    }));
  });
});

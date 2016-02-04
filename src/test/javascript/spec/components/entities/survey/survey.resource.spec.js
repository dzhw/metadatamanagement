describe('Survey', function() {
  var mockSurveyResource, $httpBackend;
  var data = {
    id: 1,
    surveyname: 'test',
    fieldPeriod: {
      start: '12-12-2012',
      end: '12-12-2012'
    }
  };
  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    mockSurveyResource = $injector.get('Survey');
  }));
  describe('getSurvey', function() {
    it('should call get with id', inject(function(Survey) {
      $httpBackend.expectGET(
        /api\/surveys\?cacheBuster=\d+&projection=complete/).respond(
        data);

      var result = mockSurveyResource.get(1);
      try {
        $httpBackend.flush();
      } catch (e) {

      }
      expect(result.surveyname).toEqual('test');
    }));
    it('should put', inject(function(Survey) {
      $httpBackend.expectPUT(
        /api\/surveys\?cacheBuster=\d+&projection=complete/).respond(
        data);

      var result = mockSurveyResource.save(data);
      try {
        $httpBackend.flush();
      } catch (e) {

      }
      expect(result.surveyname).toEqual('test');
    }));
  });
});

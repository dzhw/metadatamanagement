/* global describe */
/* global beforeEach */
/* global inject */
/* global it */
/* global expect */
/* global mockApis */

'use strict';

describe('SurveyDeleteResource', function() {
  var mockSurveyDeleteResource;
  var $httpBackend;
  var data = {
    id: 1,
    name: 'DeletedSurvey'
  };
  beforeEach(mockApis);
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    mockSurveyDeleteResource = $injector.
    get('SurveyDeleteResource');
    $httpBackend.expectPOST(
      /api\/surveys\/delete/)
      .respond(data);
  }));
  it('should return deleted AtomicQuestion resource ', function() {
    var deletedSurvey =
    mockSurveyDeleteResource.deleteByDataAcquisitionProjectId('RDC');
    $httpBackend.flush();
    expect(deletedSurvey.name).toEqual('DeletedSurvey');
  });
});

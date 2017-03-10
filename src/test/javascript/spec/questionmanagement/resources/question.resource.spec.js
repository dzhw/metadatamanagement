/* global describe */
/* global beforeEach */
/* global inject */
/* global it */
/* global expect */
/* global mockApis */

'use strict';

describe('QuestionResource', function() {
  var mockQuestionResource;
  var $httpBackend;
  var data = {
    id: 1,
    name: 'QuestionResource'
  };
  beforeEach(mockApis);
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    mockQuestionResource = $injector.get('QuestionResource');
    $httpBackend.expectGET(
      /api\/questions\?projection=complete/)
      .respond(data);
  }));
  it('should return QuestionResource resource ', function() {
    var resource =  mockQuestionResource.get(1);
    $httpBackend.flush();
    expect(resource.name).toEqual('QuestionResource');
  });
});

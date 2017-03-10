/* global describe */
/* global beforeEach */
/* global inject */
/* global it */
/* global expect */
/* global mockApis */

'use strict';

describe('QuestionCollectionResource', function() {
  var mockQuestionCollectionResource;
  var $httpBackend;
  var data = {
    id: 1,
    name: 'QuestionCollectionResource'
  };
  beforeEach(mockApis);
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    mockQuestionCollectionResource =
    $injector.get('QuestionCollectionResource');
    $httpBackend.expectGET(
      /api\/questions\?projection=complete/)
      .respond(data);
  }));
  it('should return QuestionCollectionResource resource ', function() {
    var resource =  mockQuestionCollectionResource.get(1);
    $httpBackend.flush();
    expect(resource.name).toEqual('QuestionCollectionResource');
  });
});

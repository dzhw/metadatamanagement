/* global describe */
/* global beforeEach */
/* global inject */
/* global it */
/* global expect */
/* global mockApis */

'use strict';

describe('AtomicQuestionCollectionResource', function() {
  var mockAtomicQuestionCollectionResource;
  var $httpBackend;
  var data = {
    id: 1,
    name: 'AtomicQuestionCollectionResource'
  };
  beforeEach(mockApis);
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    mockAtomicQuestionCollectionResource =
    $injector.get('AtomicQuestionCollectionResource');
    $httpBackend.expectGET(
      /api\/atomic-questions\?cacheBuster=\d+&projection=complete/)
      .respond(data);
  }));
  it('should return AtomicQuestionCollectionResource resource ', function() {
    var resource =  mockAtomicQuestionCollectionResource.get(1);
    $httpBackend.flush();
    expect(resource.name).toEqual('AtomicQuestionCollectionResource');
  });
});

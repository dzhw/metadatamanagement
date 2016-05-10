/* global describe */
/* global beforeEach */
/* global inject */
/* global it */
/* global expect */
/* global mockApis */

'use strict';

describe('AtomicQuestionResource', function() {
  var mockAtomicQuestionResource;
  var $httpBackend;
  var data = {
    id: 1,
    name: 'AtomicQuestionResource'
  };
  beforeEach(mockApis);
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    mockAtomicQuestionResource = $injector.get('AtomicQuestionResource');
    $httpBackend.expectGET(
      /api\/atomic-questions\?cacheBuster=\d+&projection=complete/)
      .respond(data);
  }));
  it('should return AtomicQuestionResource resource ', function() {
    var resource =  mockAtomicQuestionResource.get(1);
    $httpBackend.flush();
    expect(resource.name).toEqual('AtomicQuestionResource');
  });
});

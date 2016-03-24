/* global describe */
/* global beforeEach */
/* global inject */
/* global it */
/* global expect */
/* global mockApis */

'use strict';

describe('AtomicQuestion', function() {
  var mockAtomicQuestionResource;
  var $httpBackend;
  var data = {
    id: 1,
    name: 'AtomicQuestion'
  };
  beforeEach(mockApis);
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    mockAtomicQuestionResource = $injector.get('AtomicQuestion');
    $httpBackend.expectGET(
      /api\/atomic_questions\?cacheBuster=\d+&projection=complete/)
      .respond(data);
  }));
  it('should return AtomicQuestion resource ', function() {
    var resource =  mockAtomicQuestionResource.get(1);
    $httpBackend.flush();
    expect(resource.name).toEqual('AtomicQuestion');
  });
});

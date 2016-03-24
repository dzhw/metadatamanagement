/* global describe */
/* global beforeEach */
/* global inject */
/* global it */
/* global expect */
/* global mockApis */

'use strict';

describe('AtomicQuestionCollection', function() {
  var mockAtomicQuestionCollectionResource;
  var $httpBackend;
  var data = {
    id: 1,
    name: 'AtomicQuestionCollection'
  };
  beforeEach(mockApis);
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    mockAtomicQuestionCollectionResource =
    $injector.get('AtomicQuestionCollection');
    $httpBackend.expectGET(
      /api\/atomic_questions\?cacheBuster=\d+&projection=complete/)
      .respond(data);
  }));
  it('should return AtomicQuestionCollection resource ', function() {
    var resource =  mockAtomicQuestionCollectionResource.get(1);
    $httpBackend.flush();
    expect(resource.name).toEqual('AtomicQuestionCollection');
  });
});

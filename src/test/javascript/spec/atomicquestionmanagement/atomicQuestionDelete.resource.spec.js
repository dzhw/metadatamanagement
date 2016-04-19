/* global describe */
/* global beforeEach */
/* global inject */
/* global it */
/* global expect */
/* global mockApis */

'use strict';

describe('AtomicQuestionDeleteResource', function() {
  var mockAtomicQuestionDeleteResource;
  var $httpBackend;
  var data = {
    id: 1,
    name: 'DeletedAtomicQuestion'
  };
  beforeEach(mockApis);
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    mockAtomicQuestionDeleteResource = $injector.
    get('AtomicQuestionDeleteResource');
    $httpBackend.expectPOST(
      /api\/atomic-questions\/delete\?cacheBuster=\d/)
      .respond(data);
  }));
  it('should return deleted AtomicQuestion resource ', function() {
    var deletedAtomicQuestion =
    mockAtomicQuestionDeleteResource.deleteByDataAcquisitionProjectId('RDC');
    $httpBackend.flush();
    expect(deletedAtomicQuestion.name).toEqual('DeletedAtomicQuestion');
  });
});

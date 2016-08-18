/* global describe */
/* global beforeEach */
/* global inject */
/* global it */
/* global expect */
/* global mockApis */

'use strict';

describe('QuestionDeleteResource', function() {
  var mockQuestionDeleteResource;
  var $httpBackend;
  var data = {
    id: 1,
    name: 'DeletedQuestionResource'
  };
  beforeEach(mockApis);
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    mockQuestionDeleteResource = $injector.
    get('QuestionDeleteResource');
    $httpBackend.expectPOST(
      /api\/questions\/delete\?cacheBuster=\d/)
      .respond(data);
  }));
  it('should return deleted QuestionResource ', function() {
    var deletedQuestion =
    mockQuestionDeleteResource.deleteByDataAcquisitionProjectId('RDC');
    $httpBackend.flush();
    expect(deletedQuestion.name).toEqual('DeletedQuestionResource');
  });
});

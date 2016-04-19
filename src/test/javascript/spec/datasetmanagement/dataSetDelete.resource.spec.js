/* global describe */
/* global beforeEach */
/* global inject */
/* global it */
/* global expect */
/* global mockApis */

'use strict';

describe('DataSetDeleteResource', function() {
  var mockDataSetDeleteResource;
  var $httpBackend;
  var data = {
    id: 1,
    name: 'DeletedDataSet'
  };
  beforeEach(mockApis);
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    mockDataSetDeleteResource = $injector.
    get('DataSetDeleteResource');
    $httpBackend.expectPOST(
      /api\/data-sets\/delete\?cacheBuster=\d/)
      .respond(data);
  }));
  it('should return deleted DataSet resource ', function() {
    var deletedDataSet =
    mockDataSetDeleteResource.deleteByDataAcquisitionProjectId('RDC');
    $httpBackend.flush();
    expect(deletedDataSet.name).toEqual('DeletedDataSet');
  });
});

/* global describe */
/* global beforeEach */
/* global inject */
/* global it */

'use strict';

xdescribe('VariablesParser', function() {
  var DataSetsParser;
  var data = [{name: 'test.xlsx'}];
  beforeEach(inject(function($injector) {
    DataSetsParser = $injector.get('DataSetsParser');
  }));
  it('should return DataSet resource ', function() {
  DataSetsParser.getDataSets(data,'rdc');
});
});

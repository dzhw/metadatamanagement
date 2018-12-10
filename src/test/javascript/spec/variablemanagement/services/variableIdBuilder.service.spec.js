/* global describe */
/* global beforeEach */
/* global it */
/* global inject */
/* global expect */
/* global module */
'use strict';


describe('variableIdBuilderService', function() {
  var VariableIdBuilderService;

  beforeEach(module('metadatamanagementApp'));

  beforeEach(inject(function(_VariableIdBuilderService_) {
    VariableIdBuilderService = _VariableIdBuilderService_;
  }));

  it('should create a variable id', function() {
    var id = VariableIdBuilderService.buildVariableId('gra2005', 'gra42',
      'var23');
    expect(id).toEqual('var-gra2005-dsgra42-var23$');
  });
});
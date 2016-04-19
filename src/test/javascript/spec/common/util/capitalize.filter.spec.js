/*global it:false */
/*global beforeEach:false */
/*global expect:false */
/*global inject:false */
/*global afterEach:false */
/*global describe:false */

/* @author: Daniel Katzberg */

'use strict';

describe('Filter Tests: capitalized ', function() {
  var capitalizedFilter;

  //Before Tests
  beforeEach(module('metadatamanagementApp'));
  beforeEach(inject(function(_capitalizeFilter_) {
    capitalizedFilter = _capitalizeFilter_;
  }));

  //After all tests
  afterEach(function() {});

  // TESTS
  it('capitalized filter with string', function() {
      expect(capitalizedFilter('this iS a tESt STRing'))
      .toBe('This is a test string');
    });
});

/*global it:false */
/*global beforeEach:false */
/*global expect:false */
/*global inject:false */
/*global afterEach:false */
/*global describe:false */

/* @author: Daniel Katzberg */
'use strict';

describe('Filter Test: truncate', function() {
  var charactersFilter;
  var wordsFilter;

  //Before every Test
  beforeEach(inject(function(_charactersFilter_, _wordsFilter_) {
    charactersFilter = _charactersFilter_;
    wordsFilter = _wordsFilter_;
  }));

  // After each Test
  afterEach(function() {});

  // TESTS THE CHARACTERS FILTER
  it('Test the characters Filter', function() {
    expect(charactersFilter('InputTest', 5, false)).toBe('Input...');
  });

  it('Test the characters Filter with 0 chars', function() {
    expect(charactersFilter('InputTest', 0, false)).toBe('');
  });

  it('Test the characters Filter with NaN chars', function() {
    expect(charactersFilter('InputTest', 'One', false)).toBe('InputTest');
  });

  it('Test the characters Filter no break but space', function() {
    expect(charactersFilter('Input Test', 7, false)).toBe('Input...');
  });

  it('Test the characters Filter with break and space', function() {
    expect(charactersFilter('Input Test ', 6, true)).toBe('Input...');
  });

  it('Test the characters Filter inputlength < chars', function() {
    expect(charactersFilter('Input Test', 16, true)).toBe('Input Test');
  });

  //TEST THE WORD FILTER
  it('Test the words filter with NaN words', function() {
    expect(wordsFilter('Input Test', 'One')).toBe('Input Test');
  });

  it('Test the words filter with zero words', function() {
    expect(wordsFilter('Input Test', 0)).toBe('');
  });

  it('Test the words filter with NaN words', function() {
    expect(wordsFilter('Input Test', 1)).toBe('Input...');
  });
});

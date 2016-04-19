'use strict';

describe('Test Service Parse-Links', function() {
  var parseLinks;

  beforeEach(inject(function(ParseLinks) {
    parseLinks = ParseLinks;
  }));

  afterEach(function() {});
  it('test Parse-Links', function() {
    var header = 'part1;part11,part2;part22';
    var returnValue = parseLinks.parse(header);
    expect(returnValue).toEqual({ part11: undefined, part22: undefined });
  });
  it('should throw section could not be split on ";"', function() {
    var header = 'part1;part2;part3;part4';
    expect(function () {
        parseLinks.parse(header);
       }).toThrow(new Error('section could not be split on ";"'));
  });
  it('should throw input must not be of zero length', function() {
    var header = '';
    expect(function () {
        parseLinks.parse(header);
       }).toThrow(new Error('input must not be of zero length'));
    });
});

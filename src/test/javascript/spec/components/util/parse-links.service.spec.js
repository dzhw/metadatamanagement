/*global it:false */
/*global beforeEach:false */
/*global expect:false */
/*global inject:false */
/*global afterEach:false */
/*global describe:false */
/*global spyOn:false */
/*global jasmine:false */

/* @author: Daniel Katzberg */
'use strict';

describe('Test Service Parse-Links', function() {
  var mock;
  var parseLinks;

  //Before every Test
  beforeEach(inject(function(ParseLinks) {
    parseLinks = ParseLinks;
    mock = {alert: jasmine.createSpy()};
  }));

  //After every Test
  afterEach(function() {});

  //TESTS
  it('test Parse-Links', function() {
    spyOn(parseLinks, 'parse').and.callThrough();
    var header = 'part1;part11,part2;part22';
    var returnValue = parseLinks.parse(header);

    expect(returnValue).toEqual({ part11: undefined, part22: undefined });
    expect(parseLinks.parse).toHaveBeenCalled();
  });

});

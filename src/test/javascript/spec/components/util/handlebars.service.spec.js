/*global jasmine:false */
/*global it:false */
/*global beforeEach:false */
/*global expect:false */
/*global inject:false */
/*global afterEach:false */
/*global describe:false */
/*global spyOn:false */

/* @author: Daniel Katzberg */
'use strict';

describe('Test Service: Handlebars', function() {
  var handlebars;
  var mock;

  //Before every test
  beforeEach(inject(function(HandlebarsService) {
    handlebars = HandlebarsService;
    mock = {alert: jasmine.createSpy()};
  }));

  //After every test
  afterEach(function() {});

  //TESTS
  it('function translate', function() {
    spyOn(handlebars.helpers, 'translate').and.callThrough();
    var options = {};
    var hashVar = {};
    options.hash = hashVar;
    options.hash.prefix = 'en';
    options.hash.key = 'testKey';
    handlebars.helpers.translate(options);
    expect(handlebars.helpers.translate).toHaveBeenCalled();
  });

  it('function translate with null prefix', function() {
    spyOn(handlebars.helpers, 'translate').and.callThrough();
    var options = {};
    var hashVar = {};
    options.hash = hashVar;
    options.hash.prefix = null;
    options.hash.key = 'testKey';
    handlebars.helpers.translate(options);
    expect(handlebars.helpers.translate).toHaveBeenCalled();
  });

  it('function encodeMissing with true', function() {
    spyOn(handlebars.helpers, 'encodeMissing').and.callThrough();
    var returnValue = handlebars.helpers.encodeMissing(true);
    expect(handlebars.helpers.encodeMissing).toHaveBeenCalled();
    expect(returnValue).toBe('M');
  });

  it('function encodeMissing with false', function() {
    spyOn(handlebars.helpers, 'encodeMissing').and.callThrough();
    var returnValue = handlebars.helpers.encodeMissing(false);
    expect(handlebars.helpers.encodeMissing).toHaveBeenCalled();
    expect(returnValue).toBe('');
  });

});

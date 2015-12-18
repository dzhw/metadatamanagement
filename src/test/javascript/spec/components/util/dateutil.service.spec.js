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

describe('Tes Filter: DateUtil', function() {
  var dateUtils;
  var mock;

  //Before every test
  beforeEach(inject(function(DateUtils) {
    dateUtils = DateUtils;
    mock = {alert: jasmine.createSpy()};
  }));

  //After all tests
  afterEach(function() {});

  //TESTS
  it('convert local date to server', function() {
    spyOn(dateUtils, 'convertLocaleDateToServer').and.callThrough();
    var date = new Date();
    dateUtils.convertLocaleDateToServer(date);
    expect(dateUtils.convertLocaleDateToServer).toHaveBeenCalled();
  });

  it('convert local date to server with null', function() {
    spyOn(dateUtils, 'convertLocaleDateToServer').and.callThrough();
    var returnValue = dateUtils.convertLocaleDateToServer(null);
    expect(returnValue).toBe(null);
    expect(dateUtils.convertLocaleDateToServer).toHaveBeenCalled();
  });

  it('convert local date from server', function() {
    spyOn(dateUtils, 'convertLocaleDateFromServer').and.callThrough();
    var date = '2015-10-31';
    dateUtils.convertLocaleDateFromServer(date);
    expect(dateUtils.convertLocaleDateFromServer).toHaveBeenCalled();
  });

  it('convert local date from server with null', function() {
    spyOn(dateUtils, 'convertLocaleDateFromServer').and.callThrough();
    var returnValue = dateUtils.convertLocaleDateFromServer(null);
    expect(returnValue).toBe(null);
    expect(dateUtils.convertLocaleDateFromServer).toHaveBeenCalled();
  });

  it('convert date time from server', function() {
    spyOn(dateUtils, 'convertDateTimeFromServer').and.callThrough();
    var date = '2015-11-30T12:34:56';
    dateUtils.convertDateTimeFromServer(date);
    expect(dateUtils.convertDateTimeFromServer).toHaveBeenCalled();
  });

  it('convert date time from server with null', function() {
    spyOn(dateUtils, 'convertDateTimeFromServer').and.callThrough();
    var returnValue = dateUtils.convertDateTimeFromServer(null);
    expect(returnValue).toBe(null);
    expect(dateUtils.convertDateTimeFromServer).toHaveBeenCalled();
  });

});

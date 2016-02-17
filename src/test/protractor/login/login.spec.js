/* global browser */
/* global it */
/* global describe */
/* global expect */
/* global afterAll */
/* @Author Daniel Katzberg */
'use strict';

describe('Test login', function () {

  var loginHelper = require('../utils/loginHelper');
  var cacheHelper = require('../utils/cacheHelper');

  afterAll(cacheHelper.clearCache);

  it('Login in german with admin', function () {
    var languagePath = '#/de/';

    //Welcome Page
    browser.get(languagePath);
    expect(browser.getCurrentUrl()).toEqual(
      browser.baseUrl + '#/de/');

    //'Login'
    loginHelper.login();

    //'Logout'
    loginHelper.logout();
  });

  it('Login in english with admin', function () {
    var languagePath = '#/en/';

    //Welcome Page
    browser.get(languagePath);
    expect(browser.getCurrentUrl()).toEqual(
      browser.baseUrl + '#/en/');

    //'Login'
    loginHelper.login();

    //'Logout'
    loginHelper.logout();
  });
});

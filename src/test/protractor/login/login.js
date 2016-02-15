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

  it('Login in german with admin', function (done) {
    var languagePath = '#/de/';

    //Welcome Page
    browser.get(languagePath).then(done);
    expect(browser.getCurrentUrl()).toEqual(
      'https://metadatamanagement.cfapps.io/#/de/');

    //'Login'
    loginHelper.login(languagePath);

    //'Logout'
    loginHelper.logout(languagePath);
  });

  it('Login in english with admin', function (done) {
    var languagePath = '#/en/';

    //Welcome Page
    browser.get(languagePath).then(done);
    expect(browser.getCurrentUrl()).toEqual(
      'https://metadatamanagement.cfapps.io/#/en/');

    //'Login'
    loginHelper.login(languagePath);

    //'Logout'
    loginHelper.logout(languagePath);
  });
});

/* global browser */
/* global it */
/* global describe */
/* global expect */

'use strict';

describe('Test login', function() {

  var loginHelper = require('../utils/loginHelper');

  it('Login in german with admin', function() {

    //Welcome Page
    browser.get('#/de/');
    browser.waitForAngular();
    expect(browser.getCurrentUrl()).toEqual(
      'https://metadatamanagement.cfapps.io/#/de/');

    //'Login'
    loginHelper.login();

    //'Logout'
    loginHelper.logout();
  });
});

/* global browser */
/* global element */
/* global by */
/* global it */
/* global describe */
/* global expect */
'use strict';
require('../utils/locators.js');

describe('Test login', function() {

  it('Login in german with admin', function() {

    //Welcome Page
    browser.get('#/de/');

    expect(browser.getCurrentUrl()).toEqual(
      'https://metadatamanagement.cfapps.io/#/de/');

    //Click at 'Account'
    element(by.xpath('//*[@id="navbar-collapse"]/ul/li[2]/a')).click();

    //Click at 'Login'
    element(by.xpath('//*[@id="navbar-collapse"]/ul/li[2]/ul/li[1]/a'))
      .click();

    expect(browser.getCurrentUrl()).toEqual(
      'https://metadatamanagement.cfapps.io/#/de/login');

    //add account information
    element(by.id('username')).sendKeys('admin');
    element(by.id('password')).sendKeys('admin');

    //login
    element(by.dataTranslate('login.form.button')).click();

    expect(browser.getCurrentUrl()).toEqual(
      'https://metadatamanagement.cfapps.io/#/de/');

    //Click at Account
    element(by.xpath('//*[@id="navbar-collapse"]/ul/li[3]/a')).click();

    //Click at Logout
    element(by.xpath('//*[@id="navbar-collapse"]/ul/li[3]/ul/li[3]/a'))
      .click();

    expect(browser.getCurrentUrl()).toEqual(
      'https://metadatamanagement.cfapps.io/#/de/');
  });
});

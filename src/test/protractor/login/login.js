/* global browser */
/* global element */
/* global by */
/* global it */
/* global describe */
/* global expect */
'use strict';

describe('Test login', function() {

  it('Login in german with admin', function() {

    //Welcome Page
    browser.get('#/de/');
    browser.waitForAngular();
    expect(browser.getCurrentUrl()).toEqual(
      'https://metadatamanagement.cfapps.io/#/de/');

    //Click at 'Account'
    element.all(by.css('.dropdown-toggle')).then(function(dropdowns) {
      expect(dropdowns.length).toBe(2); //two drop downs
      dropdowns[0].click();
    });

    //Click at 'Login'
    element(by.uiSref('login')).click();
    expect(browser.getCurrentUrl()).toEqual(
      'https://metadatamanagement.cfapps.io/#/de/login');
    browser.waitForAngular();

    //add account information and login
    element(by.id('username')).sendKeys('admin');
    element(by.id('password')).sendKeys('admin');
    element(by.dataTranslate('login.form.button')).click();
    expect(browser.getCurrentUrl()).toEqual(
      'https://metadatamanagement.cfapps.io/#/de/');
    browser.waitForAngular();

    //Click at 'Account'
    element.all(by.css('.dropdown-toggle')).then(function(dropdownLinks) {
      expect(dropdownLinks.length).toBe(4); //four drop downs
      dropdownLinks[1].click();
    });

    //Click at Logout
    element(by.ngClick('logout()')).click();
    browser.waitForAngular();
    expect(browser.getCurrentUrl()).toEqual(
      'https://metadatamanagement.cfapps.io/#/de/');
  });
});

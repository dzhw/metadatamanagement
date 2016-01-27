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
    browser.get('#/de/login');

    //add account information
    element(by.id('username')).sendKeys('admin');
    element(by.id('password')).sendKeys('admin');

    //login
    element(by.dataTranslate('login.form.button')).click();

    var welcomeMainTitle = element(by.binding('main.title'));
    expect(welcomeMainTitle.getText()).toEqual('Willkommen');

    element(by.dataTranslate('global.menu.account.logout')).click();
  });
});

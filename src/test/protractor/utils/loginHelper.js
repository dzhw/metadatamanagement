/* global browser */
/* global element */
/* global by */
/* global expect */
/* @Author Daniel Katzberg */
'use strict';

function login(languagePath) {
  //Click at 'Account'
  element(by.dataTranslate('global.menu.account.main')).click();

  //Click at 'Login'
  element(by.uiSref('login')).click();
  expect(browser.getCurrentUrl()).toEqual(
    'https://metadatamanagement.cfapps.io/' + languagePath + 'login');

  //add account information and login
  element(by.id('username')).sendKeys('admin');
  element(by.id('password')).sendKeys('admin');
  element(by.dataTranslate('login.form.button')).click();
}

function logout(languagePath) {
  //Click at 'Account'
  element(by.dataTranslate('global.menu.account.main')).click();

  //Click at Logout
  element(by.ngClick('logout()')).click();
  expect(browser.getCurrentUrl()).toEqual(
    'https://metadatamanagement.cfapps.io/' + languagePath);
}

module.exports.login = login;
module.exports.logout = logout;

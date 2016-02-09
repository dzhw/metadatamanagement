/* global browser */
/* global element */
/* global by */
/* global expect */
/* @Author Daniel Katzberg */
'use strict';

function login(languagePath) {

  //Expect no entities dropdown (only visible with a login)
  expect(element(by.dataTranslate('global.menu.entities.main')).isPresent()).toBe(false);

  //Click at 'Account'
  element(by.dataTranslate('global.menu.account.main')).click();

  //Click at 'Login'
  expect(element(by.uiSref('login')).isPresent()).toBe(true);
  element(by.uiSref('login')).click();
  expect(browser.getCurrentUrl()).toEqual(
    'https://metadatamanagement.cfapps.io/' + languagePath + 'login');

  //add account information and login
  element(by.id('username')).sendKeys('admin');
  element(by.id('password')).sendKeys('admin');
  element(by.dataTranslate('login.form.button')).click();

  element.all(by.dataTranslate('global.menu.entities.main')).then(function(entities) {
    expect(entities.length).toBe(1); //four drop downs
  });
}

function logout(languagePath) {
  //expect(element(by.dataTranslate('global.menu.entities.main'))).toBe(true);

  //Click at 'Account'
  element(by.dataTranslate('global.menu.account.main')).click();

  //Click at Logout
  element(by.ngClick('logout()')).click();
  expect(browser.getCurrentUrl()).toEqual(
    'https://metadatamanagement.cfapps.io/' + languagePath);

    element.all(by.dataTranslate('global.menu.entities.main')).then(function(entities) {
    expect(entities.length).toBe(0); //four drop downs
  });
}

module.exports.login = login;
module.exports.logout = logout;

/* global browser */
/* global element */
/* global by */
/* global expect */
/* @Author Daniel Katzberg */
'use strict';

function login(languagePath) {
  console.log('Language Path Login: ' + languagePath);

  element.all(by.dataTranslate('global.menu.entities.main')).then(function(entities) {
    expect(entities.length).toBe(0); //four drop downs
  });
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

  element.all(by.dataTranslate('global.menu.entities.main')).then(function(entities) {
    expect(entities.length).toBe(1); //four drop downs
  });
}

function logout(languagePath) {

console.log('Language Path Logout: ' + languagePath);

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

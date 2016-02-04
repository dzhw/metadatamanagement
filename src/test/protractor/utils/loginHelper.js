/* global browser */
/* global element */
/* global by */
/* global expect */
/* @Author Daniel Katzberg */
'use strict';

function login(languagePath) {
  //Click at 'Account'
  element.all(by.css('.dropdown-toggle')).then(function(dropdowns) {
    expect(dropdowns.length).toBe(2); //two drop downs
    dropdowns[0].click();
  });

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
  element.all(by.css('.dropdown-toggle')).then(function(dropdownLinks) {
    expect(dropdownLinks.length).toBe(4); //four drop downs
    dropdownLinks[1].click();
  });

  //Click at Logout
  element(by.ngClick('logout()')).click();
  expect(browser.getCurrentUrl()).toEqual(
    'https://metadatamanagement.cfapps.io/' + languagePath);
}

module.exports.login = login;
module.exports.logout = logout;

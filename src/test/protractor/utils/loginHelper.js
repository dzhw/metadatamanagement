/* global element */
/* global browser */
/* global by */

'use strict';

function login() {
  browser.get('#/de/login');
  browser.waitForAngular();
  element(by.id('username')).sendKeys('protractor');
  element(by.id('password')).sendKeys('protractor');
  element(by.css('.form button[type="submit"]')).click();
}

function logout() {
  element(by.css('[ng-click="logout()"]')).click();
  browser.waitForAngular();
}
module.exports.login = login;
module.exports.logout = logout;

/* global element */
/* global browser */
/* global by */
/* global expect */

'use strict';

function login() {
  browser.get('#/de/login');
  element(by.id('username')).sendKeys('admin');
  element(by.id('password')).sendKeys('admin');
  element(by.css('.form button[type="submit"]')).click();
  expect(element(by.css('[ng-click="logout()"]')).isPresent()).toBe(true);
}

function logout() {
  element(by.css('[ng-click="logout()"]')).click();
  expect(element(by.css('[ng-click="logout()"]')).isPresent()).toBe(false);
}
module.exports.login = login;
module.exports.logout = logout;

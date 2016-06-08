/* global element */
/* global by */
/* global expect */

'use strict';

function login() {
  element(by.uiSref('login')).click().then(function() {
    element(by.id('username')).sendKeys('admin');
    element(by.id('password')).sendKeys('admin');
    element(by.css('.form button[type="submit"]')).click();
    expect(element(by.css('[ng-click="logout()"]')).isPresent()).toBe(true);
  });
}
function logout() {
  element(by.css('[ng-click="logout()"]')).click();
}
module.exports.login = login;
module.exports.logout = logout;

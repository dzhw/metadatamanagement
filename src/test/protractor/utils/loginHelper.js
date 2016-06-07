/* global element */
/* global by */

'use strict';

function login() {
  element(by.uiSref('login')).click().then(function() {
    element(by.id('username')).sendKeys('protractortestuser');
    element(by.id('password')).sendKeys('testUser');
    element(by.css('.form button[type="submit"]')).click();
    browser.waitForAngular();
  });
}
function logout() {
  element(by.css('[ng-click="logout()"]')).click();
}
module.exports.login = login;
module.exports.logout = logout;

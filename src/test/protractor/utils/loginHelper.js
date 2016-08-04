/* global element */
/* global browser */
/* global by */
/* global expect */

'use strict';
var protractorHelper = require('../utils/protractorWaitHelper');
function login() {
  /*browser.get('#/de/login');
  browser.waitForAngular();*/
  protractorHelper.protractorWaitHelper('login').then(function(el) {
    el.click();
    protractorHelper.protractorWaitHelper('content').then(function() {
      element(by.id('username')).sendKeys('protractor');
      element(by.id('password')).sendKeys('protractor');
      element(by.css('.form button[type="submit"]')).click();
    });
  });
  expect(element(by.css('[ng-click="logout()"]')).isPresent()).toBe(true);
}

function logout() {
  browser.sleep(2000);
  element(by.css('[ng-click="logout()"]')).click();
  protractorHelper.protractorWaitHelper('toolbar').then(function() {
    expect(element(by.css('[ng-click="logout()"]')).isPresent()).toBe(false);
  });
}
module.exports.login = login;
module.exports.logout = logout;

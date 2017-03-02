/* global element */
/* global browser */
/* global by */
/* global expect */

'use strict';
var protractorHelper = require('../utils/protractorWaitHelper');

function login() {
  var loginButton = element(by.id('login'));
  var accessMenuToggle = element(by.id('account-menu-toggle'));
  loginButton.isDisplayed().then(function(isDisplayed) {
    if (!isDisplayed) {
      accessMenuToggle.click();
    }
    loginButton.click();
    protractorHelper.protractorWaitHelper('content').then(function() {
      element(by.id('username')).sendKeys('protractor');
      element(by.id('password')).sendKeys('protractor');
      element(by.css('.form button[type="submit"]')).click();
      protractorHelper.protractorWaitHelper('SideNavBar').then(function() {
        expect(element(by.css('[ng-click="logout()"]')).isPresent())
        .toBe(true);
      });
    });
  });
}

function logout() {
  element(by.css('[ng-click="logout()"]')).click();
  protractorHelper.protractorWaitHelper('SideNavBar').then(function() {
    expect(element(by.css('[ng-click="logout()"]')).isPresent()).toBe(false);
  });
}
module.exports.login = login;
module.exports.logout = logout;

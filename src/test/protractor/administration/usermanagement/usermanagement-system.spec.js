/* global browser */
/* global it */
/* global describe */
/* global by */
/* global element */
/* global expect */
/* global beforeAll */
/* global afterAll */

'use strict';
var htmlContentHelper =
require('../../utils/htmlContentHelper');
var loginHelper = require('../../utils/loginHelper');

describe('User details page', function() {
  function testHomePage(description, link) {
    describe(description, function() {
      var currentUrl;
      beforeAll(function() {
          browser.get('#/de/login');
          loginHelper.login();
          browser.get(link);
          browser.getCurrentUrl().then(function(url) {
           currentUrl = url;
         });
        });
      afterAll(function() {
          loginHelper.logout();
        });
      it('should check translated strings', function() {
        var htmlContent = element.all(by.css('.container')).get(0);
        htmlContentHelper
          .findNotTranslationedStrings(htmlContent, currentUrl)
          .then(function(result) {
            expect(result.length).toBe(0, result.message);
          });
      });
    });
  }
  testHomePage('with german language', '#/de/user-management/system');
  testHomePage('with english language', '#/en/user-management/system');
});

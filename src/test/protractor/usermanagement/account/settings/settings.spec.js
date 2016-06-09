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
require('../../../utils/htmlContentHelper');
var loginHelper = require('../../../utils/loginHelper');

describe('Settings Page', function() {
  function testLogsPage(description, link) {
    describe(description, function() {
      var currentUrl;
      var htmlContent;
      beforeAll(function() {
          browser.get('#/de/login');
          loginHelper.login();
          browser.get(link);
          browser.getCurrentUrl().then(function(url) {
           currentUrl = url;
         });
          htmlContent = element(by.id('content'));
        });
      afterAll(function() {
          loginHelper.logout();
        });
      it('should check translated strings', function() {
          htmlContentHelper
          .findNotTranslationedStrings(htmlContent, currentUrl)
          .then(function(result) {
            expect(result.length).toBe(0, result.message);
          });
        });
    });
  }
  testLogsPage('with german language', '#/de/settings');
  testLogsPage('with english language', '#/en/settings');
});

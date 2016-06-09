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

describe('Health Page', function() {
  function testHealthPage(description, link) {
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
          var htmlContent = element(by.id('content'));
          htmlContentHelper
          .findNotTranslationedStrings(htmlContent, currentUrl)
          .then(function(result) {
            expect(result.length).toBe(0, result.message);
          });
        });
    });
  }
  testHealthPage('with german language', '#/de/health');
  testHealthPage('with english language', '#/en/health');
});

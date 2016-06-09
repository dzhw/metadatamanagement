/* global browser */
/* global it */
/* global describe */
/* global by */
/* global element */
/* global expect */
/* global beforeEach */

'use strict';

var htmlContentHelper =
require('../../../../utils/htmlContentHelper');

describe('Resert password page by given an invalid email', function() {
  function testLogsPage(description, link) {
    describe(description, function() {
      var currentUrl;
      var htmlContent;
      beforeEach(function() {
          browser.get(link);
          browser.getCurrentUrl().then(function(url) {
           currentUrl = url;
         });
          htmlContent = element(by.id('content'));
        });
      it('should check translated strings', function() {
          htmlContent.all(by.css('input[type="email"]')).then(function(items) {
          items[0].sendKeys('bala@bla.de');
          htmlContent.element(by.css('button[type="submit"]')).click();
          htmlContentHelper
          .findNotTranslationedStrings(htmlContent, currentUrl)
          .then(function(result) {
            expect(result.length).toBe(0, result.message);
          });
        });
        });
    });
  }
  testLogsPage('with german language', '#/de/reset/request');
  testLogsPage('with english language', '#/en/reset/request');
});

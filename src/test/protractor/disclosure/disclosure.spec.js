/* global browser */
/* global it */
/* global describe */
/* global by */
/* global element */
/* global expect */
/* global beforeAll */

'use strict';

var htmlContentHelper =
require('../utils/htmlContentHelper');

describe('Disclosure Page', function() {
  function testDisclosurePage(description, link) {
    describe(description, function() {
      var currentUrl;
      beforeAll(function() {
          browser.get(link);
          browser.getCurrentUrl().then(function(url) {
           currentUrl = url;
         });
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
  testDisclosurePage('with german language', '#/de/disclosure');
  testDisclosurePage('with english language', '#/en/disclosure');
});

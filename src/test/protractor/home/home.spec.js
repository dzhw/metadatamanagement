/* global browser */
/* global it */
/* global describe */

/* global expect */
/* global beforeAll */

'use strict';
var htmlContentHelper =
require('../utils/htmlContentHelper');
var translateHelper = require('../utils/translateHelper');

describe('Home page', function() {
  function callHomePage() {
    browser.get('#/de/');
  }
  function testHomePage(description, language) {
    describe(description, function() {
      var currentUrl;
      var content;
      beforeAll(function() {
        browser.getCurrentUrl().then(function(url) {
          currentUrl = url;
          translateHelper.changeLanguage('content', url, language)
          .then(function(el) {
            content = el;
          });
        });
      });
      it('should check translated strings', function() {
          htmlContentHelper
          .findNotTranslationedStrings(content, currentUrl)
          .then(function(result) {
            expect(result.length).toBe(0, result.message);
          });
        });
    });
  }
  callHomePage();
  testHomePage('with german language', 'de');
  testHomePage('with english language', 'en');
});

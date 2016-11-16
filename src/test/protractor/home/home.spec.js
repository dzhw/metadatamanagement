/* global browser */
/* global it */
/* global describe */
/* global expect */
/* global beforeAll */

'use strict';
var htmlContentHelper =
  require('../utils/htmlContentHelper');
var translateHelper = require('../utils/translateHelper');

//Called as first and only once. For more information take a look on these
//protractor.conf.js
describe('Home page', function() {
  function callHomePage() {
    browser.get('/de/search');
  }

  function testHomePage(description, language) {
    describe(description, function() {
      var currentUrl;
      var content;
      beforeAll(function() {
        browser.getCurrentUrl().then(function(url) {
          currentUrl = url;
          translateHelper.changeLanguage('content', url,
              language)
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
  //Call Home Page start with german. Switch to English and Back to German
  callHomePage();
  testHomePage('with english language', 'en');
  testHomePage('with german language', 'de');
});

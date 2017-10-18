/* global browser */
/* global it */
/* global element */
/* global by */
/* global describe */
/* global expect */
/* global beforeAll */

'use strict';
var htmlContentHelper =
  require('../utils/htmlContentHelper');
var translateHelper = require('../utils/translateHelper');
var protractorWaitHelper = require('../utils/protractorWaitHelper');

//Called as first and only once. For more information take a look on these
//protractor.conf.js
describe('Home page', function() {
  function callHomePage() {
    browser.get('#!/de/search');
    protractorWaitHelper.waitFor('closeWelcomeDialog').then(function(button) {
      button.click();
    });
  }

  function testHomePage(description, language) {
    describe(description, function() {
      var currentUrl;
      beforeAll(function(done) {
        browser.getCurrentUrl().then(function(url) {
          currentUrl = url;
          translateHelper.changeLanguage(url, language).then(done);
        });
      });
      it('should check translated strings', function(done) {
        protractorWaitHelper.waitFor('body').then(function(body) {
          htmlContentHelper
          .findNotTranslationedStrings(body, currentUrl)
          .then(function(result) {
            expect(result.length).toBe(0, result.message);
            done();
          });
        });
      });
    });
  }
  //Call Home Page start with german. Switch to English and Back to German
  callHomePage();
  testHomePage('with english language', 'en');
  testHomePage('with german language', 'de');
});

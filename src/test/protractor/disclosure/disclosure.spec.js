/* global browser */
/* global it */
/* global describe */
/* global by */
/* global expect */
/* global beforeAll */
/* global element */

'use strict';

var htmlContentHelper = require('../utils/htmlContentHelper');
var translateHelper = require('../utils/translateHelper');
var content;
describe('Disclosure Page', function() {
  function testDisclosurePage(description, language) {
    describe(description, function() {
      var currentUrl;
      beforeAll(function() {
        var disclosureState = element(by.css('[ui-sref="disclosure"]'));
        disclosureState.click();
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
      it('should check the external URL', function(done) {
        content.all(by.css('a')).then(function(items) {
          items[1].getAttribute('href').then(function(href) {
            browser.getCurrentUrl().then(function(url) {
              expect(url).toEqual(href + 'disclosure', 'Home link ' +
            url + ' is not correct...');
              done();
            });
          });
        });
      });
    });
  }
  testDisclosurePage('with german language', 'de');
  testDisclosurePage('with english language', 'en');
});

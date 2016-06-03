/* global browser */
/* global it */
/* global describe */
/* global by */
/* global element */
/* global expect */
/* global beforeAll */

'use strict';

var findNotTranslationedStrings =
require('../utils/findNotTranslationedStrings');

describe('Disclosure page', function() {
  describe('with german language', function() {
    var currentUrl;
    beforeAll(function() {
      browser.get('#/de/disclosure');
      browser.getCurrentUrl().then(function(url) {
       currentUrl = url;
     });
    });
    it('should check translated strings', function() {
      var htmlContent = element.all(by.css('.container')).get(0);
      findNotTranslationedStrings
      .checkHTMLContent(htmlContent, currentUrl)
      .then(function(result) {
        expect(result.length).toBe(0, result.message);
      });
    });
  });
  describe('with english language', function() {
    var currentUrl;
    beforeAll(function() {
      browser.get('#/en/disclosure');
      browser.getCurrentUrl().then(function(url) {
       currentUrl = url;
     });
    });
    it('should check translated strings', function() {
      var htmlContent = element.all(by.css('.container')).get(0);
      findNotTranslationedStrings
      .checkHTMLContent(htmlContent, currentUrl)
      .then(function(result) {
        expect(result.length).toBe(0, result.message);
      });
    });
  });
});

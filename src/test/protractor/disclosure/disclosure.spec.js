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
var findBrockenUrls = require('../utils/findBrockenUrls');

describe('Disclosure page', function() {
  beforeAll(function() {
    browser.get('#/de/disclosure');
  });
  describe('translations', function() {
    it('Strings should be translated', function() {
      var el = element.all(by.css('.container')).get(0);
      findNotTranslationedStrings.checkHTMLContent(el, '#/de/disclosure')
      .then(function(result) {
        expect(result.length).toBe(0, result.message);
      });
    });
  });

  describe('URLs', function() {
    var htmlContent;
    var currentUrl;
    beforeAll(function() {
      htmlContent = element.all(by.css('.container')).get(1);
      browser.getCurrentUrl().then(function(url) {
        currentUrl = url;
      });
    });
    it('should check DZHW external URL', function(done) {
      htmlContent.all(by.css('a')).then(function(items) {
        items[0].getAttribute('href').then(function(href) {
          return href;
        }).then(function(url) {
          items[0].click().then(function() {
            findBrockenUrls.checkHREFs(url, currentUrl).then(function(result) {
              done();
              expect(result.isValidUrl).toBe(true, result.message);
            });
          });
        });
      });
    });
  });
});

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
var findBrockenUrls =
require('../utils/findBrockenUrls');
var htmlContent;
describe('Disclosure Page', function() {
  function testDisclosurePage(description, link) {
    describe(description, function() {
      var currentUrl;
      beforeAll(function() {
          browser.get(link);
          browser.getCurrentUrl().then(function(url) {
           currentUrl = url;
         });
          htmlContent = element(by.id('content'));
        });
      it('should check translated strings', function() {
        htmlContentHelper
        .findNotTranslationedStrings(htmlContent, currentUrl)
        .then(function(result) {
          expect(result.length).toBe(0, result.message);
        });
      });
      it('should check the external URL', function(done) {
        htmlContent.all(by.css('a')).then(function(items) {
          items[1].getAttribute('href').then(function(href) {
            return href;
          }).then(function(url) {
            findBrockenUrls.checkHREFs(url, currentUrl)
            .then(function(result) {
              done();
              expect(result.isValidUrl).toBe(true, result.message);
            });
          });
        });
      });
    });
  }
  testDisclosurePage('with german language', '#/de/disclosure');
  testDisclosurePage('with english language', '#/en/disclosure');
});

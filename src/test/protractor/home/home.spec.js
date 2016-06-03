/* global browser */
/* global it */
/* global describe */
/* global by */
/* global element */
/* global expect */
/* global beforeEach */

'use strict';
var findNotTranslationedStrings =
require('../utils/findNotTranslationedStrings');
var findBrockenUrls = require('../utils/findBrockenUrls');

describe('Home page', function() {
  describe('with german language', function() {
    var currentUrl;
    var htmlContent;
    beforeEach(function() {
      browser.get('#/de/');
      browser.getCurrentUrl().then(function(url) {
       currentUrl = url;
     });
      htmlContent = element.all(by.css('.container')).get(0);
    });
    it('should check translated strings', function() {
      findNotTranslationedStrings
      .checkHTMLContent(htmlContent, currentUrl)
      .then(function(result) {
        expect(result.length).toBe(0, result.message);
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
    it('should check BMBF external URL', function(done) {
    htmlContent.all(by.css('a')).then(function(items) {
      items[1].getAttribute('href').then(function(href) {
        return href;
      }).then(function(url) {
        items[1].click().then(function() {
          findBrockenUrls.checkHREFs(url, currentUrl).then(function(result) {
            done();
            expect(result.isValidUrl).toBe(true, result.message);
          });
        });
      });
    });
  });
  });
  describe('with english language', function() {
    var currentUrl;
    var htmlContent;
    beforeEach(function() {
      browser.get('#/en/');
      browser.getCurrentUrl().then(function(url) {
       currentUrl = url;
     });
      htmlContent = element.all(by.css('.container')).get(0);
    });
    it('should check translated strings', function() {
      findNotTranslationedStrings
      .checkHTMLContent(htmlContent, currentUrl)
      .then(function(result) {
        expect(result.length).toBe(0, result.message);
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
    it('should check BMBF external URL', function(done) {
    htmlContent.all(by.css('a')).then(function(items) {
      items[1].getAttribute('href').then(function(href) {
        return href;
      }).then(function(url) {
        items[1].click().then(function() {
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

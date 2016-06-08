/* global browser */
/* global it */
/* global describe */
/* global by */
/* global element */
/* global expect */
/* global beforeAll */
/* global afterAll */
/* global xit */

'use strict';
var htmlContentHelper =
require('../../utils/htmlContentHelper');
var findBrockenUrls = require('../../utils/findBrockenUrls');
var loginHelper = require('../../utils/loginHelper');

describe('User-management page', function() {
  function testHomePage(description, link) {
    describe(description, function() {
      var currentUrl;
      beforeAll(function() {
          browser.get('#/de/login');
          loginHelper.login();
          browser.get(link);
          browser.getCurrentUrl().then(function(url) {
           currentUrl = url;
         });
        });
      afterAll(function() {
          loginHelper.logout();
        });
      it('should check translated strings', function() {
        var htmlContent = element.all(by.css('.container')).get(0);
        htmlContentHelper
          .findNotTranslationedStrings(htmlContent, currentUrl)
          .then(function(result) {
            expect(result.length).toBe(0, result.message);
          });
      });
      xit('should check url for details for first user', function(done) {
        var htmlContent = element.all(by.css('.container')).get(0);
        htmlContent.all(by.css('a')).then(function(items) {
           items[0].getAttribute('href').then(function(href) {
             return href;
           }).then(function(url) {
             items[0].click().then(function() {
               findBrockenUrls.checkHREFs(url, currentUrl)
               .then(function(result) {
                 done();
                 expect(result.isValidUrl).toBe(true, result.message);
               });
             });
           });
         });
      });
    });
  }
  testHomePage('with german language', '#/de/user-management');
  testHomePage('with english language', '#/en/user-management');
});

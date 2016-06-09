/* global browser */
/* global it */
/* global describe */
/* global by */
/* global element */
/* global expect */
/* global beforeAll */
/* global afterAll */

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
        var htmlContent = element(by.id('content'));
        htmlContentHelper
          .findNotTranslationedStrings(htmlContent, currentUrl)
          .then(function(result) {
            expect(result.length).toBe(0, result.message);
          });
      });
      it('should open details page for first user (System)', function(done) {
        var htmlContent = element(by.id('content'));
        htmlContent
        .all(by.uiSref('user-management-detail({login:user.login})'))
        .then(function(items) {
           items[0].getAttribute('href').then(function(href) {
             return href;
           }).then(function(href) {
             browser.driver.executeScript('arguments[0].click();',
             items[0].getWebElement()).then(function() {
               findBrockenUrls.checkStates(href, currentUrl, 'regestration')
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

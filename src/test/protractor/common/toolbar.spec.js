/* global browser */
/* global it */
/* global describe */
/* global by */
/* global element */
/* global expect */
/* global beforeEach */

'use strict';

var htmlContentHelper =
  require('../utils/htmlContentHelper');
var findBrockenUrls = require('../utils/findBrockenUrls');

describe('Tool Bar', function() {
  function testSideBar(description, link, currentLanguage) {
    describe(description, function() {
      var currentUrl;
      var htmlContent;
      beforeEach(function() {
        browser.get(link);
        browser.getCurrentUrl().then(function(url) {
          currentUrl = url;
        });
        htmlContent = element(by.id('toolbar'));
        htmlContentHelper.showHiddenElements(htmlContent)
          .then(function(content) {
            htmlContent.outerHtml = content;
          });
      });
      it('should check translated strings', function() {
        htmlContentHelper
          .findNotTranslationedStrings(htmlContent, currentUrl)
          .then(function(result) {
            expect(result.length).toBe(0, result.message);
          });
      });
      it('should open login page', function(done) {
        var loginButton = element(by.id('login'));
        loginButton.click().then(function() {
          findBrockenUrls.checkStates(browser.baseUrl +
              '#/' + currentLanguage + '/login',
              currentUrl,
              'login')
            .then(function(result) {
              expect(result.isValidUrl).toBe(true,
                result.message);
              done();
            });
        });
      });
      it('should open registration page', function(done) {
        var registerButton = element(by.id('register'));
        registerButton.click().then(function() {
          findBrockenUrls.checkStates(browser.baseUrl +
              '#/' + currentLanguage + '/register',
              currentUrl,
              'register')
            .then(function(result) {
              expect(result.isValidUrl).toBe(true,
                result.message);
              done();
            });
        });
      });
    });
  }
  testSideBar('with german language', '#/de/disclosure', 'de');
  testSideBar('with english language', '#/en/disclosure', 'en');
});

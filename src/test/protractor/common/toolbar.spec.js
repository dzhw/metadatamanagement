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
  function testToolBar(description, link, currentLanguage) {
    describe(description, function() {
      var currentUrl;
      var htmlContent;
      beforeEach(function() {
        browser.get(link);
        browser.refresh();
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
      it('should change language ', function(done) {
        var targetLanguage = currentLanguage === 'de' ? '#/en/' : '#/de/';
        var changeLanguage;
        if (currentLanguage === 'en') {
          changeLanguage = element(by.id('changeLanguageToDe'));
        }else {
          changeLanguage = element(by.id('changeLanguageToEn'));
        }
        changeLanguage.click().then(function() {
          browser.getCurrentUrl().then(function(url) {
            expect(url.indexOf(targetLanguage) !== -1).toBe(true, 'from ' +
            currentLanguage + ' to' + targetLanguage);
          });
          done();
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
  testToolBar('with german language', '#/de/', 'de');
  testToolBar('with english language', '#/en/', 'en');
});

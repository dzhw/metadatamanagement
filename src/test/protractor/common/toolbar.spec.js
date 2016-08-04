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
var protractorHelper = require('../utils/protractorWaitHelper');

describe('Tool Bar test', function() {
  var toolbar;
  var currentUrl;
  function testToolBar(description) {
    describe(description, function() {
        beforeEach(function() {
          var disclosureState = element(by.css('[ui-sref="disclosure"]'));
          disclosureState.click();
          browser.getCurrentUrl().then(function(url) {
            currentUrl = url;
          });
          protractorHelper.protractorWaitHelper('toolbar').then(function(el) {
            toolbar = el;
          });
        });
        it('should check translated strings', function() {
          htmlContentHelper.findNotTranslationedStrings(toolbar, currentUrl)
          .then(function(result) {
            expect(result.length).toBe(0, result.message);
          });
        });
        it('should open login page', function(done) {
          var loginButton = element(by.id('login'));
          loginButton.click();
          protractorHelper.protractorWaitHelper('content').then(function() {
            expect(element(by.id('password')).isPresent()).toBe(true, 'and ' +
            'display Password input field');
            done();
          });
        });
        it('should open registration page', function(done) {
          var registerButton = element(by.id('register'));
          registerButton.click();
          protractorHelper.protractorWaitHelper('content').then(function() {
            expect(element(by.id('password')).isPresent()).toBe(true, 'and ' +
            'display Password input field');
            done();
          });
        });
        it('should change language ', function(done) {
          var changeLanguageButton;
          var targetLanguage;
          if (currentUrl.indexOf('#/de') !== -1) {
            targetLanguage = 'English';
            changeLanguageButton = element(by.id('changeLanguageToEn'));
            changeLanguageButton.click();
            browser.getCurrentUrl().then(function(url) {
              expect(url).toContain('#/en', 'from German to ' + targetLanguage);
              done();
            });
          }else {
            targetLanguage = 'German';
            changeLanguageButton = element(by.id('changeLanguageToDe'));
            changeLanguageButton.click();
            browser.getCurrentUrl().then(function(url) {
              expect(url).toContain('#/de', 'from English to ' +
              targetLanguage);
              done();
            });
          }
        });
      });
  }
  testToolBar('with german language');
  testToolBar('with english language');
});

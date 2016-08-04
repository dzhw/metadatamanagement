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
var protractorHelper = require('../utils/protractorWaitHelper');
var translateHelper = require('../utils/translateHelper');
describe('Tool Bar test', function() {
  var toolbar;
  var currentUrl;
  function testToolBar(description, language) {
    describe(description, function() {
        beforeAll(function() {
          var disclosureState = element(by.css('[ui-sref="disclosure"]'));
          disclosureState.click();
          browser.getCurrentUrl().then(function(url) {
            currentUrl = url;
            translateHelper.changeLanguage('toolbar', url, language)
            .then(function(el) {
              toolbar = el;
            });
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
          browser.getCurrentUrl().then(function(url) {
            if (url.indexOf('#/de') !== -1) {
              changeLanguageButton = element(by.id('changeLanguageToEn'));
              changeLanguageButton.click();
              protractorHelper.protractorWaitHelper('toolbar').then(function() {
                expect(element(by.id('changeLanguageToDe')).isPresent())
                .toBe(true, 'from German to English ');
                done();
              });
            }else {
              changeLanguageButton = element(by.id('changeLanguageToDe'));
              changeLanguageButton.click();
              protractorHelper.protractorWaitHelper('toolbar').then(function() {
                expect(element(by.id('changeLanguageToEn')).isPresent())
                .toBe(true, 'from English to German ');
                done();
              });
            }
          });
        });
      });
  }
  testToolBar('with german language', 'de');
  testToolBar('with english language', 'en');
});

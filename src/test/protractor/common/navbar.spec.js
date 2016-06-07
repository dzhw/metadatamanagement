/* global browser */
/* global it */
/* global describe */
/* global by */
/* global element */
/* global expect */
/* global beforeEach */
/* global beforeAll */
/* global afterAll */

'use strict';

var findNotTranslationedStrings =
require('../utils/findNotTranslationedStrings');
var findBrockenUrls = require('../utils/findBrockenUrls');
var loginHelper = require('../utils/loginHelper');

describe('Navbar', function() {
  function testnavBarwithDeEn(description, link) {
    describe(description, function() {
      var currentUrl;
      var htmlContent;
      beforeEach(function() {
        browser.get(link);
        browser.driver.manage().window().maximize();
        browser.getCurrentUrl().then(function(url) {
          currentUrl = url;
        });
        htmlContent = element(by.tagName('md-sidenav'));
        findNotTranslationedStrings.openAllDropdownMenues(htmlContent)
        .then(function(content) {
          htmlContent = content;
        });
      });
      it('should check translated strings', function() {
        findNotTranslationedStrings
        .checkHTMLContent(htmlContent, currentUrl)
        .then(function(result) {
          expect(result.length).toBe(0, result.message);
        });
      });
      it('should open disclosure page', function(done) {
        var state =  element(by.uiSref('disclosure'));
        state.getAttribute('href').then(function(href) {
          return href;
        }).then(function(href) {
          state.click().then(function() {
            findBrockenUrls.checkStates(href, currentUrl, 'disclosure')
            .then(function(result) {
              done();
              expect(result.isValidUrl).toBe(true, result.message);
            });
          });
        });
      });
      it('should open login page', function(done) {
        var state =  element(by.uiSref('login'));
        state.getAttribute('href').then(function(href) {
          return href;
        }).then(function(href) {
          browser.driver.executeScript('arguments[0].click();',
          state.getWebElement()).then(function() {
            findBrockenUrls.checkStates(href, currentUrl, 'login')
            .then(function(result) {
              done();
              expect(result.isValidUrl).toBe(true, result.message);
            });
          });
        });
      });
      it('should open regestration page', function(done) {
        var state =  element(by.uiSref('register'));
        state.getAttribute('href').then(function(href) {
          return href;
        }).then(function(href) {
          browser.driver.executeScript('arguments[0].click();',
          state.getWebElement()).then(function() {
            findBrockenUrls.checkStates(href, currentUrl, 'regestration')
            .then(function(result) {
              done();
              expect(result.isValidUrl).toBe(true, result.message);
            });
          });
        });
      });
    });
  }
  testnavBarwithDeEn('with german language', '#/de/');
  //testnavBarwithDeEn('with english language', '#/en/');
});

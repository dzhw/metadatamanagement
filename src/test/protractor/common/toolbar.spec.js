/* global browser */
/* global it */
/* global describe */
/* global by */
/* global element */
/* global expect */
/* global beforeEach */
/* global xdescribe */

'use strict';

var htmlContentHelper =
require('../utils/htmlContentHelper');
var findBrockenUrls = require('../utils/findBrockenUrls');

xdescribe('Tool Bar', function() {
  function testSideBar(description, link) {
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
        var state =  element(by.id('login'));
        state.getAttribute('href').then(function(href) {
          return href;
        }).then(function(href) {
          state.click().then(function() {
            console.log(href + ':' + currentUrl);
            findBrockenUrls.checkStates(href, currentUrl, 'login')
            .then(function(result) {
              done();
              expect(result.isValidUrl).toBe(true, result.message);
            });
          });
        });
      });
      it('should open regestration page', function(done) {
        var state =  element(by.id('register'));
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
  testSideBar('with german language', '#/de/disclosure');
  testSideBar('with english language', '#/en/disclosure');
});

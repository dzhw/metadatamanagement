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

var htmlContentHelper =
  require('../utils/htmlContentHelper');
var findBrockenUrls = require('../utils/findBrockenUrls');
var loginHelper = require('../utils/loginHelper');

describe('Side Bar', function() {
  function testSideBar(description, link) {
    describe(description, function() {
      var currentUrl;
      var htmlContent;
      beforeEach(function() {
        browser.get(link);
        browser.refresh();
        browser.getCurrentUrl().then(function(url) {
          currentUrl = url;
        });
        htmlContent = element(by.tagName('md-sidenav'));
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
      it('should check BMBF external URL', function(done) {
         htmlContent.all(by.css('a')).then(function(items) {
           items[2].getAttribute('href').then(function(href) {
             return href;
           }).then(function(url) {
             findBrockenUrls.checkHREFs(url, currentUrl)
               .then(function(result) {
                 expect(result.isValidUrl).toBe(true, result.message);
                 done();
               });
           });
         });
       });
      it('should check DZHW external URL', function(done) {
        htmlContent.all(by.css('a')).then(function(items) {
          items[3].getAttribute('href').then(function(href) {
            return href;
          }).then(function(url) {
            findBrockenUrls.checkHREFs(url, currentUrl)
            .then(function(result) {
                expect(result.isValidUrl).toBe(true, result.message);
                done();
              });
          });
        });
      });
      it('should open disclosure page', function(done) {
        var state = element(by.uiSref('disclosure'));
        state.getAttribute('href').then(function(href) {
          return href;
        }).then(function(href) {
          state.click().then(function() {
            findBrockenUrls.checkStates(href, currentUrl,
                'disclosure')
              .then(function(result) {
                expect(result.isValidUrl).toBe(true,
                  result.message);
                done();
              });
          });
        });
      });
      describe('With logged user', function() {
        beforeAll(function() {
          loginHelper.login();
        });
        afterAll(function() {
          loginHelper.logout();
        });
        it('should open Settings page', function(done) {
          var state = element(by.uiSref('settings'));
          state.getAttribute('href').then(function(href) {
            return href;
          }).then(function(href) {
            browser.executeScript('arguments[0].click();',
              state.getWebElement()).then(function() {
              findBrockenUrls.checkStates(href,
                  currentUrl,
                  'settings')
                .then(function(result) {
                  expect(result.isValidUrl).toBe(true,
                    result.message);
                  done();
                });
            });
          });
        });
        it('should open Password page', function(done) {
          var state = element(by.uiSref('password'));
          state.getAttribute('href').then(function(href) {
            return href;
          }).then(function(href) {
            browser.executeScript('arguments[0].click();',
              state.getWebElement()).then(function() {
              findBrockenUrls.checkStates(href,
                  currentUrl,
                  'password')
                .then(function(result) {
                  expect(result.isValidUrl).toBe(true,
                    result.message);
                  done();
                });
            });
          });
        });
        it('should open User-management page', function(done) {
          var state = element(by.uiSref('user-management'));
          state.getAttribute('href').then(function(href) {
            return href;
          }).then(function(href) {
            browser.executeScript('arguments[0].click();',
              state.getWebElement()).then(function() {
              findBrockenUrls.checkStates(href,
                  currentUrl,
                  'user-management')
                .then(function(result) {
                  expect(result.isValidUrl).toBe(true,
                    result.message);
                  done();
                });
            });
          });
        });
        it('should open Metrics page', function(done) {
          var state = element(by.uiSref('metrics'));
          state.getAttribute('href').then(function(href) {
            return href;
          }).then(function(href) {
            browser.executeScript('arguments[0].click();',
              state.getWebElement()).then(function() {
              findBrockenUrls.checkStates(href,
                  currentUrl,
                  'metrics')
                .then(function(result) {
                  expect(result.isValidUrl).toBe(true,
                    result.message);
                  done();
                });
            });
          });
        });
        it('should open Health page', function(done) {
          var state = element(by.uiSref('health'));
          state.getAttribute('href').then(function(href) {
            return href;
          }).then(function(href) {
            browser.executeScript('arguments[0].click();',
              state.getWebElement()).then(function() {
              findBrockenUrls.checkStates(href,
                  currentUrl,
                  'health')
                .then(function(result) {
                  expect(result.isValidUrl).toBe(true,
                    result.message);
                  done();
                });
            });
          });
        });
        it('should open Configuration page', function(done) {
          var state = element(by.uiSref('configuration'));
          state.getAttribute('href').then(function(href) {
            return href;
          }).then(function(href) {
            browser.executeScript('arguments[0].click();',
              state.getWebElement()).then(function() {
              findBrockenUrls.checkStates(href,
                  currentUrl,
                  'configuration')
                .then(function(result) {
                  expect(result.isValidUrl).toBe(true,
                    result.message);
                  done();
                });
            });
          });
        });
        it('should open Logs page', function(done) {
          var state = element(by.uiSref('logs'));
          state.getAttribute('href').then(function(href) {
            return href;
          }).then(function(href) {
            browser.executeScript('arguments[0].click();',
              state.getWebElement()).then(function() {
              findBrockenUrls.checkStates(href,
                  currentUrl,
                  'logs')
                .then(function(result) {
                  expect(result.isValidUrl).toBe(true,
                    result.message);
                  done();
                });
            });
          });
        });
      });
    });
  }
  testSideBar('with german language', '#/de/');
  testSideBar('with english language', '#/en/');
});

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
        browser.getCurrentUrl().then(function(url) {
          currentUrl = url;
        });
        htmlContent = element(by.tagName('md-sidenav'));
      });
      it('should check translated strings', function() {
        findNotTranslationedStrings
        .checkHTMLContent(htmlContent, currentUrl)
        .then(function(result) {
          expect(result.length).toBe(0, result.message);
        });
      });
      it('should open disclosure page', function(done) {
        var state =  element(by.css('[ui-sref="disclosure"]'));
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
      describe('', function() {
        it('should open login page', function(done) {
          var state =  element(by.css('[ui-sref="login"]'));
          state.getAttribute('href').then(function(href) {
            return href;
          }).then(function(href) {
            browser.executeScript('arguments[0].click();',
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
          var state =  element(by.css('[ui-sref="register"]'));
          state.getAttribute('href').then(function(href) {
            return href;
          }).then(function(href) {
            browser.executeScript('arguments[0].click();',
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
      describe('User is logged in', function() {
        beforeAll(function() {
          loginHelper.login();
        });
        afterAll(function() {
          loginHelper.logout();
        });
        it('should open Variables page', function(done) {
          var state =  element(by.css('[ui-sref="variable({page: \'1\'})"]'));
          state.getAttribute('href').then(function(href) {
            return href;
          }).then(function(href) {
            browser.executeScript('arguments[0].click();',
            state.getWebElement()).then(function() {
              findBrockenUrls.checkStates(href, currentUrl, 'variable')
              .then(function(result) {
                done();
                expect(result.isValidUrl).toBe(true, result.message);
              });
            });
          });
        });
        it('should open DataSets page', function(done) {
          var state =  element(by.css('[ui-sref="dataSet"]'));
          state.getAttribute('href').then(function(href) {
          return href;
        }).then(function(href) {
          browser.executeScript('arguments[0].click();',
          state.getWebElement()).then(function() {
            findBrockenUrls.checkStates(href, currentUrl, 'dataSet')
            .then(function(result) {
              done();
              expect(result.isValidUrl).toBe(true, result.message);
            });
          });
        });
        });
        it('should open Surveys page', function(done) {
        var state =  element(by.css('[ui-sref="survey"]'));
        state.getAttribute('href').then(function(href) {
          return href;
        }).then(function(href) {
          browser.executeScript('arguments[0].click();',
          state.getWebElement()).then(function() {
            findBrockenUrls.checkStates(href, currentUrl, 'survey')
            .then(function(result) {
              done();
              expect(result.isValidUrl).toBe(true, result.message);
            });
          });
        });
      });
        it('should open DataAqcuisitionProjects page', function(done) {
        var state =  element(by.css('[ui-sref="dataAcquisitionProject"]'));
        state.getAttribute('href').then(function(href) {
          return href;
        }).then(function(href) {
          browser.executeScript('arguments[0].click();',
          state.getWebElement()).then(function() {
            findBrockenUrls.checkStates(href, currentUrl,
              'dataAcquisitionProject')
            .then(function(result) {
              done();
              expect(result.isValidUrl).toBe(true, result.message);
            });
          });
        });
      });
        it('should open Settings page', function(done) {
        var state =  element(by.css('[ui-sref="settings"]'));
        state.getAttribute('href').then(function(href) {
          return href;
        }).then(function(href) {
          browser.executeScript('arguments[0].click();',
          state.getWebElement()).then(function() {
            findBrockenUrls.checkStates(href, currentUrl,
              'settings')
            .then(function(result) {
              done();
              expect(result.isValidUrl).toBe(true, result.message);
            });
          });
        });
      });
        it('should open Password page', function(done) {
        var state =  element(by.css('[ui-sref="password"]'));
        state.getAttribute('href').then(function(href) {
          return href;
        }).then(function(href) {
          browser.executeScript('arguments[0].click();',
          state.getWebElement()).then(function() {
            findBrockenUrls.checkStates(href, currentUrl,
              'password')
            .then(function(result) {
              done();
              expect(result.isValidUrl).toBe(true, result.message);
            });
          });
        });
      });
        it('should open User-management page', function(done) {
        var state =  element(by.css('[ui-sref="user-management"]'));
        state.getAttribute('href').then(function(href) {
          return href;
        }).then(function(href) {
          browser.executeScript('arguments[0].click();',
          state.getWebElement()).then(function() {
            findBrockenUrls.checkStates(href, currentUrl,
              'user-management')
            .then(function(result) {
              done();
              expect(result.isValidUrl).toBe(true, result.message);
            });
          });
        });
      });
        it('should open Metrics page', function(done) {
        var state =  element(by.css('[ui-sref="metrics"]'));
        state.getAttribute('href').then(function(href) {
          return href;
        }).then(function(href) {
          browser.executeScript('arguments[0].click();',
          state.getWebElement()).then(function() {
            findBrockenUrls.checkStates(href, currentUrl,
              'metrics')
            .then(function(result) {
              done();
              expect(result.isValidUrl).toBe(true, result.message);
            });
          });
        });
      });
        it('should open Health page', function(done) {
        var state =  element(by.css('[ui-sref="health"]'));
        state.getAttribute('href').then(function(href) {
          return href;
        }).then(function(href) {
          browser.executeScript('arguments[0].click();',
          state.getWebElement()).then(function() {
            findBrockenUrls.checkStates(href, currentUrl,
              'health')
            .then(function(result) {
              done();
              expect(result.isValidUrl).toBe(true, result.message);
            });
          });
        });
      });
        it('should open Configuration page', function(done) {
        var state =  element(by.css('[ui-sref="configuration"]'));
        state.getAttribute('href').then(function(href) {
          return href;
        }).then(function(href) {
          browser.executeScript('arguments[0].click();',
          state.getWebElement()).then(function() {
            findBrockenUrls.checkStates(href, currentUrl,
              'configuration')
            .then(function(result) {
              done();
              expect(result.isValidUrl).toBe(true, result.message);
            });
          });
        });
      });
        it('should open Logs page', function(done) {
        var state =  element(by.css('[ui-sref="logs"]'));
        state.getAttribute('href').then(function(href) {
          return href;
        }).then(function(href) {
          browser.executeScript('arguments[0].click();',
          state.getWebElement()).then(function() {
            findBrockenUrls.checkStates(href, currentUrl,
              'logs')
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
  testnavBarwithDeEn('with german language', '#/de/');
  testnavBarwithDeEn('with english language', '#/en/');
});

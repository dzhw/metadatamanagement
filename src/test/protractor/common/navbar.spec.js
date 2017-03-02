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
  require('../utils/htmlContentHelper');
var findBrockenUrls = require('../utils/findBrockenUrls');
var loginHelper = require('../utils/loginHelper');
var protractorHelper = require('../utils/protractorWaitHelper');
var translateHelper = require('../utils/translateHelper');

describe('Navigation Bar', function() {
  var currentUrl;
  var sideNavBar;
  function testSideBar(description, language) {
    describe(description, function() {
      beforeAll(function() {
        var disclosureState = element(by.css('[ui-sref="disclosure"]'));
        disclosureState.click();
        browser.getCurrentUrl().then(function(url) {
          currentUrl = url;
          translateHelper.changeLanguage('SideNavBar', url, language)
          .then(function(el) {
            sideNavBar = el;
          });
        });
      });
      describe('', function() {
        it('should check translated strings', function() {
          htmlContentHelper.findNotTranslationedStrings(sideNavBar, currentUrl)
          .then(function(result) {
            expect(result.length).toBe(0, result.message);
          });
        });
        it('should check BMBF external URL', function(done) {
          element(by.id('bmbf-link')).getAttribute('href').then(function(href) {
            return href;
          }).then(function(url) {
            findBrockenUrls.checkHREFs(url, currentUrl)
            .then(function(result) {
              expect(result.isValidUrl).toBe(true, result.message);
              done();
            });
          });
        });
        it('should check DZHW external URL', function(done) {
          element(by.id('dzhw-link')).getAttribute('href').then(function(href) {
            return href;
          }).then(function(url) {
            findBrockenUrls.checkHREFs(url, currentUrl)
            .then(function(result) {
              expect(result.isValidUrl).toBe(true, result.message);
              done();
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
            var settingsState = element(by.css('[ui-sref="settings"]'));
            settingsState.getAttribute('href').then(function(href) {
              return href;
            }).then(function(href) {
              browser.executeScript('arguments[0].click();',
              settingsState.getWebElement()).then(function() {
                protractorHelper.protractorWaitHelper('content')
                .then(function() {
                  findBrockenUrls.checkStates(href, currentUrl,
                      'settings')
                    .then(function(result) {
                      expect(result.isValidUrl).toBe(true,
                        result.message);
                      done();
                    });
                });
              });
            });
          });
          it('should open Password page', function(done) {
            var passwordState = element(by.css('[ui-sref="password"]'));
            passwordState.getAttribute('href').then(function(href) {
              return href;
            }).then(function(href) {
              browser.executeScript('arguments[0].click();',
              passwordState.getWebElement()).then(function() {
                protractorHelper.protractorWaitHelper('content')
                .then(function() {
                  findBrockenUrls.checkStates(href, currentUrl,
                      'password')
                    .then(function(result) {
                      expect(result.isValidUrl).toBe(true,
                        result.message);
                      done();
                    });
                });
              });
            });
          });
          it('should open User-management page', function(done) {
            var userManagementState =
            element(by.css('[ui-sref="user-management"]'));
            userManagementState.getAttribute('href').then(function(href) {
              return href;
            }).then(function(href) {
              browser.executeScript('arguments[0].click();',
              userManagementState.getWebElement()).then(function() {
                protractorHelper.protractorWaitHelper('content')
                .then(function() {
                  findBrockenUrls.checkStates(href, currentUrl,
                      'user-management')
                    .then(function(result) {
                      expect(result.isValidUrl).toBe(true,
                        result.message);
                      done();
                    });
                });
              });
            });
          });
          it('should open Metrics page', function(done) {
            var metricsState =
            element(by.css('[ui-sref="metrics"]'));
            metricsState.getAttribute('href').then(function(href) {
              return href;
            }).then(function(href) {
              browser.executeScript('arguments[0].click();',
              metricsState.getWebElement()).then(function() {
                protractorHelper.protractorWaitHelper('content')
                .then(function() {
                  findBrockenUrls.checkStates(href, currentUrl,
                      'metrics')
                    .then(function(result) {
                      expect(result.isValidUrl).toBe(true,
                        result.message);
                      done();
                    });
                });
              });
            });
          });
          it('should open Health page', function(done) {
            var healthState =
            element(by.css('[ui-sref="health"]'));
            healthState.getAttribute('href').then(function(href) {
              return href;
            }).then(function(href) {
              browser.executeScript('arguments[0].click();',
              healthState.getWebElement()).then(function() {
                protractorHelper.protractorWaitHelper('content')
                .then(function() {
                  findBrockenUrls.checkStates(href, currentUrl,
                      'health')
                    .then(function(result) {
                      expect(result.isValidUrl).toBe(true,
                        result.message);
                      done();
                    });
                });
              });
            });
          });
          it('should open Configuration page', function(done) {
            var configurationState =
            element(by.css('[ui-sref="configuration"]'));
            configurationState.getAttribute('href').then(function(href) {
              return href;
            }).then(function(href) {
              browser.executeScript('arguments[0].click();',
              configurationState.getWebElement()).then(function() {
                protractorHelper.protractorWaitHelper('content')
                .then(function() {
                  findBrockenUrls.checkStates(href, currentUrl,
                      'configuration')
                    .then(function(result) {
                      expect(result.isValidUrl).toBe(true,
                        result.message);
                      done();
                    });
                });
              });
            });
          });
          it('should open Logs page', function(done) {
            var logsState =
            element(by.css('[ui-sref="logs"]'));
            logsState.getAttribute('href').then(function(href) {
              return href;
            }).then(function(href) {
              browser.executeScript('arguments[0].click();',
              logsState.getWebElement()).then(function() {
                protractorHelper.protractorWaitHelper('content')
                .then(function() {
                  findBrockenUrls.checkStates(href, currentUrl,
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
      });
    });
  }
  testSideBar('with german language', 'de');
  testSideBar('with english language', 'en');
});

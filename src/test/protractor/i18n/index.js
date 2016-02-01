/* global describe */
/* global it */
/* global xit */
/* global expect */
/* global browser */
/* global element */
/* global by */
'use strict';
// spec.js
describe('Metadatamanagement Start page with different languages', function() {

  it('should be in german', function() {
    browser.get('#/de/');
    var welcomeMainTitle = element(by.binding('main.title'));

    expect(browser.getTitle()).toEqual('metadatamanagement');
    expect(welcomeMainTitle.getText()).toEqual('Willkommen');
  });

  it('should be in english', function() {
    browser.get('#/en/');
    var welcomeMainTitle = element(by.binding('main.title'));

    expect(browser.getTitle()).toEqual('metadatamanagement');
    expect(welcomeMainTitle.getText()).toEqual('Welcome');
  });

  xit('Check all languages elements for german at the index page', function() {
    browser.get('#/de/');
    browser.manage().logs().get('browser').then(function(browserLog) {
      expect(require('util').inspect(browserLog)).not.toContain(
        'Missing Translation');
    });
  });

  xit('Check all languages elements for english at the index page',
    function() {
      browser.get('#/en/');
      browser.manage().logs().get('browser').then(function(browserLog) {
        expect(require('util').inspect(browserLog)).not.toContain(
          'Missing Translation');
      });
    });

});

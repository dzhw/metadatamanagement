/* global describe */
/* global it */
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

  it('Check all languages elements for german at the index page', function() {
    browser.get('#/de/');
    element.all(by.css('.ng-binding')).then(function(bindings) {
      for (var i = 0; i < bindings.length; i = i + 1) {
        expect(bindings[i].getText()).not.toContain('{{');
      }
    });
  });

  it('Check all languages elements for english at the index page', function() {
    browser.get('#/en/');
    element.all(by.css('.ng-binding')).then(function(bindings) {
      for (var i = 0; i < bindings.length; i = i + 1) {
        expect(bindings[i].getText()).not.toContain('{{');
      }
    });
  });
});

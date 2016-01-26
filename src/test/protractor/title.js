/* global describe */
/* global it */
/* global expect */
/* global browser */
/* global element */
/* global by */
'use strict';
// spec.js
describe('Metadatamanagement Start Page', function() {
  it('should have a title', function() {
    browser.get('https://metadatamanagement.cfapps.io/#/de/');

    expect(browser.getTitle()).toEqual('metadatamanagement');
  });
});

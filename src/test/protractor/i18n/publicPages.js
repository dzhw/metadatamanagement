/* global describe */
/* global it */

/* @Author Daniel Katzberg */

'use strict';

var utilMissingTranslations = require('../utils/findMissingTranslations');
var pages = ['/', '/login', '/register', '/reset/request',
  '/disclosure'
];

describe('Metadatamanagement Start page with different languages', function() {

  it('Check german language elements for all pages', function() {
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('Check english language elements for all pages', function() {
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages);
  });
});

/* global describe */
/* global it */
/* global afterEach */
/* @Author Daniel Katzberg */

'use strict';

var utilMissingTranslations = require('../utils/findMissingTranslations');
var cacheHelper = require('../utils/cacheHelper');

describe('Check ENGLISH language elements for ', function() {

  afterAll(function() {
    cacheHelper.clearCache();
  });

  it('... welcome page', function() {
    var pages = ['/'];
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... login page', function() {
    var pages = ['/login'];
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... register page', function() {
    var pages = ['/register'];
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... request/reset page', function() {
    var pages = ['/reset/request'];
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... disclosure page', function() {
    var pages = ['/disclosure'];
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages);
  });
});

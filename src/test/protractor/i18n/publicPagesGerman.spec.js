/* global describe */
/* global it */
/* global afterAll */
/* @Author Daniel Katzberg */

'use strict';

var utilMissingTranslations = require('../utils/findMissingTranslations');
var cacheHelper = require('../utils/cacheHelper');

describe('Check GERMAN language elements for ...', function() {

  afterAll(cacheHelper.clearCache);

  it('... welcome page', function() {
    var pages = ['/'];
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... login page', function() {
    var pages = ['/login'];
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... register page', function() {
    var pages = ['/register'];
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... request/reset page', function() {
    var pages = ['/reset/request'];
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);
  });

  it('... disclosure page', function() {
    var pages = ['/disclosure'];
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages);
  });
});

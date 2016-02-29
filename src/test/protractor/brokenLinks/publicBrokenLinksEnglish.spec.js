/* global describe */
/* global it */
/* global browser */
/* global afterAll */
/* global beforeEach */
/* global afterEach */
/* @Author Daniel Katzberg */

'use strict';
var brokenLinks = require('../utils/brokenLinks');
var utilMissingTranslations = require('../utils/findMissingTranslations');
var cacheHelper = require('../utils/cacheHelper');

describe('Check broken links in GERMAN language for ', function() {

  beforeEach(function() {
    browser.ignoreSynchronization = true;
  });

  afterEach(function() {
    browser.ignoreSynchronization = false;
  });

  afterAll(function() {
    cacheHelper.clearCache();
  });

  it('... welcome page', function() {
    var pages = ['/'];
    brokenLinks.checkLinks(
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... login page', function() {
    var pages = ['/login'];
    brokenLinks.checkLinks(
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... register page', function() {
    var pages = ['/register'];
    brokenLinks.checkLinks(
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... request/reset page', function() {
    var pages = ['/reset/request'];
    brokenLinks.checkLinks(
      utilMissingTranslations.englishLanguage, pages);
  });

  it('... disclosure page', function() {
    var pages = ['/disclosure'];
    brokenLinks.checkLinks(
      utilMissingTranslations.englishLanguage, pages);
  });
});

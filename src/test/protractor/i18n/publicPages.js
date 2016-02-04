/* global describe */
/* global it */
/* global expect */
/* global browser */
/* global element */
/* global by */
/* @Author Daniel Katzberg */

'use strict';

var germanLanguage = '#/de';
var englishLanguage = '#/en';
var pages = ['/', '/login', '/register', '/reset/request', '/disclosure'];
var utilMissingTranslations = require('../utils/findMissingTranslations');

describe('Metadatamanagement Start page with different languages', function() {

  it('Check german language elements for all pages', function() {

    //reset array of missing elements and language
    utilMissingTranslations.foundMissingTranslations = [];
    utilMissingTranslations.actualLanguage = germanLanguage;

    //iterate over all pages
    for (var pageIndex = 0; pageIndex < pages.length; pageIndex++) {

      //load page
      browser.get(utilMissingTranslations.actualLanguage + pages[
        pageIndex]);

      //check for missing translations
      element(by.tagName('html')).getInnerHtml().then(
        utilMissingTranslations.findMissingTranslations);
    }

    //Test for missing translations
    expect(utilMissingTranslations.foundMissingTranslationsArray).toEqual(
      []);
  });

  it('Check english language elements for all pages', function() {

    //reset array of missing elements and language
    utilMissingTranslations.foundMissingTranslations = [];
    utilMissingTranslations.actualLanguage = englishLanguage;

    //iterate over all pages
    for (var pageIndex = 0; pageIndex < pages.length; pageIndex++) {

      //load page
      browser.get(utilMissingTranslations.actualLanguage + pages[
        pageIndex]);

      //check for missing translations
      element(by.tagName('html')).getInnerHtml().then(
        utilMissingTranslations.findMissingTranslations);
    }

    //Test for missing translations
    expect(utilMissingTranslations.foundMissingTranslations).toEqual([]);
  });
});

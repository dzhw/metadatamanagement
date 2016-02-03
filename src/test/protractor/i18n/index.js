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
var pages = ['/'];

describe('Metadatamanagement Start page with different languages', function() {

  var actualLanguage;
  var foundMissingTranslations = [];

  //function for finding missing translations
  var findMissingTranslations = function(html) {

    //look for missing translations. their are wrapped by {{ }}
    while (html.indexOf('{{') > 0) {
      var startPosition = html.indexOf('{{');
      var endPosition = html.indexOf('}}') + 2; //+2 for }}.length
      var difference = endPosition - startPosition;

      //cut the missing translation, e.g. {{test.example}}
      var missingTranslation = html.substr(html.indexOf('{{'),
        difference);

      //add to array
      foundMissingTranslations.push(missingTranslation + '(' +
        actualLanguage + ')');

      //cut html page after the last found for more missing translations
      html = html.substr(endPosition);
    }
  };

  it('Check german language elements for all pages', function() {

    //reset array of missing elements and language
    foundMissingTranslations = [];
    actualLanguage = germanLanguage;

    //iterate over all pages
    for (var pageIndex = 0; pageIndex < pages.length; pageIndex++) {

      //load page
      browser.get(actualLanguage + pages[pageIndex]);
      browser.waitForAngular();

      //check for missing translations
      element(by.tagName('html')).getInnerHtml().then(
        findMissingTranslations);
    }

    //Test for missing translations
    expect(foundMissingTranslations).toEqual([]);
  });

  it('Check english language elements for all pages', function() {

    //reset array of missing elements and language
    foundMissingTranslations = [];
    actualLanguage = englishLanguage;

    //iterate over all pages
    for (var pageIndex = 0; pageIndex < pages.length; pageIndex++) {

      //load page
      browser.get(actualLanguage + pages[pageIndex]);
      browser.waitForAngular();

      //check for missing translations
      element(by.tagName('html')).getInnerHtml().then(
        findMissingTranslations);
    }

    //Test for missing translations
    expect(foundMissingTranslations).toEqual([]);
  });
});

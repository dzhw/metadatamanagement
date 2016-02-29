/* global expect */
/* global browser */
/* global element */
/* global by */
/* @Author Daniel Katzberg */
'use strict';

var germanLanguage = '#/de';
var englishLanguage = '#/en';
var actualLanguage;
var foundMissingTranslationsArray = [];

//function for finding missing translations
function findMissingTranslations(html) {

  //look for missing translations. their are wrapped by {{ }}
  while (html.indexOf('{{') > 0) {
    var startPosition = html.indexOf('{{');
    var endPosition = html.indexOf('}}') + 2; //+2 for }}.length
    var difference = endPosition - startPosition;

    //cut the missing translation, e.g. {{test.example}}
    var missingTranslation = html.substr(html.indexOf('{{'),
      difference);

    //add to array
    foundMissingTranslationsArray.push(missingTranslation + '(' +
      actualLanguage + ')');

    //cut html page after the last found for more missing translations
    html = html.substr(endPosition);
  }
}

function checkMissingLinks(browser, pages, counter) {

  counter = counter + 1;

  browser.getCurrentUrl().then(function(urlCurrent) {
    var urlCheck = browser.baseUrl + actualLanguage + pages[counter];
    expect(urlCurrent).toBe(urlCheck);
  });

  return counter;
}

//function for different test cases depending on a given language
function testMissingTranslations(language, pages) {

  //reset array of missing elements and language
  foundMissingTranslationsArray = [];
  actualLanguage = language;
  var counter = -1;

  //iterate over all pages
  for (var pageIndex = 0; pageIndex < pages.length; pageIndex++) {

    //load page
    browser.get(actualLanguage + pages[pageIndex]);
    counter = checkMissingLinks(browser, pages, counter);

    //check for missing translations
    element(by.tagName('html')).getInnerHtml().then(findMissingTranslations);
  }

  //Test for missing translations
  expect(foundMissingTranslationsArray).toEqual([]);
}

module.exports.testMissingTranslations = testMissingTranslations;
module.exports.germanLanguage = germanLanguage;
module.exports.englishLanguage = englishLanguage;

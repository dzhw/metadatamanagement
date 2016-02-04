/* @Author Daniel Katzberg */
'use strict';

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

module.exports.findMissingTranslations = findMissingTranslations;
module.exports.actualLanguage = actualLanguage;
module.exports.foundMissingTranslationsArray = foundMissingTranslationsArray;

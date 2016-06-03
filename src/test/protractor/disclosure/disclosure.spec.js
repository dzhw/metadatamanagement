/* global browser */
/* global it */
/* global describe */
/* global by */
/* global element */
/* global expect */
/* global beforeAll */

'use strict';

var findNotTranslationedStrings =
require('../utils/findNotTranslationedStrings');

describe('Disclosure page', function() {
  beforeAll(function() {
    browser.get('#/de/disclosure');
  });
  describe('translations', function() {
    it('Strings should be translated', function() {
      var htmlContent = element.all(by.css('.container')).get(0);
      findNotTranslationedStrings
      .checkHTMLContent(htmlContent, '#/de/disclosure')
      .then(function(result) {
        expect(result.length).toBe(0, result.message);
      });
    });
  });
});

/* Own locators for better test handling */
/* @author Daniel Katzberg */
/* global by */
/* global document */

'use strict';

(function() {
  //dataTranslate is the name of the locator
  //Protracor API:
  //http://angular.github.io/protractor/
  //#/api?view=ProtractorBy.prototype.addLocator
  by.addLocator('dataTranslate', function(translateElement, optParentElement,
    optRootSelector) {
    var documentTree = optParentElement || document.querySelector(
      optRootSelector) || document;
    return documentTree.querySelector('[data-translate=\'' +
      translateElement + '\']');
  });
})();

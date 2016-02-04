/* Own locators for better test handling */
/* @author Daniel Katzberg */
/* global by */
/* global document */

'use strict';

//Protracor API:
//http://angular.github.io/protractor/
//#/api?view=ProtractorBy.prototype.addLocator
(function() {

  //dataTranslate finds elements with by data-translate=""
  by.addLocator('dataTranslate', function(elementValue, optParentElement,
    optRootSelector) {
    var documentTree = optParentElement || document.querySelector(
      optRootSelector) || document;

    return documentTree.querySelector('[data-translate=\'' +
      elementValue + '\']');
  });

  //find tags with ui-sref attribut with a given attribute value
  by.addLocator('uiSref', function(elementValue, optParentElement,
    optRootSelector) {
    var documentTree = optParentElement || document.querySelector(
      optRootSelector) || document;

    return documentTree.querySelector('[ui-sref=\'' +
      elementValue + '\']');
  });

  // find tags with ng-click attribut with a given attribute value
  by.addLocator('ngClick', function(elementValue, optParentElement,
    optRootSelector) {
    var documentTree = optParentElement || document.querySelector(
      optRootSelector) || document;

    return documentTree.querySelector('[ng-click=\'' +
      elementValue + '\']');
  });
})();

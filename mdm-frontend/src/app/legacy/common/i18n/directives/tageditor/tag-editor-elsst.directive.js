'use strict';

/**
 * tagSearch:
 * Provide a function call with 'searchText' as the first and 'language' as the
 * second parameter (parameter names must match exactly). The function must
 * return a promise resolving to an array containing tag strings.
 *
 * e.g: <... tag-search="ctrl.searchFn(searchText, language)"/>
 */
angular.module('metadatamanagementApp').directive('tagEditorElsst', function() {
  return {
    restrict: 'E',
    controller: 'TagEditorElsstController',
    templateUrl: 'scripts/common/i18n/directives/tageditor/' +
      'tag-editor-elsst.html.tmpl',
    scope: {
      tags: '=',
      tagSearch: '&'
    }
  };
});

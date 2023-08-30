'use strict';

/**
 * tagSearch:
 * Provide a function call with 'searchText' as the first and 'language' as the
 * second parameter (parameter names must match exactly). The function must
 * return a promise resolving to an array containing tag strings.
 *
 * e.g: <... tag-search="ctrl.searchFn(searchText, language)"/>
 */
angular.module('metadatamanagementApp').directive('tagEditor', function() {
  return {
    restrict: 'E',
    controller: 'TagEditorController',
    templateUrl: 'scripts/common/i18n/directives/tageditor/' +
      'tag-editor.html.tmpl',
    scope: {
      tags: '=',
      tagSearch: '&',
      requireGermanTag: '=?',
      requireEnglishTag: '=?'
    }
  };
});

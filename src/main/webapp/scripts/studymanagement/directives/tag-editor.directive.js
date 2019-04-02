'use strict';

angular.module('metadatamanagementApp').directive('tagEditor', function() {
  return {
    restrict: 'E',
    controller: 'TagEditorController',
    templateUrl: 'scripts/studymanagement/directives/' +
      'tag-editor.html.tmpl',
    scope: {
      tags: '='
    }
  };
});

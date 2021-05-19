'use strict';

angular.module('metadatamanagementApp').directive('fdzTrackDownload',
    function($analytics) {
        return {
          restrict: 'A',
          /* jshint -W098 */
          link: function($scope, $element, $attrs) {
            /* jshint +W098 */
            angular.element($element[0]).on('click', function() {
              $analytics.trackLink($attrs.href, 'download');
            });
          }
        };
      });

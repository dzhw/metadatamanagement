'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var startTranslation = {
      //jscs:disable
      'start': {
        'data-search': 'Data Search',
        'image-title': 'Lorem ipsum dolor sit amet',
        'recommendation': 'Recommended Data Package',
        'fdz-text': 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et',
        'show-all': 'Show all data packages'
      }
      //jscs:enable
    };
    $translateProvider.translations('en', startTranslation);
  });


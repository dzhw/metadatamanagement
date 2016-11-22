'use strict';

angular.module('metadatamanagementApp').controller('DisclosureController',
  function(PageTitleService, $translate) {
    $translate('disclosure.title').then(PageTitleService.setPageTitle);
  });

'use strict';

var Component = {
  bindings: {
    lang: '<'
  },
  controller: 'DataPacketController',
  templateUrl:
    'scripts/searchmanagement/components/configurator/detail.html.tmpl'
};

angular
  .module('metadatamanagementApp')
  .component('fdzDataPacket', Component);


'use strict';

var Component = {
  bindings: {
    lang: '<',
    options: '<',
    access: '<'
  },
  controller: 'DataPacketController',
  templateUrl: 'scripts/searchmanagement/components/detail/detail.html.tmpl'
};

angular
  .module('metadatamanagementApp')
  .component('fdzDataPacket', Component);


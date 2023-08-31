/* global _ */
(function() {
  'use strict';

  function MessageBus() {
    var events = {};
    events.messages = [];

    events.set = function(eventName, data) {
      var index = _.findIndex(events.messages, {event: eventName});
      if (index !== -1) {
        events.messages[index].value = data;
      } else {
        events.messages.push({event: eventName, value: data});
      }
    };
    events.get = function(eventName, removeFlag) {
      var index = _.findIndex(events.messages, {event: eventName});
      if (index !== -1) {
        var message = events.messages[index].value;
        if (removeFlag) {
          events.messages.splice(index, 1);
        }
        return message;
      }
      return null;
    };
    events.remove = function(eventName) {
      var index = _.findIndex(events.messages, {event: eventName});
      if (index !== -1) {
        events.messages.splice(index, 1);
      }
    };

    return events;
  }

  angular
    .module('metadatamanagementApp')
    .factory('MessageBus', MessageBus);
})();

/*global SockJS, Stomp */
'use strict';

angular.module('metadatamanagementApp').factory('WebSocketService',
  function($timeout, $mdDialog, ENV, $location) {
    var stompClient = null;

    var connect = function() {
      var socket;
      if (ENV === 'local') {
        socket = new SockJS('/websocket');
      } else {
        // pivotal specific port for websockets
        socket = new SockJS(
          'https://' + $location.host() + ':4443/websocket');
      }
      stompClient = Stomp.over(socket);
      if (ENV !== 'local') {
        stompClient.debug = null;
      }
      stompClient.connect({}, function() {
        stompClient.subscribe('/topic/user-messages', function(data) {
          var message = angular.fromJson(data.body);
          var alert = $mdDialog.alert({
            title: 'Nachricht von ' + message.sender,
            textContent: message.text,
            ok: 'Schließen'
          });
          $mdDialog.show(alert);
        });
      }, function() {
        $timeout(connect, 10000);
      });
    };

    var sendMessageToAllUsers = function(sender, message) {
      stompClient.send(
        '/metadatamanagement/user-message', {},
        angular.toJson({
          'sender': sender,
          'text': message
        }));
    };

    return {
      connect: connect,
      sendMessageToAllUsers: sendMessageToAllUsers
    };
  });

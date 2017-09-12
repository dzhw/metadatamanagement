/*global SockJS, Stomp */
'use strict';

angular.module('metadatamanagementApp').factory('WebSocketService',
  function($timeout, $mdDialog, ENV, $location, localStorageService) {
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

    var sendMessageToAllUsers = function(message) {
      var token = localStorageService.get('token');

      if (token) {
        //jscs:disable requireCamelCaseOrUpperCaseIdentifiers
        token = token.access_token;
        //jscs:enable requireCamelCaseOrUpperCaseIdentifiers
      }
      stompClient.send(
        '/metadatamanagement/user-message', {'access_token': token},
        angular.toJson({
          'text': message
        }));
    };

    return {
      connect: connect,
      sendMessageToAllUsers: sendMessageToAllUsers
    };
  });

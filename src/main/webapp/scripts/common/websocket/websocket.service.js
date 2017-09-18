/*global SockJS, Stomp */
'use strict';

angular.module('metadatamanagementApp').factory('WebSocketService',
  function($timeout, $mdDialog, ENV, $location, localStorageService,
    $translate, LanguageService) {
      var socket = null;
      var stompClient = null;

      var displayUserMessage = function(message) {
          var currentLanguage = LanguageService.getCurrentInstantly();
          var alert = $mdDialog.alert({
            title: $translate.instant(
              'user-management.user-messages.new-message-title',
              {sender: message.sender}),
            textContent: message.text[currentLanguage],
            ok: $translate.instant('global.buttons.close')
          });
          $mdDialog.show(alert);
        };

      var connect = function() {
        if ((stompClient && stompClient.connected) ||
          (socket && socket.readyState !== 3)) {
          return;
        }
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
        stompClient.connect({'test': 'test'}, function() {
          stompClient.subscribe('/topic/user-messages', function(data) {
            displayUserMessage(angular.fromJson(data.body));
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
          '/metadatamanagement/user-messages', {'access_token': token},
          angular.toJson({
            'text': message
          }));
      };

      return {
        connect: connect,
        sendMessageToAllUsers: sendMessageToAllUsers
      };
    });

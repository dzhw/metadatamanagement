'use strict';

angular.module('metadatamanagementApp').config([
  '$translateProvider',

  function($translateProvider) {
    var translations = {
      //jscs:disable
      'administration': {
        'health': {
          'title': 'Verfügbarkeit aller externen Dienste',
          'refresh-button': 'Aktualisieren',
          'stacktrace': 'Stacktrace',
          'details': {
            'details': 'Details',
            'properties': 'Eigenschaften',
            'name': 'Name',
            'value': 'Wert',
            'error': 'Fehler'
          },
          'indicator': {
            'diskSpace': 'Festplattenplatz',
            'mail': 'Email',
            'mongo': 'MongoDB',
            'elasticsearch': 'Elasticsearch',
            'dara': 'Dara',
            'messageBroker': 'Message Broker (für Websockets)',
            'rabbit': 'RabbitMQ',
            'seo4Ajax': 'Seo4Ajax (Prerender as a Service)'
          },
          'table': {
            'service': 'Servicename',
            'status': 'Status',
            'action': 'Aktion'
          },
          'status': {
            'UP': 'UP',
            'DOWN': 'DOWN'
          },
          'elasticsearch': {
            'reindex': 'Reindizieren',
            'reindex-success': 'Elasticsearch Indices werden jetzt neu erstellt...'
          },
          'dara': {
            'update': 'Aktualisieren',
            'update-success': 'DARA erfolgreich aktualisiert',
            'update-failure': 'Es sind Fehler beim Aktualisieren von DARA aufgetreten! (Siehe Details)'
          }
        },
        'logs': {
          'title': 'Loglevel je Logger',
          'nbloggers': 'Es existieren {{ total }} Logger.',
          'filter': 'Filter',
          'table': {
            'name': 'Name',
            'level': 'Stufe'
          }
        },
        'usermanagement': {
          'change-user-status': 'Status ändern'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  }]);

'use strict';

angular.module('metadatamanagementApp').config(
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
            'elasticsearch': 'ElasticSearch',
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
  });

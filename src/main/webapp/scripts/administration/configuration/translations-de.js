'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'administration': {
        'configuration': {
          'title': 'Spring-Konfiguration',
          'filter': 'Filter (nach Präfix)',
          'table': {
            'prefix': 'Präfix',
            'properties': 'Eigenschaften'
          }
        },
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
            'rabbit': 'RabbitMQ'
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
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });

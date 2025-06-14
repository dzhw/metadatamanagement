'use strict';

angular.module('metadatamanagementApp').config([
  '$translateProvider',

  function($translateProvider) {
    var translations = {
      //jscs:disable
      'administration': {
        'health': {
          'title': 'Availability of External Services',
          'refresh-button': 'Refresh',
          'stacktrace': 'Stacktrace',
          'details': {
            'details': 'Details',
            'properties': 'Properties',
            'name': 'Name',
            'value': 'Value',
            'error': 'Error'
          },
          'indicator': {
            'diskSpace': 'Disk space',
            'mail': 'Email',
            'mongo': 'MongoDB',
            'elasticsearch': 'Elasticsearch',
            'dara': 'Dara',
            'daraPid': 'Dara PID Registration',
            'dataCite': 'DataCite DOI Registration',
            'messageBroker': 'Message Broker (for Websockets)',
            'rabbit': 'RabbitMQ',
            'seo4Ajax': 'Seo4Ajax (Prerender as a Service)',
            'pid': 'PID'
          },
          'table': {
            'service': 'Service name',
            'status': 'Status',
            'action': 'Action'
          },
          'status': {
            'UP': 'UP',
            'DOWN': 'DOWN'
          },
          'elasticsearch': {
            'reindex': 'Reindex',
            'reindex-success': 'Elasticsearch indices are currently recreated...'
          },
          'dara': {
            'update': 'Update',
            'update-success': 'DARA update successful',
            'update-failure': 'Errors occurred during DARA update! (see details)'
          },
          'pid': {
            'download': 'Download data'
          }
        },
        'logs': {
          'title': 'Loglevel per Logger',
          'nbloggers': 'There are {{ total }} loggers.',
          'filter': 'Filter',
          'table': {
            'name': 'Name',
            'level': 'Level'
          }
        },
        'usermanagement': {
          'change-user-status': 'Change status'
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  }]);

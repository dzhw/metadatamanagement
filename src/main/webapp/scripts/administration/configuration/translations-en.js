'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'administration': {
        'configuration': {
          'title': 'Spring Configuration',
          'filter': 'Filter (by prefix)',
          'table': {
            'prefix': 'Prefix',
            'properties': 'Properties'
          }
        },
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
            'elasticsearch': 'ElasticSearch',
            'dara': 'Dara',
            'messageBroker': 'Message Broker (for Websockets)'
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
        'metrics': {
          'title': 'Application Metrics',
          'refresh-button': 'Refresh',
          'updating': 'Updating...',
          'jvm': {
            'title': 'JVM Metrics',
            'memory': {
              'title': 'Memory',
              'total': 'Total Memory',
              'heap': 'Heap Memory',
              'nonheap': 'Non-Heap Memory'
            },
            'threads': {
              'title': 'Threads',
              'all': 'All',
              'runnable': 'Runnable',
              'timedwaiting': 'Timed waiting',
              'waiting': 'Waiting',
              'blocked': 'Blocked',
              'dump': {
                'title': 'Threads dump',
                'id': 'Id: ',
                'blockedtime': 'Blocked Time',
                'blockedcount': 'Blocked Count',
                'waitedtime': 'Waited Time',
                'waitedcount': 'Waited Count',
                'lockname': 'Lock name',
                'stacktrace': 'Stacktrace',
                'show': 'Show Stacktrace',
                'hide': 'Hide Stacktrace'
              }
            },
            'gc': {
              'title': 'Garbage collections',
              'marksweepcount': 'Mark Sweep count',
              'marksweeptime': 'Mark Sweep time',
              'scavengecount': 'Scavenge count',
              'scavengetime': 'Scavenge time'
            },
            'http': {
              'title': 'HTTP requests (events per second)',
              'active': 'Active requests:',
              'total': 'Total requests:',
              'table': {
                'code': 'Code',
                'count': 'Count',
                'mean': 'Mean',
                'average': 'Average'
              },
              'code': {
                'ok': 'Ok',
                'notfound': 'Not found',
                'servererror': 'Server Error'
              }
            }
          },
          'servicesstats': {
            'title': 'Services statistics (time in millisecond)',
            'table': {
              'name': 'Service name',
              'count': 'Count',
              'mean': 'Mean',
              'min': 'Min',
              'max': 'Max',
              'p50': 'p50',
              'p75': 'p75',
              'p95': 'p95',
              'p99': 'p99'
            }
          },
          'ehcache': {
            'title': 'Ehcache statistics',
            'cachename': 'Cache name',
            'objects': 'Objects',
            'hits': 'Hits',
            'misses': 'Misses',
            'evictioncount': 'Eviction count',
            'mean': 'Mean get time (ms)'
          },
          'datasource': {
            'usage': 'Usage',
            'title': 'DataSource statistics (time in millisecond)',
            'name': 'Pool usage',
            'count': 'Count',
            'mean': 'Mean',
            'min': 'Min',
            'max': 'Max',
            'p50': 'p50',
            'p75': 'p75',
            'p95': 'p95',
            'p99': 'p99'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });

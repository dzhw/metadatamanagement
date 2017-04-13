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
            'dara': 'Dara'
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
            'reindex-success': 'Elasticsearch Indices erfolgreich erstellt.'
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
        'metrics': {
          'title': 'Anwendungs-Metriken',
          'refresh-button': 'Aktualisieren',
          'updating': 'Aktualisierung...',
          'jvm': {
            'title': 'JVM Metriken',
            'memory': {
              'title': 'Hauptspeicher',
              'total': 'Gesamter Hauptspeicher',
              'heap': 'Heap Speicher',
              'nonheap': 'Non-Heap Speicher'
            },
            'threads': {
              'title': 'Threads',
              'all': 'Alle',
              'runnable': 'Runnable',
              'timedwaiting': 'Wartezeit',
              'waiting': 'Wartend',
              'blocked': 'Geblockt',
              'dump': {
                'title': 'Threads dump',
                'id': 'Id: ',
                'blockedtime': 'Geblockte Zeit',
                'blockedcount': 'Geblockte Zahl',
                'waitedtime': 'Gewartete Zeit',
                'waitedcount': 'Gewartete Anzahl',
                'lockname': 'Lock-Name',
                'stacktrace': 'Stacktrace',
                'show': 'Zeigen',
                'hide': 'Verstecken'
              }
            },
            'gc': {
              'title': 'Speicherbereinigung (GC)',
              'marksweepcount': 'Durchlaufmarkierungs (Mark Sweep) Anzahl',
              'marksweeptime': 'Durchlaufmarkierungs (Mark Sweep) Zeit',
              'scavengecount': 'Bereinigungslauf (Scavenge) Anzahl',
              'scavengetime': 'Bereinigungslauf (Scavenge) Zeit'
            },
            'http': {
              'title': 'HTTP Anfragen (Ereignisse pro Sekunde)',
              'active': 'Aktive Anfragen:',
              'total': 'Alle Anfragen:',
              'table': {
                'code': 'Codierung',
                'count': 'Anzahl',
                'mean': 'Durchschnittswert',
                'average': 'Mittelwert'
              },
              'code': {
                'ok': 'Ok',
                'notfound': 'Nicht gefunden',
                'servererror': 'Server Fehler'
              }
            }
          },
          'servicesstats': {
            'title': 'Service Statistiken (Zeit in Millisekunden)',
            'table': {
              'name': 'Service Name',
              'count': 'Anzahl',
              'mean': 'Durchschnitt',
              'min': 'Minimum',
              'max': 'Maximum',
              'p50': 'p50',
              'p75': 'p75',
              'p95': 'p95',
              'p99': 'p99'
            }
          },
          'ehcache': {
            'title': 'Ehcache Statistiken',
            'cachename': 'Cache Name',
            'objects': 'Objekte',
            'hits': 'Treffer',
            'misses': 'Keine Treffer',
            'evictioncount': 'Anzahl entfernter Objekte',
            'mean': 'Durchschnittliche Zugriffszeit (ms)'
          },
          'datasource': {
            'usage': 'Usage',
            'title': 'Datenquelle (Zeit in millisekunden)',
            'name': 'Pool-Auslastung',
            'count': 'Anzahl',
            'mean': 'Mittel',
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
    $translateProvider.translations('de', translations);
  });

'use strict';

angular.module('metadatamanagementApp').config([
    '$translateProvider',

    function ($translateProvider) {
        var translations = {
            //jscs:disable
            'embargo-warning': {
                'title-order-view': 'Datenpaket "{{projectId}}" unterliegt einem Embargo durch die Datengeber:innen',
                'title-provider-view': 'Datenpaket "{{projectId}}" unterliegt einem Embargo durch die Datengeber:innen',
                'content-order-view': 'Dieses Datenpaket ist derzeit noch nicht bestellbar, da es bis zum {{ dateString }} einem Embargo unterliegt. Die Veröffentlichung kann erst nach diesem Datum erfolgen. Bitte beachten Sie, dass das Embargodatum nicht dem erwarteten Veröffentlichungszeitpunkt entsprechen muss, bitte kontaktieren Sie {{link}} <a href="mailto:userservice@dzhw.eu">userservice@dzhw.eu</a>, wenn Sie Informationen bezüglich des Veröffentlichungsdatums des Datenpakets erhalten wollen.',
                'content-order-view-expired': 'Dieses Datenpaket ist derzeit noch nicht bestellbar, da es sich noch in der Aufbereitung befindet. Kontaktieren Sie <a href="mailto:userservice@dzhw.eu">userservice@dzhw.eu</a>, wenn Sie Informationen bezüglich des Veröffentlichungsdatums des Datenpakets erhalten wollen.',
                'content-provider-view': 'Dieses Datenpaket unterliegt bis zum {{ dateString }} einem Embargo. Die finale Veröffentlichung kann erst nach diesem Datum erfolgen. Das Datenpaket kann weiterhin bearbeitet werden.',
                'content-provider-view-expired': 'Dieses Datenpaket ist aktuell noch nicht final freigegeben. Das Datenpaket kann bis zur finalen Veröffentlichung bearbeitet werden.'
              }
        }
        $translateProvider.translations('de', translations);
    }])
'use strict';

angular.module('metadatamanagementApp').config([
    '$translateProvider',

    function ($translateProvider) {
        var translations = {
            //jscs:disable
            'embargo-warning': {
                'data-package': {
                    'title-order-view': 'Datenpaket "{{projectId}}" unterliegt einem Embargo durch die Datengeber:innen',
                    'title-order-view-expired': 'Das Datenpaket "{{projectId}}" unterlag einem Embargo bis zum {{ date }}. Aktuell befindet sich das Datenpaket in der Aufbereitung.',
                    'title-provider-view': 'Datenpaket "{{projectId}}" unterliegt einem Embargo durch die Datengeber:innen',
                    'title-provider-view-expired': 'Das Datenpaket "{{projectId}}" unterlag einem Embargo bis zum {{ date }}. Aktuell befindet sich das Datenpaket in der Aufbereitung.',
                    'content-order-view': 'Dieses Datenpaket ist derzeit noch nicht bestellbar, da es bis zum {{ date }} einem Embargo unterliegt. Die Veröffentlichung kann erst nach diesem Datum erfolgen. <b>Bitte beachten Sie, dass das Embargodatum nicht dem erwarteten Veröffentlichungszeitpunkt entsprechen muss.</b> Die dargestellten Metadaten sind vorläufig und können sich bis zur finalen Veröffentlichung noch ändern. Bitte kontaktieren Sie {{link}} <a href="mailto:userservice@dzhw.eu">userservice@dzhw.eu</a>, wenn Sie Informationen bezüglich des Veröffentlichungsdatums des Datenpakets erhalten wollen. Sobald die Veröffentlichung erfolgt ist, können Sie das Datenpaket an dieser Stelle in den Warenkorb legen.',
                    'content-order-view-expired': 'Dieses Datenpaket ist derzeit noch nicht bestellbar, da es sich noch in der Aufbereitung befindet. Die dargestellten Metadaten sind vorläufig und können sich bis zur finalen Veröffentlichung noch ändern. Kontaktieren Sie <a href="mailto:userservice@dzhw.eu">userservice@dzhw.eu</a>, wenn Sie Informationen bezüglich des Veröffentlichungsdatums des Datenpakets erhalten wollen. Sobald die Veröffentlichung erfolgt ist, können Sie das Datenpaket an dieser Stelle in den Warenkorb legen.',
                    'content-provider-view': 'Dieses Datenpaket unterliegt bis zum {{ date }} einem Embargo. Die finale Veröffentlichung kann erst nach diesem Datum erfolgen. Das Datenpaket kann weiterhin bearbeitet werden. Achtung! Jede gespeicherte Änderung ist <u>unmittelbar öffentlich einsehbar</u>.',
                    'content-provider-view-expired': 'Dieses Datenpaket ist aktuell noch nicht final freigegeben. Das Datenpaket kann bis zur finalen Veröffentlichung bearbeitet werden. Achtung! Jede gespeicherte Änderung ist <u>unmittelbar öffentlich einsehbar</u>.'
                },
                'analysis-package': {
                    'title-order-view': 'Analysepaket "{{projectId}}" unterliegt einem Embargo durch die Datengeber:innen',
                    'title-order-view-expired': 'Das Analysepaket "{{projectId}}" unterlag einem Embargo bis zum {{ date }}. Aktuell befindet sich das Analysepaket in der Aufbereitung.',
                    'title-provider-view': 'Analysepaket "{{projectId}}" unterliegt einem Embargo durch die Datengeber:innen',
                    'title-provider-view-expired': 'Das Analysepaket "{{projectId}}" unterlag einem Embargo bis zum {{ date }}. Aktuell befindet sich das Analysepaket in der Aufbereitung.',
                    'content-order-view': 'Dieses Analysepaket ist derzeit noch nicht bestellbar, da es bis zum {{ date }} einem Embargo unterliegt. Die Veröffentlichung kann erst nach diesem Datum erfolgen. <b>Bitte beachten Sie, dass das Embargodatum nicht dem erwarteten Veröffentlichungszeitpunkt entsprechen muss.</b> Die dargestellten Metadaten sind vorläufig und können sich bis zur finalen Veröffentlichung noch ändern. Bitte kontaktieren Sie {{link}} <a href="mailto:userservice@dzhw.eu">userservice@dzhw.eu</a>, wenn Sie Informationen bezüglich des Veröffentlichungsdatums des Analysepakets erhalten wollen. Sobald die Veröffentlichung erfolgt ist, können Sie das Analysepaket an dieser Stelle in den Warenkorb legen.',
                    'content-order-view-expired': 'Dieses Analysepaket ist derzeit noch nicht bestellbar, da es sich noch in der Aufbereitung befindet. Die dargestellten Metadaten sind vorläufig und können sich bis zur finalen Veröffentlichung noch ändern. Kontaktieren Sie <a href="mailto:userservice@dzhw.eu">userservice@dzhw.eu</a>, wenn Sie Informationen bezüglich des Veröffentlichungsdatums des Analysepakets erhalten wollen. Sobald die Veröffentlichung erfolgt ist, können Sie das Analysepaket an dieser Stelle in den Warenkorb legen.',
                    'content-provider-view': 'Dieses Analysepaket unterliegt bis zum {{ date }} einem Embargo. Die finale Veröffentlichung kann erst nach diesem Datum erfolgen. Das Analysepaket kann weiterhin bearbeitet werden. Achtung! Jede gespeicherte Änderung ist <u>unmittelbar öffentlich einsehbar</u>.',
                    'content-provider-view-expired': 'Dieses Analysepaket ist aktuell noch nicht final freigegeben. Das Analysepaket kann bis zur finalen Veröffentlichung bearbeitet werden. Achtung! Jede gespeicherte Änderung ist <u>unmittelbar öffentlich einsehbar</u>.'
                }
              }
        }
        $translateProvider.translations('de', translations);
    }])
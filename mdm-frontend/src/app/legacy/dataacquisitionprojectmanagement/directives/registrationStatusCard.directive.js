'use strict';
/**
 * Directive rendering the PID registration status of variables linked to a project
 */
angular.module('metadatamanagementApp').directive('registrationStatusCard', [
    'DaraReleaseResource',
    '$translate',
    '$mdDialog',
    function (DaraReleaseResource, $translate, $mdDialog) {
        return {
            restrict: 'E',
            templateUrl: 'scripts/dataacquisitionprojectmanagement/' +
                'directives/registrationStatusCard.directive.html.tmpl',
            scope: {
                project: '<',
                status: '<'
            },
            controllerAs: 'ctrl',
            controller: ['$scope', function ($scope) {
                /**
                 * Prompts for confirmation and proceeds
                 * to trigger the PID registration process.
                 */
                this.registerVariables = async () => {
                    const i18nPrefix = 'data-acquisition-project-management' +
                        '.project-cockpit.pid-registration.confirm-dialog.';
                    const confirmDialog = $mdDialog.confirm()
                        .title($translate.instant(i18nPrefix + 'title'))
                        .textContent(
                            $translate.instant(i18nPrefix + 'message') +
                            '\n\n' +
                            $translate.instant(i18nPrefix + 'question')
                        )
                        .ariaLabel(
                            $translate.instant(i18nPrefix + 'message') +
                            $translate.instant(i18nPrefix + 'question')
                        )
                        .ok($translate.instant('global.common-dialogs.yes'))
                        .cancel($translate.instant('global.common-dialogs.no'));
                    $mdDialog.show(confirmDialog)
                        .then(() => DaraReleaseResource.variablesRegister($scope.project));
                }
            }]
        };
    }
]);

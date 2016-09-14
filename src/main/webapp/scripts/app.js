'use strict';

angular
  .module(
    'metadatamanagementApp', ['LocalStorageModule', 'tmh.dynamicLocale',
      'pascalprecht.translate',
      'ui.bootstrap', // for modal dialogs
      'elasticsearch',
      'ngResource', 'ui.router', 'ngCookies', 'ngAria', 'ngCacheBuster',
      'ngFileUpload', 'ngMaterial',
      'blockUI', 'LocalStorageModule',
      'ngMessages', 'md.data.table'
    ])

.run(
    function($rootScope, $location, $window, $http, $state, $translate,
      Language, Auth, Principal, ENV, VERSION) {
      $rootScope.ENV = ENV;
      $rootScope.VERSION = VERSION;

      if (typeof String.prototype.endsWith !== 'function') {
        String.prototype.endsWith = function(suffix) {
            return this.indexOf(suffix, this.length - suffix.length) !== -1;
          };
      }

      //init the current language
      if ($location.path().indexOf('/en/') > -1) {
        Language.setCurrent('en');
      } else {
        Language.setCurrent('de');
      }

      $rootScope.$on('$stateChangeStart', function(event, toState,
        toStateParams) {
        $rootScope.toState = toState;
        $rootScope.toStateParams = toStateParams;
        if (Principal.isIdentityResolved()) {
          Auth.authorize();
        }
        // Update the language
        Language.setCurrent(toStateParams.lang);
      });
      $rootScope.$on('$stateChangeSuccess', function(event, toState,
        toParams, fromState, fromParams) {
        var titleKey = 'global.title';
        // Remember previous state unless we've been redirected to login or
        // we've just
        // reset the state memory after logout. If we're redirected to
        // login, our
        // previousState is already set in the authExpiredInterceptor. If
        // we're going
        // to login directly, we don't want to be sent to some previous
        // state anyway
        if (toState.name !== 'login' && $rootScope.previousStateName) {
          $rootScope.previousStateName = fromState.name;
          $rootScope.previousStateParams = fromParams;
        }

        // Set the page title key to the one configured in state or use
        // default one
        if (toState.data.pageTitle) {
          titleKey = toState.data.pageTitle;
          $rootScope.pageTitle = toState.data.pageTitle;
        }

        $translate(titleKey).then(function(title) {
          // Change window title with translated one
          $window.document.title = title;
        });
      });

      $rootScope.back = function() {
        // If previous state is 'activate' or do not exist go to 'search'
        if ($rootScope.previousStateName === 'activate' ||
          $state.get($rootScope.previousStateName) === null) {
          $state.go('search');
        } else {
          $state.go($rootScope.previousStateName,
            $rootScope.previousStateParams);
        }
      };
    })
  .config(
    function($windowProvider, $stateProvider, $urlRouterProvider,
      $httpProvider, $locationProvider, $translateProvider,
      tmhDynamicLocaleProvider, httpRequestInterceptorCacheBusterProvider,
      blockUIConfig, $mdThemingProvider, localStorageServiceProvider) {
      localStorageServiceProvider
      .setPrefix('metadatamanagementApp')
      .setStorageType('localStorage')
      .setNotify(true, true);
      // Cache everything except rest api requests
      httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/,
        /.*protected.*/
      ], true);

      $stateProvider.state('site', {
        'abstract': true,
        url: '/{lang:(?:de|en)}',
        views: {
          'navbar@': {
            templateUrl: 'scripts/common/navbar/views/navbar.html.tmpl',
            controller: 'NavbarController'
          },
          'toolbar@': {
            templateUrl: 'scripts/common/toolbar/views/toolbar.html.tmpl',
            controller: 'ToolbarController'
          }
        },
        resolve: {
          authorize: ['Auth', function(Auth) {
            return Auth.authorize();
          }],
          translatePartialLoader: ['$translatePartialLoader',
          function($translatePartialLoader) {
            $translatePartialLoader.addPart('global');
            $translatePartialLoader.addPart(
              'dataAcquisitionProject.management');
          }
        ]
        }
      });
      $urlRouterProvider.when('', '/de/');
      $urlRouterProvider.otherwise('/de/error');
      $httpProvider.interceptors.push('errorHandlerInterceptor');
      $httpProvider.interceptors.push('authExpiredInterceptor');
      $httpProvider.interceptors.push('authInterceptor');
      $httpProvider.interceptors.push('notificationInterceptor');
      // Initialize angular-translate
      $translateProvider.useLoader('$translatePartialLoader', {
        urlTemplate: 'i18n/{lang}/{part}.json'
      });
      $translateProvider.preferredLanguage('de');

      $translateProvider.useCookieStorage();
      $translateProvider.useSanitizeValueStrategy('escaped');
      $translateProvider
        .addInterpolation('$translateMessageFormatInterpolation');
      $translateProvider
        .useMissingTranslationHandler('translationErrorHandler');

      tmhDynamicLocaleProvider
        .localeLocationPattern(
          'bower_components/angular-i18n/angular-locale_{{locale}}.js');
      tmhDynamicLocaleProvider.useCookieStorage();
      tmhDynamicLocaleProvider.storageKey('NG_TRANSLATE_LANG_KEY');

      //did not manage to use a templateUrl :-(
      blockUIConfig.template =
        '<div ng-controller="BlockUIController" layout="column"' +
        ' class="fdz-block-ui-overlay">' +
        '<div flex layout="row" layout-align="center center">' +
        '<md-progress-circular md-mode="indeterminate"' +
        ' md-diameter="75px"></md-progress-circular>' +
        '<span style="font-size: 24px; margin-left: 1em;"' +
        'ng-if="job.state === \'running\' && job.id !== \'postValidation\'' +
        '" data-translate="global.joblogging.block-ui-message"' +
        ' data-translate-values="{ errors: job.errors, ' +
        'total: job.errors + job.successes}">' +
        '</span>' +
        '</div>' +
        '</div>';

      blockUIConfig.autoInjectBodyBlock = false;
      blockUIConfig.blockBrowserNavigation = true;
      // Tell the blockUI service to ignore certain requests
      blockUIConfig.requestFilter = function(config) {
        // If the request contains '/api/search' ...
        if (config.url.indexOf('_search') !== -1) {
          return false; // ... don't block it.
        }
      };

      $mdThemingProvider.definePalette('dzhwPrimaryPalette', {
        '50': 'F2F7F8',
        '100': 'E5F0F7',
        '200': 'CCE1F0',
        '300': 'B2D2E8',
        '400': '99C3E1',
        '500': '7FB4D8',
        '600': '66A6D1',
        '700': '4C96C9',
        '800': '3388C1',
        '900': '1979BA',
        'A100': '006AB2',
        'A200': '006AB2',
        'A400': '006AB2',
        'A700': '006AB2',
        'contrastDefaultColor': 'light',
        'contrastDarkColors': ['50', '100', '200', '300']
      });

      $mdThemingProvider.definePalette('dzhwAccentPalette', {
        '50': 'FFFAF4',
        '100': 'FEF5E9',
        '200': 'FDECD3',
        '300': 'FCE2BD',
        '400': 'FBD9A7',
        '500': 'FACF91',
        '600': 'FAC57C',
        '700': 'F9BC65',
        '800': 'F8B250',
        '900': 'F7A839',
        'A100': 'F69F24',
        'A200': 'F69F24',
        'A400': 'F69F24',
        'A700': 'F69F24',
        'contrastDefaultColor': 'dark'
      });

      $mdThemingProvider.definePalette('dzhwWarnPalette', {
        '50': 'FAE7E7',
        '100': 'FAE7E7',
        '200': 'F5D0D0',
        '300': 'F0B9B8',
        '400': 'EBA2A1',
        '500': 'E58A89',
        '600': 'E07372',
        '700': 'DB5B5A',
        '800': 'D64543',
        '900': 'D12D2B',
        'A100': 'C91630',
        'A200': 'C91630',
        'A400': 'C91630',
        'A700': 'C91630',
        'contrastDefaultColor': 'light',
        'contrastDarkColors': ['50', '100', '200', '300']
      });

      $mdThemingProvider.theme('default')
        .primaryPalette('dzhwPrimaryPalette', {
          'default': 'A100',
          'hue-1': '800',
          'hue-2': '600',
          'hue-3': '400'
        })
        .accentPalette('dzhwAccentPalette', {
          'default': 'A100',
          'hue-1': '800',
          'hue-2': '600',
          'hue-3': '400'
        })
        .warnPalette('dzhwWarnPalette', {
          'default': 'A100',
          'hue-1': '800',
          'hue-2': '600',
          'hue-3': '400'
        });
    });

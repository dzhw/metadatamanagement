// Karma configuration
// http://karma-runner.github.io/0.10/config/configuration-file.html

module.exports = function(config) {
  config.set({
    // base path, that will be used to resolve files and exclude
    basePath: '../../../',

    // testing framework to use (jasmine/mocha/qunit/...)
    frameworks: ['jasmine'],

    // list of files / patterns to load in the browser
    files: [
      'node_modules/jquery/dist/jquery.js',
      'node_modules/angular/angular.js',
      'node_modules/angular-aria/angular-aria.js',
      'node_modules/angular-ui-bootstrap/ui-bootstrap-tpls.js',
      'node_modules/angular-cookies/angular-cookies.js',
      'node_modules/angular-dynamic-locale/dist/tmhDynamicLocale.js',
      'node_modules/angular-local-storage/dist/angular-local-storage.js',
      'node_modules/angular-resource/angular-resource.js',
      'node_modules/angular-sanitize/angular-sanitize.js',
      'node_modules/angular-translate/dist/angular-translate.js',
      'node_modules/messageformat/messageformat.js',
      'node_modules/angular-translate-interpolation-messageformat/angular-translate-interpolation-messageformat.js',
      'node_modules/angular-translate-storage-cookie/angular-translate-storage-cookie.js',
      'node_modules/angular-ui-router/release/angular-ui-router.js',
      'node_modules/bootstrap-sass/assets/javascripts/bootstrap.js',
      'node_modules/ng-file-upload/dist/ng-file-upload.js',
      'node_modules/elasticsearch-browser/elasticsearch.angular.js',
      'node_modules/jszip/dist/jszip.js',
      'node_modules/file-saver/FileSaver.js',
      'node_modules/jszip-utils/dist/jszip-utils.js',
      'node_modules/angular-translate-handler-log/angular-translate-handler-log.js',
      'node_modules/js-xlsx/dist/xlsx.full.min.js',
      'node_modules/cheet.js/cheet.js',
      'node_modules/angular-animate/angular-animate.js',
      'node_modules/angular-messages/angular-messages.js',
      'node_modules/angular-material/angular-material.js',
      'node_modules/angular-block-ui/dist/angular-block-ui.js',
      'node_modules/lodash/lodash.js',
      'node_modules/plotly.js/dist/plotly.js',
      'node_modules/plotly.js/dist/plotly-locale-de.js',
      'node_modules/highlightjs/highlight.pack.js',
      'node_modules/angular-highlightjs/build/angular-highlightjs.js',
      'node_modules/js-beautify/js/lib/beautify.js',
      'node_modules/js-beautify/js/lib/beautify-css.js',
      'node_modules/js-beautify/js/lib/beautify-html.js',
      'node_modules/clipboard/dist/clipboard.js',
      'node_modules/ngclipboard/dist/ngclipboard.js',
      'node_modules/bowser/src/bowser.js',
      'node_modules/blob-polyfill/Blob.js',
      'node_modules/angular-file-saver/dist/angular-file-saver.bundle.js',
      'node_modules/clientjs/dist/client.min.js',
      'node_modules/sockjs-client/dist/sockjs.js',
      'node_modules/stomp-websocket/lib/stomp.min.js',
      'node_modules/es5-shim/es5-shim.js',
      'node_modules/es5-shim/es5-sham.js',
      'node_modules/es6-shim/es6-shim.js',
      'node_modules/es6-shim/es6-sham.js',
      'node_modules/hyphenator.js/Hyphenator.js',
      'node_modules/hyphenator.js/patterns/de.js',
      'node_modules/hyphenator.js/patterns/en-us.js',
      'node_modules/ng-shortcut/ng-shortcut.js',
      'node_modules/angular-jk-carousel/dist/jk-carousel.js',
      'node_modules/moment/moment.js',
      'node_modules/moment/locale/de.js',
      'node_modules/angular-moment/angular-moment.js',
      'node_modules/angular-recaptcha/release/angular-recaptcha.js',
      'node_modules/jsonformatter/dist/json-formatter.js',
      'node_modules/angular-mocks/angular-mocks.js',
      'node_modules/fdz-paginator/dest/fdz-paginator.js',
      'node_modules/ng-text-truncate-2/ng-text-truncate.js',
      'node_modules/showdown/dist/showdown.js',
      'node_modules/ng-showdown/dist/ng-showdown.js',
      'node_modules/angular-swx-session-storage/release/swx-session-storage.js',
      'node_modules/angulartics/dist/angulartics.min.js',
      'node_modules/angulartics-piwik/dist/angulartics-piwik.min.js',
      'node_modules/angular-uuid/angular-uuid.js',

      'src/main/webapp/scripts/app.js',
      'src/main/webapp/scripts/**/*.js',
      'src/main/webapp/scripts/**/*.html',
      'src/main/webapp/scripts/**/*.html.tmpl',
      'src/test/javascript/spec/helpers/module.js',
      'src/test/javascript/spec/helpers/httpBackend.js',

      // Files needed?
      // 'node_modules/messageformat/locale/en.js',
      // 'node_modules/messageformat/locale/de.js',

      'src/test/javascript/**/!(karma.conf).js'
      // fixtures
      //  {pattern: 'test/javascript/mocks/*.json', watched: true, served: true, included: false}
    ],


    // list of files / patterns to exclude
    exclude: [],

    preprocessors: {
      'src/main/webapp/scripts/**/*.js': ['coverage'],
      '**/*.html.tmpl': ['ng-html2js']
    },

    ngHtml2JsPreprocessor: {
      stripPrefix: 'src/main/webapp/',
      moduleName: 'templates'
    },

    reporters: ['dots', 'coverage'],

    coverageReporter: {

      dir: './target/test-results/coverage',
      reporters: [{
        type: 'lcov',
        subdir: 'report-lcov'
      }, {
        type: 'html',
        subdir: 'report-html'
      }, {
        type: 'cobertura',
        subdir: 'report-cobertura',
        file: 'cobertura.xml'
      }]
    },

    // web server port
    port: 9876,

    // level of logging
    // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
    logLevel: config.LOG_ERROR,
    browserConsoleLogOptions: {level: 'error', format: '%b %T: %m', terminal: false},
    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: false,

    // Start these browsers, currently available:
    // - Chrome
    // - ChromeCanary
    // - Firefox
    // - Opera
    // - Safari (only Mac)
    // - PhantomJS
    // - IE (only Windows)
    browsers: ['ChromeHeadless'],

    // Continuous Integration mode
    // if true, it capture browsers, run tests and exit
    singleRun: false,

    // to avoid DISCONNECTED messages when connecting to slow virtual machines
    browserDisconnectTimeout: 10000, // default 2000
    browserDisconnectTolerance: 1, // default 0
    browserNoActivityTimeout: 4 * 60 * 1000 //default 10000
  });
};

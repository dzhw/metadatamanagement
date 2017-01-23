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
      // bower:js
      'src/main/webapp/bower_components/jquery/dist/jquery.js',
      'src/main/webapp/bower_components/angular/angular.js',
      'src/main/webapp/bower_components/angular-aria/angular-aria.js',
      'src/main/webapp/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
      'src/main/webapp/bower_components/angular-cache-buster/angular-cache-buster.js',
      'src/main/webapp/bower_components/angular-cookies/angular-cookies.js',
      'src/main/webapp/bower_components/angular-dynamic-locale/src/tmhDynamicLocale.js',
      'src/main/webapp/bower_components/angular-local-storage/dist/angular-local-storage.js',
      'src/main/webapp/bower_components/angular-resource/angular-resource.js',
      'src/main/webapp/bower_components/angular-sanitize/angular-sanitize.js',
      'src/main/webapp/bower_components/angular-translate/angular-translate.js',
      'src/main/webapp/bower_components/messageformat/messageformat.js',
      'src/main/webapp/bower_components/angular-translate-interpolation-messageformat/angular-translate-interpolation-messageformat.js',
      'src/main/webapp/bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie.js',
      'src/main/webapp/bower_components/angular-ui-router/release/angular-ui-router.js',
      'src/main/webapp/bower_components/bootstrap-sass/assets/javascripts/bootstrap.js',
      'src/main/webapp/bower_components/json3/lib/json3.js',
      'src/main/webapp/bower_components/ng-file-upload/ng-file-upload.js',
      'src/main/webapp/bower_components/elasticsearch/elasticsearch.js',
      'src/main/webapp/bower_components/jszip/dist/jszip.js',
      'src/main/webapp/bower_components/file-saver.js/FileSaver.js',
      'src/main/webapp/bower_components/jszip-utils/dist/jszip-utils.js',
      'src/main/webapp/bower_components/angular-translate-handler-log/angular-translate-handler-log.js',
      'src/main/webapp/bower_components/d3/d3.js',
      'src/main/webapp/bower_components/nvd3/build/nv.d3.js',
      'src/main/webapp/bower_components/angular-nvd3/dist/angular-nvd3.js',
      'src/main/webapp/bower_components/js-xlsx/dist/xlsx.js',
      'src/main/webapp/bower_components/cheet.js/cheet.js',
      'src/main/webapp/bower_components/angular-animate/angular-animate.js',
      'src/main/webapp/bower_components/angular-messages/angular-messages.js',
      'src/main/webapp/bower_components/angular-material/angular-material.js',
      'src/main/webapp/bower_components/angular-block-ui/dist/angular-block-ui.js',
      'src/main/webapp/bower_components/lodash/lodash.js',
      'src/main/webapp/bower_components/plotly.js/dist/plotly.min.js',
      'src/main/webapp/bower_components/highlightjs/highlight.pack.js',
      'src/main/webapp/bower_components/angular-highlightjs/build/angular-highlightjs.js',
      'src/main/webapp/bower_components/js-beautify/js/lib/beautify.js',
      'src/main/webapp/bower_components/js-beautify/js/lib/beautify-css.js',
      'src/main/webapp/bower_components/js-beautify/js/lib/beautify-html.js',
      'src/main/webapp/bower_components/clipboard/dist/clipboard.js',
      'src/main/webapp/bower_components/ngclipboard/dist/ngclipboard.js',
      'src/main/webapp/bower_components/katex/dist/katex.js',
      'src/main/webapp/bower_components/angular-katex/angular-katex.js',
      'src/main/webapp/bower_components/angular-mocks/angular-mocks.js',
      // endbower
      'src/main/webapp/bower_components/elasticsearch/elasticsearch.angular.js',
      'src/main/webapp/scripts/app.js',
      'src/main/webapp/scripts/**/*.js',
      'src/main/webapp/scripts/**/*.html',
      'src/test/javascript/spec/helpers/module.js',
      'src/test/javascript/spec/helpers/httpBackend.js',
      'src/main/webapp/bower_components/messageformat/locale/en.js',
      'src/main/webapp/bower_components/messageformat/locale/de.js',
      'src/test/javascript/**/!(karma.conf).js'
      // fixtures
      //  {pattern: 'test/javascript/mocks/*.json', watched: true, served: true, included: false}
    ],


    // list of files / patterns to exclude
    exclude: [],

    preprocessors: {
      'src/main/webapp/scripts/**/*.js': ['coverage']
    },

    reporters: ['dots', 'coverage', 'progress'],

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
    logLevel: config.LOG_INFO,

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
    browsers: ['PhantomJS'],

    // Continuous Integration mode
    // if true, it capture browsers, run tests and exit
    singleRun: false,

    // to avoid DISCONNECTED messages when connecting to slow virtual machines
    browserDisconnectTimeout: 10000, // default 2000
    browserDisconnectTolerance: 1, // default 0
    browserNoActivityTimeout: 4 * 60 * 1000 //default 10000
  });
};

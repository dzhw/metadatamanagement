// Generated on 2015-11-09 using generator-jhipster 2.23.0
'use strict';
var fs = require('fs');

var parseString = require('xml2js').parseString;

// Returns the second occurence of the version number
var parseVersionFromPomXml = function() {
  var version;
  var pomXml = fs.readFileSync('pom.xml', 'utf8');
  parseString(pomXml, function(err, result) {
    version = result.project.version[0];
  });
  return version;
};

// usemin custom step
var useminAutoprefixer = {
  name: 'autoprefixer',
  createConfig: function(context, block) {
    if (block.src.length === 0) {
      return {};
    } else {
      return require('grunt-usemin/lib/config/cssmin').createConfig(context,
          block); // Reuse
      // cssmins
      // createConfig
    }
  }
};

module.exports = function(grunt) {
  require('load-grunt-tasks')(grunt);
  require('time-grunt')(grunt);
  var getElasticSearchProperties = function() {
    var DevPath = './src/main/resources/config/application-dev.yml';
    var ProdPath = './src/main/resources/config/application-prod.yml';
    return {
      DevProperties: grunt.file.readYAML(DevPath),
      ProdProperties: grunt.file.readYAML(ProdPath)
    };
  };
  grunt
      .initConfig({
        yeoman: {
          // configurable paths
          app: require('./bower.json').appPath || 'app',
          dist: 'src/main/webapp/dist'
        },
        watch: {
          bower: {
            files: ['bower.json'],
            tasks: ['wiredep']
          },
          ngconstant: {
            files: ['Gruntfile.js', 'pom.xml'],
            tasks: ['ngconstant:dev']
          },
          sass: {
            files: ['src/main/scss/**/*.{scss,sass}'],
            tasks: ['sass:server']
          }
        },
        autoprefixer: {
        // src and dest is configured in a subtask called "generated" by
        // usemin
        },
        wiredep: {
          app: {
            src: ['src/main/webapp/index.html', 'src/main/scss/main.scss'],
            exclude: [/angular-i18n/, // localizations are loaded
            // dynamically
            'bower_components/bootstrap/', // Exclude Bootstrap
            // LESS as
            // we use bootstrap-sass
            ],
            ignorePath: /\.\.\/webapp\/bower_components\//
            // remove
            // ../webapp/bower_components/
            // from paths of
            // injected sass
            // files
          },
          test: {
            src: 'src/test/javascript/karma.conf.js',
            exclude: [/angular-i18n/, /angular-scenario/],
            ignorePath: /\.\.\/\.\.\//, // remove ../ from paths
            // of injected
            // javascripts
            devDependencies: true,
            fileTypes: {
              js: {
                block:
                 /(([\s\t]*)\/\/\s*bower:*(\S*))(\n|\r|.)*?(\/\/\s*endbower)/gi,
                detect: {
                  js: /'(.*\.js)'/gi
                },
                replace: {
                  js: '\'src/{{filePath}}\','
                }
              }
            }
          }
        },
        browserSync: {
          dev: {
            bsFiles: {
              src: [
                  'src/main/webapp/**/*.html',
                  'src/main/webapp/**/*.json',
                  'src/main/webapp/assets/styles/**/*.css',
                  'src/main/webapp/scripts/**/*.{js,html}',
                  'src/main/webapp/assets/images/**/*.' +
                  '{png,jpg,jpeg,gif,webp,svg}',
                  'tmp/**/*.{css,js}']
            }
          },
          options: {
            watchTask: true,
            proxy: 'localhost:8080'
          }
        },
        clean: {
          dist: {
            files: [{
              dot: true,
              src: ['.tmp', '<%= yeoman.dist %>/*', '!<%= yeoman.dist %>/.git*']
            }]
          },
          server: '.tmp'
        },
        jshint: {
          options: {
            jshintrc: '.jshintrc'
          },
          all: ['Gruntfile.js', 'src/main/webapp/scripts/app.js',
              'src/main/webapp/scripts/app/**/*.js',
              'src/main/webapp/scripts/components/**/*.js']
        },
        jscs: {
          src: ['Gruntfile.js', 'src/main/webapp/scripts/app.js',
              'src/main/webapp/scripts/app/**/*.js',
              'src/main/webapp/scripts/components/**/*.js'],
          options: {
            config: 'buildconfig/.jscsrc',
            verbose: true,
            requireCurlyBraces: ['if']
          }
        },
        sass: {
          options: {
            includePaths: ['src/main/webapp/bower_components']
          },
          server: {
            files: [{
              expand: true,
              cwd: 'src/main/scss',
              src: ['*.scss'],
              dest: 'src/main/webapp/assets/styles',
              ext: '.css'
            }]
          }
        },
        concat: {
        // src and dest is configured in a subtask called "generated" by
        // usemin
        },
        uglify: {
        // src and dest is configured in a subtask called "generated" by
        // usemin
        },
        rev: {
          dist: {
            files: {
              src: [
                  '<%= yeoman.dist %>/scripts/**/*.js',
                  '<%= yeoman.dist %>/assets/styles/**/*.css',
                  '<%= yeoman.dist%>' +
                  '/assets/images/**/*.{png,jpg,jpeg,gif,webp,svg}',
                  '<%= yeoman.dist %>/assets/fonts/*']
            }
          }
        },
        useminPrepare: {
          html: 'src/main/webapp/**/*.html',
          options: {
            dest: '<%= yeoman.dist %>',
            flow: {
              html: {
                steps: {
                  js: ['concat', 'uglify'],
                  // Let cssmin concat files so it corrects
                  // relative paths to fonts and images
                  css: ['cssmin', useminAutoprefixer]
                },
                post: {}
              }
            }
          }
        },
        usemin: {
          html: ['<%= yeoman.dist %>/**/*.html'],
          css: ['<%= yeoman.dist %>/assets/styles/**/*.css'],
          js: ['<%= yeoman.dist %>/scripts/**/*.js'],
          options: {
            assetsDirs: ['<%= yeoman.dist %>',
                '<%= yeoman.dist %>/assets/styles',
                '<%= yeoman.dist %>/assets/images',
                '<%= yeoman.dist %>/assets/fonts'],
            patterns: {
              js: [[/(assets\/images\/.*?\.(?:gif|jpeg|jpg|png|webp|svg))/gm,
                  'Update the JS to reference our revved images']]
            },
            dirs: ['<%= yeoman.dist %>']
          }
        },
        imagemin: {
          dist: {
            files: [{
              expand: true,
              cwd: 'src/main/webapp/assets/images',
              src: '**/*.{jpg,jpeg}', // we don't optimize PNG
              // files as it
              // doesn't work on Linux. If you are
              // not on Linux, feel free to use
              // '**/*.{png,jpg,jpeg}'
              dest: '<%= yeoman.dist %>/assets/images'
            }]
          }
        },
        svgmin: {
          dist: {
            files: [{
              expand: true,
              cwd: 'src/main/webapp/assets/images',
              src: '**/*.svg',
              dest: '<%= yeoman.dist %>/assets/images'
            }]
          }
        },
        cssmin: {
        // src and dest is configured in a subtask called "generated" by
        // usemin
        },
        ngtemplates: {
          dist: {
            cwd: 'src/main/webapp',
            src: ['scripts/app/**/*.html', 'scripts/components/**/*.html'],
            dest: '.tmp/templates/templates.js',
            options: {
              module: 'metadatamanagementApp',
              usemin: 'scripts/app.js',
              htmlmin: '<%= htmlmin.dist.options %>'
            }
          }
        },
        htmlmin: {
          dist: {
            options: {
              removeCommentsFromCDATA: true,
              // https://github.com/yeoman/grunt-usemin/issues/44
              collapseWhitespace: true,
              collapseBooleanAttributes: true,
              conservativeCollapse: true,
              removeAttributeQuotes: true,
              removeRedundantAttributes: true,
              useShortDoctype: true,
              removeEmptyAttributes: true,
              keepClosingSlash: true
            },
            files: [{
              expand: true,
              cwd: '<%= yeoman.dist %>',
              src: ['*.html'],
              dest: '<%= yeoman.dist %>'
            }]
          }
        },
        // Put files not handled in other tasks here
        copy: {
          fonts: {
            files: [{
              expand: true,
              dot: true,
              flatten: true,
              cwd: 'src/main/webapp',
              dest: '<%= yeoman.dist %>/assets/fonts',
              src: ['bower_components/bootstrap/fonts/*.*']
            }]
          },
          dist: {
            files: [
                {
                  expand: true,
                  dot: true,
                  cwd: 'src/main/webapp',
                  dest: '<%= yeoman.dist %>',
                  src: ['*.html', 'scripts/**/*.html',
                      'assets/images/**/*.{png,gif,webp,jpg,jpeg,svg}',
                      'assets/fonts/*']
                }, {
                  expand: true,
                  cwd: '.tmp/assets/images',
                  dest: '<%= yeoman.dist %>/assets/images',
                  src: ['generated/*']
                }]
          },
          generateOpenshiftDirectory: {
            expand: true,
            dest: 'deploy/openshift',
            src: ['pom.xml', 'src/main/**']
          }
        },
        karma: {
          unit: {
            configFile: 'src/test/javascript/karma.conf.js',
            singleRun: true
          }
        },
        ngAnnotate: {
          dist: {
            files: [{
              expand: true,
              cwd: '.tmp/concat/scripts',
              src: '*.js',
              dest: '.tmp/concat/scripts'
            }]
          }
        },
        buildcontrol: {
          options: {
            commit: true,
            push: false,
            connectCommits: false,
            message: 'Built %sourceName% from commit' +
            ' %sourceCommit% on branch %sourceBranch%'
          },
          openshift: {
            options: {
              dir: 'deploy/openshift',
              remote: 'openshift',
              branch: 'master'
            }
          }
        },
        ngconstant: {
          options: {
            name: 'metadatamanagementApp',
            deps: false,
            // jscs: disable
            wrap: '"use strict";\n// DO NOT EDIT THIS FILE, EDIT THE GRUNT TASK NGCONSTANT SETTINGS INSTEAD WHICH GENERATES THIS FILE\n{%= __ngModule %}'
            // jscs: enable
          },
          dev: {
            options: {
              dest: 'src/main/webapp/scripts/app/app.constants.js'
            },
            constants: {
              ENV: 'dev',
              VERSION: parseVersionFromPomXml(),
              // jscs: disable
              ElasticSearchProperties: getElasticSearchProperties().DevProperties.metadatamanagement['elasticsearch-angular-client']
              // jscs: enable
            }
          },
          prod: {
            options: {
              dest: '.tmp/scripts/app/app.constants.js'
            },
            constants: {
              ENV: 'prod',
              VERSION: parseVersionFromPomXml(),
              // jscs: disable
              ElasticSearchProperties: getElasticSearchProperties().ProdProperties.metadatamanagement['elasticsearch-angular-client']
              // jscs: enable
            }
          }
        },
        modernizr: {
          dist: {
            dest: 'src/main/webapp/bower_components/modernizr/modernizr.js',
            crawl: false,
            tests: [
              'ambientlight',
              'applicationcache',
              'audio',
              'battery',
              'blob',
              'canvas',
              'canvastext',
              'contenteditable',
              'contextmenu',
              'cookies',
              'cors',
              'crypto',
              'custom-protocol-handler',
              'customevent',
              'dart',
              'dataview-api',
              'emoji',
              'eventlistener',
              'exif-orientation',
              'flash',
              'fullscreen-api',
              'gamepad',
              'geolocation',
              'hashchange',
              'hiddenscroll',
              'history',
              'htmlimports',
              'ie8compat',
              'indexeddb',
              'indexeddbblob',
              'input',
              'inputsearchevent',
              'inputtypes',
              'intl',
              'json',
              'lists-reversed',
              'mathml',
              'notification',
              'pagevisibility-api',
              'performance',
              'pointerevents',
              'pointerlock-api',
              'postmessage',
              'proximity',
              'queryselector',
              'quota-management-api',
              'requestanimationframe',
              'serviceworker',
              'svg',
              'templatestrings',
              'touchevents',
              'typed-arrays',
              'unicode-range',
              'unicode',
              'userdata',
              'vibration',
              'video',
              'vml',
              'web-intents',
              'webanimations',
              'webgl',
              'websockets',
              'xdomainrequest',
              'a/download',
              'audio/loop',
              'audio/preload',
              'audio/webaudio',
              'battery/lowbattery',
              'canvas/blending',
              'canvas/todataurl',
              'canvas/winding',
              'crypto/getrandomvalues',
              'css/all',
              'css/animations',
              'css/appearance',
              'css/backdropfilter',
              'css/backgroundblendmode',
              'css/backgroundcliptext',
              'css/backgroundposition-shorthand',
              'css/backgroundposition-xy',
              'css/backgroundrepeat',
              'css/backgroundsize',
              'css/backgroundsizecover',
              'css/borderimage',
              'css/borderradius',
              'css/boxshadow',
              'css/boxsizing',
              'css/calc',
              'css/checked',
              'css/chunit',
              'css/columns',
              'css/cubicbezierrange',
              'css/displayrunin',
              'css/displaytable',
              'css/ellipsis',
              'css/escape',
              'css/exunit',
              'css/filters',
              'css/flexbox',
              'css/flexboxlegacy',
              'css/flexboxtweener',
              'css/flexwrap',
              'css/fontface',
              'css/generatedcontent',
              'css/gradients',
              'css/hsla',
              'css/hyphens',
              'css/invalid',
              'css/lastchild',
              'css/mask',
              'css/mediaqueries',
              'css/multiplebgs',
              'css/nthchild',
              'css/objectfit',
              'css/opacity',
              'css/overflow-scrolling',
              'css/pointerevents',
              'css/positionsticky',
              'css/pseudoanimations',
              'css/pseudotransitions',
              'css/reflections',
              'css/regions',
              'css/remunit',
              'css/resize',
              'css/rgba',
              'css/scrollbars',
              'css/shapes',
              'css/siblinggeneral',
              'css/subpixelfont',
              'css/supports',
              'css/target',
              'css/textalignlast',
              'css/textshadow',
              'css/transforms',
              'css/transforms3d',
              'css/transformstylepreserve3d',
              'css/transitions',
              'css/userselect',
              'css/valid',
              'css/vhunit',
              'css/vmaxunit',
              'css/vminunit',
              'css/vwunit',
              'css/will-change',
              'css/wrapflow',
              'dom/classlist',
              'dom/createElement-attrs',
              'dom/dataset',
              'dom/documentfragment',
              'dom/hidden',
              'dom/microdata',
              'dom/mutationObserver',
              'elem/bdi',
              'elem/datalist',
              'elem/details',
              'elem/output',
              'elem/picture',
              'elem/progress-meter',
              'elem/ruby',
              'elem/template',
              'elem/time',
              'elem/track',
              'elem/unknown',
              'es5/array',
              'es5/date',
              'es5/function',
              'es5/object',
              'es5/specification',
              'es5/strictmode',
              'es5/string',
              'es5/syntax',
              'es5/undefined',
              'es6/array',
              'es6/contains',
              'es6/generators',
              'es6/math',
              'es6/number',
              'es6/object',
              'es6/promises',
              'es6/string',
              'event/deviceorientation-motion',
              'event/oninput',
              'file/api',
              'file/filesystem',
              'forms/capture',
              'forms/fileinput',
              'forms/fileinputdirectory',
              'forms/formattribute',
              'forms/inputnumber-l10n',
              'forms/placeholder',
              'forms/requestautocomplete',
              'forms/validation',
              'iframe/sandbox',
              'iframe/seamless',
              'iframe/srcdoc',
              'img/apng',
              'img/jpeg2000',
              'img/jpegxr',
              'img/sizes',
              'img/srcset',
              'img/webp-alpha',
              'img/webp-animation',
              'img/webp-lossless',
              'img/webp',
              'input/formaction',
              'input/formenctype',
              'input/formmethod',
              'input/formtarget',
              'network/beacon',
              'network/connection',
              'network/eventsource',
              'network/fetch',
              'network/xhr-responsetype-arraybuffer',
              'network/xhr-responsetype-blob',
              'network/xhr-responsetype-document',
              'network/xhr-responsetype-json',
              'network/xhr-responsetype-text',
              'network/xhr-responsetype',
              'network/xhr2',
              'script/async',
              'script/defer',
              'speech/speech-recognition',
              'speech/speech-synthesis',
              'storage/localstorage',
              'storage/sessionstorage',
              'storage/websqldatabase',
              'style/scoped',
              'svg/asimg',
              'svg/clippaths',
              'svg/filters',
              'svg/foreignobject',
              'svg/inline',
              'svg/smil',
              'textarea/maxlength',
              'url/bloburls',
              'url/data-uri',
              'url/parser',
              'video/autoplay',
              'video/loop',
              'video/preload',
              'webgl/extensions',
              'webrtc/datachannel',
              'webrtc/getusermedia',
              'webrtc/peerconnection',
              'websockets/binary',
              'window/atob-btoa',
              'window/framed',
              'window/matchmedia',
              'workers/blobworkers',
              'workers/dataworkers',
              'workers/sharedworkers',
              'workers/transferables',
              'workers/webworkers'
            ],
            options: [
              'domPrefixes',
              'prefixes',
              'addTest',
              'atRule',
              'hasEvent',
              'mq',
              'prefixed',
              'prefixedCSS',
              'prefixedCSSValue',
              'testAllProps',
              'testProp',
              'testStyles',
              'html5printshiv',
              'html5shiv',
              'setClasses'
            ]
          }
        }
      });

  grunt.registerTask('serve', ['clean:server', 'wiredep', 'ngconstant:dev',
      'sass:server', 'browserSync', 'watch']);

  grunt.registerTask('server', function(target) {
    grunt.log.warn('The `server` task has been deprecated.' +
        ' Use `grunt serve` to start a server.');
    grunt.task.run([target ? ('serve:' + target) : 'serve']);
  });

  grunt.registerTask('test', ['clean:server', 'modernizr:dist',
      'ngconstant:dev', 'sass:server', 'wiredep:test', 'karma',
      'jshint', 'jscs']);

  grunt.registerTask('build', ['test', 'clean:dist', 'wiredep:app',
   'ngconstant:prod',
      'useminPrepare', 'ngtemplates', 'imagemin', 'svgmin',
      'concat', 'copy:fonts', 'copy:dist', 'ngAnnotate', 'cssmin',
      'autoprefixer', 'uglify', 'rev', 'usemin', 'htmlmin']);

  grunt.registerTask('builddev', ['test', 'clean:dist', 'wiredep:app',
   'ngAnnotate']);

  grunt.registerTask('buildOpenshift', ['test', 'build',
      'copy:generateOpenshiftDirectory']);

  grunt.registerTask('deployOpenshift', ['test', 'build',
      'copy:generateOpenshiftDirectory', 'buildcontrol:openshift']);

  grunt.registerTask('default', ['serve']);
};

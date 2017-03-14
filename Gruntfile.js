// Generated on 2015-11-09 using generator-jhipster 2.23.0
'use strict';
var fs = require('fs');

var parseString = require('xml2js').parseString;

// Returns the second occurence of the version number
var parseVersionFromPomXml = function() {
  var version;
  var pomXml = fs.readFileSync('pom.xml', 'utf8');
  parseString(pomXml, function(err, result) { // jshint ignore:line
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
    var LocalPath = './src/main/resources/config/application-local.yml';
    var DevPath = './src/main/resources/config/application-dev.yml';
    var TestPath = './src/main/resources/config/application-test.yml';
    var ProdPath = './src/main/resources/config/application-prod.yml';
    return {
      LocalProperties: grunt.file.readYAML(LocalPath),
      DevProperties: grunt.file.readYAML(DevPath),
      TestProperties: grunt.file.readYAML(TestPath),
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
          tasks: ['ngconstant:local']
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
          // remove ../webapp/bower_components/
          // from paths of injected sass files
          ignorePath: /\.\.\/webapp\/bower_components\//
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
              // jscs:disable
              block: /(([\s\t]*)\/\/\s*bower:*(\S*))(\n|\r|.)*?(\/\/\s*endbower)/gi,
              // jscs:enable
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
      htmlangular: {
        options: {
          reportpath: 'target/html-angular-validate-report.json',
          reportCheckstylePath: 'target/' +
            'html-angular-validate-report-checkstyle.xml',
          tmplext: '.html.tmpl',
          customtags: [
            'jh-*',
            'uib-*',
            'password-strength-bar',
            'nvd3',
            'loading',
            'md-content',
            'md-sidenav',
            'md-toolbar',
            'md-toast',
            'md-dialog',
            'md-autocomplete',
            'variable-search-result',
            'survey-search-result',
            'question-search-result',
            'dataset-search-result',
            'instrument-search-result',
            'md-progress-linear',
            'md-divider',
            'i18n'
          ],
          relaxerror: [
            'The “date” input type is not supported in all browsers.' +
            ' Please be sure to test, and consider using a polyfill.',
            'Bad value “file” for attribute “type” on element “button”.',
            'Element “dl” not allowed as child of' +
            ' element “pre” in this context.',
            'Element “md-progress-linear” not allowed as child of ' +
            'element “body” in this context. (Suppressing further ' +
            'errors from this subtree.)',
            'Element “md-content” not allowed as child of element' +
            ' “body” in this context. (Suppressing further errors ' +
            'from this subtree.)',
            'Element “md-card” not allowed as child of element' +
            ' “body” in this context. (Suppressing further errors ' +
            'from this subtree.)', 'Attribute “layout” not allowed on' +
            ' element “div” at this point.',
            'Attribute “layout-gt-sm” not' +
            ' allowed on element “div” at this point.',
            'Element “md-card” ' +
            'not allowed as child of element “div” in this context. ' +
            '(Suppressing further errors from this subtree.)',
            'Section lacks heading. Consider using “h2”-“h6” elements' +
            ' to add identifying headings to all sections.',
            'Attribute “flex” not allowed on element “div” at this point.',
            'Element “md-toolbar” not allowed as child of element “div” in ' +
            'this context. (Suppressing further errors from this subtree.)',
            'Attribute “layout” not allowed on element “body” at this point.',
            'Attribute “block-ui” not allowed on element “body” at this point.',
            'Attribute “has-any-authority” not allowed on element “div” at' +
            ' this point.',
            'Element “md-button” not allowed as child of element “div” in' +
            ' this context. (Suppressing further errors from this subtree.)',
            'This document appears to be written in',
            'Element “md-dialog” not allowed as child of element “body” in' +
            'this context. (Suppressing further errors from this subtree.)',
            'Element “variable-search-result” not allowed as child of element' +
            '“div” in this context. (Suppressing further errors from this sub' +
            'tree.)',
            'Element “survey-search-result” not allowed as child of element' +
            '“div” in this context. (Suppressing further errors from this sub' +
            'tree.)',
            'Element “question-search-result” not allowed as child of element' +
            '“div” in this context. (Suppressing further errors from this sub' +
            'tree.)',
            'Element “study-search-result” not allowed as child of element ' +
            '“div” in this context. ' +
            '(Suppressing further errors from this subtree.)',
            'Element “dataset-search-result” not allowed as child of element' +
            '“div” in this context. (Suppressing further errors from this sub' +
            'tree.)',
            'Element “related-publication-search-result” not allowed as child' +
            ' of element “div” in this context. (Suppressing further errors' +
            ' from this subtree.)',
            'Attribute “layout-align” not allowed on element “div” at this' +
            ' point.',
            'Element “md-card” not allowed as child of element “a” in this' +
            ' context. (Suppressing further errors from this subtree.)',
            'Attribute “uib-pagination” not allowed on element' +
            ' “ul” at this point.',
            'Attribute “next-text” not allowed on element “ul” at this point.',
            'Attribute “previous-text” not allowed on element' +
            ' “ul” at this point.',
            'Attribute “total-items” not allowed on element' +
            ' “ul” at this point.',
            'Attribute “max-size” not allowed on element “ul” at this point.',
            'Attribute “items-per-page” not allowed on element' +
            ' “ul” at this point.',
            'Consider adding a “lang” attribute to the “html” start tag to' +
            ' declare the language of this document.',
            'Element “md-icon” not allowed as child of element “div” in this' +
            ' context. (Suppressing further errors from this subtree.)',
            'Attribute “layout-margin” not ' +
            'allowed on element “div” at this point.',
            'Attribute “block-ui” not allowed on element “div” at this point.',
            'Element “md-virtual-repeat-container” not allowed as child of ' +
            'element “div” in this context. (Suppressing further errors ' +
            'from this subtree.)', 'Attribute “items-per-pageobject” ' +
            'not allowed on element “ul” at this point.',
            'Consider avoiding viewport values that prevent ' +
            'users from resizing documents.', 'Attribute' +
            ' “md-highlight-text” not allowed on element “span” at this point.',
            'Attribute “layout-padding” not allowed on ' +
            'element “div” at this point.', 'Attribute “hljs” not ' +
            'allowed on element “div” at this point.', 'Attribute ' +
            '“hljs-language” not allowed on element “div” at this point.',
            'Attribute “hljs-source” not allowed on element “div” at this ' +
            'point.',
            'Attribute “katex” not allowed on element “span” at ' +
            'this point.',
            'Element “diagram” not allowed as child of element' +
            ' “div” in this context.', 'Bad value “{{row.language}}” for ' +
            'attribute “lang” on element “td”: Subtags must not exceed 8 ' +
            'characters in length.'
          ],
          customattrs: [
            'show-validation',
            'jh-*',
            'ascending',
            'callback',
            'accept',
            'ngf-*',
            'layout-sm',
            'layout-xs',
            'flex-gt-sm',
            'hide-xs',
            'hide-sm',
            'md-sidenav-focus',
            'md-autofocus',
            'md-ink-ripple'
          ]
        },
        files: {
          src: ['src/main/webapp/*.html',
            'src/main/webapp/scripts/**/*.html.tmpl',
            'src/main/webapp/scripts/**/*.html'
          ]
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
              'tmp/**/*.{css,js}'
            ]
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
            src: ['.tmp', '<%= yeoman.dist %>/*',
              '!<%= yeoman.dist %>/.git*'
            ]
          }]
        },
        server: '.tmp'
      },
      jshint: {
        options: {
          jshintrc: '.jshintrc'
        },
        all: ['Gruntfile.js', 'src/main/webapp/scripts/**/*.js']
      },
      jscs: {
        src: ['Gruntfile.js', 'src/main/webapp/scripts/**/*.js'],
        options: {
          config: 'buildconfig/.jscsrc',
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
        options: {
          compress: {
            unused: false
          }
        }
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
              '!<%= yeoman.dist%>/assets/images/egg.jpg',
              '<%= yeoman.dist %>/assets/fonts/*'
            ]
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
            '<%= yeoman.dist %>/assets/fonts'
          ],
          patterns: {
            js: [
              [/(assets\/images\/.*?\.(?:gif|jpeg|jpg|png|webp|svg))/gm,
                'Update the JS to reference our revved images'
              ]
            ]
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
          src: ['scripts/**/*.html.tmpl', 'scripts/**/*.html'],
          dest: '.tmp/templates/templates.js',
          options: {
            module: 'metadatamanagementApp',
            usemin: 'scripts/client.js',
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
        localfonts: {
          files: [{
            expand: true,
            dot: true,
            flatten: true,
            cwd: 'src/main/webapp',
            dest: 'src/main/webapp/assets/styles/fonts',
            src: [
              'bower_components/bootstrap-sass/assets/fonts/bootstrap/*.*'
            ]
          }]
        },
        fonts: {
          files: [{
            expand: true,
            dot: true,
            flatten: true,
            cwd: 'src/main/webapp',
            dest: '<%= yeoman.dist %>/assets/styles/fonts',
            src: [
              'bower_components/bootstrap-sass/assets/fonts/bootstrap/*.*'
            ]
          }, {
            expand: true,
            dot: true,
            flatten: true,
            cwd: 'src/main/webapp',
            src: ['bower_components/katex/dist/fonts/*.*'],
            dest: '<%= yeoman.dist %>/assets/styles/fonts'
          }]
        },
        dist: {
          files: [{
            expand: true,
            dot: true,
            cwd: 'src/main/webapp',
            dest: '<%= yeoman.dist %>',
            src: ['*.html', 'scripts/**/*.html',
              'assets/images/**/*.{png,gif,webp,jpg,jpeg,svg}'
            ]
          }, {
            expand: true,
            cwd: '.tmp/assets/images',
            dest: '<%= yeoman.dist %>/assets/images',
            src: ['generated/*']
          }]
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
          wrap: '// jscs:disable\n \'use strict\';\n// DO NOT EDIT THIS FILE, EDIT THE GRUNT TASK NGCONSTANT SETTINGS\n//INSTEAD WHICH GENERATES THIS FILE\n{%= __ngModule %}'
            // jscs: enable
        },
        local: {
          options: {
            dest: 'src/main/webapp/scripts/app.constants.js'
          },
          constants: {
            ENV: 'local',
            VERSION: parseVersionFromPomXml(),
            // jscs: disable
            ElasticSearchProperties: getElasticSearchProperties().LocalProperties
              .metadatamanagement['elasticsearch-angular-client']
              // jscs: enable
          }
        },
        dev: {
          options: {
            dest: '.tmp/scripts/app.constants.js'
          },
          constants: {
            ENV: 'dev',
            VERSION: parseVersionFromPomXml(),
            // jscs: disable
            ElasticSearchProperties: getElasticSearchProperties().DevProperties
              .metadatamanagement['elasticsearch-angular-client']
              // jscs: enable
          }
        },
        test: {
          options: {
            dest: '.tmp/scripts/app.constants.js'
          },
          constants: {
            ENV: 'test',
            VERSION: parseVersionFromPomXml(),
            // jscs: disable
            ElasticSearchProperties: getElasticSearchProperties().TestProperties
              .metadatamanagement['elasticsearch-angular-client']
              // jscs: enable
          }
        },
        prod: {
          options: {
            dest: '.tmp/scripts/app.constants.js'
          },
          constants: {
            ENV: 'prod',
            VERSION: parseVersionFromPomXml(),
            // jscs: disable
            ElasticSearchProperties: getElasticSearchProperties().ProdProperties
              .metadatamanagement['elasticsearch-angular-client']
              // jscs: enable
          }
        }
      }
    });

  grunt.registerTask('serve', ['clean:server', 'wiredep', 'ngconstant:local',
    'sass:server', 'copy:localfonts', 'browserSync', 'watch'
  ]);

  grunt.registerTask('server', function(target) {
    grunt.log.warn('The `server` task has been deprecated.' +
      ' Use `grunt serve` to start a server.');
    grunt.task.run([target ? ('serve:' + target) : 'serve']);
  });

  grunt.registerTask('test', ['clean:server',
    'ngconstant:local', 'sass:server', 'wiredep:test', 'karma',
    'jshint', 'jscs'
  ]);

  grunt.registerTask('builddev', ['test', 'htmlangular', 'clean:dist',
    'wiredep:app', 'ngconstant:dev',
    'useminPrepare', 'ngtemplates', 'imagemin', 'svgmin',
    'concat', 'copy:fonts', 'copy:dist', 'ngAnnotate', 'cssmin',
    'autoprefixer', 'uglify', 'rev', 'usemin', 'htmlmin'
  ]);

  grunt.registerTask('buildtest', ['test', 'htmlangular', 'clean:dist',
    'wiredep:app', 'ngconstant:test',
    'useminPrepare', 'ngtemplates', 'imagemin', 'svgmin',
    'concat', 'copy:fonts', 'copy:dist', 'ngAnnotate', 'cssmin',
    'autoprefixer', 'uglify', 'rev', 'usemin', 'htmlmin'
  ]);

  grunt.registerTask('buildprod', ['test', 'htmlangular', 'clean:dist',
    'wiredep:app', 'ngconstant:prod',
    'useminPrepare', 'ngtemplates', 'imagemin', 'svgmin',
    'concat', 'copy:fonts', 'copy:dist', 'ngAnnotate', 'cssmin',
    'autoprefixer', 'uglify', 'rev', 'usemin', 'htmlmin'
  ]);

  grunt.registerTask('buildlocal', ['test', 'clean:dist', 'copy:localfonts',
    'wiredep:app', 'ngconstant:local', 'ngAnnotate'
  ]);

  grunt.registerTask('default', ['serve']);
};

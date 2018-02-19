(function() {
  'use strict';

  var gulp = require('gulp'),
    jshint = require('gulp-jshint'),
    concat = require('gulp-concat'),
    gutil = require('gulp-util');

  function handleError(error) {
    gutil.log(error.message);
    process.exit(1);
  };

  gulp.task('lint', function() {
    return gulp.src('src/app/**/*.js')
      .pipe(jshint())
      .pipe(jshint.reporter('jshint-stylish'))
      .pipe(jshint.reporter('fail'))
      .on('error', handleError);
  });

  gulp.task('js', ['lint'], function() {
    var files = [];

    files.push('src/app/**/*.exports.js');
    files.push('src/app/**/*.module.js');
    files.push('src/app/**/*.js');

    return gulp.src(files)
      .pipe(concat('scripts.js'))
      .pipe(gulp.dest('.tmp'));
  });

})();

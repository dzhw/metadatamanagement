(function() {
  'use strict';

  var gulp = require('gulp'),
    templateCache = require('gulp-angular-templatecache'),
    size = require('gulp-size'),
    templateCacheOptions = {
      module: 'jkAngularCarousel.templates',
      moduleSystem: 'IIFE',
      standalone: true,
      root: ''
    };

  gulp.task('templates', function() {
    return gulp.src('src/app/**/*.html')
      .pipe(templateCache('templates.js', templateCacheOptions))
      .pipe(gulp.dest('.tmp'))
      .pipe(size());
  });

})();

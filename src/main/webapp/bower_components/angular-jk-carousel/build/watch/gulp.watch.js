(function() {
  'use strict';

  var gulp = require('gulp');

  gulp.task('watch', ['build'], function() {
    gulp.watch('src/app/**/*.js', ['build']);
    gulp.watch('src/app/**/*.html', ['build']);
    gulp.watch('src/**/*.scss', ['build']);
  });

})();

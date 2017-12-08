(function() {
  'use strict';

  var gulp = require('gulp'),
    runSequence = require('run-sequence');

  gulp.task('pre-build-tasks', function(done) {
    runSequence(
      'clean', //Cleans dist and .tmp folder
      'clean-cache', //Cleans gulp cache
      done
    );
  });

  gulp.task('build', function(done) {
    runSequence(
      'pre-build-tasks',
      'assets', //compile sass files.
      'templates', //Concatenates all html directives into a single js file and save them in the angular cache
      'js', //Compiles typescript files and concatenate them in a single file (app.js)
      'concat-dev',
      'concat-prod',
      'post-build-taks',
      done
    );
  });

  gulp.task('post-build-taks', function(done) {
    runSequence(
      'clean-tmp', //Cleans the .tmp folder
      done
    );
  });

})();

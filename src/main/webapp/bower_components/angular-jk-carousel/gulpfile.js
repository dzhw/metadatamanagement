'use strict';

var gulp = require('gulp');

require('require-dir')('./build', {recurse: true});

gulp.task('default', function(){
	gulp.start('build');
});

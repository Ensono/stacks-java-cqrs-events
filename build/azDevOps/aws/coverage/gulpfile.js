var gulp = require('gulp');

// For inlining images in CSS
const postcss = require('gulp-postcss');
const imageInliner = require('postcss-image-inliner');

gulp.task('inline-css-images', function() {
    var opts = {
        assetPaths: ['./jacoco/jacoco-resources'],
    };

    var plugins = [
        imageInliner(opts),
    ];

    return gulp.src('./jacoco/**/*.css')
        .pipe(postcss(plugins))
        .pipe(gulp.dest('./jacoco'));
});

// For inlining everything into the HTML files
var inlinesource = require('gulp-inline-source');

gulp.task('inline-sources', function () {
    return gulp.src('./jacoco/**/*.html')
        .pipe(inlinesource({attribute: false, compress: false}))
        .pipe(gulp.dest('./jacoco-inline'));
});

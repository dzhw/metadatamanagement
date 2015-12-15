//
// See README for overview
//

'use strict';

//
// Fetch the PDF document from the URL using promises
//
/* jshint ignore:start */
var pdfPath = '/../questionnairs/FB_2005.1_Variablenplan.pdf';
var page = 1;
PDFJS.getDocument(pdfPath).then(function(pdf) {
  // Using promise to fetch the page
  pdf.getPage(page).then(function(page) {
    var scale = 0.75;
    var viewport = page.getViewport(scale);

    //
    // Prepare canvas using PDF page dimensions
    //
    var canvas = document.getElementById('the-canvas');
    var context = canvas.getContext('2d');
    canvas.height = viewport.height;
    canvas.width = viewport.width;

    //
    // Render PDF page into canvas context
    //
    var renderContext = {
      canvasContext: context,
      viewport: viewport
    };
    page.render(renderContext);
  });
});
/* jshint ignore:end */

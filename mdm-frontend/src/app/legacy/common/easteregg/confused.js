/* global cheet */
/* global document */
'use strict';

var showConfusedGif = function(name) {
  var node = document.createElement('div');
  node.className = 'egg-confused';
  document.body.appendChild(node);

  var img = document.createElement('img');
  if (name === 'andy') {
    img.src = 'assets/images/andy.gif';
  } else {
    img.src = 'assets/images/travolta.gif';
  }
  node.appendChild(img);

  document.addEventListener('keydown', function(e) {
    if (e.keyCode === 27) {
      document.removeEventListener('keydown', this);
      node.remove();
    }
  }, false);
};

cheet('c o n f u s e d space t r a v o l t a', function() {
  showConfusedGif('travolta');
});

cheet('c o n f u s e d space a n d y', function() {
  showConfusedGif('andy');
});

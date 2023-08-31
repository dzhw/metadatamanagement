/* global cheet, document, jQuery*/
'use strict';

cheet('d a n k e k i m', function() {
  var showThankYou = function() {
    var node = document.createElement('div');
    node.id = 'egg-thank-you-container';
    document.body.appendChild(node);
    jQuery.ajax({
      url: './assets/images/thankyou.svg',
      cache: true,
      error: function(error) {
        console.log(error);
      },
      success: function(image) {
        for (var i = 0; i < image.children.length; i++) {
          node.appendChild(image.children[i]);
        }
        jQuery('.egg-path-first')
        .velocity({'stroke-dashoffset': 1800}, 0)
        .velocity({'stroke-dashoffset': 0}, {duration: 2550, delay: 0});

        jQuery('.egg-path-second')
          .velocity({'stroke-dashoffset': 300}, 0)
          .velocity({'stroke-dashoffset': 0}, {duration: 700, delay: 700});

        jQuery('.egg-path-third')
          .velocity({'stroke-dashoffset': 200}, 0)
          .velocity({'stroke-dashoffset': 0}, {duration: 700, delay: 1750});

        jQuery('.egg-path-fourth')
          .velocity({'stroke-dashoffset': 300}, 0)
          .velocity({'stroke-dashoffset': 0}, {duration: 1000, delay: 1750});

        jQuery('.egg-path-fifth')
          .velocity({'stroke-dashoffset': 800}, 0)
          .velocity({'stroke-dashoffset': 0}, {duration: 1200, delay: 2550});

        jQuery('.egg-path-sixth')
          .velocity({'stroke-dashoffset': 600}, 0)
          .velocity({'stroke-dashoffset': 0}, {duration: 1000, delay: 3450});
      }
    });

    document.addEventListener('keydown', function(e) {
      if (e.keyCode === 27) {
        document.removeEventListener('keydown', this);
        node.remove();
      }
    }, false);
  };

  jQuery.ajax({
    url: 'https://cdnjs.cloudflare.com/ajax/libs/' +
    'velocity/2.0.2/velocity.min.js',
    dataType: 'script',
    cache: true,
    success: function() {
          showThankYou();
        }
  });
});

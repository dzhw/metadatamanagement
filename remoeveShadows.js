(function() {
  var collections = ['data_acquisition_projects', 'data_sets', 'instruments', 'questions',
    'studies', 'surveys', 'variables'];

  collections.forEach(function(collection) {
    db[collection].remove({shadow: true});
  });
})();

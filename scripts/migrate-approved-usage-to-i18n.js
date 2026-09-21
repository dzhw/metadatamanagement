/*
 * One-off MongoDB migration for approvedUsage.
 *
 * Run this against the metadatamanagement database before starting a version
 * of the application where DataPackage.approvedUsage is an I18nString.
 *
 * The historical scalar value is treated as German. The migration is
 * idempotent: only documents whose approvedUsage is still a string are changed.
 * After the migration, rebuild/reindex the Elasticsearch data_packages index.
 */
(function() {
  'use strict';

  var dataPackages = db.getCollection('data_packages');
  var migrated = 0;

  dataPackages.find({approvedUsage: {$type: 2}}).forEach(function(dataPackage) {
    dataPackages.updateOne(
      {_id: dataPackage._id},
      {$set: {
        approvedUsage: {
          de: dataPackage.approvedUsage,
          en: ''
        }
      }}
    );
    migrated++;
  });

  dataPackages.getIndexes().forEach(function(index) {
    if (index.key && Object.keys(index.key).length === 1 &&
        index.key.approvedUsage !== undefined) {
      print('Dropping obsolete MongoDB index: ' + index.name);
      dataPackages.dropIndex(index.name);
    }
  });

  print('Migrated approvedUsage in ' + migrated + ' data package document(s).');
  print('Reindex Elasticsearch after this migration.');
}());

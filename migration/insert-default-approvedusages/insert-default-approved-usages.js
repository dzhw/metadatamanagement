// The current database to use.
use('metadatamanagement');


// add field approvedUsageList with default value ["SCIENTIFIC_USE"] to all documents in data_packages collection if the field does not exist
db.getCollection('data_packages')
  .updateMany(
    {
        approvedUsageList: { $exists: false }
    },
    {
      $set: {
        approvedUsageList: ["SCIENTIFIC_USE"]
      }
    }
  );
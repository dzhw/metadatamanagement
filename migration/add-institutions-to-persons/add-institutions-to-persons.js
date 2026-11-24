// The current database to use.
use('metadatamanagement');

function deepCopy(value) {
  return JSON.parse(JSON.stringify(value));
}

function migratePersons(collectionName, personFieldName) {
  const documents = db.getCollection(collectionName)
    .find({
      institutions: { $exists: true, $ne: [] },
      [personFieldName]: { $exists: true, $ne: [] }
    })
    .toArray();

  documents.forEach(doc => {
    const packageInstitutions = doc.institutions || [];
    const people = doc[personFieldName] || [];
    let changed = false;

    people.forEach(person => {
      if (!person.institutions || person.institutions.length === 0) {
        person.institutions = deepCopy(packageInstitutions);
        changed = true;
      }
    });

    if (changed) {
      db.getCollection(collectionName).updateOne(
        { _id: doc._id },
        {
          $set: {
            [personFieldName]: people
          }
        }
      );
    }
  });
}

migratePersons('analysis_packages', 'authors');
migratePersons('analysis_packages', 'dataCurators');
migratePersons('data_packages', 'projectContributors');
migratePersons('data_packages', 'dataCurators');

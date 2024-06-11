// Dokumente abrufen
const documents = db.fs.files.aggregate([
  {
    $match: {
      "metadata.type.en": "Question Flow"
    }
  },
  {
    $lookup: {
      from: "instruments",
      localField: "metadata.instrumentId",
      foreignField: "_id",
      as: "instrument"
    }
  },
  {
    $lookup: {
      from: "data_packages",
      localField: "instrument.dataPackageId",
      foreignField: "_id",
      as: "dataPackage"
    }
  }
]).toArray();

// Durchlaufe die abgerufenen Dokumente und erstelle den String
documents.forEach(document => {
  const surveyNumbers = document.instrument[0].surveyNumbers.join(", ");
  const dataPackageTitleDe = document.dataPackage[0].title.de;
  const dataPackageTitleEn = document.dataPackage[0].title.en;
  const pluralSuffixDe = document.instrument[0].surveyNumbers.length === 1 ? "Erhebung" : "Erhebungen";
  const pluralSuffixEn = document.instrument[0].surveyNumbers.length === 1 ? "survey" : "surveys";
  const updatedDescriptionDe = `Filterführungsdiagram zu “${dataPackageTitleDe}”, ${pluralSuffixDe}: ${surveyNumbers}`;
  const updatedDescriptionEn = `Question Flow of “${dataPackageTitleEn}”, ${pluralSuffixEn}: ${surveyNumbers}`;
  db.fs.files.updateOne(
    { _id: document._id },
    { $set: { 
       "metadata.description.de": updatedDescriptionDe,
       "metadata.description.en": updatedDescriptionEn
    } }
  );
});

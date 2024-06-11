db.data_sets.updateMany(
  {
    "subDataSets": {
      "$elemMatch": {
        "accessWay": "download-suf",
        "$or": [
          {
            "description.de": {
              "$ne": "Kann nach Abschluss eines Datennutzungsvertrags heruntergeladen werden"
            }
          },
          {
            "description.en": {
              "$ne": "can be downloaded after the conclusion of a data use agreement"
            }
          }
        ]
      }
    }
  },
  {
    "$set": {
      "subDataSets.$[elem].description.de": "Kann nach Abschluss eines Datennutzungsvertrags heruntergeladen werden",
      "subDataSets.$[elem].description.en": "can be downloaded after the conclusion of a data use agreement"
    }
  },
  {
      arrayFilters: [
      {
        "elem.accessWay": "download-suf",
        "$or": [
          {
            "elem.description.de": {
              "$ne": "Kann nach Abschluss eines Datennutzungsvertrags heruntergeladen werden"
            }
          },
          {
            "elem.description.en": {
              "$ne": "can be downloaded after the conclusion of a data use agreement"
            }
          }
        ]
      }
    ]
  }
)

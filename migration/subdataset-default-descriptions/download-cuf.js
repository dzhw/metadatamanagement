db.data_sets.updateMany(
  {
    "subDataSets": {
      "$elemMatch": {
        "accessWay": "download-cuf",
        "$or": [
          {
            "description.de": {
              "$ne": "Kann nach einer Registrierung ohne Datennutzungsvertrag heruntergeladen werden"
            }
          },
          {
            "description.en": {
              "$ne": "can be downloaded after registration without a data use agreement"
            }
          }
        ]
      }
    }
  },
  {
    "$set": {
      "subDataSets.$[elem].description.de": "Kann nach einer Registrierung ohne Datennutzungsvertrag heruntergeladen werden",
      "subDataSets.$[elem].description.en": "can be downloaded after registration without a data use agreement"
    }
  },
  {
      arrayFilters: [
      {
        "elem.accessWay": "download-cuf",
        "$or": [
          {
            "elem.description.de": {
              "$ne": "Kann nach einer Registrierung ohne Datennutzungsvertrag heruntergeladen werden"
            }
          },
          {
            "elem.description.en": {
              "$ne": "can be downloaded after registration without a data use agreement"
            }
          }
        ]
      }
    ]
  }
)

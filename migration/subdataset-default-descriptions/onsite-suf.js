db.data_sets.updateMany(
  {
    "subDataSets": {
      "$elemMatch": {
        "accessWay": "onsite-suf",
        "$or": [
          {
            "description.de": {
              "$ne": "Ist am Gastwissenschaftler*innenarbeitsplatz im DZHW in Hannover verfügbar"
            }
          },
          {
            "description.en": {
              "$ne": "available at the workspace for visiting scientists at the DZHW in Hannover"
            }
          }
        ]
      }
    }
  },
  {
    "$set": {
      "subDataSets.$[elem].description.de": "Ist am Gastwissenschaftler*innenarbeitsplatz im DZHW in Hannover verfügbar",
      "subDataSets.$[elem].description.en": "available at the workspace for visiting scientists at the DZHW in Hannover"
    }
  },
  {
      arrayFilters: [
      {
        "elem.accessWay": "onsite-suf",
        "$or": [
          {
            "elem.description.de": {
              "$ne": "Ist am Gastwissenschaftler*innenarbeitsplatz im DZHW in Hannover verfügbar"
            }
          },
          {
            "elem.description.en": {
              "$ne": "available at the workspace for visiting scientists at the DZHW in Hannover"
            }
          }
        ]
      }
    ]
  }
)

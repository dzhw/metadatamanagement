db.data_sets.updateMany(
  {
    "subDataSets": {
      "$elemMatch": {
        "accessWay": "remote-desktop-suf",
        "$or": [
          {
            "description.de": {
              "$ne": "Kann über Remote Desktop genutzt werden"
            }
          },
          {
            "description.en": {
              "$ne": "can be used via remote desktop"
            }
          }
        ]
      }
    }
  },
  {
    "$set": {
      "subDataSets.$[elem].description.de": "Kann über Remote Desktop genutzt werden",
      "subDataSets.$[elem].description.en": "can be used via remote desktop"
    }
  },
  {
      arrayFilters: [
      {
        "elem.accessWay": "remote-desktop-suf",
        "$or": [
          {
            "elem.description.de": {
              "$ne": "Kann über Remote Desktop genutzt werden"
            }
          },
          {
            "elem.description.en": {
              "$ne": "can be used via remote desktop"
            }
          }
        ]
      }
    ]
  }
)

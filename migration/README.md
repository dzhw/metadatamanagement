# Migration of default strings

The two folders (instruments-attachments-default-descriptions and subdataset-default-descriptions) contain javascript files which are used to correct some fields in MongoDB. In particular, this concerns the "description" field in the subdatasets. This should be filled with a default value depending on the selected accessway (download-cuf, onsite-suf,...). Similarly, the "description" field in the instrument attachments is filled with a default value depending on the selected type (questionnaire, question flow,...).
To execute these scripts, the MongoDB client (MongoDB Compass) must be used. Once you have connected to the "metadatamanagement" database, you can open the MongoDB shell (below). Here you must first select the correct DB:

```
use metdatamanagement
```

Then the content of the javascript files can be copied, pasted into the MongoDB shell and executed.

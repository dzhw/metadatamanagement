Datentypen
==========

Der jeweilige Datentyp für eine Eigenschaft kann
`hier <https://github.com/dzhw/metadatamanagement/wiki/Domain-Model>`__
nachgeschaut werden.

+-----------------+-------------------+---------------------------------+--------------------------+
| Datentyp        | Beschreibung      | Excel Export                    | Json Export              |
|                 |                   |                                 |                          |
+=================+===================+=================================+==========================+
| string          | Text              | ein Objekt in                   | z.B.:                    |
|                 |                   | einer Zelle                     | ``"value" : 1``          |
+-----------------+-------------------+---------------------------------+--------------------------+
|   I18nString    | de: string        | 2 Spalten, z.B.:                | als Liste, z.B:          |
|                 | en: string        | ``annotations.de``              | "questionText": {        |
|                 |                   | ``annotations.en``              | "de": "Wie alt sind      |
|                 |                   |                                 | Sie?",                   |
|                 |                   |                                 | "en": "How old are you?" |
|                 |                   |                                 | }                        |
+-----------------+-------------------+---------------------------------+--------------------------+
| Period          | start:            | 2 Spalten, z.B.:                |                          |
|                 | LocalDateTime     | ``fieldPeriod.start``           |                          |
|                 | end:              | ``fieldPeriod.end``             |                          |
|                 | LocalDateTime     |                                 |                          |
+-----------------+-------------------+---------------------------------+--------------------------+
| LocalDateTime   | yyyy-mm-dd,       |                                 |                          |
|                 | z.B.:             |                                 |                          |
|                 | 2011-12-23        |                                 |                          |
+-----------------+-------------------+---------------------------------+--------------------------+
| Double          | Gleitkommazahlen  |                                 |                          |
+-----------------+-------------------+---------------------------------+--------------------------+
| Integer         | Ganze Zahlen      |                                 |                          |
+-----------------+-------------------+---------------------------------+--------------------------+
| List            | Eigenschaft mit   | Objekte werden                  | als Array,               |
|                 | mehreren Objekten | in einer Zelle                  | z.B.:                    |
|                 |                   | durch Komma                     | "successorNumbers":      |
|                 |                   | getrennt, z.B.:                 | ["1","2"]                |
|                 |                   | download-cuf,                   |                          |
|                 |                   | download-suf,                   |                          |
|                 |                   | remote-desktop-suf,             |                          |
|                 |                   | onsite-suf                      |                          |
+-----------------+-------------------+---------------------------------+--------------------------+
| ImageType       | PNG               |                                 |                          |
+-----------------+-------------------+---------------------------------+--------------------------+

Dabei gibt es für strings und l18nStrings eine Obergrenze für die
maximale Länge (Zeichenanzahl):

-  SMALL: 32 characters
-  MEDIUM: 128 characters
-  LARGE: 2048 characters (2KB)
-  X-LARGE: 1 MB

Vokabular
---------

Es ist zu beachten, dass für einige Eigenschaften nur spezielles
Vokabular zugelassen ist! Dieses kann entweder auf der io-wiki Seite der
jeweiligen Exportfunktion (data package, Survey, DataSet, Variable, Instrument,
Question) oder
`hier <https://github.com/dzhw/metadatamanagement/wiki/Domain-Model>`__
nachgeschaut werden.

Beispiel:

::

   - accesWay: "download-cuf", "download-suf", "remote-desktop-suf", "onsite-suf", "not-accessible"
   - scaleLevel.de = "nominal", "ordinal", "intervall" oder "verhältnis"
   - scaleLevel.en = "nominal", "ordinal", "interval" oder "ratio"

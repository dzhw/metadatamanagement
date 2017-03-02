/* global _ */
'use strict';

angular.module('metadatamanagementApp').filter('LanguageFilter', function() {
    // jscs:disable
    var languages = [{
            'en': 'Abkhazian',
            'de': 'Abchasisch',
            'code': 'ab'
        },
        {
            'en': 'Afar',
            'de': 'Afar-Sprache',
            'code': 'aa'
        },
        {
            'en': 'Afrikaans',
            'de': 'Afrikaans',
            'code': 'af'
        },
        {
            'en': 'Akan',
            'de': 'Akan-Sprache',
            'code': 'ak'
        },
        {
            'en': 'Albanian',
            'de': 'Albanisch',
            'code': 'sq'
        },
        {
            'en': 'Amharic',
            'de': 'Amharisch',
            'code': 'am'
        },
        {
            'en': 'Arabic',
            'de': 'Arabisch',
            'code': 'ar'
        },
        {
            'en': 'Aragonese',
            'de': 'Aragonesisch',
            'code': 'an'
        },
        {
            'en': 'Armenian',
            'de': 'Armenisch',
            'code': 'hy'
        },
        {
            'en': 'Assamese',
            'de': 'Assamesisch',
            'code': 'as'
        },
        {
            'en': 'Avaric',
            'de': 'Awarisch',
            'code': 'av'
        },
        {
            'en': 'Avestan',
            'de': 'Awestisch',
            '639.2': 'ave',
            'code': 'ae'
        },
        {
            'en': 'Aymara',
            'de': 'Aymara-Sprache',
            'code': 'ay'
        },
        {
            'en': 'Azerbaijani',
            'de': 'Aserbeidschanisch',
            'code': 'az'
        },
        {
            'en': 'Bambara',
            'de': 'Bambara-Sprache',
            'code': 'bm'
        },
        {
            'en': 'Bashkir',
            'de': 'Baschkirisch',
            'code': 'ba'
        },
        {
            'en': 'Basque',
            'de': 'Baskisch',
            'code': 'eu'
        },
        {
            'en': 'Belarusian',
            'de': 'Weißrussisch',
            'code': 'be'
        },
        {
            'en': 'Bengali',
            'de': 'Bengali',
            'code': 'bn'
        },
        {
            'en': 'Bihari',
            'de': 'Bihari',
            'code': 'bh'
        },
        {
            'en': 'Bislama',
            'de': 'Bislama; Beach-la-mar',
            'code': 'bi'
        },
        {
            'en': 'Bokmål, Norwegian; Norwegian Bokmål',
            'de': 'Bokmål',
            'code': 'nb'
        },
        {
            'en': 'Bosnian',
            'de': 'Bosnisch',
            'code': 'bs'
        },
        {
            'en': 'Breton',
            'de': 'Bretonisch',
            'code': 'br'
        },
        {
            'en': 'Bulgarian',
            'de': 'Bulgarisch',
            'code': 'bg'
        },
        {
            'en': 'Burmese',
            'de': 'Burmesisch; Birmanisch',
            'code': 'my'
        },
        {
            'en': 'Castilian; Spanish',
            'de': 'Spanisch',
            'code': 'es'
        },
        {
            'en': 'Catalan; Valencian',
            'de': 'Katalanisch',
            'code': 'ca'
        },
        {
            'en': 'Central Khmer',
            'de': 'Kambodschanisch',
            'code': 'km'
        },
        {
            'en': 'Chamorro',
            'de': 'Chamorro-Sprache',
            'code': 'ch'
        },
        {
            'en': 'Chechen',
            'de': 'Tschetschenisch',
            'code': 'ce'
        },
        {
            'en': 'Chewa; Chichewa; Nyanja',
            'de': 'Nyanja-Sprache',
            'code': 'ny'
        },
        {
            'en': 'Chichewa; Chewa; Nyanja',
            'de': 'Nyanja-Sprache',
            'code': 'ny'
        },
        {
            'en': 'Chinese',
            'de': 'Chinesisch',
            'code': 'zh'
        },
        {
            'en': 'Chuang; Zhuang',
            'de': 'Zhuang',
            'code': 'za'
        },
        {
            'en': 'Church Slavic; Slavonic; Church Slavonic; Old Bulgarian; Old Church Slavonic',
            'de': 'Kirchenslawisch',
            'code': 'cu'
        },
        {
            'en': 'Chuvash',
            'de': 'Tschuwaschisch',
            'code': 'cv'
        },
        {
            'en': 'Cornish',
            'de': 'Kornisch',
            'code': 'kw'
        },
        {
            'en': 'Corsican',
            'de': 'Korsisch',
            'code': 'co'
        },
        {
            'en': 'Cree',
            'de': 'Cree-Sprache',
            'code': 'cr'
        },
        {
            'en': 'Croatian',
            'de': 'Kroatisch',
            'code': 'hr'
        },
        {
            'en': 'Czech',
            'de': 'Tschechisch',
            'code': 'cs'
        },
        {
            'en': 'Danish',
            'de': 'Dänisch',
            'code': 'da'
        },
        {
            'en': 'Divehi, Dhivehi, Maldivian',
            'de': 'Maledivisch',
            'code': 'dv'
        },
        {
            'en': 'Dutch; Flemish',
            'de': 'Niederländisch',
            'code': 'nl'
        },
        {
            'en': 'Dzongkha',
            'de': 'Dzongkha',
            'code': 'dz'
        },
        {
            'en': 'English',
            'de': 'Englisch',
            'code': 'en'
        },
        {
            'en': 'Esperanto',
            'de': 'Esperanto',
            'code': 'eo'
        },
        {
            'en': 'Estonian',
            'de': 'Estnisch',
            'code': 'et'
        },
        {
            'en': 'Ewe',
            'de': 'Ewe-Sprache',
            'code': 'ee'
        },
        {
            'en': 'Faroese',
            'de': 'Färöisch',
            '639.2': 'fao',
            'code': 'fo'
        },
        {
            'en': 'Fijian',
            'de': 'Fidschi-Sprache',
            'code': 'fj'
        },
        {
            'en': 'Finnish',
            'de': 'Finnisch',
            'code': 'fi'
        },
        {
            'en': 'Flemish; Dutch',
            'de': 'Niederländisch',
            'code': 'nl'
        },
        {
            'en': 'French',
            'de': 'Französisch',
            'code': 'fr'
        },
        {
            'en': 'Fulah',
            'de': 'Ful',
            'code': 'ff'
        },
        {
            'en': 'Gaelic; Scottish Gaelic',
            'de': 'Gälisch-Schottisch',
            'code': 'gd'
        },
        {
            'en': 'Galician',
            'de': 'Galicisch',
            'code': 'gl'
        },
        {
            'en': 'Ganda',
            'de': 'Ganda-Sprache',
            'code': 'lg'
        },
        {
            'en': 'Gbaya',
            'de': 'Gbaya-Sprache',
            'code': ''
        },
        {
            'en': 'Georgian',
            'de': 'Georgisch',
            'code': 'ka'
        },
        {
            'en': 'German',
            'de': 'Deutsch',
            'code': 'de'
        },
        {
            'en': 'Gikuyu; Kikuyu',
            'de': 'Kikuyu-Sprache',
            'code': 'ki'
        },
        {
            'en': 'Greek, Modern (1453-)',
            'de': 'Griechisch (Entstehungszeit nach 1453)',
            '639.2': 'gre/ell',
            'code': 'el'
        },
        {
            'en': 'Greenlandic; Kalaallisut',
            'de': 'Grönländisch',
            'code': 'kl'
        },
        {
            'en': 'Guarani',
            'de': 'Guarani-Sprache',
            'code': 'gn'
        },
        {
            'en': 'Gujarati',
            'de': 'Gujarati-Sprache',
            '639.2': 'guj',
            'code': 'gu'
        },
        {
            'en': 'Haitian; Haitian Creole',
            'de': 'Haitien; Frankokreolisch',
            'code': 'ht'
        },
        {
            'en': 'Hausa',
            'de': 'Haussa-Sprache',
            '639.2': 'hau',
            'code': 'ha'
        },
        {
            'en': 'Hebrew',
            'de': 'Hebräisch',
            '639.2': 'heb',
            'code': 'he'
        },
        {
            'en': 'Herero',
            'de': 'Herero-Sprache',
            'code': 'hz'
        },
        {
            'en': 'Hiligaynon',
            'de': 'Hiligaynon-Sprache',
            'code': ''
        },
        {
            'en': 'Hindi',
            'de': 'Hindi',
            '639.2': 'hin',
            'code': 'hi'
        },
        {
            'en': 'Hiri Motu',
            'de': 'Hiri-Motu',
            'code': 'ho'
        },
        {
            'en': 'Hungarian',
            'de': 'Ungarisch',
            '639.2': 'hun',
            'code': 'hu'
        },
        {
            'en': 'Icelandic',
            'de': 'Isländisch',
            'code': 'is'
        },
        {
            'en': 'Ido',
            'de': 'Sinohoan',
            'code': 'io'
        },
        {
            'en': 'Igbo',
            'de': 'Ibo-Sprache',
            'code': 'ig'
        },
        {
            'en': 'Indonesian',
            'de': 'Bahasa Indonesia',
            'code': 'id'
        },
        {
            'en': 'Interlingua (International\n Auxiliary Language Association)',
            'de': 'Interlingua',
            'code': 'ia'
        },
        {
            'en': 'Interlingue; Occidental',
            'de': 'Interlingue',
            'code': 'ie'
        },
        {
            'en': 'Inuktitut',
            'de': 'Inuktitut',
            '639.2': 'iku',
            'code': 'iu'
        },
        {
            'en': 'Inupiaq',
            'de': 'Inupik',
            'code': 'ik'
        },
        {
            'en': 'Irish',
            'de': 'Irisch',
            'code': 'ga'
        },
        {
            'en': 'Italian',
            'de': 'Italienisch',
            'code': 'it'
        },
        {
            'en': 'Japanese',
            'de': 'Japanisch',
            'code': 'ja'
        },
        {
            'en': 'Javanese',
            'de': 'Javanisch',
            'code': 'jv'
        },
        {
            'en': 'Kalaallisut; Greenlandic',
            'de': 'Grönländisch',
            'code': 'kl'
        },
        {
            'en': 'Kannada',
            'de': 'Kannada',
            'code': 'kn'
        },
        {
            'en': 'Kanuri',
            'de': 'Kanuri-Sprache',
            'code': 'kr'
        },
        {
            'en': 'Kashmiri',
            'de': 'Kaschmiri',
            'code': 'ks'
        },
        {
            'en': 'Kazakh',
            'de': 'Kasachisch',
            'code': 'kk'
        },
        {
            'en': 'Kikuyu; Gikuyu',
            'de': 'Kikuyu-Sprache',
            'code': 'ki'
        },
        {
            'en': 'Kinyarwanda',
            'de': 'Rwanda-Sprache',
            'code': 'rw'
        },
        {
            'en': 'Kirghiz; Kyrgyz',
            'de': 'Kirgisisch',
            'code': 'ky'
        },
        {
            'en': 'Komi',
            'de': 'Komi-Sprache',
            'code': 'kv'
        },
        {
            'en': 'Kongo',
            'de': 'Kongo-Sprache',
            'code': 'kg'
        },
        {
            'en': 'Korean',
            'de': 'Koreanisch',
            'code': 'ko'
        },
        {
            'en': 'Kuanyama; Kwanyama',
            'de': 'Kwanyama-Sprache',
            'code': 'kj'
        },
        {
            'en': 'Kurdish',
            'de': 'Kurdisch',
            'code': 'ku'
        },
        {
            'en': 'Kwanyama, Kuanyama',
            'de': 'Kwanyama-Sprache',
            'code': 'kj'
        },
        {
            'en': 'Lao',
            'de': 'Laotisch',
            'code': 'lo'
        },
        {
            'en': 'Latin',
            'de': 'Latein',
            'code': 'la'
        },
        {
            'en': 'Latvian',
            'de': 'Lettisch',
            'code': 'lv'
        },
        {
            'en': 'Letzeburgesch; Luxembourgish',
            'de': 'Luxemburgisch',
            'code': 'lb'
        },
        {
            'en': 'Limburgan; Limburger; Limburgish',
            'de': 'Limburgisch',
            'code': 'li'
        },
        {
            'en': 'Lingala',
            'de': 'Lingala',
            'code': 'ln'
        },
        {
            'en': 'Lithuanian',
            'de': 'Litauisch',
            'code': 'lt'
        },
        {
            'en': 'Low German; Low Saxon; German, Low; Saxon, Low',
            'de': 'Niedersächsisch; Niederdeutsch',
            'code': ''
        },
        {
            'en': 'Lower Sorbian',
            'de': 'Niedersorbisch',
            'code': ''
        },
        {
            'en': 'Luba-Katanga',
            'de': 'Luba-Katanga-Sprache',
            'code': 'lu'
        },
        {
            'en': 'Luxembourgish; Letzeburgesch',
            'de': 'Luxemburgisch',
            'code': 'lb'
        },
        {
            'en': 'Macedonian',
            'de': 'Makedonisch',
            'code': 'mk'
        },
        {
            'en': 'Malagasy',
            'de': 'Malagassi-Sprache',
            '639.2': 'mlg',
            'code': 'mg'
        },
        {
            'en': 'Malay',
            'de': 'Malaiisch',
            'code': 'ms'
        },
        {
            'en': 'Malayalam',
            'de': 'Malayalam',
            'code': 'ml'
        },
        {
            'en': 'Maltese',
            'de': 'Maltesisch',
            'code': 'mt'
        },
        {
            'en': 'Manx',
            'de': 'Manxs',
            'code': 'gv'
        },
        {
            'en': 'Maori',
            'de': 'Maori-Sprache',
            'code': 'mi'
        },
        {
            'en': 'Marathi',
            'de': 'Marathi',
            'code': 'mr'
        },
        {
            'en': 'Marshallese',
            'de': 'Marschallesisch',
            'code': 'mh'
        },
        {
            'en': 'Moldavian',
            'de': 'Moldauisch',
            'code': 'mo'
        },
        {
            'en': 'Mongolian',
            'de': 'Mongolisch',
            'code': 'mn'
        },
        {
            'en': 'Nauru',
            'de': 'Nauruanisch',
            'code': 'na'
        },
        {
            'en': 'Navaho, Navajo',
            'de': 'Navajo-Sprache',
            'code': 'nv'
        },
        {
            'en': 'Ndebele, North',
            'de': 'Ndebele-Sprache (Simbabwe)',
            'code': 'nd'
        },
        {
            'en': 'Ndebele, South',
            'de': 'Ndbele-Sprache (Transvaal)',
            'code': 'nr'
        },
        {
            'en': 'Ndonga',
            'de': 'Ndonga',
            'code': 'ng'
        },
        {
            'en': 'Nepali',
            'de': 'Nepali',
            'code': 'ne'
        },
        {
            'en': 'Northern Sami',
            'de': 'Nordsaamisch',
            'code': 'se'
        },
        {
            'en': 'North Ndebele',
            'de': 'Ndebele-Sprache (Simbabwe)',
            'code': 'nd'
        },
        {
            'en': 'Norwegian',
            'de': 'Norwegisch',
            'code': 'no'
        },
        {
            'en': 'Norwegian Bokmål; Bokmål, Norwegian',
            'de': 'Bokmål',
            'code': 'nb'
        },
        {
            'en': 'Norwegian Nynorsk; Nynorsk, Norwegian',
            'de': 'Nynorsk',
            'code': 'nn'
        },
        {
            'en': 'Nyanja; Chichewa; Chewa',
            'de': 'Nyanja-Sprache',
            'code': 'ny'
        },
        {
            'en': 'Nynorsk, Norwegian; Norwegian Nynorsk',
            'de': 'Nynorsk',
            'code': 'nn'
        },
        {
            'en': 'Occitan (post 1500)',
            'de': 'Okzitanisch (Andere)',
            'code': 'oc'
        },
        {
            'en': 'Ojibwa',
            'de': 'Ojibwa-Sprache',
            'code': 'oj'
        },
        {
            'en': 'Old Bulgarian; Old Slavonic; Church Slavonic; Church Slavic; Old Church Slavonic',
            'de': 'Kirchenslawisch',
            'code': 'cu'
        },
        {
            'en': 'Oriya',
            'de': 'Oriya-Sprache',
            'code': 'or'
        },
        {
            'en': 'Oromo',
            'de': 'Galla-Sprache',
            'code': 'om'
        },
        {
            'en': 'Ossetian; Ossetic',
            'de': 'Ossetisch',
            'code': 'os'
        },
        {
            'en': 'Pali',
            'de': 'Pali',
            'code': 'pi'
        },
        {
            'en': 'Panjabi; Punjabi',
            'de': 'Pandschabi-Sprache',
            'code': 'pa'
        },
        {
            'en': 'Persian',
            'de': 'Persisch',
            'code': 'fa'
        },
        {
            'en': 'Polish',
            'de': 'Polnisch',
            'code': 'pl'
        },
        {
            'en': 'Portuguese',
            'de': 'Portugiesisch',
            'code': 'pt'
        },
        {
            'en': 'Provençal; Occitan (post 1500)',
            'de': 'Okzitanisch (Andere)',
            'code': 'oc'
        },
        {
            'en': 'Punjabi; Panjabi',
            'de': 'Pandschabi-Sprache',
            'code': 'pa'
        },
        {
            'en': 'Pushto; Pashto',
            'de': 'Paschtu',
            'code': 'ps'
        },
        {
            'en': 'Quechua',
            'de': 'Quechua-Sprache',
            'code': 'qu'
        },
        {
            'en': 'Romanian',
            'de': 'Rumänisch',
            'code': 'ro'
        },
        {
            'en': 'Romansh',
            'de': 'Rätoromanisch; Rumantsch',
            'code': 'rm'
        },
        {
            'en': 'Rundi',
            'de': 'Rundi-Sprache',
            'code': 'rn'
        },
        {
            'en': 'Russian',
            'de': 'Russisch',
            'code': 'ru'
        },
        {
            'en': 'Samoan',
            'de': 'Samoanisch',
            'code': 'sm'
        },
        {
            'en': 'Sango',
            'de': 'Sango-Sprache',
            'code': 'sg'
        },
        {
            'en': 'Sanskrit',
            'de': 'Sanskrit',
            'code': 'sa'
        },
        {
            'en': 'Sardinian',
            'de': 'Sardisch',
            'code': 'sc'
        },
        {
            'en': 'Sasak',
            'de': 'Sasak',
            'code': ''
        },
        {
            'en': 'Scottish Gaelic; Gaelic',
            'de': 'Gälisch-Schottisch',
            'code': 'gd'
        },
        {
            'en': 'Serbian',
            'de': 'Serbisch',
            'code': 'sr'
        },
        {
            'en': 'Shona',
            'de': 'Schona-Sprache',
            'code': 'sn'
        },
        {
            'en': 'Sichuan Yi; Nuosu',
            'de': 'Nosu',
            'code': 'ii'
        },
        {
            'en': 'Sindhi',
            'de': 'Sindhi-Sprache',
            'code': 'sd'
        },
        {
            'en': 'Sinhala; Sinhalese',
            'de': 'Singhalesisch',
            'code': 'si'
        },
        {
            'en': 'Slovak',
            'de': 'Slowakisch',
            'code': 'sk'
        },
        {
            'en': 'Slovenian',
            'de': 'Slowenisch',
            'code': 'sl'
        },
        {
            'en': 'Somali',
            'de': 'Somali',
            'code': 'so'
        },
        {
            'en': 'Sotho, Southern',
            'de': 'Süd-Sotho-Sprache',
            'code': 'st'
        },
        {
            'en': 'South Ndebele',
            'de': 'Ndebele-Sprache (Transvaal)',
            'code': 'nr'
        },
        {
            'en': 'Spanish; Castilian',
            'de': 'Spanisch',
            'code': 'es'
        },
        {
            'en': 'Sundanese',
            'de': 'Sundanesisch',
            'code': 'su'
        },
        {
            'en': 'Swahili',
            'de': 'Swahili',
            'code': 'sw'
        },
        {
            'en': 'Swati',
            'de': 'Swasi-Sprache',
            'code': 'ss'
        },
        {
            'en': 'Swedish',
            'de': 'Schwedisch',
            'code': 'sv'
        },
        {
            'en': 'Swiss German; Alemannic; Alsatian',
            'de': 'Hochalemannisch',
            'code': ''
        },
        {
            'en': 'Tagalog',
            'de': 'Tagalog',
            'code': 'tl'
        },
        {
            'en': 'Tahitian',
            'de': 'Tahitisch',
            'code': 'ty'
        },
        {
            'en': 'Tajik',
            'de': 'Tadschikisch',
            'code': 'tg'
        },
        {
            'en': 'Tamil',
            'de': 'Tamil',
            'code': 'ta'
        },
        {
            'en': 'Tatar',
            'de': 'Tatarisch',
            'code': 'tt'
        },
        {
            'en': 'Telugu',
            'de': 'Telugu-Sprache',
            'code': 'te'
        },
        {
            'en': 'Thai',
            'de': 'Thailändisch',
            'code': 'th'
        },
        {
            'en': 'Tibetan',
            'de': 'Tibetisch',
            'code': 'bo'
        },
        {
            'en': 'Tigrinya',
            'de': 'Tigrinja-Sprache',
            'code': 'ti'
        },
        {
            'en': 'Tonga (Tonga Islands)',
            'de': 'Tongaisch',
            'code': 'to'
        },
        {
            'en': 'Tsonga',
            'de': 'Tsonga-Sprache',
            'code': 'ts'
        },
        {
            'en': 'Tswana',
            'de': 'Tswana-Sprache',
            'code': 'tn'
        },
        {
            'en': 'Turkish',
            'de': 'Türkisch',
            'code': 'tr'
        },
        {
            'en': 'Turkmen',
            'de': 'Turkmenisch',
            'code': 'tk'
        },
        {
            'en': 'Twi',
            'de': 'Twi-Sprache',
            'code': 'tw'
        },
        {
            'en': 'Uighur; Uyghur',
            'de': 'Uigurisch',
            'code': 'ug'
        },
        {
            'en': 'Ukrainian',
            'de': 'Ukrainisch',
            'code': 'uk'
        },
        {
            'en': 'Urdu',
            'de': 'Urdu',
            'code': 'ur'
        },
        {
            'en': 'Uyghur; Uighur',
            'de': 'Uigurisch',
            'code': 'ug'
        },
        {
            'en': 'Uzbek',
            'de': 'Usbekisch',
            'code': 'uz'
        },
        {
            'en': 'Valencian; Catalan',
            'de': 'Katalanisch',
            'code': 'ca'
        },
        {
            'en': 'Venda',
            'de': 'Venda-Sprache',
            'code': 've'
        },
        {
            'en': 'Vietnamese',
            'de': 'Vietnamesisch',
            'code': 'vi'
        },
        {
            'en': 'Volapük',
            'de': 'Volapük',
            'code': 'vo'
        },
        {
            'en': 'Walloon',
            'de': 'Wallonisch',
            'code': 'wa'
        },
        {
            'en': 'Welsh',
            'de': 'Kymrisch',
            'code': 'cy'
        },
        {
            'en': 'Western Frisian',
            'de': 'Friesisch',
            'code': 'fy'
        },
        {
            'en': 'Wolof',
            'de': 'Wolof-Sprache',
            'code': 'wo'
        },
        {
            'en': 'Xhosa',
            'de': 'Xhosa-Sprache',
            'code': 'xh'
        },
        {
            'en': 'Yiddish',
            'de': 'Jiddisch',
            'code': 'yi'
        },
        {
            'en': 'Yoruba',
            'de': 'Yoruba-Sprache',
            'code': 'yo'
        },
        {
            'en': 'Zhuang; Chuang',
            'de': 'Zhuang',
            'code': 'za'
        },
        {
            'en': 'Zulu',
            'de': 'Zulu-Sprache',
            'code': 'zu'
        }
    ];
      // jscs:enable
    return function(code, currentLanguage) {
      var language = _.find(languages, function(language) {
        return language.code === code;
      });
      if (!language) {
        return code;
      } else {
        return language[currentLanguage];
      }
    };
  });

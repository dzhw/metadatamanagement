'use strict';

angular.module('metadatamanagementApp').filter('displayIsoLanguage',
function() {
    // jscs:disable
    var languagesMap = {
        'ab': {
            'en': 'Abkhazian',
            'de': 'Abchasisch'
        },
        'aa': {
            'en': 'Afar',
            'de': 'Afar-Sprache'
        },
        'af': {
            'en': 'Afrikaans',
            'de': 'Afrikaans'
        },
        'ak': {
            'en': 'Akan',
            'de': 'Akan-Sprache'
        },
        'sq': {
            'en': 'Albanian',
            'de': 'Albanisch'
        },
        'am': {
            'en': 'Amharic',
            'de': 'Amharisch'
        },
        'ar': {
            'en': 'Arabic',
            'de': 'Arabisch'
        },
        'an': {
            'en': 'Aragonese',
            'de': 'Aragonesisch'
        },
        'hy': {
            'en': 'Armenian',
            'de': 'Armenisch'
        },
        'as': {
            'en': 'Assamese',
            'de': 'Assamesisch'
        },
        'av': {
            'en': 'Avaric',
            'de': 'Awarisch'
        },
        'ae': {
            'en': 'Avestan',
            'de': 'Awestisch'
        },
        'ay': {
            'en': 'Aymara',
            'de': 'Aymara-Sprache'
        },
        'az': {
            'en': 'Azerbaijani',
            'de': 'Aserbeidschanisch'
        },
        'bm': {
            'en': 'Bambara',
            'de': 'Bambara-Sprache'
        },
        'ba': {
            'en': 'Bashkir',
            'de': 'Baschkirisch'
        },
        'eu': {
            'en': 'Basque',
            'de': 'Baskisch'
        },
        'be': {
            'en': 'Belarusian',
            'de': 'Weißrussisch'
        },
        'bn': {
            'en': 'Bengali',
            'de': 'Bengali'
        },
        'bh': {
            'en': 'Bihari',
            'de': 'Bihari'
        },
        'bi': {
            'en': 'Bislama',
            'de': 'Bislama; Beach-la-mar'
        },
        'nb': {
            'en': 'Bokmål, Norwegian; Norwegian Bokmål',
            'de': 'Bokmål'
        },
        'bs': {
            'en': 'Bosnian',
            'de': 'Bosnisch'
        },
        'br': {
            'en': 'Breton',
            'de': 'Bretonisch'
        },
        'bg': {
            'en': 'Bulgarian',
            'de': 'Bulgarisch'
        },
        'my': {
            'en': 'Burmese',
            'de': 'Burmesisch; Birmanisch'
        },
        'es': {
            'en': 'Castilian; Spanish',
            'de': 'Spanisch'
        },
        'ca': {
            'en': 'Catalan; Valencian',
            'de': 'Katalanisch'
        },
        'km': {
            'en': 'Central Khmer',
            'de': 'Kambodschanisch'
        },
        'ch': {
            'en': 'Chamorro',
            'de': 'Chamorro-Sprache'
        },
        'ce': {
            'en': 'Chechen',
            'de': 'Tschetschenisch'
        },
        'ny': {
            'en': 'Chewa; Chichewa; Nyanja',
            'de': 'Nyanja-Sprache'
        },
        'zh': {
            'en': 'Chinese',
            'de': 'Chinesisch'
        },
        'za': {
            'en': 'Chuang; Zhuang',
            'de': 'Zhuang'
        },
        'cu': {
            'en': 'Church Slavic; Slavonic; Church Slavonic; Old Bulgarian; Old Church Slavonic',
            'de': 'Kirchenslawisch'
        },
        'cv': {
            'en': 'Chuvash',
            'de': 'Tschuwaschisch'
        },
        'kw': {
            'en': 'Cornish',
            'de': 'Kornisch'
        },
        'co': {
            'en': 'Corsican',
            'de': 'Korsisch'
        },
        'cr': {
            'en': 'Cree',
            'de': 'Cree-Sprache'
        },
        'hr': {
            'en': 'Croatian',
            'de': 'Kroatisch'
        },
        'cs': {
            'en': 'Czech',
            'de': 'Tschechisch'
        },
        'da': {
            'en': 'Danish',
            'de': 'Dänisch'
        },
        'dv': {
            'en': 'Divehi, Dhivehi, Maldivian',
            'de': 'Maledivisch'
        },
        'nl': {
            'en': 'Dutch; Flemish',
            'de': 'Niederländisch'
        },
        'dz': {
            'en': 'Dzongkha',
            'de': 'Dzongkha'
        },
        'en': {
            'en': 'English',
            'de': 'Englisch'
        },
        'eo': {
            'en': 'Esperanto',
            'de': 'Esperanto'
        },
        'et': {
            'en': 'Estonian',
            'de': 'Estnisch'
        },
        'ee': {
            'en': 'Ewe',
            'de': 'Ewe-Sprache'
        },
        'fo': {
            'en': 'Faroese',
            'de': 'Färöisch'
        },
        'fj': {
            'en': 'Fijian',
            'de': 'Fidschi-Sprache'
        },
        'fi': {
            'en': 'Finnish',
            'de': 'Finnisch'
        },
        'fr': {
            'en': 'French',
            'de': 'Französisch'
        },
        'ff': {
            'en': 'Fulah',
            'de': 'Ful'
        },
        'gd': {
            'en': 'Gaelic; Scottish Gaelic',
            'de': 'Gälisch-Schottisch'
        },
        'gl': {
            'en': 'Galician',
            'de': 'Galicisch'
        },
        'lg': {
            'en': 'Ganda',
            'de': 'Ganda-Sprache'
        },
        'ka': {
            'en': 'Georgian',
            'de': 'Georgisch'
        },
        'de': {
            'en': 'German',
            'de': 'Deutsch'
        },
        'ki': {
            'en': 'Gikuyu; Kikuyu',
            'de': 'Kikuyu-Sprache'
        },
        'el': {
            'en': 'Greek, Modern (1453-)',
            'de': 'Griechisch (Entstehungszeit nach 1453)'
        },
        'gn': {
            'en': 'Guarani',
            'de': 'Guarani-Sprache'
        },
        'gu': {
            'en': 'Gujarati',
            'de': 'Gujarati-Sprache'
        },
        'ht': {
            'en': 'Haitian; Haitian Creole',
            'de': 'Haitien; Frankokreolisch'
        },
        'ha': {
            'en': 'Hausa',
            'de': 'Haussa-Sprache'
        },
        'he': {
            'en': 'Hebrew',
            'de': 'Hebräisch'
        },
        'hz': {
            'en': 'Herero',
            'de': 'Herero-Sprache'
        },
        'hi': {
            'en': 'Hindi',
            'de': 'Hindi'
        },
        'ho': {
            'en': 'Hiri Motu',
            'de': 'Hiri-Motu'
        },
        'hu': {
            'en': 'Hungarian',
            'de': 'Ungarisch'
        },
        'is': {
            'en': 'Icelandic',
            'de': 'Isländisch'
        },
        'io': {
            'en': 'Ido',
            'de': 'Sinohoan'
        },
        'ig': {
            'en': 'Igbo',
            'de': 'Ibo-Sprache'
        },
        'id': {
            'en': 'Indonesian',
            'de': 'Bahasa Indonesia'
        },
        'ia': {
            'en': 'Interlingua (International Auxiliary Language Association)',
            'de': 'Interlingua'
        },
        'ie': {
            'en': 'Interlingue; Occidental',
            'de': 'Interlingue'
        },
        'iu': {
            'en': 'Inuktitut',
            'de': 'Inuktitut'
        },
        'ik': {
            'en': 'Inupiaq',
            'de': 'Inupik'
        },
        'ga': {
            'en': 'Irish',
            'de': 'Irisch'
        },
        'it': {
            'en': 'Italian',
            'de': 'Italienisch'
        },
        'ja': {
            'en': 'Japanese',
            'de': 'Japanisch'
        },
        'jv': {
            'en': 'Javanese',
            'de': 'Javanisch'
        },
        'kl': {
            'en': 'Kalaallisut; Greenlandic',
            'de': 'Grönländisch'
        },
        'kn': {
            'en': 'Kannada',
            'de': 'Kannada'
        },
        'kr': {
            'en': 'Kanuri',
            'de': 'Kanuri-Sprache'
        },
        'ks': {
            'en': 'Kashmiri',
            'de': 'Kaschmiri'
        },
        'kk': {
            'en': 'Kazakh',
            'de': 'Kasachisch'
        },
        'rw': {
            'en': 'Kinyarwanda',
            'de': 'Rwanda-Sprache'
        },
        'ky': {
            'en': 'Kirghiz; Kyrgyz',
            'de': 'Kirgisisch'
        },
        'kv': {
            'en': 'Komi',
            'de': 'Komi-Sprache'
        },
        'kg': {
            'en': 'Kongo',
            'de': 'Kongo-Sprache'
        },
        'ko': {
            'en': 'Korean',
            'de': 'Koreanisch'
        },
        'kj': {
            'en': 'Kuanyama; Kwanyama',
            'de': 'Kwanyama-Sprache'
        },
        'ku': {
            'en': 'Kurdish',
            'de': 'Kurdisch'
        },
        'lo': {
            'en': 'Lao',
            'de': 'Laotisch'
        },
        'la': {
            'en': 'Latin',
            'de': 'Latein'
        },
        'lv': {
            'en': 'Latvian',
            'de': 'Lettisch'
        },
        'lb': {
            'en': 'Letzeburgesch; Luxembourgish',
            'de': 'Luxemburgisch'
        },
        'li': {
            'en': 'Limburgan; Limburger; Limburgish',
            'de': 'Limburgisch'
        },
        'ln': {
            'en': 'Lingala',
            'de': 'Lingala'
        },
        'lt': {
            'en': 'Lithuanian',
            'de': 'Litauisch'
        },
        'lu': {
            'en': 'Luba-Katanga',
            'de': 'Luba-Katanga-Sprache'
        },
        'mk': {
            'en': 'Macedonian',
            'de': 'Makedonisch'
        },
        'mg': {
            'en': 'Malagasy',
            'de': 'Malagassi-Sprache'
        },
        'ms': {
            'en': 'Malay',
            'de': 'Malaiisch'
        },
        'ml': {
            'en': 'Malayalam',
            'de': 'Malayalam'
        },
        'mt': {
            'en': 'Maltese',
            'de': 'Maltesisch'
        },
        'gv': {
            'en': 'Manx',
            'de': 'Manxs'
        },
        'mi': {
            'en': 'Maori',
            'de': 'Maori-Sprache'
        },
        'mr': {
            'en': 'Marathi',
            'de': 'Marathi'
        },
        'mh': {
            'en': 'Marshallese',
            'de': 'Marschallesisch'
        },
        'mo': {
            'en': 'Moldavian',
            'de': 'Moldauisch'
        },
        'mn': {
            'en': 'Mongolian',
            'de': 'Mongolisch'
        },
        'na': {
            'en': 'Nauru',
            'de': 'Nauruanisch'
        },
        'nv': {
            'en': 'Navaho, Navajo',
            'de': 'Navajo-Sprache'
        },
        'nd': {
            'en': 'Ndebele, North',
            'de': 'Ndebele-Sprache (Simbabwe)'
        },
        'nr': {
            'en': 'Ndebele, South',
            'de': 'Ndbele-Sprache (Transvaal)'
        },
        'ng': {
            'en': 'Ndonga',
            'de': 'Ndonga'
        },
        'ne': {
            'en': 'Nepali',
            'de': 'Nepali'
        },
        'se': {
            'en': 'Northern Sami',
            'de': 'Nordsaamisch'
        },
        'no': {
            'en': 'Norwegian',
            'de': 'Norwegisch'
        },
        'nn': {
            'en': 'Norwegian Nynorsk; Nynorsk, Norwegian',
            'de': 'Nynorsk'
        },
        'oc': {
            'en': 'Occitan (post 1500)',
            'de': 'Okzitanisch (Andere)'
        },
        'oj': {
            'en': 'Ojibwa',
            'de': 'Ojibwa-Sprache'
        },
        'or': {
            'en': 'Oriya',
            'de': 'Oriya-Sprache'
        },
        'om': {
            'en': 'Oromo',
            'de': 'Galla-Sprache'
        },
        'os': {
            'en': 'Ossetian; Ossetic',
            'de': 'Ossetisch'
        },
        'pi': {
            'en': 'Pali',
            'de': 'Pali'
        },
        'pa': {
            'en': 'Panjabi; Punjabi',
            'de': 'Pandschabi-Sprache'
        },
        'fa': {
            'en': 'Persian',
            'de': 'Persisch'
        },
        'pl': {
            'en': 'Polish',
            'de': 'Polnisch'
        },
        'pt': {
            'en': 'Portuguese',
            'de': 'Portugiesisch'
        },
        'ps': {
            'en': 'Pushto; Pashto',
            'de': 'Paschtu'
        },
        'qu': {
            'en': 'Quechua',
            'de': 'Quechua-Sprache'
        },
        'ro': {
            'en': 'Romanian',
            'de': 'Rumänisch'
        },
        'rm': {
            'en': 'Romansh',
            'de': 'Rätoromanisch; Rumantsch'
        },
        'rn': {
            'en': 'Rundi',
            'de': 'Rundi-Sprache'
        },
        'ru': {
            'en': 'Russian',
            'de': 'Russisch'
        },
        'sm': {
            'en': 'Samoan',
            'de': 'Samoanisch'
        },
        'sg': {
            'en': 'Sango',
            'de': 'Sango-Sprache'
        },
        'sa': {
            'en': 'Sanskrit',
            'de': 'Sanskrit'
        },
        'sc': {
            'en': 'Sardinian',
            'de': 'Sardisch'
        },
        'sr': {
            'en': 'Serbian',
            'de': 'Serbisch'
        },
        'sn': {
            'en': 'Shona',
            'de': 'Schona-Sprache'
        },
        'ii': {
            'en': 'Sichuan Yi; Nuosu',
            'de': 'Nosu'
        },
        'sd': {
            'en': 'Sindhi',
            'de': 'Sindhi-Sprache'
        },
        'si': {
            'en': 'Sinhala; Sinhalese',
            'de': 'Singhalesisch'
        },
        'sk': {
            'en': 'Slovak',
            'de': 'Slowakisch'
        },
        'sl': {
            'en': 'Slovenian',
            'de': 'Slowenisch'
        },
        'so': {
            'en': 'Somali',
            'de': 'Somali'
        },
        'st': {
            'en': 'Sotho, Southern',
            'de': 'Süd-Sotho-Sprache'
        },
        'su': {
            'en': 'Sundanese',
            'de': 'Sundanesisch'
        },
        'sw': {
            'en': 'Swahili',
            'de': 'Swahili'
        },
        'ss': {
            'en': 'Swati',
            'de': 'Swasi-Sprache'
        },
        'sv': {
            'en': 'Swedish',
            'de': 'Schwedisch'
        },
        'tl': {
            'en': 'Tagalog',
            'de': 'Tagalog'
        },
        'ty': {
            'en': 'Tahitian',
            'de': 'Tahitisch'
        },
        'tg': {
            'en': 'Tajik',
            'de': 'Tadschikisch'
        },
        'ta': {
            'en': 'Tamil',
            'de': 'Tamil'
        },
        'tt': {
            'en': 'Tatar',
            'de': 'Tatarisch'
        },
        'te': {
            'en': 'Telugu',
            'de': 'Telugu-Sprache'
        },
        'th': {
            'en': 'Thai',
            'de': 'Thailändisch'
        },
        'bo': {
            'en': 'Tibetan',
            'de': 'Tibetisch'
        },
        'ti': {
            'en': 'Tigrinya',
            'de': 'Tigrinja-Sprache'
        },
        'to': {
            'en': 'Tonga (Tonga Islands)',
            'de': 'Tongaisch'
        },
        'ts': {
            'en': 'Tsonga',
            'de': 'Tsonga-Sprache'
        },
        'tn': {
            'en': 'Tswana',
            'de': 'Tswana-Sprache'
        },
        'tr': {
            'en': 'Turkish',
            'de': 'Türkisch'
        },
        'tk': {
            'en': 'Turkmen',
            'de': 'Turkmenisch'
        },
        'tw': {
            'en': 'Twi',
            'de': 'Twi-Sprache'
        },
        'ug': {
            'en': 'Uighur; Uyghur',
            'de': 'Uigurisch'
        },
        'uk': {
            'en': 'Ukrainian',
            'de': 'Ukrainisch'
        },
        'ur': {
            'en': 'Urdu',
            'de': 'Urdu'
        },
        'uz': {
            'en': 'Uzbek',
            'de': 'Usbekisch'
        },
        've': {
            'en': 'Venda',
            'de': 'Venda-Sprache'
        },
        'vi': {
            'en': 'Vietnamese',
            'de': 'Vietnamesisch'
        },
        'vo': {
            'en': 'Volapük',
            'de': 'Volapük'
        },
        'wa': {
            'en': 'Walloon',
            'de': 'Wallonisch'
        },
        'cy': {
            'en': 'Welsh',
            'de': 'Kymrisch'
        },
        'fy': {
            'en': 'Western Frisian',
            'de': 'Friesisch'
        },
        'wo': {
            'en': 'Wolof',
            'de': 'Wolof-Sprache'
        },
        'xh': {
            'en': 'Xhosa',
            'de': 'Xhosa-Sprache'
        },
        'yi': {
            'en': 'Yiddish',
            'de': 'Jiddisch'
        },
        'yo': {
            'en': 'Yoruba',
            'de': 'Yoruba-Sprache'
        },
        'zu': {
            'en': 'Zulu',
            'de': 'Zulu-Sprache'
        }
    };
    // jscs:enable
    return function(code, currentLanguage) {
        var language = languagesMap[code];
        if (!language) {
          return code;
        } else {
          return language[currentLanguage];
        }
      };
  });

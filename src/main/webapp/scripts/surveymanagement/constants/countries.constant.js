'use strict';

angular.module('metadatamanagementApp').constant('COUNTRIES', [
  {
    code: 'AD',
    de: 'Andorra',
    en: 'Andorra'
  },
  {
    code: 'AE',
    de: 'Vereinigte Arabische Emirate',
    en: 'United Arab Emirates'
  },
  {
    code: 'AF',
    de: 'Afghanistan',
    en: 'Afghanistan'
  },
  {
    code: 'AG',
    de: 'Antigua und Barbuda',
    en: 'Antigua and Barbuda'
  },
  {
    code: 'AI',
    de: 'Anguilla',
    en: 'Anguilla'
  },
  {
    code: 'AL',
    de: 'Albanien',
    en: 'Albania'
  },
  {
    code: 'AM',
    de: 'Armenien',
    en: 'Armenia'
  },
  {
    code: 'AN',
    de: 'Niederländische Antillen',
    en: 'Netherlands Antilles'
  },
  {
    code: 'AO',
    de: 'Angola',
    en: 'Angola'
  },
  {
    code: 'AQ',
    de: 'Antarktis',
    en: 'Antarctica'
  },
  {
    code: 'AR',
    de: 'Argentinien',
    en: 'Argentina'
  },
  {
    code: 'AS',
    de: 'Amerikanisch-Samoa',
    en: 'American Samoa'
  },
  {
    code: 'AT',
    de: 'Österreich',
    en: 'Austria'
  },
  {
    code: 'AU',
    de: 'Australien',
    en: 'Australia'
  },
  {
    code: 'AW',
    de: 'Aruba',
    en: 'Aruba'
  },
  {
    code: 'AX',
    de: 'Aaland-Inseln',
    en: 'Åland Islands'
  },
  {
    code: 'AZ',
    de: 'Aserbaidschan',
    en: 'Azerbaijan'
  },
  {
    code: 'BA',
    de: 'Bosnien und Herzegowina',
    en: 'Bosnia and Herzegovina'
  },
  {
    code: 'BB',
    de: 'Barbados',
    en: 'Barbados'
  },
  {
    code: 'BD',
    de: 'Bangladesch',
    en: 'Bangladesh'
  },
  {
    code: 'BE',
    de: 'Belgien',
    en: 'Belgium'
  },
  {
    code: 'BF',
    de: 'Burkina Faso',
    en: 'Burkina Faso'
  },
  {
    code: 'BG',
    de: 'Bulgarien',
    en: 'Bulgaria'
  },
  {
    code: 'BH',
    de: 'Bahrain',
    en: 'Bahrain'
  },
  {
    code: 'BI',
    de: 'Burundi',
    en: 'Burundi'
  },
  {
    code: 'BJ',
    de: 'Benin',
    en: 'Benin'
  },
  {
    code: 'BL',
    de: 'Saint Barthélemy',
    en: 'Saint Barthélemy'
  },
  {
    code: 'BM',
    de: 'Bermuda',
    en: 'Bermuda'
  },
  {
    code: 'BN',
    de: 'Brunei',
    en: 'Brunei'
  },
  {
    code: 'BO',
    de: 'Bolivien',
    en: 'Bolivia'
  },
  {
    code: 'BQ',
    de: 'Bonaire, Sint Eustatius und Saba',
    en: 'Bonaire, Sint Eustatius and Saba'
  },
  {
    code: 'BR',
    de: 'Brasilien',
    en: 'Brazil'
  },
  {
    code: 'BS',
    de: 'Bahamas',
    en: 'Bahamas'
  },
  {
    code: 'BT',
    de: 'Bhutan',
    en: 'Bhutan'
  },
  {
    code: 'BV',
    de: 'Bouvet-Insel',
    en: 'Bouvet Island'
  },
  {
    code: 'BW',
    de: 'Botsuana',
    en: 'Botswana'
  },
  {
    code: 'BY',
    de: 'Belarus',
    en: 'Belarus'
  },
  {
    code: 'BZ',
    de: 'Belize',
    en: 'Belize'
  },
  {
    code: 'CA',
    de: 'Kanada',
    en: 'Canada'
  },
  {
    code: 'CC',
    de: 'Kokos-Inseln',
    en: 'Cocos Islands'
  },
  {
    code: 'CD',
    de: 'Demokratische Republik Kongo',
    en: 'The Democratic Republic Of Congo'
  },
  {
    code: 'CF',
    de: 'Zentralafrikanische Republik',
    en: 'Central African Republic'
  },
  {
    code: 'CG',
    de: 'Kongo',
    en: 'Congo'
  },
  {
    code: 'CH',
    de: 'Schweiz',
    en: 'Switzerland'
  },
  {
    code: 'CI',
    de: 'Elfenbeinküste',
    en: 'Côte d\'Ivoire'
  },
  {
    code: 'CK',
    de: 'Cook-Inseln',
    en: 'Cook Islands'
  },
  {
    code: 'CL',
    de: 'Chile',
    en: 'Chile'
  },
  {
    code: 'CM',
    de: 'Kamerun',
    en: 'Cameroon'
  },
  {
    code: 'CN',
    de: 'China',
    en: 'China'
  },
  {
    code: 'CO',
    de: 'Kolumbien',
    en: 'Colombia'
  },
  {
    code: 'CR',
    de: 'Costa Rica',
    en: 'Costa Rica'
  },
  {
    code: 'CU',
    de: 'Kuba',
    en: 'Cuba'
  },
  {
    code: 'CV',
    de: 'Kap Verde',
    en: 'Cape Verde'
  },
  {
    code: 'CW',
    de: 'Curaçao',
    en: 'Curaçao'
  },
  {
    code: 'CX',
    de: 'Weihnachtsinsel',
    en: 'Christmas Island'
  },
  {
    code: 'CY',
    de: 'Zypern',
    en: 'Cyprus'
  },
  {
    code: 'CZ',
    de: 'Tschechische Republik',
    en: 'Czech Republic'
  },
  {
    code: 'DE',
    de: 'Deutschland',
    en: 'Germany'
  },
  {
    code: 'DJ',
    de: 'Dschibuti',
    en: 'Djibouti'
  },
  {
    code: 'DK',
    de: 'Dänemark',
    en: 'Denmark'
  },
  {
    code: 'DM',
    de: 'Dominica',
    en: 'Dominica'
  },
  {
    code: 'DO',
    de: 'Dominikanische Republik',
    en: 'Dominican Republic'
  },
  {
    code: 'DZ',
    de: 'Algerien',
    en: 'Algeria'
  },
  {
    code: 'EC',
    de: 'Ecuador',
    en: 'Ecuador'
  },
  {
    code: 'EE',
    de: 'Estland',
    en: 'Estonia'
  },
  {
    code: 'EG',
    de: 'Ägypten',
    en: 'Egypt'
  },
  {
    code: 'EH',
    de: 'Westsahara',
    en: 'Western Sahara'
  },
  {
    code: 'ER',
    de: 'Eritrea',
    en: 'Eritrea'
  },
  {
    code: 'ES',
    de: 'Spanien',
    en: 'Spain'
  },
  {
    code: 'ET',
    de: 'Äthiopien',
    en: 'Ethiopia'
  },
  {
    code: 'FI',
    de: 'Finnland',
    en: 'Finland'
  },
  {
    code: 'FJ',
    de: 'Fidschi',
    en: 'Fiji'
  },
  {
    code: 'FK',
    de: 'Falkland-Inseln',
    en: 'Falkland Islands'
  },
  {
    code: 'FM',
    de: 'Mikronesien',
    en: 'Micronesia'
  },
  {
    code: 'FO',
    de: 'Färöer-Inseln',
    en: 'Faroe Islands'
  },
  {
    code: 'FR',
    de: 'Frankreich',
    en: 'France'
  },
  {
    code: 'GA',
    de: 'Gabun',
    en: 'Gabon'
  },
  {
    code: 'GB',
    de: 'Vereinigtes Königreich',
    en: 'United Kingdom'
  },
  {
    code: 'GD',
    de: 'Grenada',
    en: 'Grenada'
  },
  {
    code: 'GE',
    de: 'Georgien',
    en: 'Georgia'
  },
  {
    code: 'GF',
    de: 'Französisch-Guayana',
    en: 'French Guiana'
  },
  {
    code: 'GG',
    de: 'Guernsey',
    en: 'Guernsey'
  },
  {
    code: 'GH',
    de: 'Ghana',
    en: 'Ghana'
  },
  {
    code: 'GI',
    de: 'Gibraltar',
    en: 'Gibraltar'
  },
  {
    code: 'GL',
    de: 'Grönland',
    en: 'Greenland'
  },
  {
    code: 'GM',
    de: 'Gambia',
    en: 'Gambia'
  },
  {
    code: 'GN',
    de: 'Guinea',
    en: 'Guinea'
  },
  {
    code: 'GP',
    de: 'Guadeloupe',
    en: 'Guadeloupe'
  },
  {
    code: 'GQ',
    de: 'Äquatorial-Guinea',
    en: 'Equatorial Guinea'
  },
  {
    code: 'GR',
    de: 'Griechenland',
    en: 'Greece'
  },
  {
    code: 'GS',
    de: 'Süd-Georgia und die südlichen Sandwich-Inseln',
    en: 'South Georgia And The South Sandwich Islands'
  },
  {
    code: 'GT',
    de: 'Guatemala',
    en: 'Guatemala'
  },
  {
    code: 'GU',
    de: 'Guam',
    en: 'Guam'
  },
  {
    code: 'GW',
    de: 'Guinea-Bissau',
    en: 'Guinea-Bissau'
  },
  {
    code: 'GY',
    de: 'Guyana',
    en: 'Guyana'
  },
  {
    code: 'HK',
    de: 'Hongkong',
    en: 'Hong Kong'
  },
  {
    code: 'HM',
    de: 'Heard- und McDonald-Inseln',
    en: 'Heard Island And McDonald Islands'
  },
  {
    code: 'HN',
    de: 'Honduras',
    en: 'Honduras'
  },
  {
    code: 'HR',
    de: 'Kroatien',
    en: 'Croatia'
  },
  {
    code: 'HT',
    de: 'Haiti',
    en: 'Haiti'
  },
  {
    code: 'HU',
    de: 'Ungarn',
    en: 'Hungary'
  },
  {
    code: 'ID',
    de: 'Indonesien',
    en: 'Indonesia'
  },
  {
    code: 'IE',
    de: 'Irland',
    en: 'Ireland'
  },
  {
    code: 'IL',
    de: 'Israel',
    en: 'Israel'
  },
  {
    code: 'IM',
    de: 'Isle of Man',
    en: 'Isle Of Man'
  },
  {
    code: 'IN',
    de: 'Indien',
    en: 'India'
  },
  {
    code: 'IO',
    de: 'Britische Territorien im Indischen Ozean',
    en: 'British Indian Ocean Territory'
  },
  {
    code: 'IQ',
    de: 'Irak',
    en: 'Iraq'
  },
  {
    code: 'IR',
    de: 'Iran',
    en: 'Iran'
  },
  {
    code: 'IS',
    de: 'Island',
    en: 'Iceland'
  },
  {
    code: 'IT',
    de: 'Italien',
    en: 'Italy'
  },
  {
    code: 'JE',
    de: 'Jersey',
    en: 'Jersey'
  },
  {
    code: 'JM',
    de: 'Jamaika',
    en: 'Jamaica'
  },
  {
    code: 'JO',
    de: 'Jordanien',
    en: 'Jordan'
  },
  {
    code: 'JP',
    de: 'Japan',
    en: 'Japan'
  },
  {
    code: 'KE',
    de: 'Kenia',
    en: 'Kenya'
  },
  {
    code: 'KG',
    de: 'Kirgistan',
    en: 'Kyrgyzstan'
  },
  {
    code: 'KH',
    de: 'Kambodscha',
    en: 'Cambodia'
  },
  {
    code: 'KI',
    de: 'Kiribati',
    en: 'Kiribati'
  },
  {
    code: 'KM',
    de: 'Komoren',
    en: 'Comoros'
  },
  {
    code: 'KN',
    de: 'Saint Kitts und Nevis',
    en: 'Saint Kitts And Nevis'
  },
  {
    code: 'KP',
    de: 'Nordkorea',
    en: 'North Korea'
  },
  {
    code: 'KR',
    de: 'Südkorea',
    en: 'South Korea'
  },
  {
    code: 'KW',
    de: 'Kuwait',
    en: 'Kuwait'
  },
  {
    code: 'KY',
    de: 'Kaiman-Inseln',
    en: 'Cayman Islands'
  },
  {
    code: 'KZ',
    de: 'Kasachstan',
    en: 'Kazakhstan'
  },
  {
    code: 'LA',
    de: 'Laos',
    en: 'Laos'
  },
  {
    code: 'LB',
    de: 'Libanon',
    en: 'Lebanon'
  },
  {
    code: 'LC',
    de: 'St. Lucia',
    en: 'Saint Lucia'
  },
  {
    code: 'LI',
    de: 'Liechtenstein',
    en: 'Liechtenstein'
  },
  {
    code: 'LK',
    de: 'Sri Lanka',
    en: 'Sri Lanka'
  },
  {
    code: 'LR',
    de: 'Liberia',
    en: 'Liberia'
  },
  {
    code: 'LS',
    de: 'Lesotho',
    en: 'Lesotho'
  },
  {
    code: 'LT',
    de: 'Litauen',
    en: 'Lithuania'
  },
  {
    code: 'LU',
    de: 'Luxemburg',
    en: 'Luxembourg'
  },
  {
    code: 'LV',
    de: 'Lettland',
    en: 'Latvia'
  },
  {
    code: 'LY',
    de: 'Libyen',
    en: 'Libya'
  },
  {
    code: 'MA',
    de: 'Marokko',
    en: 'Morocco'
  },
  {
    code: 'MC',
    de: 'Monaco',
    en: 'Monaco'
  },
  {
    code: 'MD',
    de: 'Moldau',
    en: 'Moldova'
  },
  {
    code: 'ME',
    de: 'Montenegro',
    en: 'Montenegro'
  },
  {
    code: 'MF',
    de: 'St. Martin',
    en: 'Saint Martin'
  },
  {
    code: 'MG',
    de: 'Madagaskar',
    en: 'Madagascar'
  },
  {
    code: 'MH',
    de: 'Marshall-Inseln',
    en: 'Marshall Islands'
  },
  {
    code: 'MK',
    de: 'Mazedonien',
    en: 'Macedonia'
  },
  {
    code: 'ML',
    de: 'Mali',
    en: 'Mali'
  },
  {
    code: 'MM',
    de: 'Myanmar',
    en: 'Myanmar'
  },
  {
    code: 'MN',
    de: 'Mongolei',
    en: 'Mongolia'
  },
  {
    code: 'MO',
    de: 'Macao',
    en: 'Macao'
  },
  {
    code: 'MP',
    de: 'Nördliche Mariannen-Inseln',
    en: 'Northern Mariana Islands'
  },
  {
    code: 'MQ',
    de: 'Martinique',
    en: 'Martinique'
  },
  {
    code: 'MR',
    de: 'Mauretanien',
    en: 'Mauritania'
  },
  {
    code: 'MS',
    de: 'Montserrat',
    en: 'Montserrat'
  },
  {
    code: 'MT',
    de: 'Malta',
    en: 'Malta'
  },
  {
    code: 'MU',
    de: 'Mauritius',
    en: 'Mauritius'
  },
  {
    code: 'MV',
    de: 'Maldiven',
    en: 'Maldives'
  },
  {
    code: 'MW',
    de: 'Malawi',
    en: 'Malawi'
  },
  {
    code: 'MX',
    de: 'Mexiko',
    en: 'Mexico'
  },
  {
    code: 'MY',
    de: 'Malaysia',
    en: 'Malaysia'
  },
  {
    code: 'MZ',
    de: 'Mosambik',
    en: 'Mozambique'
  },
  {
    code: 'NA',
    de: 'Namibia',
    en: 'Namibia'
  },
  {
    code: 'NC',
    de: 'Neukaledonien',
    en: 'New Caledonia'
  },
  {
    code: 'NE',
    de: 'Niger',
    en: 'Niger'
  },
  {
    code: 'NF',
    de: 'Norfolk-Insel',
    en: 'Norfolk Island'
  },
  {
    code: 'NG',
    de: 'Nigeria',
    en: 'Nigeria'
  },
  {
    code: 'NI',
    de: 'Nicaragua',
    en: 'Nicaragua'
  },
  {
    code: 'NL',
    de: 'Niederlande',
    en: 'Netherlands'
  },
  {
    code: 'NO',
    de: 'Norwegen',
    en: 'Norway'
  },
  {
    code: 'NP',
    de: 'Nepal',
    en: 'Nepal'
  },
  {
    code: 'NR',
    de: 'Nauru',
    en: 'Nauru'
  },
  {
    code: 'NU',
    de: 'Niue',
    en: 'Niue'
  },
  {
    code: 'NZ',
    de: 'Neuseeland',
    en: 'New Zealand'
  },
  {
    code: 'OM',
    de: 'Oman',
    en: 'Oman'
  },
  {
    code: 'PA',
    de: 'Panama',
    en: 'Panama'
  },
  {
    code: 'PE',
    de: 'Peru',
    en: 'Peru'
  },
  {
    code: 'PF',
    de: 'Französisch-Polynesien',
    en: 'French Polynesia'
  },
  {
    code: 'PG',
    de: 'Papua-Neuguinea',
    en: 'Papua New Guinea'
  },
  {
    code: 'PH',
    de: 'Philippinen',
    en: 'Philippines'
  },
  {
    code: 'PK',
    de: 'Pakistan',
    en: 'Pakistan'
  },
  {
    code: 'PL',
    de: 'Polen',
    en: 'Poland'
  },
  {
    code: 'PM',
    de: 'St. Pierre und Miquelon',
    en: 'Saint Pierre And Miquelon'
  },
  {
    code: 'PN',
    de: 'Pitcairn',
    en: 'Pitcairn'
  },
  {
    code: 'PR',
    de: 'Puerto Rico',
    en: 'Puerto Rico'
  },
  {
    code: 'PS',
    de: 'Palästina',
    en: 'Palestine'
  },
  {
    code: 'PT',
    de: 'Portugal',
    en: 'Portugal'
  },
  {
    code: 'PW',
    de: 'Palau',
    en: 'Palau'
  },
  {
    code: 'PY',
    de: 'Paraguay',
    en: 'Paraguay'
  },
  {
    code: 'QA',
    de: 'Katar',
    en: 'Qatar'
  },
  {
    code: 'RE',
    de: 'Reunion',
    en: 'Reunion'
  },
  {
    code: 'RO',
    de: 'Rumänien',
    en: 'Romania'
  },
  {
    code: 'RS',
    de: 'Serbien',
    en: 'Serbia'
  },
  {
    code: 'RU',
    de: 'Russland',
    en: 'Russia'
  },
  {
    code: 'RW',
    de: 'Ruanda',
    en: 'Rwanda'
  },
  {
    code: 'SA',
    de: 'Saudi-Arabien',
    en: 'Saudi Arabia'
  },
  {
    code: 'SB',
    de: 'Solomon-Inseln',
    en: 'Solomon Islands'
  },
  {
    code: 'SC',
    de: 'Seychellen',
    en: 'Seychelles'
  },
  {
    code: 'SD',
    de: 'Sudan',
    en: 'Sudan'
  },
  {
    code: 'SE',
    de: 'Schweden',
    en: 'Sweden'
  },
  {
    code: 'SG',
    de: 'Singapur',
    en: 'Singapore'
  },
  {
    code: 'SH',
    de: 'St. Helena',
    en: 'Saint Helena'
  },
  {
    code: 'SI',
    de: 'Slowenien',
    en: 'Slovenia'
  },
  {
    code: 'SJ',
    de: 'Svalbard und Jan Mayen',
    en: 'Svalbard And Jan Mayen'
  },
  {
    code: 'SK',
    de: 'Slowakei',
    en: 'Slovakia'
  },
  {
    code: 'SL',
    de: 'Sierra Leone',
    en: 'Sierra Leone'
  },
  {
    code: 'SM',
    de: 'San Marino',
    en: 'San Marino'
  },
  {
    code: 'SN',
    de: 'Senegal',
    en: 'Senegal'
  },
  {
    code: 'SO',
    de: 'Somalia',
    en: 'Somalia'
  },
  {
    code: 'SR',
    de: 'Suriname',
    en: 'Suriname'
  },
  {
    code: 'SS',
    de: 'Südsudan',
    en: 'South Sudan'
  },
  {
    code: 'ST',
    de: 'Sao Tome und Principe',
    en: 'Sao Tome And Principe'
  },
  {
    code: 'SV',
    de: 'El Salvador',
    en: 'El Salvador'
  },
  {
    code: 'SX',
    de: 'Sint Maarten (Niederländischer Teil)',
    en: 'Sint Maarten (Dutch part)'
  },
  {
    code: 'SY',
    de: 'Syrien',
    en: 'Syria'
  },
  {
    code: 'SZ',
    de: 'Swasiland',
    en: 'Swaziland'
  },
  {
    code: 'TC',
    de: 'Turks- und Caicos-Inseln',
    en: 'Turks And Caicos Islands'
  },
  {
    code: 'TD',
    de: 'Tschad',
    en: 'Chad'
  },
  {
    code: 'TF',
    de: 'Französische Südgebiete',
    en: 'French Southern Territories'
  },
  {
    code: 'TG',
    de: 'Togo',
    en: 'Togo'
  },
  {
    code: 'TH',
    de: 'Thailand',
    en: 'Thailand'
  },
  {
    code: 'TJ',
    de: 'Tadschikistan',
    en: 'Tajikistan'
  },
  {
    code: 'TK',
    de: 'Tokelau',
    en: 'Tokelau'
  },
  {
    code: 'TL',
    de: 'Timor-Leste',
    en: 'Timor-Leste'
  },
  {
    code: 'TM',
    de: 'Turkmenistan',
    en: 'Turkmenistan'
  },
  {
    code: 'TN',
    de: 'Tunesien',
    en: 'Tunisia'
  },
  {
    code: 'TO',
    de: 'Tonga',
    en: 'Tonga'
  },
  {
    code: 'TR',
    de: 'Türkei',
    en: 'Turkey'
  },
  {
    code: 'TT',
    de: 'Trinidad und Tobago',
    en: 'Trinidad and Tobago'
  },
  {
    code: 'TV',
    de: 'Tuvalu',
    en: 'Tuvalu'
  },
  {
    code: 'TW',
    de: 'Taiwan',
    en: 'Taiwan'
  },
  {
    code: 'TZ',
    de: 'Tansania',
    en: 'Tanzania'
  },
  {
    code: 'UA',
    de: 'Ukraine',
    en: 'Ukraine'
  },
  {
    code: 'UG',
    de: 'Uganda',
    en: 'Uganda'
  },
  {
    code: 'UM',
    de: 'Übrige Inseln im Pazifik der USA',
    en: 'United States Minor Outlying Islands'
  },
  {
    code: 'US',
    de: 'Vereinigte Staaten von Amerika',
    en: 'United States'
  },
  {
    code: 'UY',
    de: 'Uruguay',
    en: 'Uruguay'
  },
  {
    code: 'UZ',
    de: 'Usbekistan',
    en: 'Uzbekistan'
  },
  {
    code: 'VA',
    de: 'Vatikanstadt',
    en: 'Vatican'
  },
  {
    code: 'VC',
    de: 'St. Vincent und die Grenadinen',
    en: 'Saint Vincent And The Grenadines'
  },
  {
    code: 'VE',
    de: 'Venezuela',
    en: 'Venezuela'
  },
  {
    code: 'VG',
    de: 'Britische Jungferninseln',
    en: 'British Virgin Islands'
  },
  {
    code: 'VI',
    de: 'Amerikanische Jungferninseln',
    en: 'U.S. Virgin Islands'
  },
  {
    code: 'VN',
    de: 'Vietnam',
    en: 'Vietnam'
  },
  {
    code: 'VU',
    de: 'Vanuatu',
    en: 'Vanuatu'
  },
  {
    code: 'WF',
    de: 'Wallis und Futuna',
    en: 'Wallis And Futuna'
  },
  {
    code: 'WS',
    de: 'Samoa',
    en: 'Samoa'
  },
  {
    code: 'YE',
    de: 'Jemen',
    en: 'Yemen'
  },
  {
    code: 'YT',
    de: 'Mayotte',
    en: 'Mayotte'
  },
  {
    code: 'ZA',
    de: 'Südafrika',
    en: 'South Africa'
  },
  {
    code: 'ZM',
    de: 'Sambia',
    en: 'Zambia'
  },
  {
    code: 'ZW',
    de: 'Simbabwe',
    en: 'Zimbabwe'
  }
]);

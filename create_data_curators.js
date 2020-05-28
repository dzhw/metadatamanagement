/* global db, printjson */
'use strict';

var dataCuratorsMap = {
    'stu-bst02$': [{
      'firstName': 'Robert',
      'lastName': 'Birkelbach'
    }],
    'stu-cmp2014$': [{
      'firstName': 'Kim',
      'lastName': 'Sommer'
    },{
      'firstName': 'Sandra',
      'lastName': 'Vietgen'
    }],
    'stu-dps2017$': [{
      'firstName': 'Isabel',
      'lastName': 'Steinhardt'
    },{
      'firstName': 'Dilek',
      'lastName': 'İkiz-Akıncı'
    }],
    'stu-egr2018$': [{
      'firstName': 'N.',
      'lastName': 'N.'
    }],
    'stu-est2016$': [{
      'firstName': 'Robert',
      'lastName': 'Birkelbach'
    },{
      'firstName': 'Friederike',
      'lastName': 'Schlücker'
    }],
    'stu-gra2005$': [{
      'firstName': 'Florence',
      'lastName': 'Baillet'
    },{
      'firstName': 'Andreas',
      'lastName': 'Franken'
    },{
      'firstName': 'Anne',
      'lastName': 'Weber'
    }],
    'stu-gra2009$': [{
      'firstName': 'Florence',
      'lastName': 'Baillet'
    },{
      'firstName': 'Andreas',
      'lastName': 'Franken'
    },{
      'firstName': 'Anne',
      'lastName': 'Weber'
    }],
    'stu-gsl2008$': [{
      'firstName': 'Andreas',
      'lastName': 'Daniel'
    },{
      'firstName': 'Ute',
      'lastName': 'Hoffstätter'
    },{
      'firstName': 'Björn',
      'lastName': 'Huß'
    },{
      'firstName': 'Percy',
      'lastName': 'Scheller'
    }],
    'stu-gsl2012$': [{
      'firstName': 'Robert',
      'lastName': 'Birkelbach'
    },{
      'firstName': 'Sandra',
      'lastName': 'Vietgen'
    },{
      'firstName': 'Marten',
      'lastName': 'Wallis'
    }],
    'stu-gsl2015$': [{
      'firstName': 'Robert',
      'lastName': 'Birkelbach'
    },{
      'firstName': 'Johanna',
      'lastName': 'Niebuhr'
    },{
      'firstName': 'Sandra',
      'lastName': 'Vietgen'
    },{
      'firstName': 'Marten',
      'lastName': 'Wallis'
    }],
    'stu-hth2017$': [{
      'firstName': 'Ute',
      'lastName': 'Hoffstätter'
    },{
      'firstName': 'Elke',
      'lastName': 'Middendorff'
    }],
    'stu-lib2016$': [{
      'firstName': 'Bernd',
      'lastName': 'Kleimann'
    },{
      'firstName': 'Dilek',
      'lastName': 'İkiz-Akıncı'
    },{
      'firstName': 'Malte',
      'lastName': 'Hückstädt'
    }],
    'stu-mog2020$': [{
      'firstName': 'N.',
      'lastName': 'N.'
    }],
    'stu-nac2018$': [{
      'firstName': 'Robert',
      'lastName': 'Birkelbach'
    },{
      'firstName': 'Ute',
      'lastName': 'Hoffstätter'
    },{
      'firstName': 'Anne',
      'lastName': 'Weber'
    }],
    'stu-phd2014$': [{
      'firstName': 'Kerstin',
      'lastName': 'Lange'
    },{
      'firstName': 'Percy',
      'lastName': 'Scheller',
    },{
      'firstName': 'Sandra',
      'lastName': 'Vietgen'
    },{
      'firstName': 'Marten',
      'lastName': 'Wallis'
    }],
    'stu-rub18yo$': [{
      'firstName': 'Dilek',
      'lastName': 'İkiz-Akıncı'
    }],
    'stu-scs2016$': [{
      'firstName': 'Andreas',
      'lastName': 'Daniel'
    },{
      'firstName': 'Sahra-Rebecca',
      'lastName': 'Kienast'
    },{
      'firstName': 'Sandra',
      'lastName': 'Vietgen'
    }],
    'stu-ssy17$': [{
      'firstName': 'Elke',
      'lastName': 'Middendorff'
    },{
      'firstName': 'Ute',
      'lastName': 'Hoffstätter'
    }],
    'stu-ssy18$': [{
      'firstName': 'Elke',
      'lastName': 'Middendorff'
    },{
      'firstName': 'Ute',
      'lastName': 'Hoffstätter'
    }],
    'stu-ssy19$': [{
      'firstName': 'Ute',
      'lastName': 'Hoffstätter'
    },{
      'firstName': 'Andreas',
      'lastName': 'Sarcletti'
    }],
    'stu-ssy20$': [{
      'firstName': 'Andreas',
      'lastName': 'Daniel'
    },{
      'firstName': 'Andreas',
      'lastName': 'Sarcletti'
    },{
      'firstName': 'Sandra',
      'lastName': 'Vietgen'
    }],
    'stu-ssy21$': [{
      'firstName': 'Florence',
      'lastName': 'Baillet'
    },{
      'firstName': 'Anne',
      'lastName': 'Weber'
    }],
    'stu-tu18yo$': [{
      'firstName': 'Dilek',
      'lastName': 'İkiz-Akıncı'
    }],
    'stu-tuk18yo$': [{
      'firstName': 'Dilek',
      'lastName': 'İkiz-Akıncı'
    }],
    'stu-uzk18yo$': [{
      'firstName': 'Dilek',
      'lastName': 'İkiz-Akıncı'
    }],
    'stu-win2015$': [{
      'firstName': 'Adisa',
      'lastName': 'Beširović'
    },{
      'firstName': 'Dilek',
      'lastName': 'İkiz-Akıncı'
    },{
      'firstName': 'Thorben',
      'lastName': 'Sembritzki'
    },{
      'firstName': 'Lisa',
      'lastName': 'Thiele'
    }]
  };

Object.keys(dataCuratorsMap).forEach(function(studyId) {
    db.getCollection('studies').find({masterId: studyId}).forEach(
    function(study) {
      study.dataCurators = dataCuratorsMap[studyId];
      study.version = study.version + 1;
      study.lastModifiedDate = new Date();
      db.getCollection('studies').save(study);
    });
  });

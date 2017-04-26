/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('StudyBuilderService',
  function(StudyResource, CleanJSObjectService, StudyIdBuilderService) {
    var buildStudy = function(studyFromExcel, authors,
      dataAcquisitionProjectId) {
      var study = {
        id: StudyIdBuilderService.buildStudyId(
          dataAcquisitionProjectId),
        title: {
          en: studyFromExcel['title.en'],
          de: studyFromExcel['title.de']
        },
        description: {
          en: studyFromExcel['description.en'],
          de: studyFromExcel['description.de']
        },
        institution: {
          en: studyFromExcel['institution.en'],
          de: studyFromExcel['institution.de']
        },
        surveySeries: {
          en: studyFromExcel['surveySeries.en'],
          de: studyFromExcel['surveySeries.de']
        },
        sponsor: {
          en: studyFromExcel['sponsor.en'],
          de: studyFromExcel['sponsor.de']
        },
        citationHint: {
          en: studyFromExcel['citationHint.en'],
          de: studyFromExcel['citationHint.de']
        },
        dataAvailability: {
          en: studyFromExcel['dataAvailability.en'],
          de: studyFromExcel['dataAvailability.de']
        },
        surveyDesign: {
          en: studyFromExcel['surveyDesign.en'],
          de: studyFromExcel['surveyDesign.de']
        },
        authors: authors,
        dataAcquisitionProjectId: dataAcquisitionProjectId,
        doi: '10.5072/DZHW:' + dataAcquisitionProjectId
      };
      var cleanedStudyObject = CleanJSObjectService
        .removeEmptyJsonObjects(study);
      return new StudyResource(cleanedStudyObject);
    };

    var buildAuthors = function(authorsFromExcel) {
      var authors = [];
      authorsFromExcel.forEach(function(author) {
        authors.push({
          'firstName': author.firstName,
          'middleName': author.middleName,
          'lastName': author.lastName
        });
      });

      return authors;
    };

    return {
      buildStudy: buildStudy,
      buildAuthors: buildAuthors
    };
  });

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
        annotations: {
          en: studyFromExcel['annotations.en'],
          de: studyFromExcel['annotations.de']
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
    var buildStudyAttachmentsMetadata = function(metadataFromExcel,
      dataAcquisitionProjectId, studyId) {
      var attachments = [];
      metadataFromExcel.forEach(function(attachment) {
          var studyAttachmentMetadata = {
            studyId: studyId,
            dataAcquisitionProjectId: dataAcquisitionProjectId,
            description: {
              en: attachment['description.en'],
              de: attachment['description.de']
            },
            type: {
              en: attachment['type.en'],
              de: attachment['type.de']
            },
            title: attachment.title,
            language: attachment.language,
            fileName: attachment.filename,
            indexInStudy: attachment.__rowNum__
          };
          var cleanedMetadata = CleanJSObjectService
        .removeEmptyJsonObjects(studyAttachmentMetadata);
          attachments.push(cleanedMetadata);
        });
      return attachments;
    };

    return {
      buildStudy: buildStudy,
      buildAuthors: buildAuthors,
      buildStudyAttachmentsMetadata: buildStudyAttachmentsMetadata
    };
  });

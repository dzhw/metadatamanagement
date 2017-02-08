/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('StudyBuilderService',
  function(StudyResource, CleanJSObjectService) {
    var buildStudy = function(studyFromExcel, releases,
      dataAcquisitionProjectId) {
        var study = {
          id: dataAcquisitionProjectId,
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
          authors: studyFromExcel.authors,
          accessWays: CleanJSObjectService.
          removeWhiteSpace(studyFromExcel.accessWays),
          dataAcquisitionProjectId: dataAcquisitionProjectId,
          releases: releases
        };
        var cleanedStudyObject = CleanJSObjectService
        .removeEmptyJsonObjects(study);
        return new StudyResource(cleanedStudyObject);
      };
    var buildReleases = function(releasesFromExcel) {
      var releases = [];
      releasesFromExcel.forEach(function(releaseFromExcel) {
        var release = {
          version: releaseFromExcel.version,
          doi: releaseFromExcel.doi,
          date: releaseFromExcel.date,
          notes: {
            en: releaseFromExcel['notes.en'],
            de: releaseFromExcel['notes.de'],
          }
        };
        releases.push(release);
      });
      return releases;
    };
    return {
      buildStudy: buildStudy,
      buildReleases: buildReleases
    };
  });

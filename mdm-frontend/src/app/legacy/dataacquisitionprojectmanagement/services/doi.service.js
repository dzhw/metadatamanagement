'use strict';
angular.module('metadatamanagementApp').service('DoiService', ['ENV',
  function(ENV) {
    
    /**
     * Create a DOI link for releases greater than or equal to 1.0.0.
     *
     * @param dataAcquisitionProjectId project ID
     * @param release project release version
     * @returns A DOI or empty string.
     */
    var createDoiLink = function(dataAcquisitionProjectId, releaseVersion) {
      if (!!releaseVersion && !!dataAcquisitionProjectId && gteReleaseVersions(releaseVersion)) {
        if (ENV == 'prod') {
          return 'https://doi.org/10.21249/DZHW:' + stripVersionSuffix(dataAcquisitionProjectId) + ':' + releaseVersion;
        } else {
          return 'https://doi.org/10.17889/DZHW:' + stripVersionSuffix(dataAcquisitionProjectId) + ':' + releaseVersion;
        }
      }
      return '';
    };

    /**
     * Strips the version suffix from the data acquisition project ID.
     *
     * @param dataAcquisitionProjectId The data acquisition project ID.
     * @returns The data acquisition project ID without the version suffix.
     */
    var stripVersionSuffix = function(dataAcquisitionProjectId) {
      const versionSuffixRegex = /-[0-9]+\.[0-9]+\.[0-9]+$/;
      if (dataAcquisitionProjectId && dataAcquisitionProjectId.trim() != '') {
        return dataAcquisitionProjectId.replace(versionSuffixRegex, '');
      } else {
        return dataAcquisitionProjectId;
      }
    };      

    /**
     * Compares project release version with minimum release version '1.0.0'
     * 
     * @param projectReleaseVersion Current project (data aqusition) project version
     * @returns True if project release version is greater than or equal '1.0.0'
     */
    var gteReleaseVersions = function(projectReleaseVersion) {
      const minVersionParts = '1.0.0';

      // If both versions are equal, return true
      if (minVersionParts === projectReleaseVersion) {
        return true;
      }

      // Split the version strings into their components
      const projectVersionParts = projectReleaseVersion.split('.');
      // Compare the version components one by one
      for (let i = 0; i < Math.max(projectVersionParts.length, minVersionParts.length); i++) {
        const projectVersionPart = parseInt(projectVersionParts[i] || '0', 10);
        const minVersionPart = parseInt(minVersionParts[i] || '0', 10);

        // If the project version is greater, return true
        if (projectVersionPart > minVersionPart) {
          return true;
        } else if (projectVersionPart < minVersionPart) {
          // If the project version is less, return false
          return false;
        }
      }

      return false;
    };

    // make the methods global
    return {
      createDoiLink: createDoiLink
    };
  }]);


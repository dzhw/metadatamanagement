#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

const LATEST_ELSST_URL = 'https://thesauri.cessda.eu/elsst/';
const FILES_TO_CHECK = [
  'mdm-frontend/src/app/legacy/common/i18n/directives/tageditor/elsst-search.service.js',
  'mdm-frontend/src/app/legacy/common/i18n/directives/tageditor/tag-editor-elsst.controller.js',
  'src/main/java/eu/dzhw/fdz/metadatamanagement/projectmanagement/service/DataCiteService.java'
];

function extractElsstVersion(value) {
  const match = value.match(/elsst-(\d+)/);
  return match ? match[1] : null;
}

async function getLatestElsstVersion() {
  const manualResponse = await fetch(LATEST_ELSST_URL, {redirect: 'manual'});
  const location = manualResponse.headers.get('location');
  const versionFromRedirect = location ? extractElsstVersion(location) : null;

  if (versionFromRedirect) {
    return versionFromRedirect;
  }

  const followedResponse = await fetch(LATEST_ELSST_URL);
  const versionFromUrl = extractElsstVersion(followedResponse.url);

  if (versionFromUrl) {
    return versionFromUrl;
  }

  const body = await followedResponse.text();
  const versionFromBody = extractElsstVersion(body);

  if (versionFromBody) {
    return versionFromBody;
  }

  throw new Error(`Could not determine latest ELSST version from ${LATEST_ELSST_URL}`);
}

function getConfiguredElsstVersions(filePath) {
  const content = fs.readFileSync(filePath, 'utf8');
  const matches = [...content.matchAll(/elsst-(\d+)/g)];
  return [...new Set(matches.map(match => match[1]))];
}

async function main() {
  const latestVersion = await getLatestElsstVersion();
  const failures = [];

  for (const file of FILES_TO_CHECK) {
    const fullPath = path.resolve(file);
    const configuredVersions = getConfiguredElsstVersions(fullPath);

    if (configuredVersions.length === 0) {
      failures.push(`${file}: no elsst-* version found`);
      continue;
    }

    const outdatedVersions = configuredVersions.filter(version => version !== latestVersion);
    if (outdatedVersions.length > 0) {
      failures.push(`${file}: found elsst-${configuredVersions.join(', elsst-')}, expected elsst-${latestVersion}`);
    }
  }

  if (failures.length > 0) {
    console.error(`ELSST version is outdated. Latest version is elsst-${latestVersion}.`);
    console.error('');
    console.error('Update these files:');
    failures.forEach(failure => console.error(`- ${failure}`));
    console.error('');
    console.error('Also review the ELSST info-i text in translations-de.js and translations-en.js.');
    process.exit(1);
  }

  console.log(`ELSST version is current: elsst-${latestVersion}`);
}

main().catch(error => {
  console.error(error.message);
  process.exit(1);
});

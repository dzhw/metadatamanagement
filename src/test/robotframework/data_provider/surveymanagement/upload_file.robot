*** Settings ***
Documentation     Tests the upload on the survey edit site. #File upload is not possible in Firefox
Metadata          Info on data    This test suite uses the project with the name "fileuploadproject" which needs to be available in the testing environment
Force Tags        chromeonly
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/search_resource.robot

*** Test Cases ***
Upload file in survey editor
    Select project by name    fileuploadproject
    Click on surveys tab
    Search for    sur-fileuploadproject-sy1$
    Click Survey Edit Button
    Upload Response Response Rate
    Save Response Rate Changes
    Get To Survey Page
    Page Should Contain Element    xpath=//md-content//a[@ng-href="/public/files/surveys/sur-fileuploadproject-sy1$/1_responserate_de"]
    Click Edit Button on Survey Details
    Delete Response Rate Upload
    Save Response Rate Changes
    Page Should Not Contain Element    xpath=//md-content//a[@ng-href="/public/files/surveys/sur-fileuploadproject-sy1$/1_responserate_de"]
    Get back to home page and deselect project

*** Keywords ***
Get back to home page and deselect project
    Get back to german home page
    Click Element Through Tooltips    xpath=//md-sidenav//project-navbar-module//button[@aria-label='Clear Input']

Delete Response Rate Upload
    Click Element Through Tooltips    xpath=//md-card//button[@ng-click='ctrl.deleteResponseRateImageDe()']
    Wait for Angular    2s

Save Response Rate Changes
    Click Element Through Tooltips    xpath=//md-card//button[contains(.,'save')]

Get To Survey Page
    Click Element Through Tooltips    xpath=//fdz-breadcrumbs//a[contains(@ui-sref,'surveyDetail')]

Upload Response Response Rate
    Choose File    xpath=//input[@type='file'][@ngf-select='ctrl.saveResponseRateImageDe($file)'][1]    ${CURDIR}/data/1_responserate_de.svg
    Wait for Angular    2s

Click Edit Button on Survey Details
    Click Element Through Tooltips    xpath=//ui-view//button/md-icon[text()='mode_edit']

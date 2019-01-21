*** Settings ***
Documentation     Tests the upload on the survey edit site.
Force Tags        chromeonly
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/search_resource.robot


*** Test Cases ***
Upload file in survey editor
    Select project by name    fileuploadproject
    Click on surveys tab
    Click on search result by id    sur-fileuploadproject-sy1$
    Click Survey Edit Button
    Upload Response Response Rate
    # Sleep is needed as the save button is too soon available and the upload wouldn't be done.
    Sleep    2s
    Save Response Rate Changes
    Get To Survey Page
    Page Should Contain Element    xpath=//md-content//a[@ng-href="/public/files/surveys/sur-fileuploadproject-sy1$/1_responserate_de"]
    Click Survey Edit Button
    Delete Response Rate Upload
    # Same Reason as above
    Sleep    2s
    Save Response Rate Changes
    Get back to home page and deselect project

*** Keywords ***
Get back to home page and deselect project
    Get back to german home page
    Click Element Through Tooltips    xpath=//md-sidenav//project-navbar-module//button[@aria-label='Clear Input']

Click Survey Edit Button
    Click Element Through Tooltips    xpath=//ui-view//a/md-icon[text()='mode_edit']

Delete Response Rate Upload
    Click Element Through Tooltips    xpath=//md-card//button[contains(.,'delete_forever')]

Save Response Rate Changes
    Click Element Through Tooltips    xpath=//md-card//button[contains(.,'save')]

Get To Survey Page
    Click Element Through Tooltips    xpath=//md-toolbar//a[span[text()='1']]

Upload Response Response Rate
    Choose File    xpath=//input[@type='file'][@ngf-select='ctrl.saveResponseRateImageDe($file)'][1]    ${CURDIR}/data/1_responserate_de.svg

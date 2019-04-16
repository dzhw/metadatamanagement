*** Settings ***
Documentation     Upload a attachement to the instrument
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/search_resource.robot

*** Test Cases ***
Upload folder and single file in Dataset editor
    [Tags]  chromeonly
    Select project by name    fileuploadproject
    Click on instruments tab
    Click on search result by id    ins-fileuploadproject-ins1$
    Click instrument edit button
    Click add button
    Upload instrument file
    Select instrument data type
    Select a language
    Write instrument description in de and en
    Save Changes for instrument attachment
    Delete instrument with uploaded document
    Get back to home page and deselect project

*** Keywords ***
Get back to home page and deselect project
    Get back to german home page
    Click Element Through Tooltips    xpath=//md-sidenav//project-navbar-module//button[@aria-label='Clear Input']

Click instrument edit button
    Click Element Through Tooltips    xpath=//ui-view//button/md-icon[text()='mode_edit']

Click add button
    Click Element Through Tooltips    xpath=//ui-view//button/md-icon[text()='add']

Upload instrument file
    Press Key   xpath=//input[@type='file' and @ngf-select="ctrl.upload($file)"][1]   ${CURDIR}/data/gra2005_W1_Questionnaire_de.pdf  # singlefile folder contains only a single file

Select instrument data type
    Click Element Through Tooltips   xpath=//md-select[@ng-model="ctrl.instrumentAttachmentMetadata.type"]
    Click Element Through Tooltips   xpath=//md-select-menu//md-option[contains(., "Fragebogen")]

Select a language
    Clear Element Text   name=language2
    Input Text    xpath=//md-input-container//input[@name="language2"]   Deutsch

Write instrument description in de and en
    Clear Element Text   xpath=//md-input-container//input[@ng-model="ctrl.instrumentAttachmentMetadata.description.de"]
    Input Text   xpath=//md-input-container//input[@ng-model="ctrl.instrumentAttachmentMetadata.description.de"]   Dataset Description De
    Clear Element Text   xpath=//md-input-container//input[@ng-model="ctrl.instrumentAttachmentMetadata.description.en"]
    Input Text   xpath=//md-input-container//input[@ng-model="ctrl.instrumentAttachmentMetadata.description.en"]   Dataset Description En

Save Changes for instrument attachment
    Click Element Through Tooltips    //div[@class="fdz-fab-button-container layout-column"]//button//md-icon[contains(., "save")]

Delete instrument with uploaded document
    Click Element Through Tooltips    xpath=//button[md-icon[text()='delete_forever']]
    Click Element Through Tooltips    xpath=//button[text()='Ja']


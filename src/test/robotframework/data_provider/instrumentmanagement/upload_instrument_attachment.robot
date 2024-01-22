*** Settings ***
Documentation     Upload a attachement to the instrument
Metadata          Info on data    This test suite uses the project with the name "fileuploadproject" which needs to be available in the testing environment
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/search_resource.robot

*** Test Cases ***
Upload Attachment for Instrument
    [Tags]  chromeonly
    Select project by name    fileuploadproject
    Click on instruments tab
    Search for    ins-fileuploadproject-ins1$
    Click instrument edit button
    Click add button
    Upload instrument file
    Select instrument data type
    Select a language
    Write instrument description in de and en
    Save Changes for instrument attachment
    Assert gra2005_W1_Questionnaire in the attachment
    Delete uploaded document
    Get back to home page and deselect project

*** Keywords ***
Get back to home page and deselect project
    Get back to german home page
    Click Element Through Tooltips    xpath=//md-sidenav//project-navbar-module//button[@aria-label='Clear Input']

Click instrument edit button
    Click Element Through Tooltips    xpath=//ui-view//button[normalize-space()='Bearbeiten']

Click add button
    Click Element Through Tooltips    xpath=//ui-view//button/md-icon[text()='add']

Upload instrument file
    Choose File   xpath=//input[@type='file' and @ngf-select="ctrl.upload($file)"][1]   ${CURDIR}/data/gra2005_W1_Questionnaire_de.pdf  # data folder contains the PDF file

Select instrument data type
    Click Element Through Tooltips   xpath=//md-select[@ng-model="ctrl.attachmentMetadata.type"]
    Click Element Through Tooltips   xpath=//md-select-menu//md-option[contains(., "Fragebogen")]

Select a language
    Clear Element Text   name=language2
    Input Text    xpath=//md-input-container//input[@name="language2"]   Deutsch

Write instrument description in de and en
    Clear Element Text   xpath=//md-input-container//input[@ng-model="ctrl.attachmentMetadata.description.de"]
    Input Text   xpath=//md-input-container//input[@ng-model="ctrl.attachmentMetadata.description.de"]   Dataset Description De
    Clear Element Text   xpath=//md-input-container//input[@ng-model="ctrl.attachmentMetadata.description.en"]
    Input Text   xpath=//md-input-container//input[@ng-model="ctrl.attachmentMetadata.description.en"]   Dataset Description En

Save Changes for instrument attachment
    Click Element Through Tooltips    xpath=//div[@class="fdz-fab-button-container layout-column"]//button//md-icon[contains(., "save")]

Assert gra2005_W1_Questionnaire in the attachment
    Page Should Contain Element    xpath=//a[@ng-href="/public/files/instruments/ins-fileuploadproject-ins1$/attachments/gra2005_W1_Questionnaire_de.pdf"]

Delete uploaded document
    Click Element Through Tooltips    xpath=//button[md-icon[text()='delete_forever']]
    Click Element Through Tooltips    xpath=//button[text()='Ja']
    Wait for Angular    2s
    Page Should Not Contain Element   xpath=//a[@ng-href="/public/files/instruments/ins-fileuploadproject-ins1$/attachments/gra2005_W1_Questionnaire_de.pdf"]

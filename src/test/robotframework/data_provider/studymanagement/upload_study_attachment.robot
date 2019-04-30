*** Settings ***
Documentation     Upload a attachement to the study
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/search_resource.robot

*** Test Cases ***
Upload Attachment for Study
    [Tags]  chromeonly
    Select project by name    fileuploadproject
    Click on study tab
    Click study edit button
    Click add button
    Upload study file
    Select study data type
    Select a language
    Write Title in slected laguage
    Write study description in de and en
    Save Changes for study attachment
    Assert gra2005_MethodReport in the attachment
    Delete study with uploaded document
    Get back to home page and deselect project

*** Keywords ***
Get back to home page and deselect project
    Get back to german home page
    Click Element Through Tooltips    xpath=//md-sidenav//project-navbar-module//button[@aria-label="Clear Input"]

Click study edit button
    Click Element Through Tooltips    xpath=//ui-view//button//md-icon[text()="mode_edit"]

Click add button
    Click Element Through Tooltips    xpath=//ui-view//button[@ng-click="ctrl.addAttachment($event)"]//md-icon[text()="add"]

Upload study file
    Press Key   xpath=//input[@type='file' and @ngf-select="ctrl.upload($file)"][1]   ${CURDIR}/data/gra2005_MethodReport_de.pdf  # data folder contains the PDF file

Select study data type
    Click Element Through Tooltips   xpath=//md-select[@ng-model="ctrl.studyAttachmentMetadata.type"]
    Click Element Through Tooltips   xpath=//md-select-menu//md-option[contains(., "Sonstiges")]

Select a language
    Clear Element Text   name=language2
    Input Text    xpath=//md-input-container//input[@name="language2"]   Deutsch

Write Title in selected laguage
    Clear Element Text   xpath=//textarea[@name="title"]
    Input Text    xpath=//textarea[@name="title"]    Title in German Language for Study

Write study description in de and en
    Clear Element Text   xpath=//md-input-container//input[@ng-model="ctrl.studyAttachmentMetadata.description.de"]
    Input Text   xpath=//md-input-container//input[@ng-model="ctrl.studyAttachmentMetadata.description.de"]   Study Description De
    Clear Element Text   xpath=//md-input-container//input[@ng-model="ctrl.studyAttachmentMetadata.description.en"]
    Input Text   xpath=//md-input-container//input[@ng-model="ctrl.studyAttachmentMetadata.description.en"]   Study Description En

Save Changes for study attachment
    Click Element Through Tooltips    xpath=//div[@class="fdz-fab-button-container layout-column"]//button//md-icon[contains(., "save")]

Assert gra2005_MethodReport in the attachment
    Page Should Contain Element    xpath=//a[@ng-href="/public/files/studies/stu-fileuploadproject$/attachments/gra2005_MethodReport_de.pdf"]

Delete study with uploaded document
    Click Element Through Tooltips    xpath=//button[md-icon[text()="delete_forever"]]
    Click Element Through Tooltips    xpath=//button[text()="Ja"]

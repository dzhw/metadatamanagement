*** Settings ***
Documentation     Upload a attachement to the data package
Metadata          Info on data    This test suite uses the project with the name "fileuploadproject" which needs to be available in the testing environment
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/search_resource.robot

*** Test Cases ***
Upload Attachment for Data Package
    [Tags]  chromeonly
    Select project by name    fileuploadproject
    Click on data package tab
    Click data package edit button
    Click add button
    Upload data package file
    Select data package data type
    Select a language
    Write Title in selected laguage
    Write data package description in de and en
    Save Changes for data package attachment
    Assert gra2005_MethodReport in the attachment
    Delete uploaded document
    Get back to home page and deselect project

*** Keywords ***
Get back to home page and deselect project
    Get back to german home page
    Click Element Through Tooltips    xpath=//md-sidenav//project-navbar-module//button[@aria-label="Clear Input"]

Click data package edit button
    Click Element Through Tooltips    xpath=//ui-view//button[normalize-space()="Bearbeiten"]

Click add button
    Click Element Through Tooltips    xpath=//ui-view//button[@ng-click="ctrl.addAttachment($event)"]//md-icon[text()="add"]

Upload data package file
    Choose File   xpath=//input[@type='file' and @ngf-select="ctrl.upload($file)"][1]   ${CURDIR}/data/gra2005_MethodReport_de.pdf  # data folder contains the PDF file

Select data package data type
    Click Element Through Tooltips   xpath=//md-select[@ng-model="ctrl.attachmentMetadata.type"]
    Click Element Through Tooltips   xpath=//md-select-menu//md-option[contains(., "Sonstiges")]

Select a language
    Clear Element Text   name=language2
    Input Text    xpath=//md-input-container//input[@name="language2"]   Deutsch

Write Title in selected laguage
    Clear Element Text   xpath=//textarea[@name="title"]
    Input Text    xpath=//textarea[@name="title"]    Title in German Language for Data Package

Write data package description in de and en
    Clear Element Text   xpath=//md-input-container//input[@ng-model="ctrl.attachmentMetadata.description.de"]
    Input Text   xpath=//md-input-container//input[@ng-model="ctrl.attachmentMetadata.description.de"]   Data Package Description De
    Clear Element Text   xpath=//md-input-container//input[@ng-model="ctrl.attachmentMetadata.description.en"]
    Input Text   xpath=//md-input-container//input[@ng-model="ctrl.attachmentMetadata.description.en"]   Data Package Description En

Save Changes for data package attachment
    Click Element Through Tooltips    xpath=//div[@class="fdz-fab-button-container layout-column"]//button//md-icon[contains(., "save")]

Assert gra2005_MethodReport in the attachment
    Page Should Contain Element    xpath=//a[@ng-href="/public/files/data-packages/stu-fileuploadproject$/attachments/gra2005_MethodReport_de.pdf"]

Delete uploaded document
    Click Element Through Tooltips    xpath=//button[md-icon[text()="delete_forever"]]
    Click Element Through Tooltips    xpath=//button[text()="Ja"]
    Wait for Angular    2s
    Page Should Not Contain Element    xpath=//a[@ng-href="/public/files/data-packages/stu-fileuploadproject$/attachments/gra2005_MethodReport_de.pdf"]

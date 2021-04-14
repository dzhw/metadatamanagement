*** Settings ***
Documentation     Publisher Create concept and check the details of the concept. Eventually publisher delete the concept.
Force Tags        noslowpoke
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Create Concepts by Publisher
    Get Back to german home page
    Click on concept tab
    Open Concept Create Page
    Input Text    name=id    Concept-RDC-ID-007_${BROWSER}
    Input Text    name=titleDe    Test Konzepte_RDC-ID-007_${BROWSER}
    Input Text    name=titleEn    Test Concept_RDC-ID-007_${BROWSER}
    Input Text    xpath=//textarea[@name="citationHint"]    Concept Citation Hint
    Input Text    name=doi    Concept DOI
    Input Text    name=descriptionDe   Concept Description in DE
    Input Text    name=descriptionEn   Concept Description in EN
    Input Text    name=peopleFirstName_0    Md Shakawath
    Input Text    name=peopleMiddleName_0    noMiddleName
    Input Text    name=peopleLastName_0    Hossain
    Add Another Author
    Focus    xpath=//input[@name = 'peopleFirstName_1']
    Input Text    name=peopleFirstName_1    Md Rameez
    Input Text    name=peopleMiddleName_1    noMiddleName
    Input Text    name=peopleLastName_1    Raza
    Move Second Author To Place One
    Input Text    xpath=//md-autocomplete[@md-search-text="tagSearchTextDe"]//input   Konzept Tag 007
    Run Keyword And Ignore Error  Click Element Through Tooltips    xpath=//md-virtual-repeat-container//span[text()='Konzept Tag 007']
    Input Text    xpath=//md-autocomplete[@md-search-text="tagSearchTextEn"]//input   Concept Tag 007
    Run Keyword And Ignore Error  Click Element Through Tooltips    xpath=//md-virtual-repeat-container//span[text()='Concept Tag 007']
    Input Text    xpath=//textarea[@name="license"]    Concept License Agreement
    Select a language
    Save Changes
    Get back to german home page
    Click on concept tab
    Assert created concept under concept list
    Run Keyword If    '${BROWSER}' == 'chrome'   Attach documents to the concept  #upload attachment is only possible in chrome
    Delete Concept

*** Keywords ***
Open Concept Create Page
    Click Element Through Tooltips    xpath=//ui-view/descendant::button[md-icon[text()='add']]

Add Another Author

    Click Element Through Tooltips    xpath=//md-card/descendant::button[md-icon[text()='add']]

Move Second Author To Place One
    Click Element Through Tooltips    xpath=//md-card/descendant::button[md-icon[text()='keyboard_arrow_up']]

Select a language
    Click Element    xpath=//md-autocomplete[@md-search-text="languageName"]//input
    Click Element Through Tooltips    xpath=//md-virtual-repeat-container[@ng-hide="$mdAutocompleteCtrl.hidden"]//ul//li//span[contains(., "Akan")]

Delete Concept
    Click Element Through Tooltips    xpath=//concept-search-result//md-card-header[contains(., "RDC-ID-007_${BROWSER}")]/following::md-card-actions//button[normalize-space()="Löschen"]
    Click Element Through Tooltips    xpath=//button[text()='Ja']

Assert created concept under concept list
    Page Should Contain Element     xpath=//concept-search-result//span[contains(., "RDC-ID-007_${BROWSER}")]

Upload concept attchment file
    Press Key   xpath=//input[@type='file' and @ngf-select="ctrl.upload($file)"][1]   ${CURDIR}/data/gra2005_MethodReport_de.pdf  # data folder contains the PDF file

Select concept data type
    Click Element Through Tooltips   xpath=//md-select[@ng-model="ctrl.attachmentMetadata.type"]
    Click Element Through Tooltips   xpath=//md-select-menu//md-option[contains(., "Dokumentation")]

Select a language for concept attachment
    Clear Element Text   name=language2
    Input Text    xpath=//md-input-container//input[@name="language2"]   Deutsch

Select a Titel of the attachment
    Input Text   xpath=//textarea[@name="title"]  Thiis is a titel text for concept attachment

Write attachment description in de and en
    Clear Element Text   xpath=//md-input-container//input[@ng-model="ctrl.attachmentMetadata.description.de"]
    Input Text   xpath=//md-input-container//input[@ng-model="ctrl.attachmentMetadata.description.de"]   Dataset Description De
    Clear Element Text   xpath=//md-input-container//input[@ng-model="ctrl.attachmentMetadata.description.en"]
    Input Text   xpath=//md-input-container//input[@ng-model="ctrl.attachmentMetadata.description.en"]   Dataset Description En

Save Changes for concept attachment
    Click Element Through Tooltips    xpath=//div[@class="fdz-fab-button-container layout-column"]//button//md-icon[contains(., "save")]

Assert gra2005_W1_Questionnaire in the attachment
    Page Should Contain Element    xpath=//a[@ng-href="/public/files/concepts/con-Concept-RDC-ID-007_${BROWSER}$/attachments/gra2005_MethodReport_de.pdf"]

Attach documents to the concept
    Search for  Test Konzepte_RDC-ID-007_${BROWSER}
    Click Element Through Tooltips    xpath=xpath=(//concept-search-result//md-card-actions//a[normalize-space()="Bearbeiten"])[1]
    Click Element Through Tooltips    xpath=//md-card-actions[@ng-if="!ctrl.createMode"]//button//md-icon[text()="add"]
    Upload concept attchment file
    Select concept data type
    Select a language for concept attachment
    Select a Titel of the attachment
    Write attachment description in de and en
    Save Changes for concept attachment
    Assert gra2005_W1_Questionnaire in the attachment
    Get back to german home page
    Click on concept tab

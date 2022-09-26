*** Settings ***
Documentation     Publisher Create Data Package
Metadata          Info on data   Create the temporary project with name tempdatapackage${BROWSER}
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Create Data Package by Publisher
    Pass Execution If    '${BROWSER}' == 'ie'    Data Package Creation not possible in IE
    ${created}  Run Keyword and return status  Create Project  tempdatapackage${BROWSER}
    Run Keyword If  ${created}==False  Fail  Could not create new project 'tempdatapackage${BROWSER}'
    Get Back to german home page
    Click on data package tab
    Open Data Package Create Page
    Input Text    name=titleDe    Test Datenpaket
    Input Text    name=titleEn    Test Data Package
    Input Text    name=studySeriesDe    Roboter Datenpakete ${BROWSER}
    Input Text    name=studySeriesEn    Robot Data Packages ${BROWSER}
    Choose Panel As Survey Design
    Input Text    name=institutionDe_0    DZHW ${BROWSER}
    Input Text    name=institutionEn_0    DZHW ${BROWSER}
    Input Text    name=sponsorDe_0    BMBF ${BROWSER}
    Input Text    name=sponsorEn_0    BMBF ${BROWSER}
    Input Text    name=annotationsDe    Dieses Datenpaket wurde von Robot automatisch erstellt.
    Input Text    name=annotationsEn    This data package was created automatically by Robot.x
    Set Focus To Element    xpath=//textarea[@name = 'descriptionDe']
    Input Text    name=descriptionDe    Dieses Datenpaket wurde automatisch erstellt und überprüft die Eingabe valider Eigenschaften.
    Input Text    name=descriptionEn    This data package was created automatically and test the input of valid attributes.
    Set Focus To Element    xpath=//input[@name = 'projectContributorsFirstName_0']
    Input Text    name=projectContributorsFirstName_0    Anne
    Input Text    name=projectContributorsMiddleName_0    noMiddleName
    Input Text    name=projectContributorsLastName_0    Droid
    Add Another Contributor
    Set Focus To Element    xpath=//input[@name = 'projectContributorsFirstName_1']
    Input Text    name=projectContributorsFirstName_1    R2
    Input Text    name=projectContributorsMiddleName_1    D2
    Input Text    name=projectContributorsLastName_1    noLastName
    Move Second Contributor To Place One
    Set Focus To Element    xpath=//input[@name = 'dataCuratorsFirstName_0']
    Input Text    name=dataCuratorsFirstName_0    Anne
    Input Text    name=dataCuratorsMiddleName_0    noMiddleName
    Input Text    name=dataCuratorsLastName_0    Droid
    Input Text    xpath=//md-autocomplete[@md-search-text="tagSearchTextDe"]//input   Deutsche Tags Schlüsselwörter
    Run Keyword And Ignore Error  Click Element Through Tooltips    xpath=//md-virtual-repeat-container//span[text()='Deutsche Tags Schlüsselwörter']
    Input Text    xpath=//md-autocomplete[@md-search-text="tagSearchTextEn"]//input   English Days Keyword
    Run Keyword And Ignore Error  Click Element Through Tooltips    xpath=//md-virtual-repeat-container//span[text()='English Days Keyword']
    Save Changes
    Page Should Contain Element    xpath=//fdz-breadcrumbs//span[contains(.,'tempdatapackage${BROWSER}')]
    Get back to german home page
    Delete project by name  tempdatapackage${BROWSER}

*** Keywords ***
Open Data Package Create Page
    Click Element Through Tooltips    xpath=//ui-view/descendant::button[md-icon[text()='add']]

Choose Panel As Survey Design
    Click Element Through Tooltips    xpath=//md-select[@name = 'surveyDesign']
    Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., 'Panel')]

Add Another Contributor
    Click Element Through Tooltips    xpath=//md-card//button[contains(@aria-label,'Mitarbeiter') and contains(@aria-label,'hinzuzufügen')]

Move Second Contributor To Place One
    Click Element Through Tooltips    xpath=//md-card/descendant::button[md-icon[text()='keyboard_arrow_up']]

*** Settings ***
Documentation     Publisher Create Study
Resource          ../../resources/login_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Create Study by Publisher
    Pass Execution If    '${BROWSER}' == 'ie'    Study Creation not possible in IE
    Create Project  robotsproject${BROWSER}
    Get Back to german home page
    Click on study tab
    Open Study Create Page
    Input Text    name=titleDe    Test Studie
    Input Text    name=titleEn    Test Study
    Input Text    name=studySeriesDe    Roboter Studien ${BROWSER}
    Input Text    name=studySeriesEn    Robot Studies ${BROWSER}
    Choose Panel As Survey Design
    Input Text    name=institutionDe_0    DZHW ${BROWSER}
    Input Text    name=institutionEn_0    DZHW ${BROWSER}
    Input Text    name=sponsorDe    BMBF ${BROWSER}
    Input Text    name=sponsorEn    BMBF ${BROWSER}
    Input Text    name=annotationsDe    Diese Studie wurde von Robot automatisch erstellt.
    Input Text    name=annotationsEn    This study was created automatically by Robot.x
    Choose In Aufbereitung as Data Availibility
    Focus    xpath=//textarea[@name = 'descriptionDe']
    Input Text    name=descriptionDe    Diese Studie wurde automatisch erstellt und überprüft die Eingabe valider Eigenschaften.
    Input Text    name=descriptionEn    This study was created automatically and test the input of valid attributes.
    Focus    xpath=//input[@name = 'projectContributorsFirstName_0']
    Input Text    name=projectContributorsFirstName_0    Anne
    Input Text    name=projectContributorsMiddleName_0    noMiddleName
    Input Text    name=projectContributorsLastName_0    Droid
    Add Another Contributor
    Focus    xpath=//input[@name = 'projectContributorsFirstName_1']
    Input Text    name=projectContributorsFirstName_1    R2
    Input Text    name=projectContributorsMiddleName_1    D2
    Input Text    name=projectContributorsLastName_1    noLastName
    Move Second Contributor To Place One
    Focus    xpath=//input[@name = 'curatorsFirstName_0']
    Input Text    name=curatorsFirstName_0    Anne
    Input Text    name=curatorsMiddleName_0    noMiddleName
    Input Text    name=curatorsLastName_0    Droid
    Input Text    xpath=//md-autocomplete[@md-search-text="tagSearchTextDe"]//input   Deutsche Tags Schlüsselwörter
    Run Keyword And Ignore Error  Click Element Through Tooltips    xpath=//md-virtual-repeat-container//span[text()='Deutsche Tags Schlüsselwörter']
    Input Text    xpath=//md-autocomplete[@md-search-text="tagSearchTextEn"]//input   English Days Keyword
    Run Keyword And Ignore Error  Click Element Through Tooltips    xpath=//md-virtual-repeat-container//span[text()='English Days Keyword']
    Save Changes
    Page Should Contain Element    xpath=//fdz-breadcrumbs//span[contains(.,'robotsproject${BROWSER}')]
    Delete Robotsproject

*** Keywords ***
Open Study Create Page
    Click Element Through Tooltips    xpath=//ui-view/descendant::button[md-icon[text()='add']]

Choose Panel As Survey Design
    Click Element Through Tooltips    xpath=//md-select[@name = 'surveyDesign']
    Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., 'Panel')]

Choose In Aufbereitung as Data Availibility
    Click Element Through Tooltips    xpath=//md-select[@name = 'dataAvailability']
    Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., 'In Aufbereitung')]

Add Another Contributor
    Click Element Through Tooltips    xpath=//md-card//button[contains(@aria-label,'Mitarbeiter') and contains(@aria-label,'hinzuzufügen')]

Move Second Contributor To Place One
    Click Element Through Tooltips    xpath=//md-card/descendant::button[md-icon[text()='keyboard_arrow_up']]

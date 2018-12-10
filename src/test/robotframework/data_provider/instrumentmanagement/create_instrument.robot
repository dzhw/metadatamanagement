*** Settings ***
Documentation     Instrument Creation by Dataprovider
#Suite Setup       Pass Execution If    '${BROWSER}' == 'ie'    Creation not possible in IE
Force Tags        smoketest
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/login_resource.robot

*** Test Cases ***
Create Instrument by Dataprovider
    Go To Instrument Create Page
    Fill up the description and title
    Choose Type
    Choose Survey
    Fill up the annotations
    Save Changes
    Get back to german home page
    Click on instruments tab
    Delete Instrument

*** Keywords ***
Go To Instrument Create Page
    Pass Execution If    '${BROWSER}' == 'ie'    Instrument Creation not possible in IE
    Select project by name    robotproject
    Wait Until Angular Ready    6s
    Click on instruments tab
    Click Element Through Tooltips    xpath=//ui-view/descendant::a[md-icon[text()='add']]

Fill up the description and title
    Pass Execution If    '${BROWSER}' == 'ie'    Instrument Creation not possible in IE
    Input Text   xpath=//input[@name='descriptionDe']    Instrument Description De
    Input Text   xpath=//input[@name='descriptionEn']    Instrument Description De
    Input Text   xpath=//input[@name='titleDe']    Instrument Title De
    Input Text   xpath=//input[@name='titleEn']    Instrument Title En
    Input Text   xpath=//input[@name='subtitleDe']    Instrument Title De
    Input Text   xpath=//input[@name='subtitleEn']    Instrument Title En

Choose Type
    Pass Execution If    '${BROWSER}' == 'ie'    Instrument Creation not possible in IE
    Click Element  xpath=//md-select[@name="type"]
    Click Element  xpath=//md-select-menu//md-option[contains(., 'CAPI')]

Choose Survey
    Pass Execution If    '${BROWSER}' == 'ie'    Instrument Creation not possible in IE
    Click Element Through Tooltips  xpath=//md-chips[@name="surveys"]//md-autocomplete
    Click Element Through Tooltips  xpath=//span[contains(.,'sur-robotproject')]

Fill up the annotations
    Pass Execution If    '${BROWSER}' == 'ie'    Instrument Creation not possible in IE
    Input Text  xpath=//textarea[contains(@name,'annotationsDe')]  This is Annotations in De
    Input Text  xpath=//textarea[contains(@name,'annotationsEn')]  This is Annotation in En

Delete Instrument
    Click Element Through Tooltips    xpath=//button[md-icon[text()='delete_forever']]
    Click Element Through Tooltips    xpath=//button[text()='Ja']



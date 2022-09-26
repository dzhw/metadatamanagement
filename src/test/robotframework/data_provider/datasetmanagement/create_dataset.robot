*** Settings ***
Documentation     Dataset Creation by Dataprovider #Prerequisite to have robotproject4${BROWSER} and a survey for robotproject4${BROWSER}
Metadata          Info on data    This test suite uses the project with the name "robotproject4${BROWSER}" which needs to contain a survey
Resource          ../../resources/home_page_resource.robot
Resource          ../../resources/search_resource.robot
Resource          ../../resources/click_element_resource.robot
Resource          ../../resources/login_resource.robot

*** Test Cases ***
Create Dataset by Dataprovider
    Go To Dataset Create Page
    Fill up the description and title
    Choose Format
    Choose Type
    Choose Survey
    Fill up the Annotations
    Fill up the Subdatasets
    Select Access Way for the Datasets from The List    SUF: Download
    Enter Number of Observations or Episodes
    Select Data Format
    Fill up the description of Subdatasets
    Save Changes
    Get back to german home page
    Click on data set tab
    Delete Dataset

*** Keywords ***
Go To Dataset Create Page
    Select project by name    robotproject4${BROWSER}
    Wait for Angular    2s
    Click on data set tab
    Click Element Through Tooltips    xpath=//ui-view/descendant::button[md-icon[text()='add']]

Fill up the description and title
    Input Text   xpath=//input[@name='descriptionDe']    Dataset Description De
    Input Text   xpath=//input[@name='descriptionEn']    Dataset Description En

Fill up the Subdatasets
    Input Text   xpath=//input[@name='subDataSetsName_0']    Subdataset Name

Select Access Way for the Datasets from The List
    [Arguments]   ${accesswayname}
    Click Element Through Tooltips    xpath=//md-select[@name='subDataSetsAccessWay_0']
    Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., '${accesswayname}')]

Select Data Format
    Click Element Through Tooltips    xpath=//md-select[@name='subDataSetsDataFormats_0']
    Click Element Through Tooltips    xpath=//md-select-menu//md-option[contains(., 'Stata')]

Enter Number of Observations or Episodes
    Input Text   xpath=//input[@name='subDataSetsNumberOfObservations_0']    5

Fill up the description of Subdatasets
    Input Text   xpath=//input[@name='subDataSetsDescriptionDe_0']    Subdataset Description De
    Input Text   xpath=//input[@name='subDataSetsDescriptionEn_0']    subdataset Description En

Choose Format
    Click Element  xpath=//md-select[@name="format"]
    Click Element  xpath=//md-select-menu//md-content//md-option//div[contains(., 'lang')]

Choose Type
    Click Element  xpath=//md-select[@name="type"]
    Click Element  xpath=//md-select-menu//md-content//md-option//div[contains(., 'Episodendatensatz')]

Choose Survey
    Click Element   xpath=//md-chips[@name="surveys"]//md-autocomplete//input
    Click Element Through Tooltips  xpath=//span[contains(.,'sur-robotproject4${BROWSER}')]

Fill up the Annotations
    Input Text  xpath=//textarea[contains(@name,'annotationsDe')]  This is Annotations in De
    Input Text  xpath=//textarea[contains(@name,'annotationsEn')]  This is Annotation in En

Delete Dataset
    Click Element Through Tooltips    xpath=//button[normalize-space()='Löschen']
    Click Element Through Tooltips    xpath=//button[text()='Ja']

*** Settings ***
Documentation     Tests the Tags in the Study
Force Tags        smoketest
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Search Tag and Assret Tag in the Respective Study
    Searching for study tags in German
    Assert tag available in the selected Study

*** Keywords ***
Searching for study tags in German
    Click on study tab
    Search for    Deutsche Tags Schlüsselwörter
    Click on search result by id    stu-robotprojectrelease4chrome$
    Page Should Contain  Deutsche Tags Schlüsselwörter
    Click Element Through Tooltips   xpath=//a[@ng-repeat="tag in ctrl.studyTags"]//span[contains(., "Deutsche Tags Schlüsselwörter")]

Assert tag available in the selected Study
    Click on study tab
    Search for   stu-robotprojectrelease4chrome$
    Click on search result by id    stu-robotprojectrelease4chrome$
    Page Should Contain  Test Project Release Study chrome DE
    Get back to german home page



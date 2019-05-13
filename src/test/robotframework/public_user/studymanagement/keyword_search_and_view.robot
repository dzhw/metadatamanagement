*** Settings ***
Documentation     Tests the Tags in the Study
Force Tags        smoketest
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Search Tag and assret search results
    Click on study tab
    Search for    Deutsche Tags Schlüsselwörter
    Sleep  2s  # avoid failling in firefox and chrome, the are too fast
    Click on the first search result
    Page Should Contain  Deutsche Tags Schlüsselwörter
    Click Element Through Tooltips   xpath=//a[@ng-repeat="tag in ctrl.studyTags"]//span[contains(., "Deutsche Tags Schlüsselwörter")]
    Assert search results contain the id of the study   # after clicking of the tag which whas searched
    Get back to german home page

*** Keywords ***
Assert search results contain the id of the study
    Page Should Contain  stu-robotprojectrelease4chrome$

Click on the first search result
    Click Element Through Tooltips   xpath=//a[@class='fdz-search-result'][1]

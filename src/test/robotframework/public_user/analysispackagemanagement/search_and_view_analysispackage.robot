*** Settings ***
Documentation     Tests the user experience of searching & finding the analysis package Test Analysis Package (testanalysepaket)  and opening its analysis package page
Metadata          Info on data    This test suite uses the analysis package 'testanalysispackage'
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Test Cases ***
Looking for Test Analysis Package in german
    wait until page contains element    //a[@ui-sref="search({type: 'analysis_packages'})"]
    Click Element   xpath=//a[@ui-sref="search({type: 'analysis_packages'})"]
    Click on first search result
    Wait for Angular    2s
    Page Should Contain    Test Analysepaket
    #TODO: remove data dependency
#    Page should contain x scripts   1
#    Page should contain x data packages    FDZ-DZHW Datenpaket    1
#    Page should contain x items in analysis data    4
#    Data packages should have different versions    FDZ-DZHW Datenpaket
    Page should have one related publication
    Select Version for the Analysis Package    1.0.0
    Show Cite
    Get back to german home page

Looking for Test Analysis Package in english
    [Setup]    Change language to english
    wait until page contains element    //a[@ui-sref="search({type: 'analysis_packages'})"]
    Click Element   xpath=//a[@ui-sref="search({type: 'analysis_packages'})"]
    Click on first search result
    Page Should Contain    Test Analysis Package
    # TODO: Remove data dependency
#    Page should contain x scripts    1
#    Page should contain x items in analysis data    4
#    Page should contain x data packages   FDZ-DZHW Datapackage    2
#    Data packages should have different versions    FDZ-DZHW Datapackage
    Page should have one related publication
    [Teardown]    Get back to german home page

*** Keywords ***
Page should contain x scripts
    [Arguments]    ${limit}
    page should contain element   //fdz-detail[4]/md-card/div/md-card-content/table/tbody/tr    limit=${limit}

Page should contain x data packages
    [Arguments]    ${text}    ${limit}
    page should contain element   //span[text()='${text}']    limit=${limit}

Page should contain x items in analysis data
    [Arguments]    ${limit}
    ${count}    get element count    //fdz-detail[5]/md-card/div/md-card-content/md-list
    Should Be True  ${count} == ${limit}

Data packages should have different versions
    [Arguments]    ${text}
    ${version1}=    get text    //fdz-detail[5]/md-card/div/md-card-content/md-list[1]/md-list-item[3]/div/span
    ${version2}=    get text    //fdz-detail[5]/md-card/div/md-card-content/md-list[2]/md-list-item[3]/div/span
    should not be true    """${version1}""" == """${version2}"""

Page should have one related publication
    ${count}    get element count       //related-publication-search-result/md-card
    Should Be True  ${count} == 1

Select Version for the Analysis Package
    [Arguments]   ${versionname}
    Click Element Through Tooltips    //md-select[@name='version']
    Click Element Through Tooltips    //md-select-menu//md-option[contains(., '${versionname}')]

Show Cite
    Click Element Through Tooltips    //button[contains(.,'Zitieren...')]
    element should contain    //div[@class='md-dialog-content']/div/p    Spreu, M. (2021). Test Analysepaket. Version: 1.0.0. Hannover: FDZ-DZHW. Datenkuratierung: Hirsch, H. & Metzger, I. M. https://doi.org/10.17889/DZHW:testanalysepaket:1.0.0
    Click Element Through Tooltips    //button[contains(.,' Schließen ')]

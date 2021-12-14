*** Settings ***
Documentation     Publisher Keyword to fill out the form
Resource          ../../resources/search_resource.robot
Resource          ../../resources/home_page_resource.robot

*** Keywords ***
Move element
    [Arguments]    ${focusElement}    ${elementName}    ${button}
    set focus to element              //${focusElement}\[@name='${elementName}']
    Click Element Through Tooltips    ${button}

Discard Changes
    Get Back to german home page
    Click Element Through Tooltips    //md-dialog//button[2]

Open Create Page
    Click Element Through Tooltips    //ui-view/descendant::button[md-icon[text()='add']]

Create new analysis package
    Click Element Through Tooltips    //md-checkbox[@name = 'analysisPackage']
    Click Element Through Tooltips    //md-tab-item[contains(.,' Status ')]
    Click Element Through Tooltips    //button[contains(.,'Neu')]

Fill out details
    Input Text    name=titleDe                Test Analysepaket
    Input Text    name=titleEn                Test Analysis Package
    Input Text    name=annotationsDe          Dieses Analysepaket wurde von Robot automatisch erstellt.
    Input Text    name=annotationsEn          This analysis package was created automatically by Robot.

Fill out description
    Input Text    name=descriptionDe          Dieses Analysepaket wurde automatisch erstellt und überprüft die Eingabe valider Eigenschaften.
    Input Text    name=descriptionEn          This analysis package was created automatically and test the input of valid attributes.

Fill out authors
    input text      name=authorsFirstName_0       Heinz
    input text      name=authorsMiddleName_0      Schelm
    input text      name=authorsLastName_0        Erhardt
    Click Element Through Tooltips                //button[contains(@aria-label,'Klicken, um eine neue Autor:in diesem Analysepaket hinzuzufügen.')]
    input text      name=authorsFirstName_1       Gilda
    input text      name=authorsMiddleName_1      nope
    input text      name=authorsLastName_1        Zanetti
    Move element    input   authorsFirstName_1    //button[@id = 'move-authors-up-button']
    Move element    input   authorsFirstName_0    //button[@id = 'move-authors-down-button']
    Click Element Through Tooltips                //edit-people-component//div[2]//button[@aria-label='Klicken, um die Autor:in aus diesem Analysepaket zu löschen.']

Fill out data curation
    input text      name=dataCuratorsFirstName_0        Allan
    input text      name=dataCuratorsMiddleName_0       Stewart
    input text      name=dataCuratorsLastName_0         Konigsberg
    Click Element Through Tooltips                      //button[contains(@aria-label,'Klicken, um eine neue Datenkurator:in diesem Analysepaket hinzuzufügen.')]
    input text      name=dataCuratorsFirstName_1        Mia
    input text      name=dataCuratorsMiddleName_1       nope
    input text      name=dataCuratorsLastName_1         Farrow
    Move element    input   dataCuratorsFirstName_1     //button[@id = 'move-dataCurators-up-button']
    Move element    input   dataCuratorsFirstName_0     //button[@id = 'move-dataCurators-down-button']
    Click Element Through Tooltips                      //edit-people-component//div[2]//button[@aria-label='Klicken, um die ausgewählte Datenkurator:in aus diesem Analysepaket zu löschen.']

Add institutions
    Click Element Through Tooltips             //button[contains(@aria-label,'Klicken, um eine weitere Institution diesem Analysepaket hinzuzufügen.')]
    Input Text      name=institutionDe_0       DZHW ${BROWSER}
    Input Text      name=institutionEn_0       DZHW ${BROWSER}
    Click Element Through Tooltips             //button[contains(@aria-label,'Klicken, um eine weitere Institution diesem Analysepaket hinzuzufügen.')]
    Input Text      name=institutionDe_1       DZHW 2 ${BROWSER}
    Input Text      name=institutionEn_1       DZHW 2 ${BROWSER}
    Move element    input   institutionDe_1    //button[@id = 'move-institution-up-button']
    Move element    input   institutionDe_0    //button[@id = 'move-institution-down-button']
    click element through tooltips             //div[2]/div/button[@aria-label='Klicken, um die Institution aus diesem Analysepaket zu entfernen.']

Add sponsors
    Click Element Through Tooltips         //button[contains(@aria-label,'Klicken, um eine weitere Geldgeber:in diesem Analysepaket hinzuzufügen.')]
    Input Text      name=sponsorDe_0       DZHW ${BROWSER}
    Input Text      name=sponsorEn_0       DZHW ${BROWSER}
    Click Element Through Tooltips         //button[contains(@aria-label,'Klicken, um eine weitere Geldgeber:in diesem Analysepaket hinzuzufügen.')]
    Input Text      name=sponsorDe_1       DZHW 2 ${BROWSER}
    Input Text      name=sponsorEn_1       DZHW 2 ${BROWSER}
    Move element    input   sponsorDe_1    //button[@id = 'move-sponsor-up-button']
    Move element    input   sponsorDe_0    //button[@id = 'move-sponsor-down-button']
    Click Element Through Tooltips         //div[2]/div/button[@aria-label='Klicken, um die Geldgeber:in aus diesem Analysepaket zu entfernen.']

Add license
    Input Text    name=license    This is free and unencumbered software released into the public domain...

Add tags
    Input Text                    //md-autocomplete[@md-search-text="tagSearchTextDe"]//input   Deutsche Tags Schlüsselwörter
    Run Keyword And Ignore Error  Click Element Through Tooltips                                //md-virtual-repeat-container//span[text()='Deutsche Tags Schlüsselwörter']
    Input Text                    //md-autocomplete[@md-search-text="tagSearchTextEn"]//input   English Days Keyword
    Run Keyword And Ignore Error  Click Element Through Tooltips                                //md-virtual-repeat-container//span[text()='English Days Keyword']

Add scripts
    Input Text                          name=scriptsTitleDe_0              DZHW ${BROWSER}
    Input Text                          name=scriptsTitleEn_0              DZHW ${BROWSER}
    set focus to element                scriptsUsedLanguage_0
    Run Keyword And Ignore Error        Click Element Through Tooltips     //md-virtual-repeat-container//span[text()='Abchasisch']
    set focus to element                scriptsSoftwarePackage_0
    Run Keyword And Ignore Error        Click Element Through Tooltips     //md-virtual-repeat-container//span[text()='R']
    input text                          scriptsSoftwarePackageVersion_0    1.0.0
    Click Element Through Tooltips      //button[contains(@aria-label,'Klicken, um ein weiteres Skript diesem Analysepaket hinzuzufügen.')]
    Input Text                          name=scriptsTitleDe_1              DZHW 2 ${BROWSER}
    Input Text                          name=scriptsTitleEn_1              DZHW 2 ${BROWSER}
    set focus to element                scriptsUsedLanguage_1
    Run Keyword And Ignore Error        Click Element Through Tooltips     //md-virtual-repeat-container//span[text()='Abchasisch']
    set focus to element                scriptsSoftwarePackage_1
    Run Keyword And Ignore Error        Click Element Through Tooltips     //md-virtual-repeat-container//span[text()='Python']
    input text                          scriptsSoftwarePackageVersion_1    4.1.9
    Click Element Through Tooltips      //md-card[@id='scripts']//div[@ng-repeat='script in $ctrl.scripts track by $index'][2]//div[@layout='column']/button[2]
    Click Element Through Tooltips      //md-card[@id='scripts']//div[@ng-repeat='script in $ctrl.scripts track by $index'][1]//div[@layout='column']/button[3]
    Click Element Through Tooltips      //md-card[@id='scripts']//div[@ng-repeat='script in $ctrl.scripts track by $index'][2]//div[@layout='column']/button[1]

Add FDHZ data package
    Click Element Through Tooltips      //button[contains(@aria-label,'Klicken, um weitere Analysedaten zu diesem Analysepaket hinzuzufügen.')]
    Click Element Through Tooltips      //list-data-package-component//md-select[1]
    Click Element Through Tooltips      //md-select-menu//md-option[contains(., 'FDZ-DZHW Datenpaket')]
    set focus to element                name=analysisDataPackagesAnalyzedDataPackage_0
    Run Keyword And Ignore Error        Click Element Through Tooltips     //md-virtual-repeat-container//span[text()='DZHW-Absolventenpanel 2005']
    Click Element Through Tooltips      //fdz-data-package-component//md-select[@name='analysisDataPackagesAnalyzedDataPackageVersion_0']
    Click Element Through Tooltips      //md-select-menu//md-option[contains(., '1.0.2')]
    Click Element Through Tooltips      //fdz-data-package-component//md-select[@name='analysisDataPackagesAnalyzedDataPackageAccessWay_0']
    Click Element Through Tooltips      //md-select-menu//md-option[contains(., 'CUF: Download')]

Add external data package
    Click Element Through Tooltips      //button[contains(@aria-label,'Klicken, um weitere Analysedaten zu diesem Analysepaket hinzuzufügen.')]
    Click Element Through Tooltips      //div[@id='package-1']//md-select
    Click Element Through Tooltips      //body/div[last()]//md-select-menu//md-option[contains(., 'Externes Datenpaket')]
    set focus to element                //input[@name='analysisDataPackagesTitleDe_1']
    input text                          name=analysisDataPackagesTitleDe_1            Titel Externes Datenpaket
    input text                          name=analysisDataPackagesTitleEn_1            Title external data package
    input text                          name=analysisDataPackagesDescriptionDe_1      Beschreibung
    input text                          name=analysisDataPackagesDescriptionEn_1      Description
    input text                          name=analysisDataPackagesAnnotationsDe_1      Anmerkung
    input text                          name=analysisDataPackagesAnnotationsEn_1      Annotation
    input text                          name=analysisDataPackagesData-sourceDe_1      Datenquelle
    input text                          name=analysisDataPackagesData-sourceEn_1      Data source
    input text                          name=analysisDataPackagesDataSourceUrl_1      http://dzhw.de
    Click Element Through Tooltips      //div[@id='package-1']//external-data-package-component//md-select
    Click Element Through Tooltips      //body/div[last()]//md-select-menu//md-option[contains(., 'Open Access')]
    set focus to element                //input[@name='analysisDataPackagesTitleDe_1']

Add custom data package
    Click Element Through Tooltips      //button[contains(@aria-label,'Klicken, um weitere Analysedaten zu diesem Analysepaket hinzuzufügen.')]
    Click Element Through Tooltips      //div[@id='package-2']//md-select
    Click Element Through Tooltips      //body/div[last()]//md-select-menu//md-option[contains(., 'Benutzerdefiniertes Datenpaket')]
    set focus to element                //input[@name='analysisDataPackagesTitleDe_2']
    input text                          name=analysisDataPackagesTitleDe_2            Titel Externes Datenpaket
    input text                          name=analysisDataPackagesTitleEn_2            Title external data package
    input text                          name=analysisDataPackagesDescriptionDe_2      Beschreibung
    input text                          name=analysisDataPackagesDescriptionEn_2      Description
    input text                          name=analysisDataPackagesAnnotationsDe_2      Anmerkung
    input text                          name=analysisDataPackagesAnnotationsEn_2      Annotation
    Click Element Through Tooltips      //div[@id='package-2']//md-select[@name='analysisDataPackagesAvailabilityType_2']
    Click Element Through Tooltips      //body/div[last()]//md-select-menu//md-option[contains(., 'verfügbar')]
    Click Element Through Tooltips      //div[@id='package-2']//md-select[@name='analysisDataPackagesAccessWay_2']
    Click Element Through Tooltips      //body/div[last()]//md-select-menu//md-option[contains(., 'Download')]
    Click Element Through Tooltips      //button[contains(@aria-label,'Klicken, um eine weitere Datenquelle diesem benutzerdefinierten Datenpaket hinzuzufügen.')]
    input text                          name=analysisDataPackagesDisplayTextDe2_0     Mensch
    input text                          name=analysisDataPackagesDisplayTextEn2_0     Human
    input text                          name=analysisDataPackagesUrl2_0               https://www.heise.de/
    Click Element Through Tooltips      //button[contains(@aria-label,'Klicken, um eine weitere Datenquelle diesem benutzerdefinierten Datenpaket hinzuzufügen.')]
    input text                          name=analysisDataPackagesDisplayTextDe2_1     Roboter
    input text                          name=analysisDataPackagesDisplayTextEn2_1     Robot
    input text                          name=analysisDataPackagesUrl2_1               https://www.tm-robot.com/de/
    Click Element Through Tooltips      //div[@id='dataSource-1']//button
    Click Element Through Tooltips      //button[contains(@aria-label,'Klicken, um eine weitere Datenquelle diesem benutzerdefinierten Datenpaket hinzuzufügen.')]
    input text                          name=analysisDataPackagesDisplayTextDe2_1     Roboter
    input text                          name=analysisDataPackagesDisplayTextEn2_1     Robot
    input text                          name=analysisDataPackagesUrl2_1               https://www.tm-robot.com/de/
    set focus to element                //input[@name='analysisDataPackagesDisplayTextDe2_1']
    Click Element Through Tooltips      //button[@id='move-link-up-button']
    Click Element Through Tooltips      //button[@id='move-link-down-button']

Move custom data package
    Click Element Through Tooltips      //*[@id="package-2"]/div/div[1]/button[2]
    Click Element Through Tooltips      //*[@id="package-1"]/div/div[1]/button[3]

Delete custom data package
    Click Element Through Tooltips      //*[@id="package-2"]/div/div[1]/button[1]

Add analysis data
    Add FDHZ data package
    Add external data package
    Add custom data package

Upload attachments
    Upload script file

Remove attachments
    Delete script file

Upload script file
    [Tags]   chromeonly
    Choose File                        //input[@type='file' and @ng-if='!$ctrl.createMode']   ${CURDIR}/upload/test.py

Delete script file
    [Tags]   chromeonly
    Click Element Through Tooltips    //button[@aria-label='Klicken, um die Datei aus diesem Skript zu löschen!']
    Click Element Through Tooltips    //md-dialog//button[2]


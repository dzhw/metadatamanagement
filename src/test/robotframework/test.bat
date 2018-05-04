ECHO OFF



start cmd /k "cd ..\..\.. & robot -P src\test\robotframework\libs -d target\test\robotframework\logs -v BROWSER:firefox .\src\test\robotframework\"

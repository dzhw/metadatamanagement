# Utilities
These NodeJS utilities were implemented to track down missing type annotations and add them automatically were possible.

## find-missing-type-annotation.js
Searches for files that lack explicit type annotations.

**Usage:**
```bash
find src/app/legacy/ -name "*.js" | node utils/find-missing-type-annotation.js
```

## type-annotation-tool-config.js
Processes input files and adds explicit type annotations to config definition.

**Usage:**
```bash
find src/app/legacy/ -name "*.js" | node utils/type-annotation-tool-config.js
```

## type-annotation-tool-controller.js
Processes input files and adds explicit type annotations to controller definition.

**Usage:**
```bash
find src/app/legacy/ -name "*.controller.js" | node utils/type-annotation-tool-controller.js
```
**Example output:**
```bash
✓ B src/app/legacy/administration/health/health.controller.js
✓ B src/app/legacy/administration/health/health.modal.controller.js
✓ B src/app/legacy/administration/logs/logs.controller.js
✓ B src/app/legacy/administration/usermanagement/edit-user.controller.js
✓ B src/app/legacy/administration/usermanagement/user-management-detail.controller.js
✓ B src/app/legacy/administration/usermanagement/user-management.controller.js
✓ B src/app/legacy/administration/usermanagement/user-message-dialog.controller.js
✓ C src/app/legacy/analysispackagemanagement/components/customdatapackage/custom-data-package.controller.js
✓ C src/app/legacy/analysispackagemanagement/components/datapackage/data-package.controller.js
✓ C src/app/legacy/analysispackagemanagement/components/externaldatapackage/external-data-package.controller.js
✓ C src/app/legacy/analysispackagemanagement/components/listdatapackage/list-data-package.controller.js
✓ C src/app/legacy/analysispackagemanagement/components/scriptsection/edit-script-section.controller.js
✓ B src/app/legacy/analysispackagemanagement/views/analysis-package-detail.controller.js
✓ B src/app/legacy/analysispackagemanagement/views/analysis-package-edit-or-create.controller.js
✓ B src/app/legacy/common/analytics/user-consent.controller.js
✓ B src/app/legacy/common/blockui/blockUI.controller.js
...
```

## type-annotation-tool-directive.js
Processes input files and adds explicit type annotations to directive definition.

**Usage:**
```bash
find src/app/legacy/ -name "*.js" | node type-annotation-tool-directive.js
```

## type-annotation-tool-factory.js
Processes input files and adds explicit type annotations to factory definition.

**Usage:**
```bash
find src/app/legacy/ -name "*.js" | node type-annotation-tool-factory.js
```

## type-annotation-tool-filter.js
Processes input files and adds explicit type annotations to filter definition.

**Usage:**
```bash
find src/app/legacy/ -name "*.js" | node type-annotation-tool-filter.js
```

## type-annotation-tool-service.js
Processes input files and adds explicit type annotations to service definition.

**Usage:**
```bash
find src/app/legacy/ -name "*.js" | node type-annotation-tool-service.js
```

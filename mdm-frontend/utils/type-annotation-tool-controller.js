const fs = require("fs");

const REGEX_A = /\.controller\(\s*'(?<name>.+)',\s*function\((?<deps>[\s\S]+?)\)\s*{[\s\S]+}\s*\)\s*\.\s*filter/gm;
const REGEX_B = /\.controller\(\s*'(?<name>.+)',\s*function\((?<deps>[\s\S]+?)\)\s*{[\s\S]+}\s*\);$/gm;
const REGEX_C = /function\s+(?<name>\w*Controller)\s*\((?<deps>[\s\S]+?)\)[\s\S]+angular\s*.\s*module\s*\(\s*'metadatamanagementApp'\s*\)\s*.controller\s*\(\s*'\w+'\s*,\s*\w+?\)\s*;/gm;
const REGEX_D = /angular\s*.\s*module\s*\(\s*'metadatamanagementApp'\s*\)\s*.controller\s*\(\s*'\w+'\s*,\s*\w+?\)\s*;\s*function\s+(?<name>\w*Controller)\s*\((?<deps>[\s\S]+?)\)/gm;

function processPatternA(filename, content) {

    const matches = [...content.matchAll(REGEX_A)];
    if (matches.length == 0) {
        return false;
    } else if (matches.length > 1) {
        throw `More than one match for file '${filename}'`;
    }

    const match = matches[0],
          name = match.groups["name"],
          deps = match.groups["deps"];
    
    const annotations = deps.split(",")
        .map(dep => dep.trim())
        .map(dep => `'${dep}'`)
        .join(",\n  ");

    let output = content.replace(
        /\.controller\(\s*'(?<name>.+)',/gm,
        `.controller('${name}', [\n  ${annotations},`
    );
    output = output.replace(/}\s*\)\s*\.\s*filter/gm, "}]).filter");
    
    fs.writeFileSync(filename, output);

    return true;
}

function processPatternB(filename, content) {

    const matches = [...content.matchAll(REGEX_B)];
    if (matches.length == 0) {
        return false;
    } else if (matches.length > 1) {
        throw `More than one match for file '${filename}'`;
    }

    const match = matches[0],
          name = match.groups["name"],
          deps = match.groups["deps"];
    
    const annotations = deps.split(",")
        .map(dep => dep.trim())
        .map(dep => `'${dep}'`)
        .join(",\n  ");

    let output = content.replace(
        /\.controller\(\s*'(?<name>.+)',/gm,
        `.controller('${name}', [\n  ${annotations},`
    );
    output = output.replace(/([\s\S]+)(}\s*\);)$/gm, "$1}]);\n");
    
    fs.writeFileSync(filename, output);

    return true;
}

function processPatternC(filename, content) {

    const matches = [...content.matchAll(REGEX_C)];
    if (matches.length == 0) {
        return false;
    } else if (matches.length > 1) {
        throw `More than one match for file '${filename}'`;
    }

    const match = matches[0],
          name = match.groups["name"],
          deps = match.groups["deps"];
    
    const annotations = deps.split(",")
        .map(dep => dep.trim())
        .map(dep => `'${dep}'`)
        .join(",\n      ");

    
    const output = content.replace(
        /(angular\s*.\s*module\s*\(\s*'metadatamanagementApp'\s*\)\s*.controller\s*\()\s*'(\w+)'\s*,\s*\w+?\)\s*;/gm,
        `$1'$2', [\n      ${annotations},\n      ${name}\n    ]);`
    );
    
    fs.writeFileSync(filename, output);

    return true;
}

function processPatternD(filename, content) {

    const matches = [...content.matchAll(REGEX_D)];
    if (matches.length == 0) {
        return false;
    } else if (matches.length > 1) {
        throw `More than one match for file '${filename}'`;
    }

    const match = matches[0],
          name = match.groups["name"],
          deps = match.groups["deps"];
    
    const annotations = deps.split(",")
        .map(dep => dep.trim())
        .map(dep => `'${dep}'`)
        .join(",\n      ");

    
    const output = content.replace(
        /(angular\s*.\s*module\s*\(\s*'metadatamanagementApp'\s*\)\s*.controller\s*\()\s*'(\w+)'\s*,\s*\w+?\)\s*;/gm,
        `$1'$2', [\n      ${annotations},\n      ${name}\n    ]);`
    );
    
    fs.writeFileSync(filename, output);

    return true;
}

function processFile(filename) {

    const content = fs.readFileSync(filename).toString();

    if (content.match(/\.controller\(\s*'.+',\s*\[/gm) ||
        content.match(/function\s+\w*Controller\s*\(\s*\)\s*{[\s\S]+angular/gm))
    {
        console.log(`- Nothing to do for '${filename}'`);
        return false;
    }

    if (processPatternA(filename, content)) {
        console.log(`✓ A ${filename}`);
    } else if (processPatternB(filename, content)) {
        console.log(`✓ B ${filename}`);
    } else if (processPatternC(filename, content)) {
        console.log(`✓ C ${filename}`);
    } else if (processPatternD(filename, content)) {
        console.log(`✓ D ${filename}`);
    } else {
        console.error(`⨯ No match for file '${filename}'`);
    }
}

function start() {
    const filenames = data.split("\n")
        .filter(filename => filename.length !== 0);
    filenames.forEach(filename => processFile(filename));
    console.log(`\nProcessed ${filenames.length} files\n`);
}

var data = "";
process.stdin.on("readable", () => {
    var chunk = null;
    while((chunk = process.stdin.read()) != null) {
        data += chunk;
    }
});
process.stdin.on("end", () => {
    start();
});

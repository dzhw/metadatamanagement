const fs = require("fs");

const REGEX = /angular\s*\.\s*module\s*\(\s*'metadatamanagementApp'\s*\)[\s\S]*?\.\s*service\s*\(\s*'(?<name>\w+)'\s*,\s*function\s*\w*\s*\((?<deps>[\s\S]*?)\)/gm;

function processFile(filename) {

    const content = fs.readFileSync(filename).toString();

    const matches = [...content.matchAll(REGEX)];
    if (matches.length == 0 || matches.length > 1) {
        return false;
    }

    const match = matches[0],
          name = match.groups["name"],
          deps = match.groups["deps"];

    if (!deps) {
        return false;
    }

    const annotations = deps.split(",")
        .map(dep => dep.trim())
        .map(dep => `'${dep}'`)
        .join(", ");
      
    let output = content.replace(
        /\.\s*service\s*\(\s*'(?<name>\w+)'\s*,/gm,
        `.service('${name}', [${annotations}, `
    );
    output = output.replace(/([\s\S]+)}\s*\)\s*;/gm, "$1}]);\n")

    fs.writeFileSync(filename, output);

    return true;
}

function start() {

    const filenames = data.split("\n")
        .filter(filename => filename.length !== 0);

    let processed = 0;
    filenames.forEach(filename => {
        if (processFile(filename)) {
            console.log(`✓ ${filename}`);
            processed++;
        }
    });
    
    console.log(`\nProcessed ${processed} files\n`);
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

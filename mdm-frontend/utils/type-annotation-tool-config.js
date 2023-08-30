const fs = require("fs");

const REGEX = /angular\s*\.\s*module\s*\(\s*'metadatamanagementApp'\s*\)\s*\.config\s*\(\s*function\s*\((?<deps>[\s\S]+?)\)[\s\S]+}\s*\);/gm;

function processFile(filename) {

    const content = fs.readFileSync(filename).toString();
    const matches = [...content.matchAll(REGEX)];
    if (matches.length == 0) {
        return false;
    } else if (matches.length > 1) {
        throw `More than one match for file '${filename}'`;
    }

    const match = matches[0],
          deps = match.groups["deps"];

    const annotations = deps.split(",")
        .map(dep => dep.trim())
        .map(dep => `'${dep}'`)
        .join(",\n  ");
  
    let output = content.replace(
        /\.config\s*\(/gm,
        `.config([\n  ${annotations},\n`
    );
    output = output.replace(/([\s\S]+)(}\s*\);)\s*/gm, `$1}]);\n`);

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

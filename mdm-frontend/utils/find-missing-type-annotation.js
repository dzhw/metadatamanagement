const fs = require("fs");

const REGEX = /\.(directive|config|controller|filter|factory)\s*\(\s*'\w+'\s*,\s*function\s*\w*\([$,\w\s]+\)/gm;

function processFile(filename) {
    const content = fs.readFileSync(filename).toString();
    const matches = [...content.matchAll(REGEX)];
    return matches.length === 1;
}

function start() {
    const filenames = data.split("\n")
        .filter(filename => filename.length !== 0);
    filenames.forEach(filename => {
        if (processFile(filename)) {
            console.log(filename);
        }
    });
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

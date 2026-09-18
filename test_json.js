const { credential } = require('firebase-admin');
const fs = require('fs');
const raw = fs.readFileSync('test_creds.txt', 'utf16le').trim();

try {
  const json = JSON.parse(raw);
  console.log("JSON is valid!");
} catch(e) {
  console.error("JSON is invalid: ", e.message);
}

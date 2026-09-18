const { Client } = require('pg');
const client = new Client({
  host: 'aws-0-us-west-2.pooler.supabase.com',
  port: 6543,
  database: 'postgres',
  user: 'postgres.jroqekmiznjsygokvxzv',
  password: 'Sa3147861166.',
  ssl: { rejectUnauthorized: false }
});
async function run() {
  await client.connect();
  const res = await client.query("SELECT correo, fcm_token FROM usuarios");
  console.log("Usuarios en la BD:");
  res.rows.forEach(row => {
    console.log("- " + row.correo + ": " + (row.fcm_token ? row.fcm_token.substring(0, 15) + "..." : "NULL"));
  });
  await client.end();
}
run().catch(console.error);

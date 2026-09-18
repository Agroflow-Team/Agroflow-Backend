const admin = require('firebase-admin');
const fs = require('fs');

const raw = fs.readFileSync('test_creds.txt', 'utf16le').trim();
const serviceAccount = JSON.parse(raw);

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const message = {
  notification: {
    title: 'Test Notification',
    body: 'Esto es una prueba local con firebase-admin'
  },
  android: {
    priority: 'high',
    notification: {
      channelId: 'agroflow_tasks_channel_v2',
      defaultVibrateTimings: true,
      defaultSound: true,
      notificationPriority: 'PRIORITY_MAX'
    }
  },
  token: 'cZ_TDgecQYq4A65AfflKBP:APA91bGsKepdSHH1eM1fBxvm23OH0xKUgK2c2BdYXTrI_8eTjgJW_dyMAduJ5SbxZmPrwqlPEZQGB53UyXvTmfa0E0-Dq8ywJlYYLbB3QxCK2E-l_Oe7VRE'
};

admin.messaging().send(message)
  .then((response) => {
    console.log('Successfully sent message:', response);
  })
  .catch((error) => {
    console.log('Error sending message:', error);
  });

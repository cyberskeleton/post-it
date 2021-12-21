importScripts("https://www.gstatic.com/firebasejs/7.16.1/firebase-app.js");
importScripts("https://www.gstatic.com/firebasejs/7.16.1/firebase-messaging.js");

// Initialize the Firebase app in the service worker by passing in the
// messagingSenderId.
firebase.initializeApp({
    apiKey: "AIzaSyCumzLjSQJ-izhVU-GuBvgEONuuBvdkLNU",
    authDomain: "post-it-notes-program.firebaseapp.com",
    projectId: "post-it-notes-program",
    storageBucket: "post-it-notes-program.appspot.com",
    messagingSenderId: "449485541524",
    appId: "1:449485541524:web:92674f84e716a18b24a481",
    measurementId: "G-15X65GEQL3"
});

if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('../firebase-messaging-sw.js')
        .then(function(registration) {
            console.log('Registration successful, scope is:', registration.scope);
        }).catch(function(err) {
        console.log('Service worker registration failed, error:', err);
    });
}

// Retrieve an instance of Firebase Messaging so that it can handle background
// messages.
const messaging = firebase.messaging();

messaging.setBackgroundMessageHandler(function(payload) {
    console.log("[firebase-messaging-sw.js] Received background message ", payload);
    // Customize notification here
    const notificationTitle = "Background Message Title";
    const notificationOptions = {
        body: "Background Message body.",
        icon: "mail.png",
    };

    return self.registration.showNotification(notificationTitle, notificationOptions);
});

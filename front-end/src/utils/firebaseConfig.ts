// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getStorage } from "firebase/storage";


// Your web app's Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyCdA5-fetJE9lXMMhLSOVLdV61VPz3lOag",
    authDomain: "thecoffeehouse-197d2.firebaseapp.com",
    projectId: "thecoffeehouse-197d2",
    storageBucket: "thecoffeehouse-197d2.appspot.com",
    messagingSenderId: "1008079078689",
    appId: "1:1008079078689:web:d38c7615b9595ef91e6a60",
    measurementId: "G-X7T2FBPX3M"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

const storage = getStorage(app);

export { storage }; // Export the storage object

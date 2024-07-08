import { ref as storageRef, uploadBytesResumable, getDownloadURL } from "firebase/storage";
import { storage } from './firebaseConfig';
import {ElMessage, ElMessageBox, ElNotification} from 'element-plus'
import store from "@/store";

export async function checkTokenAndNotify(message: string): Promise<boolean> {
    try {
        const isAuthenticated = await store.dispatch('checkTokenExpiration');

        if (!isAuthenticated) {
            ElNotification({
                title: 'Thông báo',
                message: message,
                type: 'warning',
                showClose: false,
                offset: 100
            });
            return false;
        }

        return true;
    } catch (error) {
        console.error('Error checking token expiration:', error);
        return false;
    }
}

export function formatPrice (price: number): string {
    if (typeof price !== 'number' || isNaN(price)) {
        console.error('Invalid price:', price);
        return 'N/A';
    }
    return price.toLocaleString('vi-VN') + 'đ';
}

export async function confirm(message: string, title?: string) {
    return new Promise<boolean>((resolve, reject) => {
        ElMessageBox.confirm(
            message,
            title,
            {
                confirmButtonText: 'Confirm',
                cancelButtonText: 'Cancel',
                type: 'warning',
            }
        ).then(() => {
            resolve(true); // User confirmed
        }).catch(() => {
            resolve(false); // User cancelled
        });
    });

}

export async function uploadFileToFirebaseAndGetURL(file: any, folder: string): Promise<string | null> {
    const fileRef = storageRef(storage, `${folder}/${file.name}`);
    const metadata = {
        contentType: file.type
    };

    try {
        const uploadTask = uploadBytesResumable(fileRef, file, metadata);
        // Return a promise that resolves when the upload is complete
        return new Promise((resolve, reject) => {
            uploadTask.on('state_changed',
                // eslint-disable-next-line @typescript-eslint/no-empty-function
                (snapshot) => {}, // Progress listener (optional)
                (error) => { reject(error); }, // Error handler
                async () => { // Upload completed handler
                    try {
                        const downloadURL = await getDownloadURL(uploadTask.snapshot.ref);
                        resolve(downloadURL);
                    } catch (error) {
                        reject(error);
                    }
                }
            );
        });
    } catch (error) {
        console.error('Error uploading file:', error);
        return null;
    }
}

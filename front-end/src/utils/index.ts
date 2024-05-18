import { ref as storageRef, uploadBytesResumable, getDownloadURL } from "firebase/storage";
import { storage } from './firebaseConfig';
import type { UploadUserFile } from 'element-plus'

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

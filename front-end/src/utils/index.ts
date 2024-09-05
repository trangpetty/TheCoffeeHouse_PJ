import { ref as storageRef, uploadBytesResumable, getDownloadURL } from "firebase/storage";
import { storage } from './firebaseConfig';
import {ElMessage, ElMessageBox, ElNotification} from 'element-plus'
import store from "@/store";
import moment from 'moment';

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

export const formatValueKMB = (value: number): string => {
    if (value >= 1e9) {
        return (value / 1e9).toFixed(1) + 'B';
    } else if (value >= 1e6) {
        return (value / 1e6).toFixed(1) + 'M';
    } else if (value >= 1e3) {
        return (value / 1e3).toFixed(1) + 'K';
    } else {
        return value.toString();
    }
};

// Hàm để lấy tháng hiện tại
export const getCurrentMonth = (): number => new Date().getMonth() + 1;

// Hàm để lấy tuần hiện tại trong tháng
export const getCurrentWeekInMonth = (): number => {
    const today = moment();
    const startOfMonth = moment().startOf('month');

    // Tính số tuần của ngày hôm nay trong tháng
    const weekOfMonth = today.week() - startOfMonth.week() + 1;
    return weekOfMonth;
};

// Danh sách các tháng trong năm
export const months = [
    { value: 1, label: 'January' },
    { value: 2, label: 'February' },
    { value: 3, label: 'March' },
    { value: 4, label: 'April' },
    { value: 5, label: 'May' },
    { value: 6, label: 'June' },
    { value: 7, label: 'July' },
    { value: 8, label: 'August' },
    { value: 9, label: 'September' },
    { value: 10, label: 'October' },
    { value: 11, label: 'November' },
    { value: 12, label: 'December' }
];

// Danh sách các tuần trong tháng
export const weeks = [1, 2, 3, 4, 5];


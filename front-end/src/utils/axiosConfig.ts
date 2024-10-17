import axios from 'axios';
import { API_BASE_URL } from '@/config';
import { ElMessage } from "element-plus";

const axiosClient = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Thêm token vào header request
axiosClient.interceptors.request.use((config) => {
    const token = localStorage.getItem('token'); // Đảm bảo bạn dùng 'token' hoặc 'accessToken'
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});

axiosClient.interceptors.response.use(
    response => response,
    async error => {
        const originalRequest = error.config;

        // Kiểm tra lỗi 401 và không phải do refresh token
        if (error.response && error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            try {
                const refreshToken = localStorage.getItem('refreshToken');
                if (!refreshToken) {
                    throw new Error('No refresh token available');
                }

                // Gửi yêu cầu refresh token
                const response = await axios.post('/auth/refresh', { refreshToken });

                const newToken = response.data.token;
                localStorage.setItem('token', newToken);

                originalRequest.headers['Authorization'] = `Bearer ${newToken}`;

                return axiosClient(originalRequest);
            } catch (err) {
                // Xử lý lỗi refresh token
                // ElMessage.error('Your session has expired. Please log in again.');
                localStorage.removeItem('token');
                localStorage.removeItem('refreshToken');
                return Promise.reject(err);
            }
        }

        return Promise.reject(error);
    }
);

export default axiosClient;

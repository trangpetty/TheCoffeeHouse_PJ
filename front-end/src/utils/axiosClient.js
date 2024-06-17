import axios from 'axios';

const axiosClient = axios.create({
    baseURL: 'http://localhost:8082/api',
    headers: {
        'Content-Type': 'application/json',
    },
});

axiosClient.interceptors.request.use((config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
});

axiosClient.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;
        if (error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;
            const refreshToken = localStorage.getItem('refreshToken');
            const response = await axios.post('http://localhost:8082/api/users/refresh-token', { refreshToken });
            if (response.status === 200) {
                localStorage.setItem('accessToken', response.data);
                axios.defaults.headers.common['Authorization'] = 'Bearer ' + response.data;
                return axiosClient(originalRequest);
            }
        }
        return Promise.reject(error);
    }
);

export default axiosClient;

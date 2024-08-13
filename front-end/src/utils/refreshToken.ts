// utils/refreshToken.js
import axios from 'axios';
import { API_BASE_URL } from '@/config';

export default async function refreshToken() {
    try {
        const refreshToken = localStorage.getItem('refreshToken');
        if (!refreshToken) throw new Error('No refresh token available');

        const response = await axios.post(`${API_BASE_URL}/auth/refresh`, { refreshToken });
        const newToken = response.data.token;
        localStorage.setItem('token', newToken);

        return newToken;
    } catch (error) {
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        throw error;
    }
}


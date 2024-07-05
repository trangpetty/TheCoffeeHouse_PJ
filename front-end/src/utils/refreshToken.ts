import axios from 'axios';
import store from '@/store/index';

const refreshToken = async () => {
    try {
        const response = await axios.post('http://localhost:8082/api/auth/refresh', {
            refreshToken: store.state.refreshToken,
        });
        const result = response.data;

        store.dispatch('login', { token: result.token, refreshToken: result.refreshToken, user: store.state.user });
        return result.token;
    } catch (error) {
        store.dispatch('logout');
        throw new Error('Unable to refresh token');
    }
};

export default refreshToken;

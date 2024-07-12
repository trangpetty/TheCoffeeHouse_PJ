import { createStore } from 'vuex';
import axios from 'axios';
import jwtDecode from 'jwt-decode';
import router from '@/router/index';
import * as Utils from '@/utils';

interface JwtPayload {
    exp: number; // Thời điểm hết hạn của token (timestamp)
    // Thêm các trường khác nếu cần thiết
}

interface Product {
    name: string;
    id: number;
    productSizes: any[];
    cost: number;
    quantity: number;
    toppings: any[];
    price: number;
    selectedSize: Size;
    selectedTopping: Topping;
    description: string;
    images: any[];
}

interface Size {
    id: null;
    size: string;
    surcharge: number;
}

interface Topping {
    toppingID: number | null;
    name: string;
    quantity: number;
    price: number;
}

interface State {
    cart: Product[];
    addressDialog: boolean;
    address: string;
    voucherDialog: boolean;
    voucher: object;
    errorMessage: string;
    user: object;
    token: string;
    refreshToken: string;
    selectedProduct: object;
    dialogProduct: boolean;
}

const store = createStore<State>({
    state: {
        cart: [],
        addressDialog: false,
        address: '',
        voucherDialog: false,
        voucher: {},
        errorMessage: '',
        user: {},
        token: '',
        refreshToken: '',
        selectedProduct: {},
        dialogProduct: false
    },
    mutations: {
        setCart(state, cart: Product[]) {
            state.cart = cart;
        },
        addToCart(state, product: Product) {
            const existingProduct = state.cart.find(item =>
                item.id === product.id &&
                item.selectedSize.id === product.selectedSize.id &&
                item.selectedTopping.toppingID === product.selectedTopping.toppingID &&
                (item.selectedTopping.quantity / item.quantity) === product.selectedTopping.quantity
            );

            if (existingProduct) {
                existingProduct.quantity += product.quantity;
                existingProduct.cost += product.cost;
                existingProduct.selectedTopping.quantity += product.selectedTopping.quantity;
            } else {
                state.cart.push(product);
            }
        },
        updateCart(state, payload: { product: Product, index: number }) {
            const { product, index } = payload;
            const existingProduct = state.cart[index];

            if (existingProduct) {
                if (
                    existingProduct.id === product.id &&
                    existingProduct.selectedSize.id === product.selectedSize.id &&
                    existingProduct.selectedTopping.toppingID === product.selectedTopping.toppingID
                ) {
                    existingProduct.cost = product.cost;
                    existingProduct.quantity = product.quantity;
                    existingProduct.selectedTopping.quantity = product.selectedTopping.quantity;
                } else {
                    state.cart[index] = product;
                }
            } else {
                console.error(`Product not found at index ${index}`);
            }
        },
        removeFromCart(state, index: number) {
            state.cart.splice(index, 1);
        },
        clearCart(state) {
            state.cart = [];
        },
        openAddressDialog(state, visible: boolean) {
            state.addressDialog = visible;
        },
        setAddress(state, address: string) {
            state.address = address;
        },
        clearAddress(state) {
            state.address = '';
        },
        VoucherDialog(state, visible: boolean) {
            state.voucherDialog = visible;
        },
        setVoucher(state, voucher: object) {
            state.voucher = voucher;
            state.errorMessage = '';
        },
        clearVoucher(state) {
            state.voucher = {};
            state.errorMessage = '';
        },
        setErrorMessage(state, errorMessage: string) {
            state.errorMessage = errorMessage;
        },
        clearErrorMessage(state) {
            state.errorMessage = '';
        },
        setToken(state, token: string) {
            state.token = token;
            localStorage.setItem('token', token);
            axios.defaults.headers.common['Authorization'] = 'Bearer ' + token;
        },
        setRefreshToken(state, refreshToken: string) {
            state.refreshToken = refreshToken;
            localStorage.setItem('refreshToken', refreshToken);
        },
        setUser(state, user: object) {
            state.user = user;
            localStorage.setItem('user', JSON.stringify(user));
        },
        clearAuthData(state) {
            console.log('Clearing auth data...');
            state.token = '';
            state.user = {};
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            delete axios.defaults.headers.common['Authorization'];
        },
        setSelectedProduct(state, product: object) {
            state.selectedProduct = product;
        },
        setDialogVisible(state, visible: boolean) {
            state.dialogProduct = visible;
        }
    },
    actions: {
        loadCart({ commit }) {
            try {
                const localCart = JSON.parse(localStorage.getItem('cart') || '[]');
                commit('setCart', localCart);
            } catch (e) {
                console.error('Lỗi khi parse cart từ localStorage:', e);
                commit('setCart', []);
            }
        },
        saveCart({ state }) {
            localStorage.setItem('cart', JSON.stringify(state.cart));
        },
        addProductToCart({ commit, dispatch }, product: Product) {
            commit('addToCart', product);
            dispatch('saveCart');
        },
        updateProductInCart({ commit, dispatch }, payload: { product: Product, index: number }) {
            commit('updateCart', payload);
            dispatch('saveCart');
        },
        removeProductFromCart({ commit, dispatch }, index: number) {
            commit('removeFromCart', index);
            dispatch('saveCart');
        },
        clearCart({ commit }) {
            commit('clearCart');
            localStorage.removeItem('cart');
        },
        openAddressDialog({ commit }, visible: boolean) {
            commit('openAddressDialog', visible);
        },
        loadAddress({ commit }) {
            try {
                const localAddress = JSON.parse(localStorage.getItem('address') || '""');
                commit('setAddress', localAddress);
            } catch (e) {
                console.error('Lỗi khi parse address từ localStorage:', e);
                commit('setAddress', '');
            }
        },
        saveAddress({ state }) {
            localStorage.setItem('address', JSON.stringify(state.address));
        },
        VoucherDialog({ commit }, visible: boolean) {
            commit('VoucherDialog', visible);
        },
        loadVoucher({ commit }) {
            try {
                const localVoucher = JSON.parse(localStorage.getItem('voucher') || '{}');
                commit('setVoucher', localVoucher);
            } catch (e) {
                console.error('Lỗi khi parse voucher từ localStorage:', e);
                commit('setVoucher', {});
            }
        },
        saveVoucher({ state }) {
            localStorage.setItem('voucher', JSON.stringify(state.voucher));
        },
        clearVoucher({ commit }) {
            commit('clearVoucher');
            localStorage.removeItem('voucher');
        },
        loadErrorMessageVoucher({ commit }) {
            const localErrorMessage = localStorage.getItem('errorMessage') || '';
            commit('setErrorMessage', localErrorMessage);
        },
        setErrorMessage({ commit }, errorMessage: string) {
            commit('setErrorMessage', errorMessage);
            localStorage.setItem('errorMessage', errorMessage);
        },
        clearErrorMessage({ commit }) {
            commit('clearErrorMessage');
            localStorage.removeItem('errorMessage');
        },
        login({ commit }, { token, refreshToken, user }: { token: string; refreshToken: string; user: object }) {
            commit('setToken', token);
            commit('setRefreshToken', refreshToken);
            commit('setUser', user);
        },
        logout({ commit }) {
            commit('clearAuthData');
        },
        async checkTokenExpiration({ state, commit, dispatch }) {
            const token = state.token;
            if (!token) {
                console.log('No token found. Clearing auth data.');
                commit('clearAuthData');
                return false;
            }

            try {
                const decoded: JwtPayload = jwtDecode(token);
                const currentTime = Date.now() / 1000;

                console.log('Decoded token:', decoded);

                if (decoded.exp < currentTime) {
                    try {
                        await dispatch('refreshToken');
                        return true; // Token refreshed successfully
                    } catch (error) {
                        console.error('Failed to refresh token:', error);
                        return false; // Token refresh failed
                    }
                } else {
                    console.log('Token is valid.');
                    return true;
                }
            } catch (error) {
                console.error('Error decoding token:', error);
                commit('clearAuthData');
                return false;
            }
        },
        async refreshToken({ commit, state }) {
            try {
                const response = await axios.post('http://10.30.100.178:8082/api/auth/refresh', { refreshToken: state.refreshToken });
                const { token } = response.data; // Assuming your response contains a new token

                commit('setToken', token); // Update token in Vuex store
                return token; // Optionally return the new token
            } catch (error) {
                console.error('Error refreshing token:', error);
                throw error; // Handle error appropriately
            }
        },
        loadUser({ commit }) {
            const localUser = JSON.parse(localStorage.getItem('user') || '{}');
            commit('setUser', localUser);
        },
        setProductDialog({ commit }, product: object) {
            commit('setSelectedProduct', product);
            commit('setDialogVisible', true);
        },
        openProductDialog({ commit }) {
            commit('setDialogVisible', true);
        },
        closeProductDialog({ commit }) {
            commit('setDialogVisible', false);
        },
    },
    getters: {
        cartItems: state => state.cart,
        cartTotalQuantity: state => state.cart.reduce((total, item) => total + item.quantity, 0),
        addressDialog: state => state.addressDialog,
        address: state => state.address,
        voucherDialog: state => state.voucherDialog,
        voucher: state => state.voucher,
        errorMessage: state => state.errorMessage,
        isLoggedIn: state => !!state.token,
        user: state => state.user,
    },
});

export default store;

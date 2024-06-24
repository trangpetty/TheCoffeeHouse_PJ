import { createStore } from 'vuex';
import {ref} from "vue";

interface Product {
    name: string,
    productId: number,
    productSize: Size,
    cost: number,
    quantity: number,
    topping: Topping
}

interface Size {
    sizeId: null,
    size: '',
    surcharge: 0
}

interface Topping {
    toppingID: number,
    toppingName: string,
    quantity: number
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
        refreshToken: ''
    },
    mutations: {
        setCart(state, cart: Product[]) {
            state.cart = cart;
        },
        addToCart(state, product: Product) {
            const existingProduct = state.cart.find(item =>
                item.productId === product.productId &&
                item.productSize.sizeId === product.productSize.sizeId &&
                item.topping.toppingID === product.topping.toppingID &&
               (item.topping.quantity / item.quantity ) === product.topping.quantity
            );

            if (existingProduct) {
                existingProduct.quantity += product.quantity;
                existingProduct.cost += product.cost;
                existingProduct.topping.quantity += product.topping.quantity; // Corrected typo here
            } else {
                state.cart.push(product);
            }
        },
        removeFromCart(state, index: number) {
            state.cart.splice(index, 1)
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
            state.errorMessage = ''; // Clear error message when voucher is set
        },
        clearVoucher(state) {
            state.voucher = {};
            state.errorMessage = ''; // Clear error message when voucher is cleared
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
            state.token = '';
            state.refreshToken = '';
            state.user = {};
            localStorage.removeItem('token');
            localStorage.removeItem('refreshToken');
            localStorage.removeItem('user');
        },
    },
    actions: {
        loadCart({ commit }) {
            const localCart = JSON.parse(localStorage.getItem('cart') || '[]');
            commit('setCart', localCart);
        },
        saveCart({ state }) {
            localStorage.setItem('cart', JSON.stringify(state.cart));
        },
        addProductToCart({ commit, dispatch }, product: Product) {
            commit('addToCart', product);
            dispatch('saveCart');
        },
        removeProductFromCart({ commit, dispatch }, productId: number) {
            commit('removeFromCart', productId);
            dispatch('saveCart');
        },
        clearCart({ commit }) {
            commit('clearCart');
            localStorage.removeItem('cart');
        },
        openAddressDialog({ commit, dispatch }, visible: boolean) {
            commit('openAddressDialog', visible);
        },
        loadAddress({ commit }) {
            const localAddressString = localStorage.getItem('address');
            let localAddress = '';

            if (localAddressString) {
                try {
                    localAddress = JSON.parse(localAddressString);
                } catch (e) {
                    console.error('Error parsing address from localStorage:', e);
                    localAddress = '';
                }
            }

            commit('setAddress', localAddress);
        },
        saveAddress({state}) {
            localStorage.setItem('address', JSON.stringify(state.address));
        },
        VoucherDialog({ commit, dispatch }, visible: boolean) {
            commit('VoucherDialog', visible);
        },
        loadVoucher({ commit }) {
            const localVoucherString = localStorage.getItem('voucher');
            let localVoucher = {};

            if (localVoucherString) {
                try {
                    localVoucher = JSON.parse(localVoucherString);
                } catch (e) {
                    console.error('Error parsing voucher from localStorage:', e);
                    localVoucher = {};
                }
            }

            commit('setVoucher', localVoucher);
        },
        saveVoucher({state}) {
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
        loadUser({ commit }) {
            const localUser = JSON.parse(localStorage.getItem('user') || '{}');
            commit('setUser', localUser);
        },
    },
    getters: {
        cartItems: (state) => state.cart,
        cartTotalQuantity: (state) => state.cart.reduce((total, item) => total + item.quantity, 0),
        addressDialog: (state) => state.addressDialog,
        address: (state) => state.address,
        voucherDialog: (state) => state.voucherDialog,
        voucher: (state) => state.voucher,
        errorMessage: (state) => state.errorMessage,
        isLoggedIn: (state) => !!state.token,
        user: (state) => state.user,
    },
});

export default store;

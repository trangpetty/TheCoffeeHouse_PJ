import { createStore } from 'vuex';

interface Product {
    name: string,
    id: number,
    productSizes: any[],
    cost: number,
    quantity: number,
    toppings: any[],
    price: number,
    selectedSize: Size,
    selectedTopping: Topping,
    description: string,
    images: any[]
}

interface Size {
    id: null,
    size: '',
    surcharge: 0
}

interface Topping {
    toppingID: number | null,
    name: string,
    quantity: number,
    price: number
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
    selectedProduct: object,
    dialogProduct: boolean
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
               (item.selectedTopping.quantity / item.quantity ) === product.selectedTopping.quantity
            );

            if (existingProduct) {
                existingProduct.quantity += product.quantity;
                existingProduct.cost += product.cost;
                existingProduct.selectedTopping.quantity += product.selectedTopping.quantity; // Corrected typo here
            } else {
                state.cart.push(product);
            }
        },
        updateCart(state, payload: { product: Product, index: number }) {
            const { product, index } = payload;

            const existingProduct = state.cart[index];

            if (existingProduct) {
                // Check if the product being updated matches the existing product's ID and size
                if (
                    existingProduct.id === product.id &&
                    existingProduct.selectedSize.id === product.selectedSize.id &&
                    existingProduct.selectedTopping.toppingID === product.selectedTopping.toppingID
                ) {
                    // Update the properties of the existing product
                    existingProduct.cost = product.cost;
                    existingProduct.quantity = product.quantity;
                    existingProduct.selectedTopping.quantity = product.selectedTopping.quantity;
                } else {
                    // If not a match, replace the product at the specified index
                    state.cart[index] = product;
                }
            } else {
                // If the product does not exist at the index, handle it accordingly
                console.error(`Product not found at index ${index}`);
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
        setSelectedProduct(state, product) {
            state.selectedProduct = product;
        },
        setDialogVisible(state, visible) {
            state.dialogProduct = visible;
        }
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
        updateProductInCart({ commit, dispatch }, payload: { product: Product, index: number }) {
            const { product, index } = payload;

            commit('updateCart', { product, index });
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
        setProductDialog({ commit }, product) {
            commit('setSelectedProduct', product);
            commit('setDialogVisible', true); // Action to open dialog
        },
        openProductDialog({ commit }) {
            commit('setDialogVisible', true); // Action to open dialog
        },
        closeProductDialog({ commit }) {
            commit('setDialogVisible', false); // Action to close dialog
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

// store/index.ts
import { createStore } from 'vuex';

interface Product {
    name: string,
    productId: number,
    productSize: object,
    cost: number,
    quantity: number,
    topping: object
}

interface State {
    cart: Product[];
    addressDialog: boolean;
    address: string;
}

const store = createStore<State>({
    state: {
        cart: [],
        addressDialog: false,
        address: ''
    },
    mutations: {
        setCart(state, cart: Product[]) {
            state.cart = cart;
        },
        addToCart(state, product: Product) {
            state.cart.push(product);
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
        removeProductFromCart({ commit, dispatch }, productId: number) {
            commit('removeFromCart', productId);
            dispatch('saveCart');
        },
        clearCart({ commit }) {
            commit('clearCart');
            localStorage.removeItem('cart'); // Optionally, remove cart data from localStorage
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
        }
    },
    getters: {
        cartItems: (state) => state.cart,
        cartTotalQuantity: (state) => state.cart.reduce((total, item) => total + item.quantity, 0),
        addressDialog: (state) => state.addressDialog,
        address: (state) => state.address
    },
});

export default store;

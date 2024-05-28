// store/index.ts
import { createStore } from 'vuex';

interface Product {
    name: string,
    productId: number,
    productSize: object,
    cost: number,
    quantity: number,
}

interface State {
    cart: Product[];
}

const store = createStore<State>({
    state: {
        cart: [],
    },
    mutations: {
        setCart(state, cart: Product[]) {
            state.cart = cart;
        },
        addToCart(state, product: Product) {
            state.cart.push(product);
        },
        removeFromCart(state, productId: number) {
            state.cart = state.cart.filter(item => item.productId !== productId);
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
    },
    getters: {
        cartItems: (state) => state.cart,
        cartTotalQuantity: (state) => state.cart.reduce((total, item) => total + item.quantity, 0),
    },
});

export default store;

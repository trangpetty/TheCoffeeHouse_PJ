import axios from 'axios'

const PRODUCT_API_BASE_URL = 'http://localhost:8083/products'

class ProductService {
    getProducts () {
        return axios.get(PRODUCT_API_BASE_URL)
    }

    getProduct (name, size) {
        return axios.get(`${PRODUCT_API_BASE_URL}/price?name=${name}&size=${size}`)
    }

    getProductByType (type) {
        return axios.get(`${PRODUCT_API_BASE_URL}/type?type=${type}`)
    }

    addProduct (itemData) {
        return axios.post(PRODUCT_API_BASE_URL, itemData);
    }

    updateProduct (id, itemData) {
        return axios.put(`${PRODUCT_API_BASE_URL}/${id}`, itemData);
    }

    deteleProduct (id) {
        return axios.delete(`${PRODUCT_API_BASE_URL}/${id}`)
    }
}

export default new ProductService()
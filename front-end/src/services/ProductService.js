import axios from 'axios'

const PRODUCT_API_BASE_URL = 'http://localhost:8082/products'

class ProductService {
    getProducts () {
        return axiosClient.get(PRODUCT_API_BASE_URL)
    }

    getProduct (name, size) {
        return axiosClient.get(`${PRODUCT_API_BASE_URL}/price?name=${name}&size=${size}`)
    }

    getProductByType (type) {
        return axiosClient.get(`${PRODUCT_API_BASE_URL}/type?type=${type}`)
    }

    addProduct (itemData) {
        return axiosClient.post(PRODUCT_API_BASE_URL, itemData);
    }

    updateProduct (id, itemData) {
        return axiosClient.put(`${PRODUCT_API_BASE_URL}/${id}`, itemData);
    }

    deteleProduct (id) {
        return axiosClient.delete(`${PRODUCT_API_BASE_URL}/${id}`)
    }
}

export default new ProductService()
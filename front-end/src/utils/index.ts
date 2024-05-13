import Axios from 'axios';
import {Codes} from './Codes';


export async function axiosAction(method: string, url: string, data?: any): Promise<any> {
    return new Promise((resolve, reject) => {
        Axios({
            method: method,
            url: url,
            data: data
        }).then(function (response) {
            // console.log(response.data);
            resolve(response.data);
        }).catch(function (error) {
            console.log(error);
            reject(error);
        })
    });
}

export async function doPost(context: any, url: string, data?: any) {
    try {
        // let resp = await context.$http.post(url, data);
        // let result = resp.body;
        console.log(data)
        const result = await axiosAction('POST', url, data);
        const code = result.code;
        // if (code && (code == Codes.INVALID_ACCESS_TOKEN.code || code == Codes.EXPIRED_TOKEN.code)) { // Token
        //     localStorage.removeItem("TOKEN");
        //     localStorage.removeItem("user");
        //     await confirmRelogin(String(("common.session-expired-message")));
        //     context.$router.push({path: login});
        //     // return {success: false, message: ""};
        //     return;
        // }
        return result;
    } catch (e) {
        console.log(e);
        const ret = {success: false, message: 'Failed to execute POST.'};
        // if (e.status)
        //     ret.message = ret.message + "HTTP return code：" + e.status;
        // return ret;
    }
}

export async function doGet(context: any, url: string) {
    try {
        // let resp = await context.$http.get(url);
        // let result = resp.body;
        const result = await axiosAction('GET', url);
        const code = result.code;
        // if (localStorage.getItem("TOKEN") === null) {
        //     context.$router.push({path: login});
        //     return;
        // }
        // if (code && (code == Codes.INVALID_ACCESS_TOKEN.code || code == Codes.EXPIRED_TOKEN.code)) { // Token
        //     localStorage.removeItem("TOKEN");
        //     localStorage.removeItem("user");
        //     await confirmRelogin(String(("common.session-expired-message")));
        //     context.$router.push({path: login});
        //     // return {success: false, message: ""};
        //     return;
        // }
        return result;
    } catch (e) {
        console.log(e);
        const ret = {success: false, message: 'Failed to execute GET.'};
        // if (e.status)
        //     ret.message = ret.message + "HTT return code：" + e.status;
        // return ret;
    }
}

export async function doDelete(context: any, url: string, data?: any) {
    try {
        // let resp = await context.$http.delete(url, data);
        // let result = resp.body;
        const result = await axiosAction('DELETE', url, data);
        const code = result.code;
        // if (code && (code == Codes.INVALID_ACCESS_TOKEN.code || code == Codes.EXPIRED_TOKEN.code)) { // Token
        //     localStorage.removeItem("TOKEN");
        //     localStorage.removeItem("user");
        //     await confirmRelogin(String(("common.session-expired-message")));
        //     context.$router.push({path: login});
        //     // return {success: false, message: ""};
        //     return;
        // }
        return result;
    } catch (e) {
        console.log(e);
        const ret = {success: false, message: 'Failed to execute DELETE'};
        // if (e.status)
        //     ret.message = ret.message + "HTTP return code：" + e.status;
        // return ret;
    }
}

export async function doPut(context: any, url: string, data?: any) {
    try {
        // let resp = await context.$http.put(url, data);
        // let result = resp.body;
        const result = await axiosAction('PUT', url, data);
        const code = result.code;
        // if (code && (code == Codes.INVALID_ACCESS_TOKEN.code || code == Codes.EXPIRED_TOKEN.code)) { // Token
        //     localStorage.removeItem("TOKEN");
        //     localStorage.removeItem("user");
        //     await confirmRelogin(String(("common.session-expired-message")));
        //     context.$router.push({path: login});
        //     // return {success: false, message: ""};
        //     return;
        // }
        return result;
    } catch (e) {
        console.log(e);
        const ret = {success: false, message: 'Failed to execute PUT.'};
        // if (e.status)
        //     ret.message = ret.message + "HTTP return code：" + e.status;
        // return ret;
    }
}


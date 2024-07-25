package com.example.thecoffeehouse.Utils;

public class VnPayConfig {
    private void init(AppConfig appConfig) {
        String serverAddress = AppConfig.getServerAddress();
        vnp_ReturnUrl = "http://" + serverAddress + ":8082/api/payment/vnpay/result";
    }


    //    public static String vnp_ReturnUrl = "http://localhost:8081/#/check-bill/";
    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_TmnCode = "L3CVFABV";
    public static String vnp_Version = "2.1.0";
    public static String vnp_Command = "pay";
    public static String secretKey = "LGI5BEW1Z2PH6OIPBQONED11IS2WHS3I";
    public static String vnp_ApiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";
//    public static final String vnp_ReturnUrl = "http://" + serverAddress + ":8082/api/payment/vnpay/result";
    public static String vnp_ReturnUrl;
}

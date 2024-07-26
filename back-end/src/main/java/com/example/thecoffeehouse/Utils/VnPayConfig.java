package com.example.thecoffeehouse.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VnPayConfig {
    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_TmnCode = "L3CVFABV";
    public static String vnp_Version = "2.1.0";
    public static String vnp_Command = "pay";
    public static String secretKey = "LGI5BEW1Z2PH6OIPBQONED11IS2WHS3I";
    public static String vnp_ApiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";
    public static String vnp_ReturnUrl;

    @Autowired
    public VnPayConfig(AppConfig appConfig) {
        init(appConfig);
    }

    private void init(AppConfig appConfig) {
        String serverAddress = appConfig.getServerAddress();
        String serverPort = appConfig.getServerPort();
        vnp_ReturnUrl = "http://" + serverAddress + ":" + serverPort + "/api/payment/vnpay/result";
    }
}

package com.example.thecoffeehouse.Utils;

import java.util.*;

public class VnPayConfig {
    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_ReturnUrl = "http://localhost:8081/#/check-bill/";
    public static String vnp_TmnCode = "L3CVFABV";
    public static String vnp_Version = "2.1.0";
    public static String vnp_Command = "pay";
    public static String secretKey = "LGI5BEW1Z2PH6OIPBQONED11IS2WHS3I";
    public static String vnp_ApiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}

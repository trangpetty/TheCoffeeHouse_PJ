package com.example.thecoffeehouse.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MomoConfig {
    public static final String SECRET_KEY = "at67qH6mk8w5Y1nAyMoYKMWACiEi2bsa";
    public static final String PARTNER_CODE = "MOMOBKUN20180529";
    public static final String ACCESS_KEY = "klm05TvNBzhg7h7j";
    public static final String ENDPOINT = "https://test-payment.momo.vn/v2/gateway/api/create";
    public static final String ORDER_INFO = "Thanh toán qua MoMo";
    public static String IPN_URL;
    public static String REDIRECT_URL;

    @Autowired
    public MomoConfig(AppConfig appConfig) {
        init(appConfig);
    }

    private void init(AppConfig appConfig) {
        String serverAddress = appConfig.getServerAddress();
        String serverPort = appConfig.getServerPort();
        IPN_URL = "http://" + serverAddress + ":" + serverPort + "/api/payment/momo/result";
        REDIRECT_URL = "http://" + serverAddress + ":" + serverPort + "/api/payment/momo/result";
    }
}

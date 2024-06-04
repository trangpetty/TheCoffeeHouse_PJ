package com.example.thecoffeehouse.controller.bill;

import com.example.thecoffeehouse.Utils.HmacUtil;
import com.example.thecoffeehouse.Utils.MomoConfig;
import com.example.thecoffeehouse.Utils.VnPayConfig;
import com.example.thecoffeehouse.dto.BillDto;
import com.example.thecoffeehouse.dto.PaymentResDTO;
import com.example.thecoffeehouse.dto.TestAmountDto;
import com.example.thecoffeehouse.service.bill.BillService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final BillService billService;

    public PaymentController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping("/momo")
    public ResponseEntity<?> queryTransaction(@RequestBody TestAmountDto amountDto) throws Exception {
        String orderId = String.valueOf(System.currentTimeMillis());
        String requestId = String.valueOf(System.currentTimeMillis());
        String requestType = "captureWallet";
        String extraData = "";

        String rawHash = String.format("accessKey=%s&amount=%s&extraData=%s&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
                MomoConfig.ACCESS_KEY, amountDto.getAmount(), extraData, MomoConfig.IPN_URL, orderId, MomoConfig.ORDER_INFO, MomoConfig.PARTNER_CODE, MomoConfig.REDIRECT_URL, requestId, requestType);

        String signature = HmacUtil.hmacSHA256(MomoConfig.SECRET_KEY, rawHash);

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("partnerCode", MomoConfig.PARTNER_CODE);
        requestData.put("partnerName", "Test");
        requestData.put("storeId", "MomoTestStore");
        requestData.put("requestId", requestId);
        requestData.put("amount", amountDto.getAmount());
        requestData.put("orderId", orderId);
        requestData.put("orderInfo", MomoConfig.ORDER_INFO);
        requestData.put("redirectUrl", MomoConfig.REDIRECT_URL);
        requestData.put("ipnUrl", MomoConfig.IPN_URL);
        requestData.put("lang", "vi");
        requestData.put("extraData", extraData);
        requestData.put("requestType", requestType);
        requestData.put("signature", signature);
        requestData.put("orderExpireTime", 5);

        String response = execPostRequest(MomoConfig.ENDPOINT, requestData);

        // Parse the response JSON and extract the payment URL
        Map<String, Object> responseBody = new ObjectMapper().readValue(response, new TypeReference<>() {
        });
        String paymentUrl = (String) responseBody.get("payUrl");

        PaymentResDTO paymentResDTO = new PaymentResDTO();
        paymentResDTO.setStatus("OK");
        paymentResDTO.setMessage("success");
        paymentResDTO.setURL(paymentUrl);

        return new ResponseEntity<>(paymentResDTO, HttpStatus.OK);
    }

    private String execPostRequest(String url, Map<String, Object> requestData) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = new ObjectMapper().writeValueAsBytes(requestData);
            os.write(input, 0, input.length);
        }

        int responseCode = con.getResponseCode();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }


    @PostMapping("/cash")
    public ResponseEntity<BillDto> payWithCash(@RequestParam Long billId, @RequestParam String cashTransactionId) {
        BillDto updatedBill = billService.payWithCash(billId, cashTransactionId);
        return ResponseEntity.ok(updatedBill);
    }

    @PostMapping("/vnpay")
    public ResponseEntity<?> payWithVnPay(@RequestBody TestAmountDto amountDto) throws UnsupportedEncodingException {

        long amount = amountDto.getAmount()*100;
        String orderType = "other";

        String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VnPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VnPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderType", orderType);


        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = HmacUtil.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;

        PaymentResDTO paymentResDTO = new PaymentResDTO();
        paymentResDTO.setStatus("OK");
        paymentResDTO.setMessage("success");
        paymentResDTO.setURL(paymentUrl);

        return new ResponseEntity<>(paymentResDTO, HttpStatus.OK);
    }

    @PutMapping("/markDelivered")
    public ResponseEntity<BillDto> markAsDelivered(@RequestParam Long orderId) {
        BillDto updatedBill = billService.markAsDelivered(orderId);
        return ResponseEntity.ok(updatedBill);
    }

}

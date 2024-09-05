package com.example.thecoffeehouse.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    // Khi client gửi tin nhắn tới "/app/newBill", tin nhắn sẽ được xử lý tại đây
    @MessageMapping("/newBill")
    @SendTo("/topic/bills")
    public String notifyNewBill(String bill) {
        // Trả về thông báo cho các admin đã subscribe topic /topic/bills
        return bill; // Bạn có thể gửi thông tin hóa đơn ở đây
    }
}

// src/utils/websocket.ts
import SockJS from 'sockjs-client';
import { Client, IMessage } from '@stomp/stompjs';

const socket = new SockJS(`http://10.30.100.178:8082/ws`);
const stompClient = new Client({
    brokerURL: 'ws://10.30.100.178:8082/ws',
    connectHeaders: {},
    webSocketFactory: () => socket,
    debug: (msg) => console.log(new Date(), msg),
    onConnect: (frame) => {
        console.log('Connected: ' + frame);
    },
    onStompError: (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
    },
});

export const connectWebSocket = (onMessage: (message: IMessage) => void) => {
    stompClient.onConnect = (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/statusUpdates', (message) => {
            onMessage(message);
        });
    };
    stompClient.activate();
};

export const sendMessage = (destination: string, body: any) => {
    if (stompClient.connected) {
        stompClient.publish({ destination, body: JSON.stringify(body) });
    } else {
        console.error('WebSocket connection is not open');
    }
};

export default stompClient;

// src/utils/websocket.ts
import SockJS from 'sockjs-client';
import { Client, IMessage } from '@stomp/stompjs';

const socket = new SockJS(`https://3caf-2401-d800-21f1-9bde-ddcb-3d6e-76a4-f8e2.ngrok-free.app/ws`);
const stompClient = new Client({
    brokerURL: 'ws://3caf-2401-d800-21f1-9bde-ddcb-3d6e-76a4-f8e2.ngrok-free.app/ws',
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

import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

// WebSocket connection setup
const stompClient = new Client({
    brokerURL: 'ws://3caf-2401-d800-21f1-9bde-ddcb-3d6e-76a4-f8e2.ngrok-free.app/ws',
    connectHeaders: {},
    reconnectDelay: 5000,  // Auto reconnect after 5 seconds if disconnected
    debug: (msg) => console.log(new Date(), msg),
    onConnect: (frame) => {
        console.log('Connected: ' + frame);
    },
    onStompError: (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
    },
    webSocketFactory: () => new SockJS('https://3caf-2401-d800-21f1-9bde-ddcb-3d6e-76a4-f8e2.ngrok-free.app/ws') // Use SockJS as a fallback
});

// Function to connect WebSocket and handle incoming messages
export const connectWebSocket = (subscriptions: { [key: string]: (message: IMessage) => void }) => {
    stompClient.onConnect = (frame) => {
        console.log('Connected: ' + frame);

        // Subscribe to all topics
        for (const [topic, handler] of Object.entries(subscriptions)) {
            stompClient.subscribe(topic, (message) => {
                handler(message);
            });
        }
    };

    stompClient.activate();
};

// Function to send messages to the server
export const sendMessage = (destination: string, body: any) => {
    if (stompClient.connected) {
        stompClient.publish({ destination, body: JSON.stringify(body) });
    } else {
        console.error('WebSocket connection is not open');
    }
};

export default stompClient;

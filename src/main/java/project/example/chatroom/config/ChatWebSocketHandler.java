package project.example.chatroom.config;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.stereotype.Component;
import project.example.chatroom.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Firestore firestore;
    private final ObjectMapper objectMapper;

    public ChatWebSocketHandler(Firestore firestore) {
        this.firestore = firestore;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Parse the incoming message
        Message chatMessage = objectMapper.readValue(message.getPayload(), Message.class);

        // Validate the messageId
        if (chatMessage.getMessageId() == null || chatMessage.getMessageId().isEmpty()) {
            session.sendMessage(new TextMessage("Error: messageId must not be null or empty"));
            return;
        }

        // Save the message to Firestore
        WriteResult writeResult = firestore.collection("messages").document(chatMessage.getMessageId()).set(chatMessage).get();

        // Send a response back to the WebSocket client
        session.sendMessage(new TextMessage("Message received and saved: " + message.getPayload()));
    }
}
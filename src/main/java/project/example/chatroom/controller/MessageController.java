package project.example.chatroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import project.example.chatroom.model.Message;
import project.example.chatroom.service.ChatroomService;

@Controller
public class MessageController {
    @Autowired
    private ChatroomService chatroomService;

    @MessageMapping("/chatroom/{chatroomId}/sendMessage")
    @SendTo("/topic/chatroom/{chatroomId}")
    public Message sendMessage(@DestinationVariable String chatroomId, Message message) {
        try {
            return chatroomService.sendMessage(chatroomId, message.getSender(), message.getContent());
        } catch (Exception e) {
            throw new RuntimeException("Error sending message");
        }
    }

    @MessageMapping("/chatroom/{chatroomId}/typing")
    @SendTo("/topic/chatroom/{chatroomId}/typing")
    public String userTyping(@DestinationVariable String chatroomId, String username) {
        return username + " is typing...";
    }
}

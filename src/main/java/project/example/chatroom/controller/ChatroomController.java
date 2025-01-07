package project.example.chatroom.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.example.chatroom.model.Chatroom;
import project.example.chatroom.model.Message;
import project.example.chatroom.model.User;
import project.example.chatroom.service.ChatroomService;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/chatrooms")
public class ChatroomController {

    private static final Logger logger = LoggerFactory.getLogger(ChatroomController.class);


    @Autowired
    private ChatroomService chatroomService;

    @PostMapping("/{chatroomId}/join")
    public ResponseEntity<?> joinChatroom(@PathVariable String chatroomId, @RequestBody String username) {
        try {
            logger.debug("Received request to join chatroomId: {}", chatroomId);

            Chatroom chatroom = chatroomService.joinChatroom(chatroomId, username);
            return ResponseEntity.ok(chatroom);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chatroom or user not found");
        }
    }

    @PostMapping("/{chatroomId}/messages")
    public ResponseEntity<?> sendMessage(@PathVariable String chatroomId, @RequestBody Message message) {
        try {
            logger.debug("Received request to send message for chatroom: {}", chatroomId);

            Message savedMessage = chatroomService.sendMessage(chatroomId, message.getSender(), message.getContent());
            return ResponseEntity.ok(savedMessage);
        } catch (RuntimeException | ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while sending the message");
        }
    }


    @GetMapping("/{chatroomId}/history")
    public ResponseEntity<?> getChatHistory(@PathVariable String chatroomId) {
        try {
            List<Message> chatHistory = chatroomService.getChatHistory(chatroomId);
            return ResponseEntity.ok(chatHistory);
        } catch (RuntimeException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving the chat history");
        }
    }

    @PostMapping("/{chatroomId}/leave")
    public ResponseEntity<?> leaveChatroom(@PathVariable String chatroomId, @RequestBody String username) {
        try {
            chatroomService.leaveChatroom(chatroomId, username);
            return ResponseEntity.ok().build();
        } catch (RuntimeException | ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while leaving the chatroom");


        }

    }
}

package project.example.chatroom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String messageId;
    private String chatroomId;
    private String sender;
    private String content;
    private String timestamp;

}

package project.example.chatroom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chatroom {

    private String chatroomId;
    private String name;
    private List<String> participants;
    private List<Message> messageHistory;

}

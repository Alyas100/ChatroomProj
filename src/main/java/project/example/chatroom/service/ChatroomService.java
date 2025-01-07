package project.example.chatroom.service;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.example.chatroom.model.Chatroom;
import project.example.chatroom.model.Message;
import project.example.chatroom.model.User;


import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ChatroomService {

    private final Firestore db;

    @Autowired
    public ChatroomService(FirebaseApp firebaseApp) {
        this.db = FirestoreClient.getFirestore(firebaseApp);
    }

    public Chatroom joinChatroom(String chatroomId, String username) throws ExecutionException, InterruptedException {

        DocumentReference docRef = db.collection("chatrooms").document(chatroomId);
        DocumentSnapshot document = docRef.get().get();

        if (document.exists()) {
            Chatroom chatroom = document.toObject(Chatroom.class);
            chatroom.getParticipants().add(username);
            docRef.set(chatroom);
            return chatroom;
        } else {
            throw new RuntimeException("Chatroom not found");
        }
    }

    public Message sendMessage(String chatroomId, String username, String content) throws ExecutionException, InterruptedException {

        DocumentReference chatroomRef = db.collection("chatrooms").document(chatroomId);
        DocumentSnapshot chatroomSnap = chatroomRef.get().get();

        if (chatroomSnap.exists()) {
            Chatroom chatroom = chatroomSnap.toObject(Chatroom.class);
            Message message = new Message();
            message.setChatroomId(chatroomId);
            message.setSender(username);
            message.setContent(content);
            message.setTimestamp(String.valueOf(LocalDateTime.now()));
            chatroom.getMessageHistory().add(message);
            chatroomRef.set(chatroom);
            return message;
        } else {
            throw new RuntimeException("Chatroom not found");
        }
    }

    public List<Message> getChatHistory(String chatroomId) throws ExecutionException, InterruptedException {

        DocumentReference docRef = db.collection("chatrooms").document(chatroomId);
        DocumentSnapshot document = docRef.get().get();

        if (document.exists()) {
            Chatroom chatroom = document.toObject(Chatroom.class);
            return chatroom.getMessageHistory();
        } else {
            throw new RuntimeException("Chatroom not found");
        }
    }

    public void leaveChatroom(String chatroomId, String username) throws ExecutionException, InterruptedException {

        DocumentReference docRef = db.collection("chatrooms").document(chatroomId);
        DocumentSnapshot document = docRef.get().get();

        if (document.exists()) {
            Chatroom chatroom = document.toObject(Chatroom.class);
            chatroom.getParticipants().remove(username);
            docRef.set(chatroom);
        } else {
            throw new RuntimeException("Chatroom not found");
        }
    }
}

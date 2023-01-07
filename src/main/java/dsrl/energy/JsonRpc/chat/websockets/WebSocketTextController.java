package dsrl.energy.JsonRpc.chat.websockets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcBasicServer;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
public class WebSocketTextController {


    final SimpMessagingTemplate template;

    final ChattingService chattingService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public WebSocketTextController(SimpMessagingTemplate template, ChattingService chattingService) {
        this.template = template;
        this.chattingService = chattingService;
    }

    @MessageMapping("/user/{userId}/message")
    @SendTo("/topic/{userId}")
    public JsonNode sendMessage(@Payload String request, @DestinationVariable String userId) throws IOException {
        val jsonRpcServer = new JsonRpcBasicServer(chattingService);
        OutputStream outputStream = new ByteArrayOutputStream();
        jsonRpcServer.handleRequest(new ByteArrayInputStream(request.getBytes()), outputStream);
        return objectMapper.readTree(outputStream.toString());
    }

    @MessageMapping("/user/{userId}/typing")
    @SendTo("/topic/{userId}/typing")
    public JsonNode typingMessage(@Payload String request, @DestinationVariable String userId) throws IOException {
        val jsonRpcServer = new JsonRpcBasicServer(chattingService);
        OutputStream outputStream = new ByteArrayOutputStream();
        jsonRpcServer.handleRequest(new ByteArrayInputStream(request.getBytes()), outputStream);
        return objectMapper.readTree(outputStream.toString());
    }

    @MessageMapping("/user/{userId}/readMessages")
    @SendTo("/topic/{userId}/readMessages")
    public JsonNode readMessages(@Payload String request, @DestinationVariable String userId) throws IOException {
        val jsonRpcServer = new JsonRpcBasicServer(chattingService);
        OutputStream outputStream = new ByteArrayOutputStream();
        jsonRpcServer.handleRequest(new ByteArrayInputStream(request.getBytes()), outputStream);
        return objectMapper.readTree(outputStream.toString());
    }

    @MessageMapping("/user/commonMessage")
    @SendTo("/topic/commonMessage")
    public JsonNode commonMessages(@Payload String request) throws IOException {
        val jsonRpcServer = new JsonRpcBasicServer(chattingService);
        OutputStream outputStream = new ByteArrayOutputStream();
        jsonRpcServer.handleRequest(new ByteArrayInputStream(request.getBytes()), outputStream);
        return objectMapper.readTree(outputStream.toString());
    }

    @MessageMapping("/user/commonChatTyping")
    @SendTo("/topic/commonChatTyping")
    public JsonNode commonChatTyping(@Payload String request) throws IOException {
        val jsonRpcServer = new JsonRpcBasicServer(chattingService);
        OutputStream outputStream = new ByteArrayOutputStream();
        jsonRpcServer.handleRequest(new ByteArrayInputStream(request.getBytes()), outputStream);
        return objectMapper.readTree(outputStream.toString());
    }


}
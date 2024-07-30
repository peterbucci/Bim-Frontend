package edu.bhcc.bim.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.bhcc.bim.model.Conversation;
import edu.bhcc.bim.util.HttpClientHelper;

import java.util.List;

public class ConversationService {

    private static final String BASE_URL = "http://localhost:8080/conversations/";

    public static List<Conversation> getConversations(int userId) throws Exception {
        String response = HttpClientHelper.sendGetRequest(BASE_URL + userId);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response,
                mapper.getTypeFactory().constructCollectionType(List.class, Conversation.class));
    }

}

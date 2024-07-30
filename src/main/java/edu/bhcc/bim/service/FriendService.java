package edu.bhcc.bim.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.bhcc.bim.model.User;
import edu.bhcc.bim.state.AppState;
import edu.bhcc.bim.util.HttpClientHelper;

import java.util.List;

public class FriendService {
    private static final String HOST = AppState.HOST;
    private static final String PORT = AppState.PORT;
    private static final String BASE_URL = "http://" + HOST + ":" + PORT + "/friendships/";

    public static List<User> getFriendsWithStatus(int userId) throws Exception {
        String response = HttpClientHelper.sendGetRequest(BASE_URL + userId);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, User.class));
    }
}

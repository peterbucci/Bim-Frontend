package edu.bhcc.bim.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.bhcc.bim.model.User;
import edu.bhcc.bim.state.AppState;
import edu.bhcc.bim.util.HttpClientHelper;

public class AuthService {
    private static final String HOST = AppState.HOST;
    private static final String PORT = AppState.PORT;
    private static final String BASE_URL = "http://" + HOST + ":" + PORT + "/users";

    public static User authenticateOrCreateUser(String username, String password) throws Exception {
        // Check if the user exists
        String userUrl = BASE_URL + "/authenticate?username=" + username + "&password=" + password;
        String response;
        try {
            response = HttpClientHelper.sendGetRequest(userUrl);
        } catch (Exception e) {
            // If user does not exist, create a new user
            String createUserUrl = BASE_URL + "/add";
            response = HttpClientHelper.sendPostRequest(createUserUrl,
                    "username=" + username + "&passwordHash=" + password + "&email=" + username + "@example.com");
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response, User.class);
    }
}

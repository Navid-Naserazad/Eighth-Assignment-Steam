package Shared;

import org.json.*;

import java.util.UUID;

public class Request {
    private JSONObject jsonObject;
    public Request(String request) {
        this.jsonObject = new JSONObject();
        jsonObject.put("Request",request);
    }

    public void infoAboutAGame(String title) {
        jsonObject.put("infoAboutASpecificGame", title);
    }
    public void downloadAGame(String title, String username) {
        jsonObject.put("downloadAGame", title);
        jsonObject.put("username", username);
    }
    public void createAccount(String username, String password, String dateOfBirth) {
        JSONObject json = new JSONObject();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        json.put("id", id);
        json.put("username", username);
        json.put("password", password);
        json.put("dateOfBirth", dateOfBirth);
        jsonObject.put("createAccount", json);
    }
    public void logIn(String username, String password) {
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", password);
        jsonObject.put("logIn", json);
    }
    public String done() {
        return jsonObject.toString();
    }
}

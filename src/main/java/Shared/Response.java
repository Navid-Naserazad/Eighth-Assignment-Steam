package Shared;

import org.json.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Response {
    private JSONObject jsonObject;
    public Response(String response) {
        jsonObject = new JSONObject();
        jsonObject.put("Response",response);
    }
    public void aListOfAvailableGames(ResultSet resultSet) {
        JSONArray titles = new JSONArray();
        try {
            while (resultSet.next()) {
                String title = resultSet.getString(2) ;
                titles.put(title);
            }
            jsonObject.put("aListOfAvailableGames", titles);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void infoAboutAGame(ResultSet resultSet) {
        JSONObject game = new JSONObject();
        try {
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String title = resultSet.getString(2);
                String developer = resultSet.getString(3);
                String genre = resultSet.getString(4);
                double price = resultSet.getDouble(5);
                int releaseYear = resultSet.getInt(6);
                boolean controllerSupport = resultSet.getBoolean(7);
                int reviews = resultSet.getInt(8);
                int size = resultSet.getInt(9);
                String filePath = resultSet.getString(10);
                game.put("id", id);
                game.put("title", title);
                game.put("developer", developer);
                game.put("genre", genre);
                game.put("price", price);
                game.put("release year", releaseYear);
                game.put("controller support", controllerSupport);
                game.put("reviews", reviews);
                game.put("size", size);
                game.put("file path", filePath);
                jsonObject.put("infoAboutASpecificGame", game);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void downloadAGame(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String filePath = resultSet.getString(10);
            jsonObject.put("downloadAGame", filePath);
        }
    }
    public void createAccount(Boolean existance) {
        jsonObject.put("createAccount", existance);
    }
    public void logIn(Boolean check) {
        jsonObject.put("logIn", check);
    }
//    public void logOut() {
//        jsonObject.put("logOut", true);
//    }
    public String done() {
        return jsonObject.toString();
    }
}

package Server;

import Shared.Response;
import jdk.nashorn.internal.runtime.ECMAException;
import org.json.JSONObject;
import org.json.JSONPropertyIgnore;
import org.mindrot.jbcrypt.BCrypt;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerMain {
    public static void main(String[] args) {
        serverRun();

    }

    public static void serverRun() {
        String url = "jdbc:postgresql://localhost:5432/steam";
        String user = "postgres";
        String pass = "nav1382id0805";
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            System.out.println("Connected to the database!");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM games");
            int rowCount = 0;
            while (resultSet.next()) {
                rowCount++;
                break;
            }
            if (rowCount == 0) {
                fileToDataBase(statement);
            }
            try {
                ServerSocket serverSocket = new ServerSocket(1234);
                while (true) {
                    System.out.println("Waiting for the client request ...");
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected!");
                    DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

                    while (true) {
                        String jsonTextInput = dataInputStream.readUTF();
                        JSONObject jsObj = new JSONObject(jsonTextInput);
                        String request = jsObj.getString("Request");
                        if (request.equalsIgnoreCase("aListOfAvailableGames")) {
                            Response response = new Response("aListOfAvailableGames");
                            resultSet = statement.executeQuery("SELECT * FROM games");
                            response.aListOfAvailableGames(resultSet);
                            String jsonTextOutput = response.done();
                            dataOutputStream.writeUTF(jsonTextOutput);
                            dataOutputStream.flush();
                        }
                        else if (request.equalsIgnoreCase("infoAboutASpecificGame")) {
                            Response response = new Response("infoAboutASpecificGame");
                            String title = jsObj.getString("infoAboutASpecificGame");
                            resultSet = statement.executeQuery("SELECT * FROM games WHERE title = '" + title + "';");
                            response.infoAboutAGame(resultSet);
                            String jsonTextOutput = response.done();
                            dataOutputStream.writeUTF(jsonTextOutput);
                            dataOutputStream.flush();
                        }
                        else if (request.equalsIgnoreCase("downloadAGame")) {
                            Response response = new Response("downloadAGame");
                            String title = jsObj.getString("downloadAGame");
                            resultSet = statement.executeQuery("SELECT * FROM games WHERE title = '" + title + "';");
                            response.downloadAGame(resultSet);
                            String jsonTextOutput = response.done();
                            dataOutputStream.writeUTF(jsonTextOutput);
                            dataOutputStream.flush();
                            String username = jsObj.getString("username");
                            resultSet = statement.executeQuery("SELECT * FROM accounts WHERE username ='" + username + "' ;");
                            String accountID = "";
                            while (resultSet.next()) {
                                accountID = resultSet.getString(1);
                            }
                            String gameID = "";
                            resultSet = statement.executeQuery("SELECT * FROM games WHERE title = '" + title + "';");
                            while (resultSet.next()) {
                                gameID = resultSet.getString(1);
                            }
                            resultSet = statement.executeQuery("SELECT * FROM downloads WHERE account_id = '"
                                    + accountID + "' AND game_id = '" + gameID + "';");
                            int downloadCount = 1;
                            if (resultSet.next()) {
                                downloadCount = resultSet.getInt(3);
                                downloadCount++;
                               statement.executeUpdate("UPDATE downloads SET download_count = " + downloadCount
                                        + " WHERE account_id = '" + accountID + "' AND game_id = '" + gameID + "';");
                            }
                            else {
                                statement.executeUpdate("INSERT INTO downloads VALUES ('" + accountID
                                        + "', '" + gameID + "', " + downloadCount + ")");
                            }
                        }
                        else if (request.equalsIgnoreCase("createAccount")) {
                            Response response = new Response("createAccount");
                            String id = jsObj.getJSONObject("createAccount").getString("id");
                            String clinetUsername = jsObj.getJSONObject("createAccount").getString("username");
                            String clinetPassword = jsObj.getJSONObject("createAccount").getString(("password"));
                            // hashing the clinet password!
                            String hashedPassword = BCrypt.hashpw(clinetPassword, BCrypt.gensalt());
                            String clinetDOB = jsObj.getJSONObject("createAccount").getString(("dateOfBirth"));
                            resultSet = statement.executeQuery("SELECT * FROM accounts WHERE username ='" + clinetUsername + "' ;");
                            rowCount = 0;
                            while (resultSet.next()) {
                                rowCount++;
                                break;
                            }
                            if (rowCount == 0) {
                                response.createAccount(false);
                                statement.executeUpdate("INSERT INTO accounts VALUES ( '" + id + "', '" + clinetUsername +
                                        "', '" + hashedPassword + "', '" + clinetDOB + "' );");
                            }
                            else {
                                response.createAccount(true);
                            }
                            String jsonTextOutput = response.done();
                            dataOutputStream.writeUTF(jsonTextOutput);
                            dataOutputStream.flush();
                        }
                        else if (request.equalsIgnoreCase("logIn")) {
                            Response response = new Response("logIn");
                            String clinetUsername = jsObj.getJSONObject("logIn").getString("username");
                            String clinetPassword = jsObj.getJSONObject("logIn").getString(("password"));
                            resultSet = statement.executeQuery("SELECT * FROM accounts WHERE username = '" + clinetUsername + "' ;");
                            rowCount = 0;
                            while (resultSet.next()) {
                                rowCount++;
                                break;
                            }
                            if (rowCount == 0) {
                                response.logIn(false);
                            }
                            else {
                                if (BCrypt.checkpw(clinetPassword, resultSet.getString("password"))) {
                                    response.logIn(true);
                                }
                                else {
                                    response.logIn(false);
                                }
                            }
                            String jsonTextOutput = response.done();
                            dataOutputStream.writeUTF(jsonTextOutput);
                            dataOutputStream.flush();
                        }
                        else if (request.equalsIgnoreCase("logOut")) {
                            System.out.println("The client is disconnected!");
                            dataInputStream.close();
                            dataOutputStream.close();
                            clientSocket.close();
                            break;
                        }
                    }
                }
                // serverSocket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            statement.close();
            connection.close();
            System.out.println("Connection Closed....");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void fileToDataBase(Statement statement) throws SQLException {
        try {
            File directoryPath = new File("D:\\SBU\\Term 2\\AP\\Assignments\\Eighth-Assignment-Steam\\src\\main\\java\\Server\\Resources");
            File filesList[] = directoryPath.listFiles();
            Scanner sc = null;
            for(int i=1; i<filesList.length; i++) {
                sc= new Scanner(filesList[i]);
                ArrayList<String> fileInput = new ArrayList<>();
                while (sc.hasNextLine()) {
                    fileInput.add(sc.nextLine());
                }
                String gameID = fileInput.get(0);
                String title = fileInput.get(1);
                String developer = fileInput.get(2);
                String genre = fileInput.get(3);
                double price = Double.parseDouble(fileInput.get(4));
                int releaseYear = Integer.parseInt(fileInput.get(5));
                boolean controllerSupport = Boolean.parseBoolean(fileInput.get(6));
                int reviews = Integer.parseInt(fileInput.get(7));
                int size = Integer.parseInt(fileInput.get(8));
                String fileName = filesList[i].getName();
                String filePath = "D:\\SBU\\Term 2\\AP\\Assignments\\Eighth-Assignment-Steam\\src\\main\\java\\Server\\images" + "\\" + fileName.replace("txt","png");
                statement.executeUpdate("INSERT INTO Games VALUES ( '" + gameID + "', '" + title + "', '"
                        + developer + "', '" + genre + "', " + price + ", " + releaseYear + ", " + controllerSupport + ", "
                        + reviews + ", " + size + ", '" + filePath + "' );");
            }
            sc.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

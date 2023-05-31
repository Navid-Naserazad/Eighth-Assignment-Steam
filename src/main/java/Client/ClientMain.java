package Client;

import Shared.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.xml.ws.Response;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.net.*;
import java.util.UUID;

public class ClientMain {
    public static void main(String[] args) {
        clientRun();
    }
    public static void clientRun() {
        try {
            Socket clientSocket = new Socket("localhost", 1234);
            DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());

            runMenu(dataOutputStream, dataInputStream);

            dataOutputStream.close();
            dataInputStream.close();
            clientSocket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void runMenu(DataOutputStream dataOutputStream, DataInputStream dataInputStream) throws Exception {
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to Steam!");
        System.out.println("1. Create account");
        System.out.println("2. Sign in");
        System.out.println("Enter your command : ");
        System.out.println("Command : ");
        String command = in.nextLine();
        if (command.equals("1") || command.equalsIgnoreCase("Create account"))
        {
            createAccount(dataOutputStream, dataInputStream);
        } else if (command.equals("2") || command.equalsIgnoreCase("Sign in"))
        {
            signIn(dataOutputStream, dataInputStream);
        }
        else {
            System.out.println("Wrong command!");
            System.out.println("Try again!");
            runMenu(dataOutputStream, dataInputStream);
        }
    }

    public static void createAccount(DataOutputStream dataOutputStream, DataInputStream dataInputStream) throws Exception {
        Scanner in = new Scanner(System.in);
        Request request = new Request("createAccount");
        System.out.println("Enter your username");
        System.out.println("Username : ");
        String username = in.nextLine();
        System.out.println("Enter your password");
        System.out.println("Password : ");
        String password = in.nextLine();
        System.out.println("Enter your date of birth");
        System.out.println("Date of birth : ");
        String dateOfBirth = in.nextLine();
        request.createAccount(username, password, dateOfBirth);
        String jsonTextOutput = request.done();
        dataOutputStream.writeUTF(jsonTextOutput);
        dataOutputStream.flush();

        String jsonTextInput = dataInputStream.readUTF();
        JSONObject jsObj = new JSONObject(jsonTextInput);
        if (!jsObj.getBoolean("createAccount")) {
            System.out.println("Congratulations!");
            System.out.println("You have created an account!");
            signInMenu(username, dataOutputStream, dataInputStream);
        }
        else {
            System.out.println("There is an existing account with this username!");
            System.out.println("Try to create an account with a different username!");
            runMenu(dataOutputStream, dataInputStream);
        }

    }

    public static void signIn(DataOutputStream dataOutputStream, DataInputStream dataInputStream) throws Exception {
        Scanner in = new Scanner(System.in);
        Request request = new Request("logIn");
        System.out.println("Enter your username");
        System.out.println("Username : ");
        String username = in.nextLine();
        System.out.println("Enter your password");
        System.out.println("Password : ");
        String password = in.nextLine();
        request.logIn(username, password);
        String jsonTextOutput = request.done();
        dataOutputStream.writeUTF(jsonTextOutput);
        dataOutputStream.flush();

        String jsonTextInput = dataInputStream.readUTF();
        JSONObject jsObj = new JSONObject(jsonTextInput);
        if (jsObj.getBoolean("logIn")) {
            signInMenu(username, dataOutputStream, dataInputStream);
        }
        else {
            System.out.println("The username or the password is wrong!");
            System.out.println("Try again!");
            runMenu(dataOutputStream, dataInputStream);
        }
    }

    public static void signInMenu(String username, DataOutputStream dataOutputStream, DataInputStream dataInputStream) throws IOException{
        Scanner in = new Scanner(System.in);
        Request request;
        System.out.println("Welcome!");
        while (true) {
            System.out.println("1. A list of available games");
            System.out.println("2. Info about a specific game");
            System.out.println("3. Download a game");
            System.out.println("4. Log out");
            System.out.println("Enter your command : ");
            System.out.println("Command : ");
            String command = in.nextLine();
            if (command.equals("1") || command.equalsIgnoreCase("A list of available games")) {
                request = new Request("aListOfAvailableGames");
                String jsonTextOutput = request.done();
                dataOutputStream.writeUTF(jsonTextOutput);
                dataOutputStream.flush();

                String jsonTextInput = dataInputStream.readUTF();
                JSONObject jsObj = new JSONObject(jsonTextInput);
                JSONArray titles = jsObj.getJSONArray("aListOfAvailableGames");
                System.out.println(titles);
            }
            else if (command.equals("2") || command.equalsIgnoreCase("Info about a specific game")) {
                request = new Request("infoAboutASpecificGame");
                System.out.println("Enter the title of game : ");
                System.out.println("Name :");
                String title = in.nextLine();
                request.infoAboutAGame(title);
                String jsonTextOutput = request.done();
                dataOutputStream.writeUTF(jsonTextOutput);
                dataOutputStream.flush();

                String jsonTextInput = dataInputStream.readUTF();
                JSONObject jsObj = new JSONObject(jsonTextInput);
                JSONObject specificGame = jsObj.getJSONObject("infoAboutASpecificGame");
                System.out.println(specificGame);
            }
            else if (command.equals("3") || command.equalsIgnoreCase("Download a game")) {
                request = new Request("downloadAGame");
                System.out.println("Enter the title of game : ");
                System.out.println("Name :");
                String title = in.nextLine();
                request.downloadAGame(title, username);
                String jsonTextOutput = request.done();
                dataOutputStream.writeUTF(jsonTextOutput);
                dataOutputStream.flush();

                String jsonTextInput = dataInputStream.readUTF();
                JSONObject jsObj = new JSONObject(jsonTextInput);
                String filePath = jsObj.getString("downloadAGame");
                String fileName = "";
                for (int i = 81; i < filePath.length(); i++) {
                    fileName = fileName + filePath.charAt(i);
                }
                File file = new File("D:\\SBU\\Term 2\\AP\\Assignments\\Eighth-Assignment-Steam\\src\\main\\java\\Client\\Downloads\\" + fileName);
                if (!file.exists())
                {
                    FileInputStream fis = null;
                    FileOutputStream fos = null;
                    try {
                        fis = new FileInputStream(filePath);
                        fos = new FileOutputStream("D:\\SBU\\Term 2\\AP\\Assignments\\Eighth-Assignment-Steam\\src\\main\\java\\Client\\Downloads\\" + fileName);
                        int check;
                        while ((check = fis.read()) != -1) {
                            fos.write(check);
                        }
                    }
                    finally {
                        if (fis != null) {
                            fis.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    }
                    System.out.println("You have downloaded : " + title);
                }
                else {
                    System.out.println("You have already downloaded : " + title);
                }

            }
            else if (command.equals("4") || command.equalsIgnoreCase("Log out")) {
                request = new Request("logOut");
                String jsonTextOutput = request.done();
                dataOutputStream.writeUTF(jsonTextOutput);
                dataOutputStream.flush();
                break;
            }
            else {
                System.out.println("Wrong command!");
                System.out.println("Try again!");
                signInMenu(username, dataOutputStream, dataInputStream);
            }
        }
    }
}

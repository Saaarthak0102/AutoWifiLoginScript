import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Login {

    private static final String LOGIN_URL = "http://172.16.68.6:8090/login.xml";
    private static final String LOGIN_MODE = "191";
    private static final String USERNAME = "";//ENTER YOUR USERNAME IN THE DOUBLE QUOTES "" 
    private static final String PASSWORD = "";//ENTER YOUR PASSWORD IN THE DOUBLE QUOTES ""
    //That is it!! Now run the file(only after you've entered your username and password correctly)
    private static final String A = String.valueOf(System.currentTimeMillis());
    private static final String PRODUCTTYPE = "0";
    
    public static void main(String[] args) {
        try {
            authenticate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    private static void authenticate() throws Exception {
        URI uri = new URI(LOGIN_URL);
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        StringBuilder urlParameters = new StringBuilder();
        urlParameters.append("mode=").append(LOGIN_MODE)
                     .append("&username=").append(USERNAME)
                     .append("&a=").append(A)
                     .append("&producttype=").append(PRODUCTTYPE)
                     .append("&password=").append(PASSWORD);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = urlParameters.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        String responseMessage = "Response Code: " + responseCode;
        System.out.println(responseMessage);

        String notificationMessage = "logged in";
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("You are signed in as")) {
                    notificationMessage = "Logged in successfully!";
                } else if (line.contains("Your data transfer has been exceeded, Please contact the administrator")) {
                    notificationMessage = "Data transfer exceeded, unable to log in.";
                } else if (line.contains("Login failed")) {
                    notificationMessage = "Login failed. Please try again.";
                }
            }
        } else {
            notificationMessage = "Failed to connect. Please check your connection.";
        }
        displayNotification(notificationMessage);
    }

    private static void displayNotification(String message) {
        try {
            if (SystemTray.isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();
                TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage("icon.png"), "WiFi Authenticator");
                trayIcon.setImageAutoSize(true);
                trayIcon.setToolTip("WiFi Authenticator Status");
                tray.add(trayIcon);

                trayIcon.displayMessage("WiFi Authenticator", message, MessageType.INFO);
            } else {
                System.out.println("System tray not supported on this platform.");
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}

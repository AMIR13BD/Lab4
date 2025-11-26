package labapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UsersRepo {

    private final List<User> users = new ArrayList<>();

    public UsersRepo() {
        loadUsers();
    }

    private void loadUsers() {
        try {
            InputStream is = getClass().getResourceAsStream("/Users.txt");
            if (is == null) {
                System.out.println("Users.txt not found!");
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // email + space + password   (like lab1/lab2)
                String[] parts = line.split("\\s+");
                if (parts.length == 2) {
                    String email = parts[0];
                    String password = parts[1];
                    users.add(new User(email, password));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkUser(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public User findByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }
}

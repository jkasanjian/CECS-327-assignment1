package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Interface that allows manipulation of Profile json objects
 */
public class ProfileAccounts {

    private final String FILE_NAME = "profiles.json";

    private List<Profile> profiles;

    public ProfileAccounts() {
        loadProfiles();
    }

    public void deleteProfile(String username) {
        profiles.removeIf(u -> (u.getUsername().equals(username)));
        saveProfiles(profiles);
    }

    public boolean contains(String username) {
        for(Profile profile : profiles) {
            if(profile.getUsername().equalsIgnoreCase(username))
                return true;
        }
        return false;
    }

    public boolean verify(String username, String password) {
        if(!contains(username)) return false;
        for (Profile profile: profiles) {
            if (profile.getUsername().equalsIgnoreCase(username)) {
                if (profile.getPassword().equals(password))
                    return true;
                else
                    return false;
            }
        }
        return false;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public boolean addProfile(Profile profile) {
        if (contains(profile.getUsername())) {
            return false;
        }
        Gson gson = new Gson();
        File file = new File(FILE_NAME);
        String currJson = "";
        String newJson;
        if(file.exists() && file.length() > 0) {
            try {
                Scanner sc = new Scanner(file);
                while(sc.hasNextLine())
                    currJson += sc.nextLine();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        try {
            List<Profile> oldList;
            List<Profile> newList = new ArrayList<>();
            if(file.exists() && file.length() > 0) {
                oldList = gson.fromJson(currJson, new TypeToken<List<Profile>>(){}.getType());
                newList.addAll(oldList);
            }
            newList.add(profile);
            newJson = gson.toJson(newList);
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.print(newJson);
            printWriter.close();
            profiles = newList;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return true;
    }

    protected void saveProfiles(List<Profile> profiles) {
        Gson gson = new Gson();
        File file = new File(FILE_NAME);
        assert profiles != null;
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        printWriter.print(gson.toJson(profiles));
        printWriter.close();
    }

    protected void loadProfiles() {
        Gson gson = new Gson();
        File file = new File(FILE_NAME);
        if( !file.exists() ) {
            profiles = new ArrayList<>();
            return;
        }
        try {
            Scanner sc = new Scanner(file);
            String jsonString = "";
            while(sc.hasNextLine()) {
                jsonString += sc.nextLine();
            }
            profiles = gson.fromJson(jsonString, new TypeToken<List<Profile>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}

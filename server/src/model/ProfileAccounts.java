package model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Interface that allows manipulation of ProfileAccount json objects
 */
public class ProfileAccounts {

    private final String FILE_NAME = "profiles.json";

    private List<ProfileAccount> profileAccounts;

    public ProfileAccounts() {
        loadProfiles();
    }


    /**
     * Delete profile with the passed in username in profiles.json
     * @param username
     */
    public boolean deleteProfile(String username) {
        boolean res = profileAccounts.removeIf(u -> (u.getUsername().equals(username)));
        saveProfiles(profileAccounts);
        return res;
    }

    /**
     * Checks if the username exists in profile.json
     * @param username
     * @return
     */
    public boolean contains(String username) {
        for(ProfileAccount profileAccount : profileAccounts) {
            if(profileAccount.getUsername().equalsIgnoreCase(username))
                return true;
        }
        return false;
    }

    /**
     * Verifies if the username exists in profiles.json, then verifies if the password
     * matches the user password in profiles.json
     * @param username
     * @param password
     * @return true if verified false otherwise
     */
    public boolean verify(String username, String password) {
        if(!contains(username)) return false;
        for (ProfileAccount profileAccount : profileAccounts) {
            if (profileAccount.getUsername().equalsIgnoreCase(username)) {
                if (profileAccount.getPassword().equals(password))
                    return true;
                else
                    return false;
            }
        }
        return false;
    }

    public List<ProfileAccount> getProfileAccounts() {
        return profileAccounts;
    }

    /**
     * Adds a ProfileAccount object to profiles.json
     * @param profileAccount object to be added to profiles.json
     * @return true if object is successfully added otherwise false
     */
    public boolean addProfile(ProfileAccount profileAccount) {
        if (contains(profileAccount.getUsername())) {
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
            List<ProfileAccount> oldList;
            List<ProfileAccount> newList = new ArrayList<>();
            if(file.exists() && file.length() > 0) {
                oldList = gson.fromJson(currJson, new TypeToken<List<ProfileAccount>>(){}.getType());
                newList.addAll(oldList);
            }
            newList.add(profileAccount);
            newJson = gson.toJson(newList);
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.print(newJson);
            printWriter.close();
            profileAccounts = newList;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return true;
    }

    /**
     * This method will save and overwrite the current profiles.json file with param profileAccounts
     * @param profileAccounts profile accounts that contains information to write profiles.json
     */
    protected void saveProfiles(List<ProfileAccount> profileAccounts) {
        Gson gson = new Gson();
        File file = new File(FILE_NAME);
        assert profileAccounts != null;
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        printWriter.print(gson.toJson(profileAccounts));
        printWriter.close();
    }

    /**
     * Syncs the profile account with current profiles.json
     * @param profile profile account to be synced
     */
    public void sync(ProfileAccount profile) {
        for (int i = 0; i < profileAccounts.size(); i++) {
            if (profileAccounts.get(i).getUsername().equals(profile.getUsername())) {
                profileAccounts.set(i, profile);
                break;
            }
        }
        saveProfiles(profileAccounts);
    }

    /**
     * Syncs the object's data with profiles.json
     */
    protected void loadProfiles() {
        Gson gson = new Gson();
        File file = new File(FILE_NAME);
        if( !file.exists() ) {
            profileAccounts = new ArrayList<>();
            return;
        }
        try {
            Scanner sc = new Scanner(file);
            String jsonString = "";
            while(sc.hasNextLine()) {
                jsonString += sc.nextLine();
            }
            profileAccounts = gson.fromJson(jsonString, new TypeToken<List<ProfileAccount>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public List<Playlist> getPlaylist(String username) {
        for (ProfileAccount profileAccount : profileAccounts) {
            if (profileAccount.getUsername().equals(username)) {
                return profileAccount.getPlaylists();
            }
        }
        return null;
    }

    public ProfileAccount getProfile(String username) {
        for (ProfileAccount profileAccount : profileAccounts) {
            if(profileAccount.getUsername().equals(username)) {
                return profileAccount;
            }
        }
        return null;
    }
}

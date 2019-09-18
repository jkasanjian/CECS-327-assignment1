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
 * Interface that allows manipulation of ProfileAccount json objects
 */
public class ProfileAccounts {

    private final String FILE_NAME = "profiles.json";

    private List<ProfileAccount> profileAccounts;

    public ProfileAccounts() {
        loadProfiles();
    }

    public void deleteProfile(String username) {
        profileAccounts.removeIf(u -> (u.getUsername().equals(username)));
        saveProfiles(profileAccounts);
    }

    public boolean contains(String username) {
        for(ProfileAccount profileAccount : profileAccounts) {
            if(profileAccount.getUsername().equalsIgnoreCase(username))
                return true;
        }
        return false;
    }

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
}

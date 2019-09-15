package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Profiles {

    private final String FILE_NAME = "profiles.json";

    private List<Profile> profiles;

    public Profiles() {
        loadProfiles();
    }

    public Profiles(Profile profile) {
        addProfile(profile);
    }

    public void deleteProfile(String username) {
        profiles.removeIf(u -> (u.getUsername().equals(username)));
        saveProfiles(profiles);
    }

    public void addProfile(Profile profile) {
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
    }

    public void saveProfiles(List<Profile> profiles) {
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

    public void loadProfiles() {
        Gson gson = new Gson();
        File file = new File(FILE_NAME);
        assert file.exists();
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

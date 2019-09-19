package model;

public class Profile extends ProfileAccount {

    public Profile(String user, String password) {
        super(user, password);
    }

    private static final Profile profile = new Profile("", "");

    public static Profile GetInstance() {
        return profile;
    }
}

package model;

public class Profiles extends ProfileAccounts {

    private static final Profiles profiles = new Profiles();

    public static Profiles GetInstance() {
        return profiles;
    }
}

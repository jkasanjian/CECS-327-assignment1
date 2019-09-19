package model;

public class SingletonProfiles extends ProfileAccounts {

    private static final SingletonProfiles SINGLETON_PROFILES = new SingletonProfiles();

    public static SingletonProfiles GetInstance() {
        return SINGLETON_PROFILES;
    }
}

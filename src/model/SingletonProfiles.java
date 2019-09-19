package model;

public class SingletonProfiles extends ProfileAccounts {

    private static final SingletonProfiles SINGLETON_PROFILES = new SingletonProfiles();

    /**
     * @return instance of ProfileAccounts which directly interfaces with profiles.json
     */
    public static SingletonProfiles GetInstance() {
        return SINGLETON_PROFILES;
    }
}

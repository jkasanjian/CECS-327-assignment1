package model;

public class SingletonProfile extends ProfileAccount {

    private static final SingletonProfile SINGLETON_PROFILE = new SingletonProfile();

    /**
     * @return instance of the logged in user
     */
    public static SingletonProfile GetInstance() {
        return SINGLETON_PROFILE;
    }
}

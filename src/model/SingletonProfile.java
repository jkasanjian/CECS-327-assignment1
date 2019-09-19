package model;

public class SingletonProfile extends ProfileAccount {

    public SingletonProfile(String user, String password) {
        super(user, password);
    }

    private static final SingletonProfile SINGLETON_PROFILE = new SingletonProfile("", "");

    public static SingletonProfile GetInstance() {
        return SINGLETON_PROFILE;
    }
}

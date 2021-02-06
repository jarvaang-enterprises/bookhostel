package lan.tmsystem.bookhostel.data;

import java.util.concurrent.Future;

public class DataManager {
    private static volatile DataManager instance;
    public boolean loggedIn = false;
    public String userName = "";
    private DataManager(){};

    public static DataManager getInstance() {
        if(instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void authUser(String uemail, String upasswd, boolean isStudent) {
        userName = uemail;
        loggedIn = true;
    }

    public void logOut(){
        userName = "";
        loggedIn = false;
    }
}

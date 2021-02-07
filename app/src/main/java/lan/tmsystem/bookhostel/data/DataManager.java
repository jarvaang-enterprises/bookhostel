package lan.tmsystem.bookhostel.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DataManager {
    private static volatile DataManager instance;
    public FirebaseAuth mAuth;
    public boolean loggedIn = false;
    public String userName = "";
    private FirebaseUser mCurrentUser;
    private UserModel user;

    private DataManager(){};

    public static DataManager getInstance() {
        if(instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void logOut(){
        mAuth.signOut();
        loggedIn = false;
    }

    public FirebaseUser getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentUser(FirebaseUser currentUser) {
        mCurrentUser = currentUser;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}

package lan.tmsystem.bookhostel.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class DataManager {
    private static volatile DataManager instance;
    private FirebaseAuth mAuth;
    public boolean loggedIn = false;
    public String userName = "";
    private FirebaseUser mCurrentUser;
    private UserModel user;
    private FirebaseFirestore mDb;
    private FirebaseStorage mStorage;

    // Create a storage reference from our app
    private StorageReference storageRef;
    private StorageReference mHostelImagesRef;
    private StorageReference mUserImagesRef;

    private DataManager(){};

    public static DataManager getInstance() {
        if(instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void logOut(){
        getAuth().signOut();
        mCurrentUser = null;
        user = null;
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

    public void updateUserLocation(Context mContext, double latitude, double longitude) {
        user.updateLocation(latitude, longitude);
        Map<String, Object> userData = new HashMap<>();
        userData.put("location", new GeoPoint(latitude, longitude));
        final DocumentReference[] uDocument = new DocumentReference[1];
        mDb.collection("users")
                .whereEqualTo("userId", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            uDocument[0] = task.getResult().getDocuments().get(0).getReference();
                            uDocument[0].update(userData)
                                    .addOnCompleteListener(task1 -> {
                                        if(task1.isSuccessful()) {
                                            user.updateLocation(latitude, longitude);
                                            Toast.makeText(mContext, "Location updated!", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(mContext, "Location update failed, Something went wrong.\n Try Again!", Toast.LENGTH_LONG).show();
                                            Log.e("Location update", task1.getException().getMessage());
                                        }
                                    });
                        }
                    }
                });

    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public void setAuth(FirebaseAuth auth) {
        mAuth = auth;
    }

    public FirebaseFirestore getDb() {
        return mDb;
    }

    public void setDb(FirebaseFirestore db) {
        mDb = db;
    }

    public FirebaseStorage getStorage() {
        return mStorage;
    }

    public void setStorage(FirebaseStorage storage) {
        mStorage = storage;
    }

    public StorageReference getStorageRef() {
        return storageRef;
    }

    public void setStorageRef(StorageReference storageRef) {
        this.storageRef = storageRef;
    }

    public StorageReference getHostelImagesRef() {
        return mHostelImagesRef;
    }

    public void setHostelImagesRef(StorageReference hostelImagesRef) {
        mHostelImagesRef = hostelImagesRef;
    }

    public StorageReference getUserImagesRef() {
        return mUserImagesRef;
    }

    public void setUserImagesRef(StorageReference userImagesRef) {
        mUserImagesRef = userImagesRef;
    }
}

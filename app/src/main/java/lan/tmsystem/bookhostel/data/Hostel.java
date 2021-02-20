package lan.tmsystem.bookhostel.data;

import com.google.firebase.firestore.GeoPoint;

public class Hostel {
    private String mName;
    private GeoPoint mLocation;
    private String userId;

    public Hostel(){};
    public Hostel(String name, GeoPoint location, String userId){
        this.setName(name);
        this.setLocation(location);
        this.setUserId(userId);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public GeoPoint getLocation() {
        return mLocation;
    }

    public void setLocation(GeoPoint location) {
        mLocation = location;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

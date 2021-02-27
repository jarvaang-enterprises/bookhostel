package lan.tmsystem.bookhostel.data;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class Hostel {
    private String mName;
    private GeoPoint mLocation;
    private String userId;
    private String price;
    private List<HostelRoom> rooms;
    private String numSingles;
    private String numDoubles;

    public Hostel(){};
    public Hostel(String name, String price, String numSingles, String numDoubles, GeoPoint location, String userId){
        this.setName(name);
        this.setLocation(location);
        this.setUserId(userId);
        List<HostelRoom> r = new ArrayList<HostelRoom>();
        this.setRooms(r);
        this.setPrice(price);
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

    public List<HostelRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<HostelRoom> rooms) {
        this.rooms = rooms;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

class HostelRoom {
    public HostelRoom(){}
}

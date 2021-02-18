package lan.tmsystem.bookhostel.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserModel implements Parcelable {
    private final String displayName;
    private final String email;
    private final Uri photoUrl;
    private final boolean emailVerified;
    private final String uid;
    private final String firstName;
    private final String lastName;
    private final String country;
    private final String city;
    private final boolean manager;
    private final String hostel;
    private final String phoneNumber;
    private GeoPoint location;

    public void updateLocation(double lat, double lng){
        this.location = new GeoPoint(lat, lng);
    }

    public UserModel(@NotNull FirebaseUser user, DocumentSnapshot document) {
        this.displayName = user.getDisplayName();
        this.email = user.getEmail();
        this.photoUrl = user.getPhotoUrl();
        this.emailVerified = user.isEmailVerified();
        this.uid = user.getUid();
        this.firstName = Objects.requireNonNull(document.get("firstName")).toString();
        this.lastName = Objects.requireNonNull(document.get("lastName")).toString();
        this.country = Objects.requireNonNull(document.get("country")).toString();
        this.city = Objects.requireNonNull(document.get("city")).toString();
        this.manager = document.get("manager") != null && Objects.equals(document.get("manager"), true);
        if(this.isManager()) {
            this.hostel = Objects.requireNonNull(document.get("hostel")).toString();
        } else this.hostel = null;
        this.phoneNumber = Objects.requireNonNull(document.get("phoneNumber")).toString();
        this.location = document.getGeoPoint("location");
    }

    protected UserModel(Parcel in) {
        displayName = in.readString();
        email = in.readString();
        photoUrl = in.readParcelable(Uri.class.getClassLoader());
        emailVerified = in.readByte() != 0;
        uid = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        country = in.readString();
        city = in.readString();
        manager = in.readByte() != 0;
        hostel = in.readString();
        phoneNumber = in.readString();
        List<Double> loc = new ArrayList<>();
        in.readList(loc, getClass().getClassLoader());
        location = new GeoPoint(loc.get(0), loc.get(1));
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getDisplayName());
        dest.writeString(getEmail());
        dest.writeParcelable(getPhotoUrl(), flags);
        dest.writeByte((byte) (isEmailVerified() ? 1 : 0));
        dest.writeString(getUid());
        dest.writeString(getFirstName());
        dest.writeString(getLastName());
        dest.writeString(getCountry());
        dest.writeString(getCity());
        dest.writeByte((byte) (isManager() ? 1 : 0));
        dest.writeString(getHostel());
        dest.writeString(getPhoneNumber());
        List<Double> loc = new ArrayList<>();
        loc.add(getLocation().getLatitude());
        loc.add(getLocation().getLongitude());
        dest.writeList(loc);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public String getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public boolean isManager() {
        return manager;
    }

    public String getHostel() {
        return hostel;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public GeoPoint getLocation() {
        return location;
    }
}

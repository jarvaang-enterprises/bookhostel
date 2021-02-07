package lan.tmsystem.bookhostel.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;

public class UserModel implements Parcelable {
    private final String name;
    private final String email;
    private final Uri photoUrl;
    private final boolean emailVerified;
    private final String uid;


    public UserModel(FirebaseUser user) {
        this.name = user.getDisplayName();
        this.email = user.getEmail();
        this.photoUrl = user.getPhotoUrl();
        this.emailVerified = user.isEmailVerified();
        this.uid = user.getUid();
    }

    protected UserModel(Parcel in) {
        name = in.readString();
        email = in.readString();
        photoUrl = in.readParcelable(Uri.class.getClassLoader());
        emailVerified = in.readByte() != 0;
        uid = in.readString();
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

    public String getName() {
        return name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeParcelable(photoUrl, flags);
        dest.writeByte((byte) (emailVerified ? 1 : 0));
        dest.writeString(uid);
    }
}

package com.example.administrador.myapplication.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.administrador.myapplication.models.persistence.UsersRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by Rodrigo on 01/06/2015.
 */
public class User implements Parcelable {

    private String mUserId;
    private String mPassword;

    public User() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User that = (User) o;

        if (mUserId != null ? !mUserId.equals(that.mUserId) : that.mUserId != null) return false;
        return !(mPassword != null ? !mPassword.equals(that.mPassword) : that.mPassword != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mUserId != null ? mUserId.hashCode() : 0;
        result = 31 * result + (mPassword != null ? mPassword.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "\"userId\":" + mUserId +
                ", \"password\": \"" + mPassword + '\"' +
                "}";
    }

    public static List<User> getAll() {
        return UsersRepository.getInstance().getAll();
    }

    public static List<User> findAll(User user) {
        return UsersRepository.getInstance().findAll(user);
    }

    public void save() {
        UsersRepository.getInstance().save(this);
    }

    public void delete() {
        UsersRepository.getInstance().delete(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mUserId);
        dest.writeString(this.mPassword);
    }

    private User(Parcel in) {
        this.mUserId = in.readString();
        this.mPassword = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String Password) {
        this.mPassword = Password;
    }
}

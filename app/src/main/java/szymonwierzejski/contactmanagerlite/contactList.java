package szymonwierzejski.contactmanagerlite;

import android.net.Uri;

public class contactList {
    private String listName, listPhone, listSecondPhone, listEmail, listAddress;             //setting list of pirvate variables only usuable withing this class
    private Uri listPhotoUri;
    private int listID;
    public contactList (int ID, String name, String phone, String secondPhone, String email, String address, Uri profilePhotoUri){    //setting variables specific for this class, accessable by others
        listName = name;
        listPhone = phone;
        listSecondPhone = secondPhone;
        listEmail = email;
        listAddress = address;
        listPhotoUri = profilePhotoUri;
        listID = ID;
    }
    public int getID(){
        return listID;
    }

    public String getListName() {
        return listName;
    }        //setting getters for the list

    public String getListPhone(){
        return listPhone;
    }
    public String getSecondPhone(){
        return listSecondPhone;
    }
    public String getEmail(){
        return listEmail;
    }

    public String getAddress() {
        return listAddress;
    }

    public Uri getProfilePhotoUri() {
        return listPhotoUri;
    }
}

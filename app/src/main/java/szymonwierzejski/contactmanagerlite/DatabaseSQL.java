package szymonwierzejski.contactmanagerlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSQL extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    //setting variables for future database columns
    private static final String DATABASE_NAME = "ContactManagerLite",
            TABLE_CONTACTS = "Contacts",
            KEY_NAME = "Name",
            KEY_PHONE = "Phone",
            KEY_SECONDPHONE = "SecondPhone",
            KEY_EMAIL = "Email",
            KEY_ADDRESS = "Address",
            KEY_PHOTOURI = "PhotoUri",
            KEY_ID = "ID";

    public DatabaseSQL(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){                       //creates a table upon starting the application
        db.execSQL("CREATE TABLE " + TABLE_CONTACTS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_PHONE + " TEXT," + KEY_SECONDPHONE + " TEXT," + KEY_EMAIL + " TEXT," + KEY_ADDRESS + " TEXT," + KEY_PHOTOURI + " TEXT)");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){                       //if something changes the old table is being deleted and new one is being created with new data
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }
    public void createContact(contactList contact){        //variables from getters in contactList are being set as values for database
        SQLiteDatabase db= getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getListName());
        values.put(KEY_PHONE, contact.getListPhone());
        values.put(KEY_SECONDPHONE, contact.getSecondPhone());
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_ADDRESS, contact.getAddress());
        values.put(KEY_PHOTOURI, contact.getProfilePhotoUri().toString());

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    public contactList saveContact(int id){          //allows to edit contact
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID, KEY_NAME, KEY_PHONE, KEY_SECONDPHONE, KEY_EMAIL, KEY_ADDRESS, KEY_PHOTOURI}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        contactList contact = new contactList(Integer.parseInt(cursor.getString(0)), cursor.getString(1),  cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), Uri.parse(cursor.getString(6)));
        db.close();
        cursor.close();
        return contact;
    }

    public void deleteContact(contactList contact){            //allows to delete contact
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + "=?", new String[]{String.valueOf(contact.getID())});
        db.close();
    }

    public int contactsCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);
        int c=cursor.getCount();
        cursor.close();
        db.close();
        return c;
    }
    public int updateContact(contactList contact){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getListName());
        values.put(KEY_PHONE, contact.getListPhone());
        values.put(KEY_SECONDPHONE, contact.getSecondPhone());
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_ADDRESS, contact.getAddress());
        values.put(KEY_PHOTOURI, contact.getProfilePhotoUri().toString());
        return db.update(TABLE_CONTACTS, values, KEY_ID + "=?", new String[]{String.valueOf(contact.getID())});
    }

    public List<contactList> getAll(){
        List<contactList> contacts = new ArrayList<contactList>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);

        if (cursor.moveToFirst()){
            do {
                contactList contact = new contactList(Integer.parseInt(cursor.getString(0)), cursor.getString(1),  cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), Uri.parse(cursor.getString(6)));
                contacts.add(contact);
            }
            while (cursor.moveToNext());
        }
        return contacts;
    }
}

package szymonwierzejski.contactmanagerlite;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static szymonwierzejski.contactmanagerlite.R.id.buttonAddContact;

public class MainActivity extends AppCompatActivity {


    EditText nameT, phoneT, secondPhoneT, emailT, addressT;      //sets first variables
    List<contactList>  Contacts = new ArrayList<contactList>();
    ListView contactListView;
    ImageView contactImageView;
    Uri profilePhotoUri = Uri.parse("android.resource://szymonwierzejski.contactmanager/drawable/untitled_1.png");   //sets default picture for list with saved contacts
    DatabaseSQL SQLdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {          //onCreate it sets up the layout and variables - links items in layout to variables and creates tabs
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLdb = new DatabaseSQL(getApplicationContext());

        contactImageView = (ImageView) findViewById(R.id.empty_profile);
        contactListView = (ListView) findViewById(R.id.listView);
        nameT = (EditText) findViewById(R.id.contactName);
        phoneT = (EditText) findViewById(R.id.contactPhone);
        secondPhoneT = (EditText) findViewById(R.id.contactSecondaryPhone);
        emailT = (EditText) findViewById(R.id.contactEmail);
        addressT = (EditText) findViewById(R.id.contactAddress);
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Create");
        tabSpec.setContent(R.id.tabCreate);
        tabSpec.setIndicator("Create");
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("Saved");
        tabSpec.setContent(R.id.tabSaved);
        tabSpec.setIndicator("Saved");
        tabHost.addTab(tabSpec);

        final Button addBtn = (Button) findViewById(buttonAddContact);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                        //saves contact to list with saved contacts and sends data to database
                contactList contact = new contactList(SQLdb.contactsCount(), String.valueOf(nameT.getText()), String.valueOf(phoneT.getText()), String.valueOf(secondPhoneT.getText()), String.valueOf(emailT.getText()), String.valueOf(addressT.getText()), profilePhotoUri);
                SQLdb.createContact(contact);
                Contacts.add(contact);
                saveToList();
                Toast toast = Toast.makeText(MainActivity.this, nameT.getText().toString() + " has been saved", Toast.LENGTH_SHORT);      //sends toast messagage upon saving a contact
                toast.show();
            }
        });

        nameT.addTextChangedListener(new TextWatcher() {                                    //all below make so you cannot use the 'create' button unless at least one gap is filled with something else that empty space
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                addBtn.setEnabled(!nameT.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
        phoneT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                addBtn.setEnabled(!phoneT.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
        secondPhoneT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                addBtn.setEnabled(!secondPhoneT.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
        emailT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                addBtn.setEnabled(!emailT.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
        addressT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                addBtn.setEnabled(!addressT.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
        contactImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {              //allows the user to set his own profile picture for a contact
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Choose image"), 1);
            }
        });
        List<contactList> addableContacts = SQLdb.getAll();
        int contactCount = SQLdb.contactsCount();

        for (int i = 0; i < contactCount; i++){
            Contacts.add(addableContacts.get(i));
        }

        if(!addableContacts.isEmpty())
            saveToList();

    }
    public void onActivityResult(int request, int result, Intent data){                //double checks if picture was chosen upon clicking the default picture, if user have chosen a picture, it sets the photo if not nothing happens
        if (result == RESULT_OK){
            if (request == 1)
                profilePhotoUri = data.getData();
            contactImageView.setImageURI(data.getData());
        }
    }
    private void saveToList(){
        ArrayAdapter<contactList> adapter = new ContactListAdapter();
        contactListView.setAdapter(adapter);
    }

    private class ContactListAdapter extends ArrayAdapter<contactList>{
        public ContactListAdapter(){
            super (MainActivity.this, R.layout.list_item, Contacts);
        }
        @Override
        public View getView(int position, View view, ViewGroup parent){
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.list_item, parent, false);
            contactList currentContact = Contacts.get(position);
            TextView name = (TextView) view.findViewById(R.id.contactName);
            name.setText(currentContact.getListName());
            TextView phone = (TextView) view.findViewById(R.id.contactPhone);
            phone.setText(currentContact.getListPhone());
            TextView secondPhone = (TextView) view.findViewById(R.id.contactSecondaryPhone);
            secondPhone.setText(currentContact.getSecondPhone());
            TextView email = (TextView) view.findViewById(R.id.contactEmail);
            email.setText(currentContact.getEmail());
            TextView address = (TextView) view.findViewById(R.id.contactAddress);
            address.setText(currentContact.getAddress());
            ImageView profilePhotoSaved = (ImageView) view.findViewById(R.id.profilePhotoSaved);
            profilePhotoSaved.setImageURI(currentContact.getProfilePhotoUri());

            return view;

        }
    }
}

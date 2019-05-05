package com.prashanthgurunath.mycontactapp;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import android.os.Bundle;
import android.provider.ContactsContract;

import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;


public class MainActivity extends ListActivity {

    @Override
    public long getSelectedItemId() {
        // TODO Auto-generated method stub
        return super.getSelectedItemId();
    }

    @Override
    public int getSelectedItemPosition() {
        // TODO Auto-generated method stub
        return super.getSelectedItemPosition();
    }

    ListView listView;
    Cursor cursor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // using content provider
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, null);

        startManagingCursor(cursor);
        String[] from = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone._ID };
        int[] to = {android.R.id.text1, android.R.id.text2};

        SimpleCursorAdapter listadapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, cursor, from, to);

        setListAdapter(listadapter);


        listView = getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


//       Toast.makeText(this, "Count: " + listView.getCount() , Toast.LENGTH_LONG).show();

        ArrayList<String> emailList =   getNameEmailInfo();
        Toast.makeText(this, "Email(0): " + emailList.get(0) , Toast.LENGTH_LONG).show();

//        Log.d("email_list" , "email" + emailList);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public ArrayList<String> getNameEmailInfo() {

        ArrayList<String> emailArrayList = new ArrayList<String>();
        HashSet<String> emailHashSet = new HashSet<String>();

        Context context = getApplicationContext();
        ContentResolver contentResolver = context.getContentResolver();

        String[] projection = new String[] {
                    ContactsContract.RawContacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                            ContactsContract.Contacts.PHOTO_ID,
                                ContactsContract.CommonDataKinds.Email.DATA,
                                    ContactsContract.CommonDataKinds.Photo.CONTACT_ID };  // string array


        String filterEmail = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";  // for selection

        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    projection, filterEmail, null, null);


        if (cursor.moveToFirst()) {

                        // boolean function to check if the query returns an empty set
                        // if set isn't empty , move to first.

            do {
                 String contactName = cursor.getString(1);
                 String contactEmailID = cursor.getString(3);

                                            // store email id
                                            // emailid is in column 3 of cursor

                if (emailHashSet.add(contactEmailID)) {

                    emailArrayList.add(contactEmailID);
//                    Log.d("emailArrayList.add" , "add" + emailArrayList );
                    Log.d("name_email" , "name:" + contactName + " email:" + contactEmailID);

                }
            } while (cursor.moveToNext());  // contact traversal - next value
        }

        cursor.close();
        return emailArrayList;
    }

}  // end of MainActivity
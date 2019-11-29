package com.example.contactSync.contactsyncdemo.ContactFetching;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.content.CursorLoader;

import com.example.contactSync.contactsyncdemo.GlobalClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

// new ContactFetcher(this).fetchAll();
public class ContactFetcher {

    private final Context context;
    GlobalClass gc;

    public ContactFetcher(Context c) {
        this.context = c;
        this.gc = GlobalClass.getInstance();
    }

    public ArrayList<Contact> fetchAll() {
        String[] projectionFields = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
        };
        ArrayList<Contact> listContacts = new ArrayList<>();
        CursorLoader cursorLoader = new CursorLoader(context,
                ContactsContract.Contacts.CONTENT_URI,
                projectionFields, // the columns to retrieve
                null, // the selection criteria (none)
                null, // the selection args (none)
                null // the sort order (default)
        );

        Cursor c = cursorLoader.loadInBackground();

        final Map<String, Contact> contactsMap = new HashMap<>(c.getCount());

        if (c.moveToFirst()) {

            int idIndex = c.getColumnIndex(ContactsContract.Contacts._ID);
            int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                do {

                String contactId = c.getString(idIndex);
                String contactDisplayName = c.getString(nameIndex);

                String contactPhoto;
                    Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
                    Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                    contactPhoto =photoUri.toString();

                    Bitmap my_btmp = gc.getImageBitmap(context,contactPhoto);

                Contact contact = new Contact(contactId, contactDisplayName,my_btmp);
                contactsMap.put(contactId, contact);
                System.out.println("Contact fetcher do while: "+ contactId+ " name:"+contactDisplayName);
                listContacts.add(contact);

                } while (c.moveToNext());

        }

        c.close();

        matchContactNumbers(contactsMap);
        matchContactEmails(contactsMap);
       // Collections.sort(listContacts);
        Collections.sort(listContacts, new ContactNameComparator());

        return listContacts;
    }


    public ArrayList<Contact> fetchFavContacts(ArrayList contactIdArray) {
        ArrayList<Contact> listFavContacts = new ArrayList<>();

                    String[] projectionFields = new String[]{
                            ContactsContract.Contacts._ID,
                            ContactsContract.Contacts.DISPLAY_NAME,
                    };

                CursorLoader cursorLoader = new CursorLoader(context,
                        ContactsContract.Contacts.CONTENT_URI,
                        projectionFields, // the columns to retrieve
                        null, // the selection criteria (none)
                        null, // the selection args (none)
                        null // the sort order (default)
                );

                Cursor c = cursorLoader.loadInBackground();

                final Map<String, Contact> contactsMap = new HashMap<>(c.getCount());

                if (c.moveToFirst()) {

                    int idIndex = c.getColumnIndex(ContactsContract.Contacts._ID);
                    int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                    do {

                        String contactId = c.getString(idIndex);

                        if(contactIdArray!=null) {
                            if (contactIdArray.contains(contactId)) {
                                String contactDisplayName = c.getString(nameIndex);

                                String contactPhoto;
                                Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
                                Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                                contactPhoto =photoUri.toString();
                                Bitmap my_btmp = gc.getImageBitmap(context,contactPhoto);


                                Contact contact = new Contact(contactId, contactDisplayName, my_btmp);
                                contactsMap.put(contactId, contact);

                                System.out.println("Fav Contact fetcher do while: " + contactId + " name:" + contactDisplayName);
                                listFavContacts.add(contact);
                            }
                        }


                    } while (c.moveToNext());
        }
        c.close();

        match_FAV_ContactEmails(contactIdArray,contactsMap);
        match_FAV_ContactNumbers(contactIdArray,contactsMap);

        Collections.sort(listFavContacts, new ContactNameComparator());

        return listFavContacts;

    }

    public class ContactNameComparator implements Comparator<Contact> {
        public int compare(Contact c1, Contact c2) {
            return c1.name.compareTo(c2.name);
        }
    }

    public void matchContactNumbers(Map<String, Contact> contactsMap) {
        // Get numbers
        final String[] numberProjection = new String[]{
                Phone.NUMBER,
                Phone.TYPE,
                Phone.CONTACT_ID,
        };

        Cursor phone = new CursorLoader(context,
                Phone.CONTENT_URI,
                numberProjection,
                null,
                null,
                null).loadInBackground();

        if (phone.moveToFirst()) {
            final int contactNumberColumnIndex = phone.getColumnIndex(Phone.NUMBER);
            final int contactTypeColumnIndex = phone.getColumnIndex(Phone.TYPE);
            final int contactIdColumnIndex = phone.getColumnIndex(Phone.CONTACT_ID);

            while (!phone.isAfterLast()) {


                final String number = phone.getString(contactNumberColumnIndex);
                final String contactId = phone.getString(contactIdColumnIndex);
                Contact contact = contactsMap.get(contactId);
                if (contact == null) {
                    continue;
                }
                System.out.println("ContactFetcher::: "+phone.isAfterLast() +
                        " Contact index:"+contactNumberColumnIndex +
                        " Contact number:"+ number);
                final int type = phone.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                CharSequence phoneType = Phone.getTypeLabel(context.getResources(), type, customLabel);
                contact.addNumber(number, phoneType.toString());
                phone.moveToNext();
            }

        }

        phone.close();
    }

    public void matchContactEmails(Map<String, Contact> contactsMap) {
        // Get email
        final String[] emailProjection = new String[]{
                Email.DATA,
                Email.TYPE,
                Email.CONTACT_ID,
        };

        Cursor email = new CursorLoader(context,
                Email.CONTENT_URI,
                emailProjection,
                null,
                null,
                null).loadInBackground();

        if (email.moveToFirst()) {
            final int contactEmailColumnIndex = email.getColumnIndex(Email.DATA);
            final int contactTypeColumnIndex = email.getColumnIndex(Email.TYPE);
            final int contactIdColumnsIndex = email.getColumnIndex(Email.CONTACT_ID);

            while (!email.isAfterLast()) {
                final String address = email.getString(contactEmailColumnIndex);
                final String contactId = email.getString(contactIdColumnsIndex);
                final int type = email.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                Contact contact = contactsMap.get(contactId);
                if (contact == null) {
                    continue;
                }
                CharSequence emailType = Email.getTypeLabel(context.getResources(), type, customLabel);
                contact.addEmail(address, emailType.toString());
                email.moveToNext();
            }
        }

        email.close();
    }








    //For Fav Contacts:::
    public void match_FAV_ContactNumbers(ArrayList contactIdArray, Map<String, Contact> contactsMap) {
        // Get numbers
        final String[] numberProjection = new String[]{
                Phone.NUMBER,
                Phone.TYPE,
                Phone.CONTACT_ID,
        };

        Cursor phone = new CursorLoader(context,
                Phone.CONTENT_URI,
                numberProjection,
                null,
                null,
                null).loadInBackground();

        if (phone.moveToFirst()) {
            final int contactNumberColumnIndex = phone.getColumnIndex(Phone.NUMBER);
            final int contactTypeColumnIndex = phone.getColumnIndex(Phone.TYPE);
            final int contactIdColumnIndex = phone.getColumnIndex(Phone.CONTACT_ID);

            /*while (!phone.isAfterLast()) {


                final String number = phone.getString(contactNumberColumnIndex);
                final String contactId = phone.getString(contactIdColumnIndex);
                Contact contact = contactsMap.get(contactId);
                if (contact == null) {
                    continue;
                }
                System.out.println("ContactFetcher::: "+phone.isAfterLast() +
                        " Contact index:"+contactNumberColumnIndex +
                        " Contact number:"+ number);
                final int type = phone.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                CharSequence phoneType = Phone.getTypeLabel(context.getResources(), type, customLabel);
                contact.addNumberFav(number, phoneType.toString());
                phone.moveToNext();
            }*/
            do {

                String contactId = phone.getString(contactIdColumnIndex);

                if(contactIdArray!=null) {
                    if (contactIdArray.contains(contactId)) {
                        final String number = phone.getString(contactNumberColumnIndex);
                        final int type = phone.getInt(contactTypeColumnIndex);
                        String customLabel = "Custom";
                        Contact contact = contactsMap.get(contactId);
                        if (contact == null) {
                            continue;
                        }
                        CharSequence phoneType = Phone.getTypeLabel(context.getResources(), type, customLabel);
                        contact.addNumberFav(number, phoneType.toString());
                    }
                }


            } while (phone.moveToNext());

        }

        phone.close();
    }

    public void match_FAV_ContactEmails(ArrayList contactIdArray, Map<String, Contact> contactsMap) {
        // Get email
        final String[] emailProjection = new String[]{
                Email.DATA,
                Email.TYPE,
                Email.CONTACT_ID,
        };

        Cursor email = new CursorLoader(context,
                Email.CONTENT_URI,
                emailProjection,
                null,
                null,
                null).loadInBackground();

        if (email.moveToFirst()) {
            final int contactEmailColumnIndex = email.getColumnIndex(Email.DATA);
            final int contactTypeColumnIndex = email.getColumnIndex(Email.TYPE);
            final int contactIdColumnsIndex = email.getColumnIndex(Email.CONTACT_ID);

            /*while (!email.isAfterLast()) {
                final String address = email.getString(contactEmailColumnIndex);
                final String contactId = email.getString(contactIdColumnsIndex);
                final int type = email.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                Contact contact = contactsMap.get(contactId);
                if (contact == null) {
                    continue;
                }
                CharSequence emailType = Email.getTypeLabel(context.getResources(), type, customLabel);
                contact.addEmailFav(address, emailType.toString());
                email.moveToNext();
            }*/
            do {

                String contactId = email.getString(contactIdColumnsIndex);

                if(contactIdArray!=null) {
                    if (contactIdArray.contains(contactId)) {
                        final String address = email.getString(contactEmailColumnIndex);
                        final int type = email.getInt(contactTypeColumnIndex);
                        String customLabel = "Custom";
                        Contact contact = contactsMap.get(contactId);
                        if (contact == null) {
                            continue;
                        }
                        CharSequence emailType = Email.getTypeLabel(context.getResources(), type, customLabel);
                        contact.addEmailFav(address, emailType.toString());
                    }
                }


            } while (email.moveToNext());
        }

        email.close();
    }
}

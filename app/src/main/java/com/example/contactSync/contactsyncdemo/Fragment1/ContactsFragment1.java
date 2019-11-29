package com.example.contactSync.contactsyncdemo.Fragment1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.contactSync.contactsyncdemo.ContactFetching.Contact;
import com.example.contactSync.contactsyncdemo.ContactFetching.ContactFetcher;
import com.example.contactSync.contactsyncdemo.R;

import java.util.ArrayList;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

public class ContactsFragment1 extends Fragment  {
    ArrayList<Contact> listContacts;
    IndexFastScrollRecyclerView lvContacts;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.contactsfragment, container, false);
       // lvContacts = (ListView) v.findViewById(R.id.lvContacts);

        lvContacts = (IndexFastScrollRecyclerView) v.findViewById(R.id.lvContacts);

        initialiseUI();

        listContacts = new ContactFetcher(getContext()).fetchAll();

        /*ContactsAdapter adapterContacts = new ContactsAdapter(getContext(), listContacts);
        lvContacts.setAdapter(adapterContacts);*/

        ContactsAdapterRecycler adapterContacts = new ContactsAdapterRecycler(getContext(), listContacts);
        lvContacts.setAdapter(adapterContacts);

        return v;
    }


    protected void initialiseUI() {
        lvContacts.setLayoutManager(new LinearLayoutManager(getActivity()));


        lvContacts.setIndexTextSize(12);
        lvContacts.setIndexBarColor("#FFFFFF");
        lvContacts.setIndexBarCornerRadius(0);
        lvContacts.setIndexBarTransparentValue((float) 0.4);
        lvContacts.setIndexbarMargin(0);
        lvContacts.setIndexbarWidth(30);

        lvContacts.setPreviewPadding(0);
        lvContacts.setIndexBarTextColor("#E31F26");
        lvContacts.setIndexBarVisibility(true);
    }
}

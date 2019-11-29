package com.example.inextrix.contactsyncdemo.Fragment1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.inextrix.contactsyncdemo.ContactFetching.Contact;
import com.example.inextrix.contactsyncdemo.FavContactGetSet;
import com.example.inextrix.contactsyncdemo.GlobalClass;
import com.example.inextrix.contactsyncdemo.R;
import com.pkmmte.view.CircularImageView;

public class ContactsAdapter extends ArrayAdapter<Contact> implements View.OnClickListener {

    ArrayList<String> favContactGetSets = new ArrayList<>();
    FavContactGetSet favContactGetSetModel = new FavContactGetSet();
    GlobalClass gc;
    Context ctx;
    ArrayList<Contact> contact;

	public ContactsAdapter(Context context, ArrayList<Contact> contacts) {
		super(context, 0, contacts);
		ctx= context;
		this.contact= contacts;
		System.out.println("ContactAdapter::: "+contacts.size());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item
		//final Contact contact = getItem(position);
        final Contact contact_data =contact.get(position);
		// Check if an existing view is being reused, otherwise inflate the view
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(R.layout.contact_row, parent, false);
		}
		gc = GlobalClass.getInstance();

		// Populate the data into the template view using the data object
		TextView tvName = (TextView) view.findViewById(R.id.contactEntryText);
		TextView tvEmail = (TextView) view.findViewById(R.id.contactEmail);
		TextView tvPhone = (TextView) view.findViewById(R.id.textSipUser);
        CircularImageView contact_image= (CircularImageView) view.findViewById(R.id.contact_image);

        final ImageView ivAddToFav1 = (ImageView) view.findViewById(R.id.idFav);
        ImageView idedit = (ImageView) view.findViewById(R.id.idFav12);

        final SwipeRevealLayout swipeview = (SwipeRevealLayout) view.findViewById(R.id.swipelayout);
        swipeview.close(true);

        System.out.println("Photo Uri::::: "+contact_data.phot_uri);

       /* Bitmap my_btmp = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.userdefault);
        if (contact.phot_uri != null) {
            Uri my_contact_Uri = Uri.parse(contact.phot_uri);
            try {

                my_btmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), my_contact_Uri);


            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }*/


        contact_image.setImageBitmap(contact_data.phot_uri);

        tvName.setText(contact_data.name);
		tvEmail.setText("");
		tvPhone.setText("");
		if (contact_data.emails.size() > 0 && contact_data.emails.get(0) != null) {
			tvEmail.setText(contact_data.emails.get(0).address);
		}
		if (contact_data.numbers.size() > 0 && contact_data.numbers.get(0) != null) {
			tvPhone.setText(contact_data.numbers.get(0).number);
		}

		if(gc.getFavContactList(ctx)!=null) {
            favContactGetSets = gc.getFavContactList(ctx);
        }

            if (favContactGetSets.contains(contact_data.id)) {
                ivAddToFav1.setImageResource(R.drawable.imgfav);
            }else {
                ivAddToFav1.setImageResource(R.drawable.imgunfav);
            }

        view.setOnClickListener(this);
        ivAddToFav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeview.close(true);
                    if (favContactGetSets != null) {
                            //if (!favContactGetSets.contains(contact.numbers.get(0).number)) {
                            if (!favContactGetSets.contains(contact_data.id)) {

                                favContactGetSets.add(contact_data.id);
                                ivAddToFav1.setImageResource(R.drawable.imgfav);

                                gc.setFavContactList(ctx, favContactGetSets);

                        }else {
                            ivAddToFav1.setImageResource(R.drawable.imgunfav);
                            notifyDataSetChanged();
                            favContactGetSets.remove(contact_data.id);
                             gc.setFavContactList(ctx, favContactGetSets);

                        }
                    }
            }
        });
        idedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeview.close(true);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contact_data.id));
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });

        return view;
	}

    @Override
    public void onClick(View view) {

    }
}

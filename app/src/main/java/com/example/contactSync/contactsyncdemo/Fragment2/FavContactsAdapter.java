package com.example.contactSync.contactsyncdemo.Fragment2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.contactSync.contactsyncdemo.ContactFetching.Contact;
import com.example.contactSync.contactsyncdemo.FavContactGetSet;
import com.example.contactSync.contactsyncdemo.GlobalClass;
import com.example.contactSync.contactsyncdemo.R;
import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;

public class FavContactsAdapter extends ArrayAdapter<Contact> implements View.OnClickListener {

    ArrayList<String> favContactGetSets = new ArrayList<>();
    FavContactGetSet favContactGetSetModel = new FavContactGetSet();
    GlobalClass gc;
    Context ctx;
    ArrayList<Contact> contact;

    public FavContactsAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
        ctx= context;
        contact=contacts;
        System.out.println("ContactAdapter::: "+contacts.size());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item
        final Contact contact_data =contact.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.fav_contact_row, parent, false);
        }
        gc = GlobalClass.getInstance();

        // Populate the data into the template view using the data object
        TextView tvName = (TextView) view.findViewById(R.id.contactEntryText);
        TextView tvEmail = (TextView) view.findViewById(R.id.contactEmail);
        TextView tvPhone = (TextView) view.findViewById(R.id.textSipUser);
        final ImageView ivAddToFav1 = (ImageView) view.findViewById(R.id.idFav);
        ImageView idedit = (ImageView) view.findViewById(R.id.idFav12);
        CircularImageView contact_image= (CircularImageView) view.findViewById(R.id.contact_image);

        final SwipeRevealLayout swipeview = (SwipeRevealLayout) view.findViewById(R.id.swipelayout);
        swipeview.close(true);

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

        System.out.println("Photo Uri::::: "+contact_data.phot_uri);
        /*Bitmap my_btmp = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.userdefault);
        if (contact_data.phot_uri != null) {
            Uri my_contact_Uri = Uri.parse(contact_data.phot_uri);
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

        ivAddToFav1.setImageResource(R.drawable.imgfav);


        view.setOnClickListener(this);
        ivAddToFav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeview.close(true);
                if (favContactGetSets != null) {

                        ivAddToFav1.setImageResource(R.drawable.imgunfav);

                        favContactGetSets.remove(contact_data.id);
                        gc.setFavContactList(ctx, favContactGetSets);
                    notifyDataSetChanged();

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

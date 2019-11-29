package com.example.inextrix.contactsyncdemo.Fragment1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.inextrix.contactsyncdemo.ContactFetching.Contact;
import com.example.inextrix.contactsyncdemo.GlobalClass;
import com.example.inextrix.contactsyncdemo.R;
import java.util.ArrayList;

import in.myinnos.alphabetsindexfastscrollrecycler.utilities_fs.StringMatcher;

public class ContactsAdapterRecycler  extends RecyclerView.Adapter<ContactsAdapterRecycler.ViewHolder>
        implements SectionIndexer {

    public OnItemClickListener listener;

    public int i = 0;

    private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public TextView alpha;

    public Context ctx;

    public String delsipnumber = "";
    public String delnumber = "";
    public boolean popupval;
    int pos1;

    GlobalClass gc;

    ArrayList<String> favContactGetSets = new ArrayList<>();
    ArrayList<Contact> contact;



    public ContactsAdapterRecycler(Context context, ArrayList<Contact> contacts) {
        ctx= context;
        this.contact= contacts;
        System.out.println("ContactAdapter::: "+contacts.size());
        gc= GlobalClass.getInstance();

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row, parent, false);

        return new ViewHolder(v);
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getItemCount() {
        System.out.println("Contact Adapter Get Item count:: "+contact.size());
        if (contact == null)
            return 0;
        else
            return contact.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Contact contact_data =contact.get(position);


        String label = contact_data.name;

        holder.fl_AddToFav.setTag(position);
        // to get favorite list

        holder.cont_alpha_seperator.setVisibility(View.VISIBLE);
        holder.header.setVisibility(View.VISIBLE);
        char firstChar = label.toUpperCase().charAt(0);
        if (position == 0) {
            setSection(holder, label);
        } else {
            String preLabel = contact.get(position-1).name;
            char preFirstChar = preLabel.toUpperCase().charAt(0);
            if (firstChar != preFirstChar) {
                setSection(holder, label);
            } else {
                holder.header.setVisibility(View.GONE);
                holder.cont_alpha_seperator.setVisibility(View.GONE);
            }


            if (label.equals(preLabel)) {
                System.out.println("Contact myListadapter label : " + label + " Pre label : " + preLabel);
                holder.contactimage.setVisibility(View.VISIBLE);
                holder.txtname.setVisibility(View.VISIBLE);
                holder.textSipUser.setVisibility(View.VISIBLE);


            } else {

                holder.contactimage.setVisibility(View.VISIBLE);
                holder.txtname.setVisibility(View.VISIBLE);
                holder.textSipUser.setVisibility(View.VISIBLE);


            }
        }


        holder.txtname.setText(contact_data.name);

        if (contact_data.emails.size() > 0 && contact_data.emails.get(0) != null) {
            holder.contactEmail.setText(contact_data.emails.get(0).address);
        }
        if (contact_data.numbers.size() > 0 && contact_data.numbers.get(0) != null) {
            holder.textSipUser.setText(contact_data.numbers.get(0).number);
        }

        if(contact_data.phot_uri != null){
            holder.contactimage.setImageBitmap(contact_data.phot_uri);
        }else{
            //System.out.println("number:1: "+label);
            String letter = gc.split_word(label);
            TextDrawable drawable = gc.name_image(letter);
            holder.contactimage.setImageDrawable(drawable);
        }


        //getNameFromNumber(((String) Books.get(position).get(CONTACTKEY)),((String) Books.get(position).get(CONTACTNumber)), holder.contactimage);


        if (contact != null && 0 <= position && position < contact.size()) {

            // Bind your data here
            holder.bind(contact_data);
        }

    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        String[] sections = new String[mSections.length()];
        for (int i = 0; i < mSections.length(); i++)
            sections[i] = String.valueOf(mSections.charAt(i));
        return sections;
    }

    @Override
    public int getPositionForSection(int section) {
        // If there is no item for current section, previous section will be selected
        for (int i = section; i >= 0; i--) {
            for (int j = 0; j < getItemCount(); j++) {
                String cont_no = contact.get(j).name;//(String) ((HashMap<String, Object>) getItem(j)).get(CONTACTKEY);
                if (i == 0) {
                    // For numeric section
                    for (int k = 0; k <= 9; k++) {
                        if (StringMatcher.match(String.valueOf(cont_no.charAt(0)).toUpperCase(), String.valueOf(k).toUpperCase()))
                            return j;
                    }
                } else {
                    if (StringMatcher.match(String.valueOf(cont_no.charAt(0)).toUpperCase(), String.valueOf(mSections.charAt(i)).toUpperCase()))
                        /*if ((String.valueOf(cont_no.charAt(0)).equalsIgnoreCase("x"))) {
							System.out.println("X is matched  ::: "+String.valueOf(cont_no.charAt(0)));
						}*/
                        return j;
                }
            }
        }
        return 0;
    }


    private void setSection(ViewHolder holder, String label) {
        holder.alpha.setText("   " + label.substring(0, 1).toUpperCase());
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtname, textSipUser, txttype, alpha,contactEmail;
        ImageView ivAddToFav1, idedit;
        LinearLayout header;
        LinearLayout contactListLayout;
        ImageView contactimage;

        View cont_alpha_seperator;
        LinearLayout fl_AddToFav;
        private SwipeRevealLayout swipeview;
        ViewHolder holder;
        String contact_name;

        ViewHolder(final View itemView) {

            super(itemView);

            header = (LinearLayout) itemView.findViewById(R.id.section);
            contactListLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout3);
            cont_alpha_seperator = (View) itemView.findViewById(R.id.cont_alpha_seperator);
            txtname = (TextView) itemView.findViewById(R.id.contactEntryText);
            contactEmail = (TextView) itemView.findViewById(R.id.contactEmail);
            textSipUser = (TextView) itemView.findViewById(R.id.textSipUser);
            alpha = (TextView) itemView.findViewById(R.id.alpha);
            contactimage = (ImageView) itemView.findViewById(R.id.contact_image);
            swipeview = (SwipeRevealLayout) itemView.findViewById(R.id.swipelayout);
            fl_AddToFav = (LinearLayout) itemView.findViewById(R.id.delete_layout);
            ivAddToFav1 = (ImageView) itemView.findViewById(R.id.idFav);
            idedit = (ImageView) itemView.findViewById(R.id.idFav12);
            //  txttype = (TextView) view.findViewById(R.id.siptype);
            //  ivAddToFav = (ImageView) view.findViewById(R.id.ivAddToFav);
            // ivAddToFav = (ImageView) itemView.findViewById(R.id.idFav);
            contact_name = txtname.getText().toString();

            itemView.setOnClickListener(this);


            header.setVisibility(View.VISIBLE);


        }

        public void bind(final Contact contact_data) {

            swipeview.close(true);

            int pos = getAdapterPosition();

            String phNo="";
            final String contID = contact_data.id;
            final String getName = contact_data.name;

            if (contact_data.numbers.size() > 0 && contact_data.numbers.get(0) != null) {
                phNo = contact_data.numbers.get(0).number;
            }

            System.out.println("Contacts getName : " + getName);
            System.out.println("Contacts getPhNo : " + phNo);

            if(gc.getFavContactList(ctx)!=null) {
                favContactGetSets = gc.getFavContactList(ctx);
            }

            if (favContactGetSets.contains(contact_data.id)) {
                ivAddToFav1.setImageResource(R.drawable.imgfav);
            }else {
                ivAddToFav1.setImageResource(R.drawable.imgunfav);
            }

            ivAddToFav1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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


            contactListLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Listitem click called");
                    pos1 = getAdapterPosition();


                    if (popupval) {

                        String label="";
                        if (contact_data.numbers.size() > 0 && contact_data.numbers.get(0) != null) {
                            label = contact_data.numbers.get(0).number;
                        }

                        Intent intent1 = new Intent();
                        //intent1.setAction(SipManager.ACTION_MAKE_DIAL_CALL);
                        intent1.putExtra("selectednumber", label);
                        ctx.sendBroadcast(intent1);
                    } else {
                        CustomDialogClass cdd = new CustomDialogClass(ctx, contact_data, pos1);
                        cdd.show();
                    }

                }
            });

            /*ArrayList<HashMap<String, Object>> MultiBooks = new ArrayList<HashMap<String, Object>>();

            for (int i = 0; i < Books.size(); i++) {

                if (Books.get(i).get(CONTACTKEY).toString().equals(adapter.getBooks().get(pos).get(CONTACTKEY).toString())) {
                    MultiBooks.add(Books.get(i));
                }
            }*/


            /*Contacts.CustomDialogClass cdd = new Contacts.CustomDialogClass(ctx, Books, pos);
            cdd.show();*/


        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onItemClick(view, getPosition());
            }
        }
    }

    public class CustomDialogClass extends Dialog implements View.OnClickListener {

        public Context c;
        public Dialog d;
        public TextView btn_call, btn_delete;
        Contact contactBook;
        int position = 0;

        public CustomDialogClass(Context a, Contact contact_data, int pos) {
            super(a);
            this.c = a;
            this.contactBook = contact_data;
            this.position = pos;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.custom_dialog);

            RadioGroup rgp = (RadioGroup) findViewById(R.id.radio_group);
            RadioGroup.LayoutParams rprms;
            //setTitle(MultiBooks.get(position).get(Contacts.CONTACTKEY).toString());
            TextView txttitle = (TextView) findViewById(R.id.txttile);
            txttitle.setText(contactBook.name);
            if (contactBook.numbers.size() > 0 && contactBook.numbers.get(0) != null) {
                delsipnumber = delnumber = contactBook.numbers.get(0).number;
            }


            /*rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    for (int i = 0; i < contactBook; i++) {
                        if (checkedId == Integer.parseInt("1000" + i)) {
                            delsipnumber = delnumber = contactBook.numbers.get(0).number;
                            // Log.d("onCheckedChanged call number", delsipnumber);
                        }
                    }

                }
            });*/


            btn_call = (TextView) findViewById(R.id.btn_call);
            btn_delete = (TextView) findViewById(R.id.btn_delete);
            btn_call.setOnClickListener(this);
            btn_delete.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_call:

                    /*Intent intent = new Intent();
                    intent.setAction(SipManager.ACTION_MAKE_CALL);
                    intent.putExtra("action", "call");
                    intent.putExtra("number", delsipnumber);
                    ctx.sendBroadcast(intent);*/

                    break;

                case R.id.btn_delete:

                    /*Intent intent1 = new Intent();
                    intent1.setAction(SipManager.ACTION_MAKE_CALL);
                    intent1.putExtra("action", "delete");
                    intent1.putExtra("position_contact", position);
                    intent1.putExtra("number", delsipnumber);
                    ctx.sendBroadcast(intent1);*/
                    break;

                default:
                    break;
            }
            dismiss();
        }
    }

   /* public Filter getFilter() {

        if (valueFilter == null) {

            valueFilter = new ValueFilter(mStringFilterList, this);
        }

        return valueFilter;
    }*/

    /*public class ValueFilter extends Filter {

        ContactsAdapterRecycler adapter;
        private ArrayList<HashMap<String, Object>> mfilterList;

        public ValueFilter(ArrayList<HashMap<String, Object>> filterList, ContactsAdapterRecycler adapter) {
            this.adapter = adapter;
            this.mfilterList = filterList;
        }

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {

                ArrayList<HashMap<String, Object>> filterListbk = new ArrayList<HashMap<String, Object>>();

                for (int i = 0; i < mfilterList.size(); i++) {
                    String contactName = (String) mfilterList.get(i).get(Contacts.CONTACTKEY);
                    String contactNo = (String) mfilterList.get(i).get(Contacts.DURATION);
                    contactName = contactName.toLowerCase();
                    constraint = (CharSequence) constraint.toString().toLowerCase();
                    System.out.println("contactName " + contactName + " char " + constraint);
                    System.out.println("contactNo " + contactNo + " char " + constraint);
                   *//* if (contactName.contains(constraint)) {
                        filterListbk.add(mfilterList.get(i));
                    }
                    if (contactNo.contains(constraint)) {
                        filterListbk.add(mfilterList.get(i));
                    }*//*
                    if (contactName.startsWith(constraint.toString())) {
                        filterListbk.add(mfilterList.get(i));
                    }
                    if (contactNo.startsWith(constraint.toString())) {
                        filterListbk.add(mfilterList.get(i));
                    }

                    results.count = filterListbk.size();

                    results.values = filterListbk;
                }

            } else {

                results.count = mfilterList.size();

                results.values = mfilterList;

            }

            return results;


        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            //  adapter.players= (ArrayList<Player>) results.values;
            adapter.Books = (ArrayList<HashMap<String, Object>>) results.values;
            String new_result = results.values.toString();
            if(!new_result.equals(null)){
                adapter.Books = (ArrayList<HashMap<String, Object>>) results.values;

                Intent intent = new Intent();
                intent.setAction(SipManager.ACTION_NO_RECORD_FOUND);
                intent.putExtra("flag","0");
                // add data
                ctx.sendBroadcast(intent);
            }

            if(new_result.equals("[]")){
                Intent intent = new Intent();
                intent.setAction(SipManager.ACTION_NO_RECORD_FOUND);
                intent.putExtra("flag","1");
                ctx.sendBroadcast(intent);
            }
            notifyDataSetChanged();
        }
    }*/

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

}
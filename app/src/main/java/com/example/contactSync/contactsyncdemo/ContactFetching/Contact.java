package com.example.contactSync.contactsyncdemo.ContactFetching;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Contact {
	public String id;
	public String name;
	public Bitmap phot_uri;
	public ArrayList<ContactEmail> emails;
	public ArrayList<ContactPhone> numbers;

	public ArrayList<ContactEmailFav> emails_fav;
	public ArrayList<ContactPhoneFav> numbers_fav;

	public Contact(String id, String name, Bitmap contactPhoto) {
		this.id = id;
		this.name = name;
		this.phot_uri =contactPhoto;
		this.emails = new ArrayList<ContactEmail>();
		this.numbers = new ArrayList<ContactPhone>();

		this.emails_fav = new ArrayList<ContactEmailFav>();
		this.numbers_fav = new ArrayList<ContactPhoneFav>();
	}

	@Override
	public String toString() {
		String result = name;
		System.out.println("Contacts::: name: "+name);
		if (numbers.size() > 0) {
			ContactPhone number = numbers.get(0);
			result += " (" + number.number + " - " + number.type + ")";
		}
		if (emails.size() > 0) {
			ContactEmail email = emails.get(0);
			result += " [" + email.address + " - " + email.type + "]";
		}

        if (numbers_fav.size() > 0) {
            ContactPhoneFav numberfav = numbers_fav.get(0);
            result += " (" + numberfav.number + " - " + numberfav.type + ")";
        }
        if (emails_fav.size() > 0) {
            ContactEmailFav emailfav = emails_fav.get(0);
            result += " [" + emailfav.address + " - " + emailfav.type + "]";
        }

		return result;
	}

	public void addEmail(String address, String type) {
		emails.add(new ContactEmail(address, type));
	}

	public void addNumber(String number, String type) {
		System.out.println("Contact ::: "+ number+" type:"+type);

		numbers.add(new ContactPhone(number, type));
	}


	//For Fav contact
	public void addEmailFav(String address, String type) {
		emails_fav.add(new ContactEmailFav(address, type));
	}

	public void addNumberFav(String number, String type) {
		System.out.println("Contact ::: "+ number+" type:"+type);

		numbers_fav.add(new ContactPhoneFav(number, type));
	}
}

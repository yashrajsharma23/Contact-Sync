package com.example.inextrix.contactsyncdemo.ContactFetching;

public class ContactPhoneFav {
	public String number;
	public String type;

	public ContactPhoneFav(String number, String type) {
		System.out.println("Contact phone ::: "+ number+" type:"+type);

		this.number = number;
		this.type = type;
	}
}

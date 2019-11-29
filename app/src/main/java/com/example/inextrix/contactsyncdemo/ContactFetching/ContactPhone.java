package com.example.inextrix.contactsyncdemo.ContactFetching;

public class ContactPhone {
	public String number;
	public String type;

	public ContactPhone(String number, String type) {
		System.out.println("Contact phone ::: "+ number+" type:"+type);

		this.number = number;
		this.type = type;
	}
}

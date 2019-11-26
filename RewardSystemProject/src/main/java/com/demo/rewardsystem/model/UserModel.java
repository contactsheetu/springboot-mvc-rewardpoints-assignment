package com.demo.rewardsystem.model;

public class UserModel {

	String doj;
	String  firstName;
	String ID;
	String lastName;
	
	public UserModel(String iD, String firstName, String lastName, String doj) {
		super();
		this.ID = iD;
		this.firstName = firstName;
		this.lastName = lastName;
		this.doj = doj;
	}
	
	public String getDoJ() {
		return doj;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getID() {
		return ID;
	}
	public String getLastName() {
		return lastName;
	}
	public void setDoJ(String doj) {
		this.doj = doj;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public boolean equals(Object obj) {
		UserModel user = (UserModel)obj;
		return this.ID.equals(user.getID());
	}
	
	@Override
	public int hashCode() {
		return Integer.valueOf(this.ID);
	}
	
}

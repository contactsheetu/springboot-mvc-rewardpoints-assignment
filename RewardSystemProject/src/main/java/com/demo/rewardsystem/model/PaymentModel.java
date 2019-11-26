package com.demo.rewardsystem.model;

import java.util.Date;

public class PaymentModel {

	Double amount;
	String description;
	String paymentID;
	UserModel user;
	Date paymentDate;
	
	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	public PaymentModel(String paymentID, Double amount, Date paymentDate, String description) {
		super();
		this.paymentID = paymentID;
		this.amount = amount;
		this.description = description;
		this.paymentDate = paymentDate;		
	}

	public PaymentModel(String paymentID, Double amount, Date paymentDate, String description,  UserModel user) {
		super();
		this.paymentID = paymentID;
		this.amount = amount;
		this.description = description;
		this.user = user;
		this.paymentDate = paymentDate;
	}

	public Double getAmount() {
		return amount;
	}

	public String getDescription() {
		return description;
	}

	public String getPaymentID() {
		return paymentID;
	}

	public UserModel getUser() {
		return user;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPaymentID(String paymentID) {
		this.paymentID = paymentID;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

}

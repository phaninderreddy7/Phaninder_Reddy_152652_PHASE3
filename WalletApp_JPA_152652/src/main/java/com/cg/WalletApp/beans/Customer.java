package com.cg.WalletApp.beans;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity

@Table(name = "Customer_Wallet")
public class Customer implements Serializable {
	/**
	 * 
	 */
	
	@Id
	@Column(name = "Mobile_No", length = 10)
	private String mobileNumber;
	@Column(name = "Customer_Name", length = 30)
	private String name;
	@Column(name = "Customer_Password", length = 20)
	private String password;
	@Column(name = "Email_ID", length = 30)
	private String EmailId;
	@Embedded
	private Wallet wallet;

	/*
	 * CREATE TABLE TRANSACTIONS ( ID int NOT NULL, Mobile_no varchar2(10)
	 * references customer_wallet(MOBILE_NO), TimeStampOfTrans Timestamp, Type
	 * char(10), Amount Number(19,2), PRIMARY KEY (ID) );
	 */
	public Customer() {
		wallet = new Wallet();

	}

	public Customer(String mobileNumber, String name, String password, String emailId, Wallet wallet) {
		super();
		this.mobileNumber = mobileNumber;
		this.name = name;
		this.password = password;
		EmailId = emailId;
		this.wallet = wallet;

	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return EmailId;
	}

	public void setEmailId(String emailId) {
		EmailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

}

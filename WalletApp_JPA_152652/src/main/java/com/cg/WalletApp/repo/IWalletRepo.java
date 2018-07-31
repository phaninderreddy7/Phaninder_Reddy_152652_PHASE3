package com.cg.WalletApp.repo;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.cg.WalletApp.exception.WalletException;
import com.cg.WalletApp.beans.Customer;

public interface IWalletRepo {


	String addCustomer(Customer customer);

	void beginTransaction();

	void commitTransaction();

	Customer showBalance(String mobileNum, String password);

	Customer findCustomer(String mobileNum, String password);

	void deposit(Customer customer, BigDecimal amount) throws SQLException, ClassNotFoundException, WalletException;

	boolean withdraw(Customer customer, BigDecimal amount) throws ClassNotFoundException, SQLException, WalletException;

	boolean customerExists(String receiverMobile);

	boolean transfer(String senderMobile, String receiverMobile, BigDecimal amount) throws ClassNotFoundException, SQLException, WalletException;

	String printTransactions(Customer customer) throws ClassNotFoundException, SQLException;

	

}
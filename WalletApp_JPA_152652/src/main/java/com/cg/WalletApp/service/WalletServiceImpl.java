package com.cg.WalletApp.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cg.WalletApp.exception.WalletException;
import com.cg.WalletApp.exception.IWalletException;
import com.cg.WalletApp.beans.Customer;
import com.cg.WalletApp.repo.IWalletRepo;
import com.cg.WalletApp.repo.WalletRepoImpl;

public class WalletServiceImpl implements IWalletService {
	static IWalletRepo iWalletDao = null;

	static {

		iWalletDao = new WalletRepoImpl();
	}

	public void checkName(String name) throws WalletException {
		Pattern pattern = Pattern.compile("[a-zA-Z]{1,}");
		Matcher matcher = pattern.matcher(name);
		if (!(matcher.matches())) {
			throw new WalletException(IWalletException.nameException);
		}
	}

	public void checkMobileNumber(String mobileNumber) throws WalletException {

		Pattern pattern = Pattern.compile("[7-9]{1}[0-9]{9}");
		Matcher matcher = pattern.matcher(mobileNumber);
		if (!(matcher.matches())) {
			throw new WalletException(IWalletException.mobileNumberException);
		}
	}

	public void checkPassword(String password) throws WalletException {
		Pattern pattern = Pattern.compile(".*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*" + "");
		Matcher matcher = pattern.matcher(password);
		if (!(matcher.matches())) {
			throw new WalletException(IWalletException.passwordException);
		}

	}

	public void checkEmail(String emailId) throws WalletException {
		Pattern pattern = Pattern.compile("[a-z]{1}[a-z0-9._]{1,}@[a-zA-Z0-9]{1,}.com");
		Matcher matcher = pattern.matcher(emailId);
		if (!(matcher.matches())) {
			throw new WalletException(IWalletException.emailIdException);
		}

	}

	public String addCustomer(Customer customer) throws WalletException {
		String result = null;
		iWalletDao.beginTransaction();
		result = iWalletDao.addCustomer(customer);
		iWalletDao.commitTransaction();

		/*
		 * else throw new BankException(IBankException.ACCOUNTEXISTS);
		 */
		return result;
	}

	public Customer showBalance(String mobileNum, String password) throws WalletException {
		Customer result = null;
		result = iWalletDao.showBalance(mobileNum, password);

		if (result == null)
			throw new WalletException(IWalletException.invalidDetails);

		return result;
	}

	public Customer check(String mobileNum, String password) throws WalletException {
		Customer result = null;
		result = iWalletDao.findCustomer(mobileNum, password);

		if (result == null)
			throw new WalletException(IWalletException.invalidDetails);
		return result;
	}

	public void deposit(Customer customer, BigDecimal amount)
			throws ClassNotFoundException, SQLException, WalletException {
		iWalletDao.beginTransaction();
		iWalletDao.deposit(customer, amount);
		iWalletDao.commitTransaction();

	}

	public boolean withDraw(Customer customer, BigDecimal amount)
			throws WalletException, ClassNotFoundException, SQLException {
		boolean result = false;
		iWalletDao.beginTransaction();
		result = iWalletDao.withdraw(customer, amount);
		iWalletDao.commitTransaction();
		if (result == false) {
			throw new WalletException(IWalletException.insufficientFunds);
		}
		return result;
	}

	public boolean isFound(String receiverMobile) throws WalletException {
		boolean result = false;
		iWalletDao.beginTransaction();
		if (iWalletDao.customerExists(receiverMobile)) {
			result = true;
		}
		iWalletDao.commitTransaction();
		if (result == false)
			throw new WalletException(IWalletException.mobileNumberNotExists);

		return result;
	}

	public boolean transfer(String senderMobile, String receiverMobile, BigDecimal amount)
			throws InterruptedException, WalletException, ClassNotFoundException, SQLException {
		
		boolean result=false;
	
		iWalletDao.beginTransaction();
		result=iWalletDao.transfer(senderMobile,receiverMobile,amount);
		iWalletDao.commitTransaction();
		return result;
	}

	public String printTransactions(Customer customer) throws ClassNotFoundException, SQLException {
		
		iWalletDao.beginTransaction();
	    String builder = iWalletDao.printTransactions(customer);
		iWalletDao.commitTransaction();
		return builder;
	}



}
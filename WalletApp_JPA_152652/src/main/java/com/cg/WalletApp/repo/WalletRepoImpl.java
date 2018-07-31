package com.cg.WalletApp.repo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;

import com.cg.WalletApp.exception.WalletException;
import com.cg.WalletApp.jpaUtil.JPAUtil;
import com.cg.WalletApp.exception.IWalletException;
import com.cg.WalletApp.beans.Customer;


public class WalletRepoImpl implements IWalletRepo{
	private static EntityManager ENTITY_MANAGER=JPAUtil.getEntityManager();


public String addCustomer(Customer customer) {
	ENTITY_MANAGER.persist(customer);
	return customer.getMobileNumber();	
}
public void beginTransaction() {
	ENTITY_MANAGER.getTransaction().begin();
	
}
public void commitTransaction() {
	ENTITY_MANAGER.getTransaction().commit();
}
public Customer showBalance(String mobileNum, String password) {
	Customer customer = ENTITY_MANAGER.find(Customer.class,mobileNum);
	if(customer.getPassword().equals(password))
		return customer;
	else 
		return null;
	
}
public Customer findCustomer(String mobileNum, String password) {
	
	Customer customer = ENTITY_MANAGER.find(Customer.class,mobileNum);
	if(customer.getPassword().equals(password))
		return customer;
	else 
		return null;
}
public void deposit(Customer customer, BigDecimal amount) throws SQLException, ClassNotFoundException, WalletException {
	BigDecimal newAmount=customer.getWallet().getBalance().add(amount);
	customer.getWallet().setBalance(newAmount);
	ENTITY_MANAGER.merge(customer);	
	Class.forName("oracle.jdbc.driver.OracleDriver");
	Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","Capgemini123");
    String query = "Insert Into Transactions VALUES (?,?,?,?,?)";
    PreparedStatement pstmt= con.prepareStatement(query);
    pstmt.setInt(1, getTransactionId());
    pstmt.setString(2,customer.getMobileNumber());
    java.util.Date today = new java.util.Date();
    pstmt.setTimestamp(3,new java.sql.Timestamp(today.getTime()) );
    pstmt.setString(4, "Credited");
    pstmt.setBigDecimal(5, amount);
    pstmt.executeUpdate();
	
}
private int getTransactionId() throws ClassNotFoundException, WalletException {
	// TODO Auto-generated method stub
	int empId = 0;
	String sql = "SELECT transaction_sequence.NEXTVAL FROM DUAL";
	try {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","Capgemini123");
		PreparedStatement pstmt = con.prepareStatement(sql);
		ResultSet res = pstmt.executeQuery();
		if (res.next()) {
			empId = res.getInt(1);
		}
	} catch (SQLException e) {
         throw new WalletException(IWalletException.sqlException);
	}
	return empId;
}
public boolean withdraw(Customer customer, BigDecimal amount) throws ClassNotFoundException, SQLException, WalletException {
	boolean result=false;
	if(customer.getWallet().getBalance().subtract(amount).compareTo(BigDecimal.valueOf(0.0)) >= 0) {
	BigDecimal newAmount=customer.getWallet().getBalance().subtract(amount);
	customer.getWallet().setBalance(newAmount);
	ENTITY_MANAGER.merge(customer);	
	Class.forName("oracle.jdbc.driver.OracleDriver");
	Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","Capgemini123");
    String query = "Insert Into Transactions VALUES (?,?,?,?,?)";
    PreparedStatement pstmt= con.prepareStatement(query);
    pstmt.setInt(1, getTransactionId());
    pstmt.setString(2,customer.getMobileNumber());
    java.util.Date today = new java.util.Date();
    pstmt.setTimestamp(3,new java.sql.Timestamp(today.getTime()) );
    pstmt.setString(4, "Debited");
    pstmt.setBigDecimal(5, amount);
    int i=pstmt.executeUpdate();
    if(i==1)
    	result=true;
	}
	return result;
}
public boolean customerExists(String receiverMobile) {
	boolean result= false;
	Customer customer = null;
	customer=ENTITY_MANAGER.find(Customer.class,receiverMobile);
	if(customer!=null)
		result=true;
	return result;
}
public boolean transfer(String senderMobile, String receiverMobile, BigDecimal amount) throws ClassNotFoundException, SQLException, WalletException {
	boolean result=false;
	Customer receiverCustomer = ENTITY_MANAGER.find(Customer.class,receiverMobile);
	Customer senderCustomer = ENTITY_MANAGER.find(Customer.class,senderMobile);
	if(withdraw(senderCustomer, amount))
	{
		deposit(receiverCustomer, amount);
		result=true;
	}
	return result;
}
public String printTransactions(Customer customer) throws ClassNotFoundException, SQLException {
	Class.forName("oracle.jdbc.driver.OracleDriver");
	Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","Capgemini123");
    String query = "Select * from transactions where Mobile_no = '"+customer.getMobileNumber()+"' order by id";
    PreparedStatement pstmt= con.prepareStatement(query);
    ResultSet resultSet= pstmt.executeQuery();
    StringBuilder builder=new StringBuilder();
    while(resultSet.next())
    {
    	builder.append(resultSet.getTimestamp("TIMESTAMPOFTRANS") + " " + resultSet.getString("TYPE")+ " " + resultSet.getBigDecimal("AMOUNT"));
    	builder.append(",");
    }
	return builder.toString();
}


}
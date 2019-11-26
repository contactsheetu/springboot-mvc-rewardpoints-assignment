package com.demo.rewardsystem.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.demo.rewardsystem.model.PaymentModel;
import com.demo.rewardsystem.model.UserModel;



@Repository
public class RewardSystemRepository {


		// Spring Boot will create and configure DataSource and JdbcTemplate
		// To use it, just @Autowired
	    @Autowired
	    private JdbcTemplate jdbcTemplate;

	    public List<UserModel> fetchAllUsers() {
	        return jdbcTemplate.query(
	                "SELECT * FROM USER",
	                (rs, rowNum) ->
	                        new UserModel(
	                                rs.getString("id"),
	                                rs.getString("firstname"),
	                                rs.getString("lastname"),
	                                rs.getString("dateofjoining")
	                        )
	        );
	    }

	    public UserModel findById(int id) {
	        return jdbcTemplate.queryForObject(
	                "SELECT * FROM USER WHERE ID = ?",
	                new Object[]{id},
	                (rs, rowNum) ->
	                	new UserModel(
	                                rs.getString("id"),
	                                rs.getString("firstname"),
	                                rs.getString("lastname"),
	                                rs.getString("dateofjoining")
	                        )
	        );
	    }	    

	    
	    /**
	     * @return
	     */
	    public List<PaymentModel> fetchPaymentDetailsByUserID(int userID) {
	        return jdbcTemplate.query(
	                "SELECT P.PAYMENTID, P.AMOUNT, P.DESCRIPTION, P.PAYMENTDATE, U.ID, U.FIRSTNAME, U.LASTNAME, U.DATEOFJOINING FROM PAYMENT P,USER U WHERE P.USER_ID = U.ID AND"
				                + " U.ID = ?", new Object[]{userID}, new ResultSetExtractor<List<PaymentModel>>(){
				
				    public List<PaymentModel> extractData(ResultSet rs) throws SQLException, DataAccessException {
				
			        List<PaymentModel> allTransactions = new ArrayList<PaymentModel>();
			        UserModel user = null;  
				    while (rs.next()) {
				    	
				    	if(user == null) {
				    		user = new UserModel(
				    		rs.getString("id"),
				            rs.getString("firstname"),
				            rs.getString("lastname"),
				            rs.getString("dateofjoining"));
				    	}
				    	
				    	PaymentModel paymentModel = new PaymentModel(rs.getString("paymentid"),rs.getDouble("amount"), rs.getDate("PAYMENTDATE"), 
				    			rs.getString("DESCRIPTION"));
				         	
				            paymentModel.setUser(user);
				            allTransactions.add(paymentModel);
				
				            }
				        return allTransactions;
				    }
				});
	    }
	    
	    
	    /**
	     * @return
	     */
	    public Map<UserModel, List<PaymentModel>> fetchPaymentDetails() {
	        return jdbcTemplate.query(
	                "SELECT P.PAYMENTID, P.AMOUNT, P.DESCRIPTION, P.PAYMENTDATE, U.ID, U.FIRSTNAME, U.LASTNAME, U.DATEOFJOINING "
	                + "FROM PAYMENT P,USER U WHERE P.USER_ID = U.ID" , new ResultSetExtractor<Map<UserModel, List<PaymentModel>>>(){
				
	                public Map<UserModel, List<PaymentModel>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				    	
					    Map<UserModel, List<PaymentModel>> userPaymentDetails = new HashMap<UserModel, List<PaymentModel>>();
					    List<PaymentModel> userTransactions = new ArrayList<PaymentModel>();
								while (rs.next()) {
									UserModel user = new UserModel(rs
											.getString("id"), rs
											.getString("firstname"), rs
											.getString("lastname"), rs
											.getString("dateofjoining"));

									if (!userPaymentDetails.containsKey(user)) {
										userTransactions = new ArrayList<PaymentModel>();
										userPaymentDetails.put(user,
												userTransactions);
									} else if(userPaymentDetails.containsKey(user)) {
										userTransactions = userPaymentDetails.get(user);
									}

									PaymentModel paymentModel = new PaymentModel(
											rs.getString("paymentid"), rs
													.getDouble("amount"), rs
													.getDate("PAYMENTDATE"), rs
													.getString("DESCRIPTION"));

									paymentModel.setUser(user);
									userTransactions.add(paymentModel);

								}
								return userPaymentDetails;
							}
				});
	    }

}
package com.demo.rewardsystem.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AggregateRewardPointsModel {
	
	UserModel user;
	
	Double totalAmount;
	
	Long totalRewardPoints;

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		BigDecimal bd = new BigDecimal(totalAmount).setScale(2, RoundingMode.HALF_UP);
		this.totalAmount = bd.doubleValue();
	}

	public Long getTotalRewardPoints() {
		return totalRewardPoints;
	}

	public void setTotalRewardPoints(Long totalRewardPoints) {
		this.totalRewardPoints = totalRewardPoints;
	}
	
	

}

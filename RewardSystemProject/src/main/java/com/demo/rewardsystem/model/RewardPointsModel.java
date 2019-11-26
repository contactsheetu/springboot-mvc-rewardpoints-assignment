package com.demo.rewardsystem.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RewardPointsModel {
	
	Double amount = 0.0;

	String rewardMonth;

	Long rewardPoints = 0L;

	Long totalRewardPoints = 0L;

	UserModel user;

	public void addAmount(Double amount) {
		BigDecimal bd = new BigDecimal(amount).setScale(2, RoundingMode.CEILING);
		this.amount += bd.doubleValue();
	}

	public void addRewardPoints(Long rewardPoints) {
		this.rewardPoints += rewardPoints;
	}
	
	public void addTotalRewardPoints(Long totalRewardPoints) {
		this.totalRewardPoints += totalRewardPoints;
	}
	
	public Double getAmount() {
		BigDecimal bd = new BigDecimal(amount).setScale(2, RoundingMode.CEILING);
		return bd.doubleValue();
	}
	
	public String getRewardMonth() {
		return rewardMonth;
	}
	
	public Long getRewardPoints() {
		return rewardPoints;
	}

	public Long getTotalRewardPoints() {
		return totalRewardPoints;
	}

	public UserModel getUser() {
		return user;
	}

	public void setRewardMonth(String rewardMonth) {
		this.rewardMonth = rewardMonth;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}
	
}

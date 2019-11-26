package com.demo.rewardsystem.model;

import java.util.List;

public class MonthlyRewardPointsModel {
	
	List<RewardPointsModel> rewardPoints;
	
	Long totalRewardPoints;

	UserModel user;

	public List<RewardPointsModel> getRewardPoints() {
		return rewardPoints;
	}
	
	public Long getTotalRewardPoints() {
		return totalRewardPoints;
	}
	
	public UserModel getUser() {
		return user;
	}

	public void setRewardPoints(List<RewardPointsModel> rewardPoints) {
		this.rewardPoints = rewardPoints;
	}


	public void setTotalRewardPoints(Long totalRewardPoints) {
		this.totalRewardPoints = totalRewardPoints;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}
	
}

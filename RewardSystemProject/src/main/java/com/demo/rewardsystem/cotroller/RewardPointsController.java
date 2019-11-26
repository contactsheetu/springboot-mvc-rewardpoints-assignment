package com.demo.rewardsystem.cotroller;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.demo.rewardsystem.helper.MonthComparator;
import com.demo.rewardsystem.model.AggregateRewardPointsModel;
import com.demo.rewardsystem.model.MonthlyRewardPointsModel;
import com.demo.rewardsystem.model.PaymentModel;
import com.demo.rewardsystem.model.RewardPointsModel;
import com.demo.rewardsystem.model.UserModel;
import com.demo.rewardsystem.repository.RewardSystemRepository;


@Controller
public class RewardPointsController {

	
	@Autowired
	RewardSystemRepository rewardSystemRepository;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat();

	static {
		sdf.applyPattern("MM/dd/yyyy");
	}
	
	@Value("${welcome.message}")
	private String welcomeMessage = "";

	/*
	 * 
	 */
	@RequestMapping("/")
	public String goToHome(Model messageModel) {
		messageModel.addAttribute("welcomeMessage", this.welcomeMessage);
		return "index";
	}

	/*
	 * 
	 */
	@GetMapping("getUserList")
	public String getUserList(Model m) {
		m.addAttribute("userList", rewardSystemRepository.fetchAllUsers());
		return "userList";
	}

	/*
	 * 
	 */
	@GetMapping("getPaymentDetails")
	@RequestMapping(value = "/getPaymentDetails/{userID}", method = RequestMethod.GET)
	public String getPaymentList(@PathVariable("userID") String userID, Model m) {
		m.addAttribute("userDetail", rewardSystemRepository.findById(Integer.valueOf(userID)));
		m.addAttribute("paymentDetails", rewardSystemRepository.fetchPaymentDetailsByUserID(Integer.valueOf(userID)));
		return "paymentDetails";
	}

	/*
	 * 
	 */
	@GetMapping("getQuarterlyRewardPoints")
	@RequestMapping(value = "/getQuarterlyRewardPoints/{userID}/{quaterNYear}", method = RequestMethod.GET)
	public String getQuarterlyRewardPoints(
			@PathVariable(value = "userID", required = true) String userID,
			@PathVariable(value = "quaterNYear", required = true) String quarter,
			Model m) {

		Calendar quarterStart = Calendar.getInstance();
		Calendar quarterEnd = Calendar.getInstance();
		setQuaterDates(Integer.valueOf(quarter), quarterStart, quarterEnd);
		
		Calendar paymentDate = Calendar.getInstance();
		Map<String, RewardPointsModel> quarterlyPaymentDataMap = new TreeMap<String, RewardPointsModel>(new MonthComparator<String>());
		List<PaymentModel> userPaymentDetails =  rewardSystemRepository.fetchPaymentDetailsByUserID(Integer.valueOf(userID));
		 
		long totalRewardPoints = 0;
		RewardPointsModel rewardPointModel = null;
		for(PaymentModel paymentDetails : userPaymentDetails) {
			  
			 	//This loop will create dataset, for 3 months transactions
			 	//3 entries will be added in map for 3 months and transaction amount would be aggregated
			 	
			 	if(paymentDetails.getPaymentDate() != null) {
			 		if (((paymentDetails.getPaymentDate().after(quarterStart.getTime())) || (paymentDetails.getPaymentDate().equals(quarterStart.getTime())))
							&& paymentDetails.getPaymentDate().before(quarterEnd.getTime())) {
						
						paymentDate.setTime(paymentDetails.getPaymentDate());
						String paymentMonth = Month.of(paymentDate.get(Calendar.MONTH) + 1).name();
						
						if(quarterlyPaymentDataMap.get(paymentMonth) == null) {
							rewardPointModel = new RewardPointsModel();
							quarterlyPaymentDataMap.put(paymentMonth, rewardPointModel);
						} else {
							rewardPointModel = quarterlyPaymentDataMap.get(paymentMonth) ;
						}
						
						rewardPointModel.setRewardMonth(paymentMonth);
						rewardPointModel.addAmount(paymentDetails.getAmount());
						long lRewardPoints = calcRewardPoints(paymentDetails.getAmount());
						rewardPointModel.addRewardPoints(lRewardPoints);
						totalRewardPoints += lRewardPoints;
					}
				}
		}
		 
		m.addAttribute("totalRewardPoints", totalRewardPoints);
		m.addAttribute("userDetail", rewardSystemRepository.findById(Integer.valueOf(userID)));
		m.addAttribute("quarterlyRewardPoints", quarterlyPaymentDataMap.values());
		return "quarterlyRewardPoints";
	}

	/*
	 * 
	 */
	@GetMapping("getMonthlyRewardPoints")
	public String getMonthlyRewardPoints(Model m) {
		
		 List<MonthlyRewardPointsModel> monthlyRewardPoints = new LinkedList<MonthlyRewardPointsModel>();
		 
		 Calendar paymentDate = Calendar.getInstance();

		 Map<UserModel, List<PaymentModel>> userPaymentDetails =  rewardSystemRepository.fetchPaymentDetails();
		 
		 for(Entry<UserModel, List<PaymentModel>> userPaymentDetail : userPaymentDetails.entrySet()) {
			 
			 Map<String, RewardPointsModel> monthlyPaymentDataMap = new TreeMap<String, RewardPointsModel>(new MonthComparator<String>());		 
			 MonthlyRewardPointsModel monthlyRewardPoint = new MonthlyRewardPointsModel();
			 RewardPointsModel rewardPointModel = null;
			 long totalRewardPoints = 0;
			 
			 for(PaymentModel paymentDetails : userPaymentDetail.getValue()) {
				 	if(paymentDetails.getPaymentDate() != null) {
							paymentDate.setTime(paymentDetails.getPaymentDate());
							String paymentMonth = Month.of(paymentDate.get(Calendar.MONTH) + 1).name();
							
							if (monthlyPaymentDataMap.get(paymentMonth) == null) {
								rewardPointModel = new RewardPointsModel();
								monthlyPaymentDataMap.put(paymentMonth, rewardPointModel);
							} else {
								rewardPointModel = monthlyPaymentDataMap.get(paymentMonth);
							}
							
							rewardPointModel.setRewardMonth(paymentMonth);
							rewardPointModel.addAmount(paymentDetails.getAmount());
							long rewardPoints = calcRewardPoints(paymentDetails.getAmount());
							rewardPointModel.addRewardPoints(rewardPoints);
							totalRewardPoints += rewardPoints;

					}
			 }

			 monthlyRewardPoint.setUser(userPaymentDetail.getKey());
			 monthlyRewardPoint.setTotalRewardPoints(totalRewardPoints);
			 monthlyRewardPoint.setRewardPoints(new ArrayList<RewardPointsModel>(monthlyPaymentDataMap.values()));
			 monthlyRewardPoints.add(monthlyRewardPoint);
			 
			
		 }
		
		m.addAttribute("monthlyRewardPoints", monthlyRewardPoints);
		return "monthlyRewardPoints";
	}	
	
	
	/*
	 * 
	 */
	@GetMapping("getAggregateRewardPoints")
	public String getTotalRewardPoints(Model m) {
		
		 List<AggregateRewardPointsModel> aggregateRewardPoints = new ArrayList<AggregateRewardPointsModel>();
		 Map<UserModel, List<PaymentModel>> userPaymentDetails =  rewardSystemRepository.fetchPaymentDetails();
		 
		 for(Entry<UserModel, List<PaymentModel>> userPaymentDetail : userPaymentDetails.entrySet()) {
			 Double totalPayment = 0.0;
			 long totalRewardPoints = 0;
			 for(PaymentModel paymentDetails: userPaymentDetail.getValue()) {
			 	if(paymentDetails.getAmount() != null) {
			 			totalPayment =  totalPayment + paymentDetails.getAmount();
			 			totalRewardPoints = totalRewardPoints + calcRewardPoints(paymentDetails.getAmount());
				}
			 }
			 
		 
			 AggregateRewardPointsModel totalRewardPoint = new AggregateRewardPointsModel();
			 totalRewardPoint.setUser(userPaymentDetail.getKey());
			 totalRewardPoint.setTotalAmount(totalPayment);
			 totalRewardPoint.setTotalRewardPoints(totalRewardPoints);
			 aggregateRewardPoints.add(totalRewardPoint);
		 }
		
		
		m.addAttribute("aggregateRewardPoints", aggregateRewardPoints);
		return "aggregateRewardPoints";
	}
	
	
	private void setQuaterDates(int quarter, Calendar quarterStart, Calendar quarterEnd) {

		
		String currentQuarterBeginDate = "";
		String fromDt=""; String toDt="";
		
		
		if ( quarter ==1){
			currentQuarterBeginDate = "01/01/"+quarterStart.get(Calendar.YEAR);
		}
		if ( quarter ==2){
			currentQuarterBeginDate = "04/01/"+quarterStart.get(Calendar.YEAR);
		}
		if ( quarter ==3){
			currentQuarterBeginDate = "07/01/"+quarterStart.get(Calendar.YEAR);
		}
		if ( quarter ==4){
			currentQuarterBeginDate = "10/01/"+quarterStart.get(Calendar.YEAR);
		}
		try {
			quarterStart.setTime(sdf.parse(currentQuarterBeginDate));
			fromDt = sdf.format(quarterStart.getTime());
			
			quarterEnd.setTime(quarterStart.getTime());
			quarterEnd.add(Calendar.MONTH, 3);
			toDt = sdf.format(quarterEnd.getTime());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Long calcRewardPoints(Double dAmount){
		
		Long rewardPoints = 0L;
		//2 points for every dollar spent over $100 in each transaction, plus 1 point for every dollar spent over $50 in each transaction
		Long lAmount = Math.round(dAmount);
		
		if(lAmount > 50 && lAmount <=100) {
			rewardPoints = (lAmount - 50);
		} else if(lAmount > 100) {
				rewardPoints = 50 + (2 * (lAmount - 100)); 
		}
		
		return rewardPoints;
	}
}

package cst.gu.util.sql.test;

import java.util.Map;



/**
 * @author guweichao 20171102
 * 
 */
public class MainTest {
	static String charset = "utf-8";
	public static void main(String[] args) {
		System.out.println(Integer.MAX_VALUE );
		System.out.println(Integer.MAX_VALUE + Integer.MAX_VALUE);
	}
	protected void doPost(Map<String,String> map)   {
		String amount = map.get("creditAmount");
		int value = Integer.parseInt(amount);
		String prevCreditBalanceS = map.get("user");
		int prevCreditBalance = 0;
		if(prevCreditBalanceS != null){
			prevCreditBalance = Integer.parseInt(prevCreditBalanceS);
		}
		// maximum credit is 5000
		int sum = prevCreditBalance + value;
		if ((prevCreditBalance > 0 == value > 0) && (sum >= 0 != prevCreditBalance >= 0)) {
			// 溢出
		}else{
			if(sum > 5000){
				User u = new User();
				System.out.println(u);
			}
		}
		 
	}
	
	protected void doPost1(Map<String,String> map)   {
		String amount = map.get("creditAmount");
		int value = Integer.parseInt(amount);
		int prevCreditBalance = 0;
		String prevCreditBalanceS = map.get("user");
		if(prevCreditBalanceS != null){
			prevCreditBalance = Integer.parseInt(prevCreditBalanceS);
		}
		if (prevCreditBalance + value > 5000) {
			User u = new User();
			System.out.println(u);
		} 
	}

}

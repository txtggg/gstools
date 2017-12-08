package cst.gu.util.sql.test;

import java.math.BigInteger;
import java.util.Map;

import sun.security.util.BigInt;



/**
 * @author guweichao 20171102
 * 
 */
public class MainTest {
	static String charset = "utf-8";
	public static void main(String[] args) {
		BigInt bi = new BigInt(1);
		for(int x = 0;x < 100000;x++){
		}
		System.out.println(bi);
		
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

package cst.util.common.string;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public class IpMacUtil {
	

	/**
	 * 返回客户端真实ip
	 * 如果无法获取,可能返回null
	 * 本地ip统一返回 localhost
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = getIpByHeader(request, "X-Forwarded-For");
		if(ip!=null){
			return getFirstIp(ip);
		}
		
		ip = getIpByHeader(request, "Proxy-Client-IP");
		if(ip != null){
			return getFirstIp(ip);
		}
		

		ip = getIpByHeader(request, "WL-Proxy-Client-IP");
		if(ip != null){
			return getFirstIp(ip);
		}
		
		ip = request.getRemoteAddr();
		return changeLocalhostIp(ip);
	}
	
	/**
		 * 可能返回null
		 * 不带有- :等,只有16进制的数字
		 * @param ip
		 * @return
		 * @throws UnknownHostException
		 * @throws SocketException
		 */
		public static String getMACAddress(String ip) throws UnknownHostException, SocketException {
			String line = "";
			String macAddress = "";
			final String LOOPBACK_ADDRESS = "localhost";
			Set<String> macPrefixes = new HashSet<String>();
			macPrefixes.add("MAC Address = ");
			macPrefixes.add("MAC 地址 = ");
	//		Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();  
			// 如果为127.0.0.1,则获取本地MAC地址。
			if (LOOPBACK_ADDRESS.equals(ip)) {
				InetAddress inetAddress = InetAddress.getLocalHost();
				// 貌似此方法需要JDK1.6。
				byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
				// 下面代码是把mac地址拼装成String
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < mac.length; i++) {
					if (i != 0) {
						sb.append("");
					}
					// mac[i] & 0xFF 是为了把byte转化为正整数
					String s = Integer.toHexString(mac[i] & 0xFF);
					sb.append(s.length() == 1 ? 0 + s : s);
				}
				// 把字符串所有小写字母改为大写成为正规的mac地址并返回
				macAddress = sb.toString().trim().toUpperCase();
				return macAddress;
			}
			// 获取非本地IP的MAC地址
			try {
				Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
				InputStreamReader isr = new InputStreamReader(p.getInputStream(),Charset.forName("GBK"));
				BufferedReader br = new BufferedReader(isr);
				while ((line = br.readLine()) != null) {
					if (line != null) {
						for(String macPrefix : macPrefixes){
							int index = line.indexOf(macPrefix);
							if (index != -1) {
								macAddress = line.substring(index + macPrefix.length()).trim().toUpperCase();
								break;
							}
						}
					}
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return macAddress.replaceAll("-", "");
		}

	private static String getFirstIp(String ip){
		String[] ips = ip.split(",");
		for(String s : ips){
			if(isIp(s)){
				return s;
			}
		}
		return null;
	}
	
	private static String getIpByHeader(HttpServletRequest request,String HeaderName) {
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()){
			String hn = headerNames.nextElement();
			if(hn.equalsIgnoreCase(HeaderName)){
				String ip = request.getHeader(hn);
				if(isIp(ip)){
					return changeLocalhostIp(ip);
				}
				return null;
			}
		}
		return null;
	}
	
	private static String changeLocalhostIp(String ip){
		if("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)||"::1".equals(ip)){
			ip = "localhost";
		}
		return ip;
	}

	private static boolean isIp(String ip){
		return ip!= null && ip.length() >0 &&!ip.equalsIgnoreCase("unknown");
	}
}

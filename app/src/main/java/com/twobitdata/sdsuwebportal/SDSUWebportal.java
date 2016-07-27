package com.twobitdata.sdsuwebportal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

import java.net.URL;

public class SDSUWebportal {
	
	public static SDSUWebportal instance = new SDSUWebportal();
	
	public String sessionID;
	private CookieManager cookieManager;
	
	public SDSUWebportal(){
		cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);
	}
	
	
	public Boolean login(String username, String password) throws Exception {
		String url = "https://sunspot.sdsu.edu/AuthenticationService/loginVerifier.html" + "?pc=portal";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");

		con.setRequestProperty("Origin", "https://sunspot.sdsu.edu");
		con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		con.setRequestProperty("Cache-Control", "max-age=0");
		con.setRequestProperty("Upgrade-Insecure-Requests", "1");
		con.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
		con.setRequestProperty("Referer",
				"https://sunspot.sdsu.edu/AuthenticationService/loginVerifier.html?pc=portal");
		con.setRequestProperty("Connection", "keep-alive");
		con.setRequestProperty("Host", "sunspot.sdsu.edu");
		con.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
		con.setRequestProperty("DNT", "1");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		String postData = "userPassword=" + password
				+ "&partnerParm=pc=portal&partnerName=WebPortal&partnerHomepage=https://sunspot.sdsu.edu/pls/webapp/web_menu.login&showField=Y&partnerUrl=https://sunspot.sdsu.edu/pls/webapp/web_menu2.homepage&partnerDomainName=sunspot_ssl_portal&userName="
				+ username + "&login=Sign+In";
		con.setDoOutput(true);

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(postData);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		//System.out.println("Sending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);

		InputStream input = con.getInputStream();
		if ("gzip".equals(con.getContentEncoding())) {
			input = new GZIPInputStream(input);
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		in.close();


		if(response.toString().contains("<meta content=\"The San Diego State University Authentication Service is a single sign-on protocol for online services. Its purpose is to permit an SDSU user to access multiple SDSU applications while providing credentials only once.\" name=\"DESCRIPTION\" />")){
			System.out.println(response.toString());
			throw new Exception();
		}
		
		sessionID = con.getURL().toString().substring(con.getURL().toString().indexOf("sess_id=") + 8, con.getURL().toString().length());
		
		//System.out.println("sessionID URL: " + sessionID);
		return true;
	}

	public String admission(String sessionId) throws Exception {
		String url = "https://sunspot.sdsu.edu/pls/webapp/appcheck03.htm" + "?sess_id=" + sessionId + "&menu=2";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		con.setRequestProperty("Upgrade-Insecure-Requests", "1");
		con.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
		con.setRequestProperty("Referer",
				"https://sunspot.sdsu.edu/pls/webapp/web_menu.main?sess_id=" + sessionId + "&p_option=2");
		con.setRequestProperty("Connection", "keep-alive");
		con.setRequestProperty("Host", "sunspot.sdsu.edu");
		con.setRequestProperty("DNT", "1");
		con.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch, br");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.8");

		int responseCode = con.getResponseCode();
		//System.out.println("Sending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);

		InputStream input = con.getInputStream();
		if ("gzip".equals(con.getContentEncoding())) {
			input = new GZIPInputStream(input);
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		in.close();
		//System.out.println(response.toString());
		return response.toString();
	}

	public String registration(String sessionId) throws Exception {
		String url = "https://sunspot.sdsu.edu/schedule/login" + "?sess_id=" + sessionId + "&menu=2";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		con.setRequestProperty("Upgrade-Insecure-Requests", "1");
		con.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
		con.setRequestProperty("Referer",
				"https://sunspot.sdsu.edu/pls/webapp/web_menu.main_page?sess_id=728593272|10312546");
		con.setRequestProperty("Connection", "keep-alive");
		con.setRequestProperty("Host", "sunspot.sdsu.edu");
		con.setRequestProperty("DNT", "1");
		con.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch, br");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.8");

		int responseCode = con.getResponseCode();
		//System.out.println("Sending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);

		InputStream input = con.getInputStream();
		if ("gzip".equals(con.getContentEncoding())) {
			input = new GZIPInputStream(input);
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		in.close();
		//System.out.println(response.toString());
		return response.toString();
	}

	public String messageCenter(String sessionId) throws Exception {
		String url = "https://sunspot.sdsu.edu/pls/webapp/mc_msg_pkg.msg_list" + "?sess_id=" + sessionId + "&menu=2";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod("GET");

		con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		con.setRequestProperty("Upgrade-Insecure-Requests", "1");
		con.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
		con.setRequestProperty("Referer",
				"https://sunspot.sdsu.edu/pls/webapp/web_menu.main?sess_id=" + sessionId + "&p_option=2");
		con.setRequestProperty("Connection", "keep-alive");
		con.setRequestProperty("Host", "sunspot.sdsu.edu");
		con.setRequestProperty("DNT", "1");
		con.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch, br");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
		

		int responseCode = con.getResponseCode();
		//System.out.println("Sending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);

		InputStream input = con.getInputStream();
		if ("gzip".equals(con.getContentEncoding())) {
			input = new GZIPInputStream(input);
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		//System.out.println(response.toString());
		return response.toString();
	}

	public String logout (String sessionID) throws Exception
	{
		String userID = sessionID.substring(sessionID.indexOf("|")+1);
		String url = "https://sunspot.sdsu.edu/pls/webapp/user_session.p_invalidate" + "?p_message=&p_ref=N&p_session_id=" + sessionID + "&p_user_id=" + userID + "";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		con.setRequestProperty("Upgrade-Insecure-Requests", "1");
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
		con.setRequestProperty("Referer", "https://sunspot.sdsu.edu/pls/webapp/web_menu.main_page?sess_id=845058276|10312546");
		con.setRequestProperty("Connection", "keep-alive");
		con.setRequestProperty("Host", "sunspot.sdsu.edu");
		con.setRequestProperty("DNT", "1");
		con.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch, br");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.8");


		int responseCode = con.getResponseCode();
		System.out.println("Sending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		InputStream input = con.getInputStream()
				;if ("gzip".equals(con.getContentEncoding())) {
		input = new GZIPInputStream(input);
	}

		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {	response.append(inputLine);
		}

		in.close();
		System.out.println(response.toString());
		return response.toString();
	}

	public String getCalendar (String sessionID) throws Exception
	{
		String url = "https://sunspot.sdsu.edu/schedule/mycalendar?sess_id=" + sessionID + "&category=my_calendar";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		con.setRequestProperty("Upgrade-Insecure-Requests", "1");
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
		con.setRequestProperty("Referer", "https://sunspot.sdsu.edu/schedule/myregistrationinfo");
		con.setRequestProperty("Connection", "keep-alive");
		con.setRequestProperty("Host", "sunspot.sdsu.edu");
		con.setRequestProperty("DNT", "1");
		con.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch, br");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.8");


		int responseCode = con.getResponseCode();

		InputStream input = con.getInputStream();
		if ("gzip".equals(con.getContentEncoding())) {
			input = new GZIPInputStream(input);
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {	response.append(inputLine);
		}

		in.close();
		//System.out.println(response.toString());
		return response.toString();
	}
}
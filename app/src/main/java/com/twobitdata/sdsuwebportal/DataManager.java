package com.twobitdata.sdsuwebportal;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager extends AsyncTask<Void, Void, Boolean>{

	public static Map<String, String> AdmisionStatus = new HashMap<String, String>();

	public static Map<String, String> RegisrationStatus = new HashMap<String, String>();

	public static String username;
	public static String password;
	Intent logedIn;

	// Message Center
	static List<Message> messages;

	public static boolean login(String username, String password) {
		try {
			DataManager.username = username;
			DataManager.password = password;

			AsyncTask<Void, Void, Boolean> login = new AsyncTask<Void, Void, Boolean>() {
				@Override
				protected Boolean doInBackground(Void... params) {
					try {
						SDSUWebportal.instance.login(DataManager.username, DataManager.password);
						System.out.println("Logged in!");
						return true;
					} catch (Exception e) {
						System.err.println("NOPE, Login FAIL!!");
						System.err.println(e.toString());
						return false;
					}
				}
			};
			login.execute();
			return login.get();
		} catch(Exception e){
			return true;
		}
	}

	public static boolean retrieveData(){
		try {
			DataManager manager = new DataManager();
			manager.execute();
			return manager.get();
		} catch(Exception e) {
			return false;
		}

	}

	String[] admissionData;
	String[] registrationData;
	String[] messageData;


	@Override
	protected Boolean doInBackground(Void... params) {
		messages = new ArrayList<>();

		//Convert to lower API level
		try { admissionData = WebportalParser.admissionParser(SDSUWebportal.instance.admission(SDSUWebportal.instance.sessionID)); System.out.println("2 is done");} catch (Exception e) { System.err.println(e.toString() + " 2");}
		try { registrationData = WebportalParser.registrationParser(SDSUWebportal.instance.registration(SDSUWebportal.instance.sessionID)); System.out.println("3 is done");} catch (Exception e) { System.err.println(e.toString() + " 3");}
		try { messageData = WebportalParser.messageParser(SDSUWebportal.instance.messageCenter(SDSUWebportal.instance.sessionID)); System.out.println("4 is done");} catch (Exception e) { System.err.println(e.toString() + " 4");}
		try {System.out.println(WebportalParser.calendarCleaner(SDSUWebportal.instance.getCalendar(SDSUWebportal.instance.sessionID)));} catch(Exception e){};

		ArrayList<String> cleaned = new ArrayList<>();

		for (int i = 0; i < admissionData.length; i++) {
			if(!admissionData[i].trim().isEmpty() ){
				cleaned.add(admissionData[i]);
			}
		}

		for (int i = 1; i < cleaned.size() - 1 ; i+=2) {
			AdmisionStatus.put(cleaned.get(i), cleaned.get(i+1));
		}

		for (int i = 0; i < registrationData.length-1; i++){
			RegisrationStatus.put(registrationData[i], registrationData[i+1]);
		}


		// LOL, Don't you just love hard coding XD
		// Personally I hate it
		for (int i = 0; i < messageData.length; i += 6) {
			messages.add(new Message(messageData[i + 2], messageData[i + 3]));
		}



		System.out.println("WE MADE IT!");
		return true;
	}
}

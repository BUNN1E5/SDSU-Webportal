package com.twobitdata.sdsuportalunffl;

import android.content.Intent;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager{

	public static Map<String, String> AdmisionStatus = new HashMap<String, String>();

	public static Map<String, String> RegisrationStatus = new HashMap<String, String>();

	public static String username;
	public static String password;
	Intent logedIn;

	// Message Center
	static List<ListItem> messages;
	static List<ListItem> admissions; //With S for consistency
	static List<ListItem> registrations;
	static List<GradeItem> grades; // <- So much for polymorphism

	public static boolean doneLoading = false;


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


	//AAAH THe Workarounds!
	static String[] admissionData;
	static String[] registrationData;
	static String[] messageData;
	static String[] gradeData;

	public static void retrieveData(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				//So we don't relead everything more than once
				doneLoading = false;

				messages = new ArrayList<>();
				admissions = new ArrayList<>();
				registrations = new ArrayList<>();
				grades = new ArrayList<>();

				//Convert to lower API level
				try { admissionData = WebportalParser.admissionParser(SDSUWebportal.instance.admission(SDSUWebportal.instance.sessionID)); System.out.println("2 is done");} catch (Exception e) { System.err.println(e.toString() + " 2");}
				try { registrationData = WebportalParser.registrationParser(SDSUWebportal.instance.registration(SDSUWebportal.instance.sessionID)); System.out.println("3 is done");} catch (Exception e) { System.err.println(e.toString() + " 3");}
				try { messageData = WebportalParser.messageParser(SDSUWebportal.instance.messageCenter(SDSUWebportal.instance.sessionID)); System.out.println("4 is done");} catch (Exception e) { System.err.println(e.toString() + " 4");}
				try { gradeData = WebportalParser.gradesParser(SDSUWebportal.instance.grades(SDSUWebportal.instance.sessionID)); System.out.println("5 is done");} catch (Exception e) { System.err.println(e.toString() + " 5");}

				ArrayList<String> cleaned = new ArrayList<>();

				if(admissionData != null) {
					for (int i = 0; i < admissionData.length; i++) {
						if (!admissionData[i].trim().isEmpty()) {
							cleaned.add(admissionData[i]);
							//System.out.println(admissionData[i]);
						}
					}

					// -_- Hard coding
					cleaned.remove("Institution");
					cleaned.remove("Dates");
					cleaned.remove("Degree");
					cleaned.remove("HIGH SCHOOL");
					cleaned.remove("Type");
					cleaned.remove("Total");
					cleaned.remove("Date");
					cleaned.remove("Status");
					cleaned.remove("Status Details");
					cleaned.remove("Status");
					cleaned.remove("Status Details");
					cleaned.remove("Status");
					cleaned.remove("Status Details");
					cleaned.remove("Test Scores:");
					cleaned.remove("Freshmen Testing Requirements:");
					cleaned.remove("Type");

					for (int i = 1; i < cleaned.size() - 1; i += 2) {
						AdmisionStatus.put(cleaned.get(i), cleaned.get(i + 1));
						admissions.add(new ListItem(cleaned.get(i), cleaned.get(i + 1)));
					}
				}
				if(registrationData != null) {
					for (int i = 0; i < registrationData.length - 1; i += 2) {
						RegisrationStatus.put(registrationData[i], registrationData[i + 1]);
						registrations.add(new ListItem(registrationData[i], registrationData[i + 1]));
						//System.out.println(registrationData[i] + " " + registrationData[i + 1]);
					}
				}


				// LOL, Don't you just love hard coding XD
				// Personally I hate it
				if(messageData != null) {
					for (int i = 0; i < messageData.length; i += 6) {
						messages.add(new ListItem(messageData[i + 2], messageData[i + 3]));
					}
				}

				if(gradeData != null){
					for(int i = 4 ; i < gradeData.length -1 ; i+=3){
						GradeItem item = new GradeItem(gradeData[i], gradeData[i+1], gradeData[i+2]);
						grades.add(item);
					}
				}

				System.out.println("WE MADE IT!");
				doneLoading = true;
			}
		}).start();
	}

	//AAAHHHHH THE WORKAROUNDS
	static String classes;
	static boolean doneClasses = false;
	public static void cacheClasses(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					DataManager.classes = WebportalParser.calendarCleaner(SDSUWebportal.instance.getCalendar(SDSUWebportal.instance.sessionID));
					System.out.println("Classes have been cached!");
					doneClasses = true;
					//FileOutputStream stream = DataManager.context.openFileOutput("Classes.html", Context.MODE_PRIVATE);
					//stream.write(DataManager.classes.getBytes());
					//stream.close();
				}catch(Exception e){System.err.println(e.toString() + " Data Manager!");}
			}
		}).start();

	}
}

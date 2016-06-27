package com.twobitdata.sdsuwebportal;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Map;

public class DataManager {

    public static Dictionary<String, String> AdmisionStatus;

    //Regestration Data
    public static String
    RegistrationDate,
    FeePaymentStatus,
    MailingAdress,
    ClassLevel,
    Residency,
    FeePaymentDeadline,
    FeeDerfermentEligibility;

    //Message Center
    static ArrayList<Message> messages = new ArrayList<Message>();

    public static void login(String username, String password){
        try {
            SDSUWebportal.instance.login(username, password);

            String[] admissionData = WebportalParser.admissionParser(SDSUWebportal.instance.admission(SDSUWebportal.instance.sessionID));
            for (int i = 2; i < admissionData.length; i++){
                AdmisionStatus.put(admissionData[i-1], admissionData[i]);
            }

            String[] registrationData = WebportalParser.registrationParser(SDSUWebportal.instance.registration(SDSUWebportal.instance.sessionID));


            String[] messageData = WebportalParser.messageParser(SDSUWebportal.instance.messageCenter(SDSUWebportal.instance.sessionID));
            //LOL, Don't you just love hard coding XD
            //Personally I hate it
            for (int i = 0; i < messageData.length; i+=6) {
                messages.add(new Message(messageData[i + 2], messageData[i + 3]));
            }

        } catch(Exception e) {
            System.err.println(e.toString());
        }
    }
}

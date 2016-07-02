import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataManager {

	public static Map<String, String> AdmisionStatus = new HashMap<String, String>();

	public static Map<String, String> RegisrationStatus = new HashMap<String, String>();

	// Message Center
	static ArrayList<Message> messages = new ArrayList<Message>();

	public static void login(String username, String password) {
		try {
			SDSUWebportal.instance.login(username, password);

			String[] admissionData = WebportalParser.admissionParser(SDSUWebportal.instance.admission(SDSUWebportal.instance.sessionID));
			
			System.out.println(admissionData.length);
			
			ArrayList<String> cleaned = new ArrayList<>();
			cleaned.addAll(Arrays.asList(admissionData).stream().filter(ad -> !ad.trim().isEmpty()).collect(Collectors.toList()));
			
			for (int i = 1; i < cleaned.size() - 1 ; i+=2) {
				System.out.println(cleaned.get(i) + " | " + cleaned.get(i+1));
				AdmisionStatus.put(cleaned.get(i), cleaned.get(i+1));
			}
			
			String[] registrationData = WebportalParser.registrationParser(SDSUWebportal.instance.registration(SDSUWebportal.instance.sessionID));

			String[] messageData = WebportalParser.messageParser(SDSUWebportal.instance.messageCenter(SDSUWebportal.instance.sessionID));
			// LOL, Don't you just love hard coding XD
			// Personally I hate it
			for (int i = 0; i < messageData.length; i += 6) {
				messages.add(new Message(messageData[i + 2], messageData[i + 3]));
			}

		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
}

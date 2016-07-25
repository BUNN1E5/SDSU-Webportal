package com.twobitdata.sdsuwebportal;

import java.util.ArrayList;

public class WebportalParser {

	public static String[] messageParser(String data){
		String parseStatementBeginning = "><table border";
		String parseStatementEnd = "</form></table>";
		//System.out.println(data.indexOf(parseStatementBeginning)+ parseStatementBeginning.length());
		//System.out.println(data.indexOf(parseStatementEnd, data.indexOf(parseStatementBeginning)+ parseStatementBeginning.length()));
		
		String returnStatement = data.substring(data.indexOf(parseStatementBeginning)+ parseStatementBeginning.length(),
				data.indexOf(parseStatementEnd, data.indexOf(parseStatementBeginning)+ parseStatementBeginning.length()));
		
		String[] returnList = returnStatement.split("<td bgcolor");
		
		for (int i = 0; i < returnList.length; i++) {
			returnList[i] = returnList[i].substring(returnList[i].indexOf(">") + 1, returnList[i].indexOf("<"));
			if(returnList[i].contains("&nbsp;")){
				returnList[i] = "";
			}
		}
		
		return returnList;
	}
	
	
	public static String[] admissionParser(String data){
		
		data = data.replace("&nbsp;", "");
		
		//System.out.println(data.indexOf("<!--txt 99-N-0007-->"));
		//System.out.println(data.length());
		
		data = data.substring(data.indexOf("<!--txt 99-N-0007-->"), data.length());
		
		String[] returnData = data.split("<td");
		
		for (int i = 1; i < returnData.length; i++) {
			returnData[i] = returnData[i].substring(returnData[i].indexOf('>') + 1, returnData[i].indexOf('<'));
		}
		
		return returnData;
	}

	public static String[] registrationParser(String data){

		data = data.replace("&nbsp;", "");

		int leftNavBoxBeginIndex = data.indexOf("<div class=\"leftNavBox\">            <h3>My Registration Info</h3>") + ("<div class=\"leftNavBox\">            <h3>My Registration Info</h3>").length();
		String leftNavBoxFull = data.substring(leftNavBoxBeginIndex,
				data.indexOf("</div>", leftNavBoxBeginIndex));
		String[] leftNavBox = leftNavBoxFull.split("<span ");

		for (int i = 1; i < leftNavBox.length; i++) {
			if(leftNavBox[i].contains("<img")){
				leftNavBox[i] = leftNavBox[i].substring(leftNavBox[i].indexOf('>', leftNavBox[i].indexOf("<img")+ 4) + 1,
						leftNavBox[i].indexOf('<', leftNavBox[i].indexOf('>', leftNavBox[i].indexOf("<img")+ 4) + 1)).trim();
			}else if(leftNavBox[i].contains("<")){
				leftNavBox[i] = leftNavBox[i].substring(leftNavBox[i].indexOf('>') + 1, leftNavBox[i].indexOf('<')).trim();
			}else{
				leftNavBox[i] = "";
			}
		}

		int mainIndex = data.indexOf("<div id=\"bodyMainContent\"");
		String mainDataFull = data.substring(mainIndex);
		String[] mainInfo = mainDataFull.split("<td");

		for (int i = 1; i < mainInfo.length; i++) {
			if(mainInfo[i].contains("statusValues")){

				mainInfo[i] = mainInfo[i].substring(mainInfo[i].indexOf('>', mainInfo[i].indexOf("<span")) + 1,
						mainInfo[i].indexOf('<', mainInfo[i].indexOf('>', mainInfo[i].indexOf("</span>")) + 1)).trim();
				mainInfo[i] = mainInfo[i].replace("</span>", "").trim();

			}else if(mainInfo[i].contains("<span")){
				mainInfo[i] = mainInfo[i].substring(mainInfo[i].indexOf('>', mainInfo[i].indexOf("<span")) + 1,
						mainInfo[i].indexOf('<', mainInfo[i].indexOf('>', mainInfo[i].indexOf("<span")) + 1)).trim();
			} else{
				mainInfo[i] = mainInfo[i].substring(mainInfo[i].indexOf('>') + 1,
						mainInfo[i].indexOf('<', mainInfo[i].indexOf('>') + 1)).trim();
			}
		}

		ArrayList<String> returnData = new ArrayList<>();

		for(int i = 1; i < leftNavBox.length; i++){
			if(mainInfo[i].contains("<")){
				mainInfo[i] = "";
			}

			if(!leftNavBox[i].equals("")){
				//System.out.println(leftNavBox[i]);
				returnData.add(leftNavBox[i]);
			}
		}

		for(int i = 0; i < mainInfo.length; i++){
			if(mainInfo[i].contains("<")){
				mainInfo[i] = "";
			}

			if(!mainInfo[i].equals("")){
				//System.out.println(leftNavBox[i]);
				returnData.add(mainInfo[i]);
			}
		}

		for(int i = 0; i < returnData.size(); i++){
			System.out.println(returnData.get(i));
		}

		return returnData.toArray(new String[returnData.size()]);
	}

	public static String calendarCleaner(String data){
		String cleaned = data.substring(0, data.indexOf("</style>") + "</style>".length()) + data.substring(data.indexOf("<div id=\"calendarTitles\">"), data.indexOf("<div id=\"footer\">"));
		return cleaned;
	}
	
}

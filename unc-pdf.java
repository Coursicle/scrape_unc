import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import com.sun.xml.internal.ws.util.StringUtils;

public class workingAndMostUpToDate {

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */

	
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		
		File allData = new File("fall2022_3-16-22.txt");
		String csvtitle = "fall2022";
		
		Pattern classPatternDelimit = Pattern.compile("(?=[A-Z]{4} [0-9]{2,3}[A-Z]{0,1} [0-9]{2,3}[A-Z]{0,1})|(?=(?<![A-Z]{1})[A-Z]{3} [0-9]{2,3}[A-Z]{0,1} [0-9]{3}[A-Z]{0,1})"); //Does this still need to be updated?
		Scanner allCourses = new Scanner(allData).useDelimiter(classPatternDelimit);

		ArrayList<ArrayList<String>> allCoursesRefined = new ArrayList<ArrayList<String>>();
		militaryTimeConverter timeConvert = new militaryTimeConverter();
		System.out.println("Tried to set an element, got index out of bounds on probably means");
		System.out.println("that the information wasn't found in the expected order, which means");
		System.out.println("that these classes weren't actually blocks, but rather interpreted as ");
		System.out.println("blocks while looking in the descriptions of other classes");
		System.out.println();
		System.out.println();
		
		String first_newInstructorLastName = "";
		String second_newInstructorLastName = "";
		String first_previousInstructorsLastName = "";
		
		while (allCourses.hasNext()) {
			
			//Need to store this value and add at the end
			String creditHoursForCurrentCourse=""; 
			
			//Takes in the next input class block
			String currentCourseString = allCourses.next();
			
			//gets rid of all the bad stuff one block at a time
			currentCourseString = currentCourseString.replaceAll("Report ID:.*\r?\n", "");
			currentCourseString = currentCourseString.replaceAll("Semester Section Book For.*\r?\n", "");
			currentCourseString = currentCourseString.replaceAll("Run Time:.", "");
			currentCourseString = currentCourseString.replaceAll("College of Arts & Sciences.*\r?\n", "");
			currentCourseString = currentCourseString.replaceAll("CollegeofArts&Sciences.*\r?\n", "");
			currentCourseString = currentCourseString.replaceAll("__S_u_b_j_e_c_t____C_a_t_a_l_o_g.*\r?\n", "");
			
			
			//if(!currentCourseString.contains("Report ID:")&&!currentCourseString.contains("Run Time: 08:21:36")&&!currentCourseString.contains("__S_u_b_j_e_c_t_")&&!currentCourseString.contains("Semester Section")){
			ArrayList currentCourse = new ArrayList();

			//Makes the pattern to extract the blocks of "MATH 521 001"
			Pattern classPatternForExtraction = Pattern.compile("[A-Z]{4} [0-9]{2,3}[A-Z]{0,1} [0-9]{2,3}[A-Z]{0,1}|(?<![A-Z]{1})[A-Z]{3} [0-9]{2,3}[A-Z]{0,1} [0-9]{3}[A-Z]{0,1}"); //Does this still need to be updated?
			Matcher matchingForExtraction = classPatternForExtraction.matcher(currentCourseString);

			//If the block matches the pattern, which everyone should.
			if(matchingForExtraction.find()){
				//Take out just the part of the text that has "MATH 521 001"
				String courseBlock = currentCourseString.substring(matchingForExtraction.start(),matchingForExtraction.end());
				//Delimit by spaces

				Scanner courseBlockSeperator = new Scanner(courseBlock).useDelimiter(" ");
				//Add each to our ArrayList.
				currentCourse.add("Not Found");
				currentCourse.add("Not Found");
				currentCourse.add("Not Found");
				try{
					currentCourse.set(0,courseBlockSeperator.next());
					currentCourse.set(1,courseBlockSeperator.next());
					currentCourse.set(2,courseBlockSeperator.next());
				}catch(java.lang.IndexOutOfBoundsException e){

				}
			}


			String[] curCrseSpaces = currentCourseString.split(" |\\s+"); 

			try{
				//j=4 makes it start after the MATH 521 001 part.
				for(int j=4; j<curCrseSpaces.length; j++){
					String courseNameforStoring = null;

					String testCourseName = curCrseSpaces[j];
					String longAssRegex = "[A-Z]{2,}(?!-)|[A-Z]{2,}/[A-Z]{2,}|&|[A-Z]{2,}-[A-Z]{2,}|[A-Z]{2,}/[A-Z]{2,}/[A-Z]{2,}|[A-Z]{2,}'[A-Z]{1}|[A-Z]{2,}:[A-Z]{2,}|[A-Z]{2,}:|[A-Z]{2,},|[A-Z]{2,}'";


					//all of this just makes sure that we're actually looking at the start of a course name
					//added "not equal to ID:" because combined sections have "ID:"
					boolean courseNameFound = testCourseName.matches(longAssRegex);
					boolean notCombinedSection = curCrseSpaces[j].matches("ID:");
					boolean notCombinedSectionAfterHyphen = curCrseSpaces[j-1].matches("-");
					//makes sure that the title won't be just four characters long (this would only happen if it were ANTH or MATH for example)
					boolean notCourseSubjectCombinedSection = (curCrseSpaces[j].matches("[A-Z]{4}")&&!curCrseSpaces[j+1].matches("[A-Z]+(?!-)|[0-9]{1,4}|[A-Z]{2,}/[A-Z]{2,}|&|[A-Z]{2,}-[A-Z]{2,}|[A-Z]{2,}/[A-Z]{2,}/[A-Z]{2,}|[A-Z]{2,}'[A-Z]{1}|[A-Z]{2,}:[A-Z]{2,}|[A-Z]{2,}:|[A-Z]{2,},|[A-Z]{2,}'"));
					boolean notFullCourseWithNumber = (curCrseSpaces[j].matches("[A-Z]{4}")&&curCrseSpaces[j+1].matches("[0-9]{3}"));
					boolean notAfterColon = curCrseSpaces[j-1].matches("[A-Za-z]{2,}:");
					boolean notMiddleName = curCrseSpaces[j-1].matches("[A-Z]{2,},[A-Z]{2,}|[A-Z]{2,}-[A-Z]{2,},[A-Z]{2,}");
					boolean notMiddleName2 = curCrseSpaces[j-2].matches("[A-Z]{2,},[A-Z]{2,}|[A-Z]{2,}-[A-Z]{2,},[A-Z]{2,}");

					//if we really are looking at the start of the course name
					if(courseNameFound&&!notAfterColon&&!notMiddleName&&!notMiddleName2&&!notCombinedSection&&!notCombinedSectionAfterHyphen&&!notCourseSubjectCombinedSection&&!notFullCourseWithNumber){

						//start to store the part we thought was the start
						courseNameforStoring = curCrseSpaces[j];

						boolean courseNameEnded = false;

						//start looking at the index where we found it start
						int l= j;
						while(!courseNameEnded){

							//go to the next word
							l++;
							String testCourseNameThrough = curCrseSpaces[l];
							//make sure that this word is still in the form that the title would be in
							courseNameEnded = !testCourseNameThrough.matches("[A-Z]+(?!-)|[0-9]{1,4}|[A-Z]{2,}/[A-Z]{2,}|&|[A-Z]{2,}-[A-Z]{2,}|[A-Z]{2,}/[A-Z]{2,}/[A-Z]{2,}|[A-Z]{2,}'[A-Z]{1}|[A-Z]{2,}:[A-Z]{2,}|[A-Z]{2,}:|[A-Z]{2,},|[A-Z]{2,}'");
							//if it is
							if(!courseNameEnded){
								//add it to the title
								courseNameforStoring = courseNameforStoring + " "+curCrseSpaces[l];
							}


						}//once we found the end of the title, we'll get here.
						
						//now we get number of credits
						
						
					
						while(!curCrseSpaces[l].matches("-?\\d+(\\.\\d+)?")){
							 l++; 
						}
						
						creditHoursForCurrentCourse = curCrseSpaces[l]; 
						
						
						currentCourse.add("Not Listed");
						try{
							
							org.apache.commons.lang3.text.WordUtils wordUtility = new org.apache.commons.lang3.text.WordUtils();
							courseNameforStoring = wordUtility.capitalizeFully(courseNameforStoring);
							courseNameforStoring = courseNameforStoring.replaceAll(" Ii", " II");
							courseNameforStoring = courseNameforStoring.replaceAll(" IIi", " III");
							
							courseNameforStoring = courseNameforStoring.replaceAll(" Iv", " IV");
							courseNameforStoring = courseNameforStoring.replaceAll("Fys", "FYS");
							
							//System.out.println(courseNameforStoring);
							
							currentCourse.set(3,courseNameforStoring);
							
								
						}catch(java.lang.IndexOutOfBoundsException e){

						}

						break;	
					}
					
				
					


				}

			}catch(java.lang.ArrayIndexOutOfBoundsException e){

			}

			//Now we handle the following in this order: Bldg, Room, Days, Time, Instructor, Attributes, then all the class enrl cap stuff. 

			//First split into groups determined by the multi-lettered class enrollment things "Class Enrl Cap:"

			String splitBetweenClassWordWord[] = currentCourseString.split("(?= Class [A-Za-z]{2,} [A-Za-z]{2,}:)|(?=\\s+Class [A-Za-z]{2,} [A-Za-z]{2,}:)|(?=Foot note:)|(?=Special Grading:)");
			ArrayList<String> classEnrollmentStatsToBeAppended = new ArrayList();

			
			
			boolean hasAlreadyFoundBldg = false; 
			boolean hasAlreadyFoundRoom = false; 
			boolean hasAlreadyFoundDays = false; 
			boolean hasAlreadyFoundTime = false;  
			boolean hasAlreadyFoundName = false; 
			 
			
			//for each of these groups
			for(int a=0; a<splitBetweenClassWordWord.length;a++){
				try{
					
					
					
					//if the group is not exclusively a Class Enrl Cap block
					if(!splitBetweenClassWordWord[a].matches("\\s*Class [A-Za-z]{2,} [A-Za-z]{2,}: [0-9]{1,}")){

						//Split it into groups of one word followed by a colon.
						String wordsColons[] = splitBetweenClassWordWord[a].split("(?= [A-Za-z]{2,}:)|(?=\\s+[A-Za-z]{2,}:)");
						
						
						
						//Bldg - 4
						currentCourse.add("Not Listed");
						for(int j=0;j<wordsColons.length;j++){
							if(wordsColons[j].contains("Bldg:")){
								//we delimit by line breaks and take the first line
								String stringToFindBldgIn = wordsColons[j].substring(wordsColons[j].indexOf("Bldg:")+6,wordsColons[j].length());
								String[] arrayOfLines = stringToFindBldgIn.split("\\s+"); 

								try{
									int k=0;
									String bldgCollection = "";
									//This creates a concactanated string by adding on the next word in the array until it's a number 
									while(k<arrayOfLines.length&&arrayOfLines[k].matches("[A-Za-z]{2,}")){
										bldgCollection=bldgCollection+" "+arrayOfLines[k];
										k++;
									}
									
									//Sets the blding name to the string we found above
									if(hasAlreadyFoundBldg==false){
										currentCourse.set(4,bldgCollection);
										hasAlreadyFoundBldg=true; 
										
									}else if (hasAlreadyFoundBldg==true){
										String newBuilding = currentCourse.get(4)+"/"+bldgCollection.substring(1); 
										currentCourse.set(4,newBuilding); 
									}
									 


								}catch(java.lang.IndexOutOfBoundsException e){
									//this is just the first entry that doesn't actually matter
								}

							}
						}


						//Room - 5
						currentCourse.add("Not Listed");
						for(int j=0;j<wordsColons.length;j++){
							if(wordsColons[j].contains("Room:")&&!wordsColons[j].contains("BuiRloom:")){
								try{
									
									if(hasAlreadyFoundRoom==false){
										currentCourse.set(5,wordsColons[j].substring(wordsColons[j].indexOf("Room:")+6,wordsColons[j].length()));
										hasAlreadyFoundRoom=true; 
									}else{
										String newRoom = currentCourse.get(5)+"/"+wordsColons[j].substring(wordsColons[j].indexOf("Room:")+6,wordsColons[j].length());
										currentCourse.set(5,newRoom);
									}
									
									
								}catch(java.lang.IndexOutOfBoundsException e){
									//this is just the first entry that doesn't actually matter
								}

								//genome sciences case
							}else if (wordsColons[j].contains("BuiRloom:")){
								try{
									
									if(hasAlreadyFoundRoom==false){
										currentCourse.set(5,wordsColons[j].substring(wordsColons[j].indexOf("BuiRloom:")+9,wordsColons[j].length()));
										hasAlreadyFoundRoom=true; 
									}else{
										String newRoom = currentCourse.get(5)+"/"+currentCourse.set(5,wordsColons[j].substring(wordsColons[j].indexOf("BuiRloom:")+9,wordsColons[j].length()));
										currentCourse.set(5,newRoom);
									}
									
									
								}catch(java.lang.IndexOutOfBoundsException e){
									//this is just the first entry that doesn't actually matter
								}

							}
						}

						//Days - 6
						currentCourse.add("Not Listed");
						for(int j=0;j<wordsColons.length;j++){
							if(wordsColons[j].contains("Days:")){
								
								
								
								try{
									
									if(hasAlreadyFoundDays==false){
										currentCourse.set(6,wordsColons[j].substring(wordsColons[j].indexOf("Days:")+6,wordsColons[j].length()));
										hasAlreadyFoundDays=true; 
									}else{
							
										String newDays = currentCourse.get(6)+"/"+wordsColons[j].substring(wordsColons[j].indexOf("Days:")+6,wordsColons[j].length());
										currentCourse.set(6,newDays);
										
									}
									
								}catch(java.lang.IndexOutOfBoundsException e){
									//this is just the first entry that doesn't actually matter
								}
							}
						}
						
						
						
						
						//Time - 7 and 8
						currentCourse.add("Not Listed");
						currentCourse.add("Not Listed");
						for(int j=0;j<wordsColons.length;j++){
							if(wordsColons[j].contains("Time:")){
								try{
									if(!wordsColons[j].contains("Time: TBA")&&!wordsColons[j].contains("00:00")){
										
										if(hasAlreadyFoundTime==false){
											currentCourse.set(7,timeConvert.militaryToOrdinaryTime(wordsColons[j].substring(wordsColons[j].indexOf("Time:")+6,wordsColons[j].indexOf("-")+0)));
											currentCourse.set(8,timeConvert.militaryToOrdinaryTime(wordsColons[j].substring(wordsColons[j].indexOf("-")+1,wordsColons[j].indexOf("Time:")+19)));
											hasAlreadyFoundTime=true; 
										}else{
											String newStartTime, newEndTime; 
											newStartTime = currentCourse.get(7)+"/"+timeConvert.militaryToOrdinaryTime(wordsColons[j].substring(wordsColons[j].indexOf("Time:")+6,wordsColons[j].indexOf("-")+0)); 
											newEndTime = currentCourse.get(8)+"/"+timeConvert.militaryToOrdinaryTime(wordsColons[j].substring(wordsColons[j].indexOf("-")+1,wordsColons[j].indexOf("Time:")+19)); 
											currentCourse.set(7,newStartTime);
											currentCourse.set(8,newEndTime);
										}
									
										
									}else{
										if(hasAlreadyFoundTime==false){
											currentCourse.set(7, "TBA");
											currentCourse.set(8, "TBA");
											hasAlreadyFoundTime=true; 
											
										}else{
											String newStartTime, newEndTime; 
											newStartTime = currentCourse.get(7)+"/TBA"; 
											newEndTime = currentCourse.get(8)+"/TBA"; 
											currentCourse.set(7,newStartTime);
											currentCourse.set(8,newEndTime);
										}
										
										
									}
								}catch(java.lang.IndexOutOfBoundsException e){
								
								}


							}
						}


						//Instructor - 9 //Need to figure out why some of these are empty
						currentCourse.add("Not Listed");
						currentCourse.add("Not Listed");
						
						boolean professorAlreadyFound = false;//we put this in so that we only get the first professor if there are  
						//ever more than one listed for a section. This averts the bug where some sections erraneously had two instructors 
						//listed and the second one was the same for like 50 classes. 
						for(int j=0;j<wordsColons.length;j++){
							if(wordsColons[j].contains("Instructor:")&&!wordsColons[j].equals("")){
								if(!professorAlreadyFound){
									professorAlreadyFound = true;
								try{
									//Reverse the instructor names so that first name is first. 
									String originalInstructorName = wordsColons[j].substring(wordsColons[j].indexOf("Instructor:")+11,wordsColons[j].length());
									String[] splitByComma = originalInstructorName.split(",");
									if(splitByComma[0].contains(" ")){
										splitByComma[0]=splitByComma[0].replaceAll("Jr", "");
										splitByComma[0]=splitByComma[0].replaceAll(" ", "");
										//System.out.println(splitByComma[0]);
									}
									//String correctedNames = splitByComma[1] + splitByComma[0];
									
									org.apache.commons.lang3.text.WordUtils wordUtility = new org.apache.commons.lang3.text.WordUtils();
									
									String instructorFirstNameTitleCase = wordUtility.capitalizeFully(splitByComma[1]);
									String instructorLastNameTitleCase = wordUtility.capitalizeFully(splitByComma[0]);

									//Uncomment to print instructor names:
									//System.out.println(instructorFirstNameTitleCase+" "+instructorLastNameTitleCase);
									
									if(hasAlreadyFoundName == false){
										currentCourse.set(9, instructorFirstNameTitleCase);
										currentCourse.set(10,instructorLastNameTitleCase);
										hasAlreadyFoundName = true; 
									}else{
										String newFirstName, newLastName; 
										newFirstName = currentCourse.get(9)+"/"+instructorFirstNameTitleCase; 
										newLastName = currentCourse.get(10)+"/"+instructorLastNameTitleCase; 
										currentCourse.set(9,newFirstName); 
										currentCourse.set(10,newLastName); 
									}
									
									
									//save this professor's last name so that the next class knows if there's an error
									first_previousInstructorsLastName = first_newInstructorLastName; // this should save the old one 
									first_newInstructorLastName = instructorLastNameTitleCase;
									
									
								}catch(java.lang.IndexOutOfBoundsException e){

								}
							}else{
								//print this to show classes that have multiple professors
								//System.out.println(currentCourse.get(0)+" "+currentCourse.get(1)+" "+currentCourse.get(2));
								
								try{
								//repeat process for this professor, so we can save 
								
								//Reverse the instructor names so that first name is first. 
								String originalInstructorName = wordsColons[j].substring(wordsColons[j].indexOf("Instructor:")+11,wordsColons[j].length());
								String[] splitByComma = originalInstructorName.split(",");
								if(splitByComma[0].contains(" ")){
									splitByComma[0]=splitByComma[0].replaceAll(" ", "");
									//System.out.println(splitByComma[0]);
								}
								//String correctedNames = splitByComma[1] + splitByComma[0];
								
								org.apache.commons.lang3.text.WordUtils wordUtility = new org.apache.commons.lang3.text.WordUtils();
								
								String instructorFirstNameTitleCase = wordUtility.capitalizeFully(splitByComma[1]);
								String instructorLastNameTitleCase = wordUtility.capitalizeFully(splitByComma[0]);

								
								//save this professor's last name so that the next class knows if there's an error
								String second_previousInstructorsLastName = second_newInstructorLastName; // this should save the old one 
								second_newInstructorLastName = instructorLastNameTitleCase;
								
								//System.out.println(second_previousInstructorsLastName);
								
								/************/
								//now deal with first_previousInstructorsLastName and second_previousInstructorsLastName
								
								//the new ones are first_newInstructorLastName and second_newInstructorLastName
								/*********/
								
								//System.out.println(first_newInstructorLastName);
								//System.out.println(first_previousInstructorsLastName);
								//if it turns out that the first instructors are repeating, then set it to the second. 
								//if they both repeat, we're just going to leave it as the first
				
								if(first_newInstructorLastName.equals(first_previousInstructorsLastName)&&!second_previousInstructorsLastName.equals(second_newInstructorLastName)){
									currentCourse.set(9, instructorFirstNameTitleCase);
									currentCourse.set(10,instructorLastNameTitleCase);
								}
								
								}catch(java.lang.IndexOutOfBoundsException e){
									
								}
								
							}
								
								
								
								}
						}
						
						/** Print the gen eds without the abbreviations
						//Attributes - 9
						currentCourse.add("None");
						for(int j=0;j<wordsColons.length;j++){
							if(wordsColons[j].contains("Attributes:")&&!wordsColons[j].contains("Prerequisites|Course")){
								try{
								String stringToFindAttributesIn = wordsColons[j].substring(wordsColons[j].indexOf("Attributes:")+11,wordsColons[j].length());
								String[] arrayOfLines = stringToFindAttributesIn.split("\\n"); 
								String originalAttributes = arrayOfLines[0];
								
								String splitByCommasHyphens[]=originalAttributes.split(",|-");
								System.out.println(splitByCommasHyphens[0]);
								//System.out.println(splitByCommasHyphens[0]);
								for(int t=0; t<splitByCommasHyphens.length+1;t++){
									if(splitByCommasHyphens[t].matches("\\s[A-Z]{1}[0-9]{1,}|\\s[A-Z]{2}")){
										System.out.println(splitByCommasHyphens[t]);
									}
								}

								String splitByAbbreviationAndHyphen[] = originalAttributes.split("([A-Z]{2}- )|([A-Z][0-9]- )");
								String longSeparatedByCommas = "";
								
								
								for(int x=0; x<splitByAbbreviationAndHyphen.length;x++){
									longSeparatedByCommas= longSeparatedByCommas+splitByAbbreviationAndHyphen[x];
								}
									currentCourse.set(9,longSeparatedByCommas);
								}catch(java.lang.IndexOutOfBoundsException e){


								}
							}
						}
						 **/
						
						currentCourse.add("None");
						currentCourse.add("None");
						currentCourse.add("0");
						for(int j=0;j<wordsColons.length;j++){
							if(wordsColons[j].contains("Attributes:")&&!wordsColons[j].contains("Prerequisites|Course")){
								try{
								String stringToFindAttributesIn = wordsColons[j].substring(wordsColons[j].indexOf("Attributes:")+11,wordsColons[j].length());
								String[] arrayOfLines = stringToFindAttributesIn.split("\\n"); 
								String originalAttributes = arrayOfLines[0];
								
								String splitByCommasHyphens[]=originalAttributes.split(",|-");
								
								int numberOfAttributes=0;
								int numberOfGenEds=0;
						
								//System.out.println(splitByCommasHyphens[0]);
								String shortSeparatedBySpaces = "";
								for(int t=0; t<splitByCommasHyphens.length;t++){
									if(splitByCommasHyphens[t].matches("\\s[A-Z]{1}[0-9]{1,}|\\s[A-Z]{2}")){
										shortSeparatedBySpaces = shortSeparatedBySpaces+splitByCommasHyphens[t];
										numberOfAttributes=numberOfAttributes+1;
										
										if(!splitByCommasHyphens[t].contains("E1")&&!splitByCommasHyphens[t].contains("E2")&&!splitByCommasHyphens[t].contains("E3")&&!splitByCommasHyphens[t].contains("E4")&&!splitByCommasHyphens[t].contains("E5")&&!splitByCommasHyphens[t].contains("E6")){
											numberOfGenEds=numberOfGenEds+1;
											//System.out.println(splitByCommasHyphens[t]);
										}
										
										//System.out.print(splitByCommasHyphens[t]);
									}
								}
								
								currentCourse.set(11, shortSeparatedBySpaces);
								currentCourse.set(12, Integer.toString(numberOfAttributes));
								currentCourse.set(13, Integer.toString(numberOfGenEds));
								break; //this prevents it from adding "NOT LISTED" beyond the last thing it should find.
								
								
								}catch(java.lang.IndexOutOfBoundsException e){
									//System.out.println("Error");

								}
							}
						}
						
						
						currentCourse.add("0"); 
						
						
						
				
						
						//if the block does exactly match a Class Enrl Cap: then below will execute.
					}else{
						//Extracting two word colon information, still need to do here.
						
						
						try{
						
						
							
							
						String[] splitByColon = splitBetweenClassWordWord[a].split(": ");
						
						
						if(splitBetweenClassWordWord[a].matches("\\s*Class Enrl Cap: [0-9]{1,}")){
							
							//Class Enrollement Cap
							currentCourse.add("Not Listed");
							currentCourse.set(14,splitByColon[1]);
						}
						if(splitBetweenClassWordWord[a].matches(" Class Enrl Tot: [0-9]{1,}")){
							
							//Class Enrollement Total
							currentCourse.add("Not Listed");
							currentCourse.set(15,splitByColon[1]);
						}
						if(splitBetweenClassWordWord[a].matches(" Class Wait Cap: [0-9]{1,}")){
							
							//Class WaitList Cap
							currentCourse.add("Not Listed");
							currentCourse.set(16,splitByColon[1]);
						}
						if(splitBetweenClassWordWord[a].matches(" Class Wait Tot: [0-9]{1,}")){
							
							//Class Waitlist Total 
							currentCourse.add("Not Listed");
							currentCourse.set(17,splitByColon[1]);
							
							
							
						}
						
						
						
						
						}catch(IndexOutOfBoundsException e){
							System.out.println("Tried to set an element, got index out of bounds on: "+currentCourse);
						}
					}


				}catch(java.lang.StringIndexOutOfBoundsException e){

				}

			}

			//currentCourse.addAll(classEnrollmentStatsToBeAppended);
			
			
			
			//add credits
			currentCourse.add("Not Listed");
			try{
				currentCourse.set(18,creditHoursForCurrentCourse); 
			}catch(java.lang.IndexOutOfBoundsException e){
				
			}
			
			
			
			
			
			allCoursesRefined.add(currentCourse);
			//System.out.println(currentCourse);
			}
		//}


		//Remove Whitespace. This doesn't seem to work.
		for(int t=0;t<allCoursesRefined.size();t++){
			for(int j=0;j<allCoursesRefined.get(t).size();j++){
				String stringToClean = allCoursesRefined.get(t).get(j);
				allCoursesRefined.get(t).set(j, stringToClean.trim());
				allCoursesRefined.get(t).set(j, stringToClean.replaceAll("[\n\r]", ""));
			}
		}

		//APPLY TARA'S METHOD OF FIXING MISSING COURSE NAMES BY 
		//SEARCHING THE ENTIRE ARRAY FOR CLASSES THAT HAVE THE SAME NUMBER AND SUBJECT AND THEN ADDING THEIR NAME TO THE COURSE'S NAME 

		for(int t=0; t<allCoursesRefined.size();t++){
			try{
				if(allCoursesRefined.get(t).get(3).equals("Not Listed")){
					for(int u=0; u<allCoursesRefined.size();u++){
						if(allCoursesRefined.get(t).get(0).equals(allCoursesRefined.get(u).get(0))&&allCoursesRefined.get(t).get(1).equals(allCoursesRefined.get(u).get(1))){

							allCoursesRefined.get(t).set(3, allCoursesRefined.get(u).get(3));

						}
					}
				}
			
				if(allCoursesRefined.get(t).get(0).equals("AMST")&&allCoursesRefined.get(t).get(1).equals("498")&&allCoursesRefined.get(t).get(2).equals("001")){
					allCoursesRefined.get(t).set(3, "Global Impacts on American Waters"); 
				}
			

				
				
				//Manual Changes for Fall 2015
				if((csvtitle.contains("fall")||csvtitle.contains("Fall"))&&csvtitle.contains("2016")){
					if(allCoursesRefined.get(t).get(0).equals("LATN")&&allCoursesRefined.get(t).get(1).equals("223")){
						allCoursesRefined.get(t).set(3, "Ovid"); 
						allCoursesRefined.get(t).set(4, "Alumni"); 
						allCoursesRefined.get(t).set(5, "205"); 
						allCoursesRefined.get(t).set(6, "TuTh");
						allCoursesRefined.get(t).set(7, "2:00pm"); 
						allCoursesRefined.get(t).set(8, "3:15pm"); 
						allCoursesRefined.get(t).set(9, "Robert");
						allCoursesRefined.get(t).set(10, "Babcock"); 
					}
					
					if(allCoursesRefined.get(t).get(0).equals("AMST")&&allCoursesRefined.get(t).get(1).equals("60")&&allCoursesRefined.get(t).get(2).equals("001")){
						allCoursesRefined.get(t).set(3, "Indian History, Law, Lit"); 
						allCoursesRefined.get(t).set(4, "Greenlaw"); 
						allCoursesRefined.get(t).set(5, "317"); 
						allCoursesRefined.get(t).set(6, "MW"); 
						allCoursesRefined.get(t).set(7, "3:35pm"); 
						allCoursesRefined.get(t).set(8, "4:50pm"); 
						allCoursesRefined.get(t).set(9, "Daniel");
						allCoursesRefined.get(t).set(10, "Cobb"); 
					}
					
					if(allCoursesRefined.get(t).get(0).equals("AMST")&&allCoursesRefined.get(t).get(1).equals("486")){
						allCoursesRefined.get(t).set(3, "Jewish Exp. in the American South"); 
					}

				}
			
				
				/*

				else if(allCoursesRefined.get(t).get(0).equals("DTCH")&&allCoursesRefined.get(t).get(1).equals("396")){
					allCoursesRefined.get(t).set(3, "Independent Readings"); 
					allCoursesRefined.get(t).set(4, "TBA"); 
					allCoursesRefined.get(t).set(5, "TBA"); 
					allCoursesRefined.get(t).set(6, "TBA"); 
					allCoursesRefined.get(t).set(8, "Not Listed"); 
					allCoursesRefined.get(t).set(10, "Thornton"); 
				}

				else if(allCoursesRefined.get(t).get(0).equals("ASTR")&&allCoursesRefined.get(t).get(1).equals("301")){
					allCoursesRefined.get(t).set(3, "Stars, Galaxies, and Cosmology"); 
					
					if(allCoursesRefined.get(t).get(2).equals("002")){
						allCoursesRefined.get(t).set(4, "Phillips"); 
						allCoursesRefined.get(t).set(5, "220"); 
						allCoursesRefined.get(t).set(6, "M"); 
						allCoursesRefined.get(t).set(8, "1:00pm"); 
						allCoursesRefined.get(t).set(10, "Erickcek"); 
					}
				}

				else if(allCoursesRefined.get(t).get(0).equals("POLI")&&allCoursesRefined.get(t).get(1).equals("288")){
					allCoursesRefined.get(t).set(3, "Strategy and Politics"); 
					allCoursesRefined.get(t).set(4, "Peabody"); 
					allCoursesRefined.get(t).set(5, "306"); 
					allCoursesRefined.get(t).set(6, "TuTh"); 
					allCoursesRefined.get(t).set(8, "12:15pm"); 
					allCoursesRefined.get(t).set(10, "Bassi"); 
				}

				else if(allCoursesRefined.get(t).get(0).equals("RELI")&&allCoursesRefined.get(t).get(1).equals("423")){
					allCoursesRefined.get(t).set(3, "Ethnicity,Race,Rel"); 
					allCoursesRefined.get(t).set(4, "Murray Hall"); 
					allCoursesRefined.get(t).set(5, "G201"); 
					allCoursesRefined.get(t).set(6, "TuTh"); 
					allCoursesRefined.get(t).set(8, "3:15pm"); 
					allCoursesRefined.get(t).set(10, "Dougherty"); 
				}

				else if(allCoursesRefined.get(t).get(0).equals("PHCO")&&allCoursesRefined.get(t).get(1).equals("702")){
					allCoursesRefined.get(t).set(3, "Princ/Pharmacol-Tox"); 
					allCoursesRefined.get(t).set(4, "TBA"); 
					allCoursesRefined.get(t).set(5, ""); 
					allCoursesRefined.get(t).set(6, "TBA"); 
					allCoursesRefined.get(t).set(8, "TBA"); 
					allCoursesRefined.get(t).set(10, "Justice"); 
				}

				else if(allCoursesRefined.get(t).get(0).equals("ENVR")&&allCoursesRefined.get(t).get(1).equals("630")){
					allCoursesRefined.get(t).set(3, "Sys. Biol. Env. Health"); 
					allCoursesRefined.get(t).set(4, "McGavran-Greenberg"); 
					allCoursesRefined.get(t).set(5, "2304"); 
					allCoursesRefined.get(t).set(6, "TuTh"); 
					allCoursesRefined.get(t).set(8, "12:15pm"); 
					allCoursesRefined.get(t).set(10, "Fry"); 
				}

			*/
					
			}catch(java.lang.IndexOutOfBoundsException e){

			}
		}
		
		
		

		//Export to CSV 
		System.out.println("----------------------------------------");
		try {
			FileWriter writer = new FileWriter(csvtitle+".csv");
			
			writer.append("\"subject\"");
			writer.append(',');
			writer.append("\"number\"");
			writer.append(',');
			writer.append("\"section\"");
			writer.append(',');
			writer.append("\"title\"");
			writer.append(',');
			writer.append("\"building\"");
			writer.append(',');
			writer.append("\"room\"");
			writer.append(',');
			writer.append("\"days\"");
			writer.append(',');
			writer.append("\"startTime\"");
			writer.append(',');
			writer.append("\"endTime\"");
			writer.append(',');
			writer.append("\"instructorFirstName\"");
			writer.append(',');
			writer.append("\"instructorLastName\"");
			writer.append(',');
			writer.append("\"attributes\"");
			writer.append(',');
			writer.append("\"genEdCount\"");
			writer.append(',');
			writer.append("\"enrlCap\"");
			writer.append(',');
			writer.append("\"enrlTot\"");
			writer.append(',');
			writer.append("\"waitCap\"");
			writer.append(',');
			writer.append("\"waitTot\"");
			writer.append(',');
			writer.append("\"credits\"");
			writer.append(',');
			writer.append("\"crn\"");
			writer.append('\n');
			
			
			for(int h=0; h<allCoursesRefined.size(); h++){
				if(allCoursesRefined.get(h).size()<18){
					System.out.println("Not added because less than 16 elements: "+allCoursesRefined.get(h));
				}else{
				try{
				writer.append("\""+allCoursesRefined.get(h).get(0).toString()+"\"");
				writer.append(',');
				writer.append("\""+allCoursesRefined.get(h).get(1).toString()+"\"");
				writer.append(',');
				writer.append("\""+allCoursesRefined.get(h).get(2).toString()+"\"");
				writer.append(',');
				writer.append("\""+allCoursesRefined.get(h).get(3).toString()+"\"");
				writer.append(',');
				writer.append("\""+allCoursesRefined.get(h).get(4).toString()+"\"");
				writer.append(',');
				writer.append("\""+allCoursesRefined.get(h).get(5).toString()+"\"");
				writer.append(',');
				writer.append("\""+allCoursesRefined.get(h).get(6).toString()+"\"");
				writer.append(',');
				writer.append("\""+allCoursesRefined.get(h).get(7).toString()+"\"");
				writer.append(',');
				writer.append("\""+allCoursesRefined.get(h).get(8).toString()+"\"");
				writer.append(',');
				writer.append("\""+allCoursesRefined.get(h).get(9).toString()+"\"");
				writer.append(',');
				writer.append("\""+allCoursesRefined.get(h).get(10).toString()+"\"");
				writer.append(',');
				writer.append("\""+allCoursesRefined.get(h).get(11).toString()+"\"");
				writer.append(',');
				//writer.append("\""+allCoursesRefined.get(h).get(12).toString()+"\"");
				//writer.append(',');
				writer.append("\""+allCoursesRefined.get(h).get(13).toString()+"\"");
				writer.append(',');
				writer.append("\""+allCoursesRefined.get(h).get(14).toString()+"\"");
				writer.append(',');
				writer.append("\""+allCoursesRefined.get(h).get(15).toString()+"\"");
				writer.append(',');
				writer.append("\""+allCoursesRefined.get(h).get(16).toString()+"\"");
				writer.append(',');
				writer.append("\""+allCoursesRefined.get(h).get(17).toString()+"\"");
				writer.append(',');
				writer.append("\""+allCoursesRefined.get(h).get(18).toString()+"\"");
				writer.append(','); //blank CRN
				writer.append("\"\"");
				
				writer.append('\n');
				} catch(IndexOutOfBoundsException e) {
					e.printStackTrace();
					//System.out.println(allCoursesRefined.get(h));
				}
				}
				
			}
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}
		

		//PRINTING
		int countOfNamesNotExtant =0;

		for(int h=0; h<allCoursesRefined.size(); h++){
			try{

				//To Test Uncomment this: 
				/**
				System.out.print(allCoursesRefined.get(h).get(0).toString());
				System.out.print(allCoursesRefined.get(h).get(1).toString());
				System.out.print(allCoursesRefined.get(h).get(2).toString());
				System.out.print("|||");
				System.out.println(allCoursesRefined.get(h).get(12).toString());
				**/
			/**
				
				//print this for checking the entire thing
				//System.out.println(allCoursesRefined.get(h).toString());


				//To Create CSV Uncomment this: YEAH YEAH I KNOW I DIDN'T DO IT YOUR WAY 
				
				System.out.print(allCoursesRefined.get(h).get(0).toString());
				System.out.print("*");
				System.out.print(allCoursesRefined.get(h).get(1).toString());
				System.out.print("*");
				System.out.print(allCoursesRefined.get(h).get(2).toString());
				System.out.print("*");
				System.out.print(allCoursesRefined.get(h).get(3).toString());
				System.out.print("*");
				System.out.print(allCoursesRefined.get(h).get(4).toString());
				System.out.print("*");
				System.out.print(allCoursesRefined.get(h).get(5).toString());
				System.out.print("*");
				System.out.print(allCoursesRefined.get(h).get(6).toString());
				System.out.print("*");
				System.out.print(allCoursesRefined.get(h).get(7).toString());
				System.out.print("*");
				System.out.print(allCoursesRefined.get(h).get(8).toString());
				System.out.print("*");
				System.out.print(allCoursesRefined.get(h).get(9).toString());
				System.out.print("*");
				System.out.print(allCoursesRefined.get(h).get(10).toString());
				System.out.print("*");
				System.out.print(allCoursesRefined.get(h).get(11).toString());
				System.out.print("*");
				System.out.print(allCoursesRefined.get(h).get(12).toString());
				System.out.print("*");
				System.out.print(allCoursesRefined.get(h).get(13).toString());
				System.out.print("*");
				System.out.println(allCoursesRefined.get(h).get(14).toString());
				
					*/

				if(allCoursesRefined.get(h).get(9).equals("None")){
					countOfNamesNotExtant++;
				}

			}catch(java.lang.IndexOutOfBoundsException e){
				System.out.println();
			}

		}
		System.out.println();
		System.out.println();
		System.out.println("All courses discovered (this is probably not the correct number valid, though): "+allCoursesRefined.size());

	}

}

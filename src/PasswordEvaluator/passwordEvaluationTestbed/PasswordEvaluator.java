package passwordEvaluationTestbed;


public class PasswordEvaluator {

	public static String passwordErrorMessage = "";
	public static String passwordInput = "";
	public static int passwordIndexofError = -1;
	public static boolean foundUpperCase = false;
	public static boolean foundLowerCase = false;
	public static boolean foundNumericDigit = false;
	public static boolean foundSpecialChar = false;
	public static boolean foundLongEnough = false;
	private static String inputLine = "";
	private static char currentChar;
	private static int currentCharNdx;
	private static boolean running;

	private static void displayInputState() {		//displays password size and current character index
		System.out.println(inputLine);
		System.out.println(inputLine.substring(0,currentCharNdx) + "?");
		System.out.println("The password size: " + inputLine.length() + "  |  The currentCharNdx: " + 
				currentCharNdx + "  |  The currentChar: \"" + currentChar + "\"");
	}

	public static String evaluatePassword(String input) {		//Method to evaluate a password
		passwordErrorMessage = "";
		passwordIndexofError = 0;
		inputLine = input;
		currentCharNdx = 0;
		
		if(input.length() <= 0) return "*** Error *** The password is empty!";
		
		currentChar = input.charAt(0);		// The current character from the above indexed position

		passwordInput = input;
		foundUpperCase = false;
		foundLowerCase = false;	
		foundNumericDigit = false;
		foundSpecialChar = false;
		foundNumericDigit = false;
		foundLongEnough = false;
		running = true;

		while (running) {
			displayInputState();
			if (currentChar >= 'A' && currentChar <= 'Z') {			//performs test to see if an upper case is found
				System.out.println("Upper case letter found");
				foundUpperCase = true;
			} else if (currentChar >= 'a' && currentChar <= 'z') {		//performs test to see if a lower case is found
				System.out.println("Lower case letter found");
				foundLowerCase = true;
			} else if (currentChar >= '0' && currentChar <= '9') {		//performs test to see if a digit is found
				System.out.println("Digit found");
				foundNumericDigit = true;
			} else if ("~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/".indexOf(currentChar) >= 0) {	//performs test to see if a symbol is found
				System.out.println("Special character found");
				foundSpecialChar = true;
			} else {
				passwordIndexofError = currentCharNdx;			//else case if character is not recognized
				return "*** Error *** An invalid character has been found!";
			}
			if (currentCharNdx >= 7) {
				System.out.println("At least 8 characters found");		//performs test to see if the password is long enough
				foundLongEnough = true;
			}
			currentCharNdx++;
			if (currentCharNdx >= inputLine.length())		// Case if password has more characters
				running = false;
			else
				currentChar = input.charAt(currentCharNdx);		//change current character if more characters in password
			
			System.out.println();
		}
		
		String errMessage = "";		//error message to be presented if password was not satisfied
		if (!foundUpperCase)
			errMessage += "Upper case; ";
		
		if (!foundLowerCase)
			errMessage += "Lower case; ";
		
		if (!foundNumericDigit)
			errMessage += "Numeric digits; ";
			
		if (!foundSpecialChar)
			errMessage += "Special character; ";
			
		if (!foundLongEnough)
			errMessage += "Long Enough; ";
		
		if (errMessage == "")
			return "";
		
		passwordIndexofError = currentCharNdx;
		return errMessage + "conditions were not satisfied";

	}
}

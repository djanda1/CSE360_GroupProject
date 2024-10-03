package passwordEvaluationTestbed;


public class PasswordEvaluationTestingAutomation {
	
	static int numPassed = 0;
	static int numFailed = 0;
	public static void main(String[] args) {
		System.out.println("____________________________________________________________________________");
		System.out.println("\nTesting Automation");
		
		performTestCase(1, "Aa!15678", true);	

		performTestCase(2, "A!", false);
		
		performTestCase(3, "Aa!15678", false);		//Test to see if the password was valid but expected it not to be
		
		performTestCase(4, "A!", false);			//changed it to false because this is an invalid password
		performTestCase(5, "", false);			//changed it to false because this is an invalid password
		performTestCase(6, "lllllleor&2", false);	//test to see if the class will reject a password without capitals
		performTestCase(7, "Djanda1202!", true);	//test to see if the class will accept a valid password
		System.out.println("____________________________________________________________________________");
		System.out.println();
		System.out.println("Number of tests passed: "+ numPassed);
		System.out.println("Number of tests failed: "+ numFailed);
	}

	private static void performTestCase(int testCase, String inputText, boolean expectedPass) {
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		System.out.println("Input: \"" + inputText + "\"");
		System.out.println("______________");
		System.out.println("\nFinite state machine execution trace:");
		
		String resultText= PasswordEvaluator.evaluatePassword(inputText);
		
		System.out.println();
		
		if (resultText != "") {			//print results of the test cases from the output of the evaluatePassword method
			if (expectedPass) {			//if the password was supposed to pass but didn't
				System.out.println("***Failure*** The password <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be valid, so this is a failure!\n");
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			else {					//if the password was supposed to be invalid but passed
				System.out.println("***Success*** The password <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be invalid, so this is a pass!\n");
				System.out.println("Error message: " + resultText);
				numPassed++;
			}
		}
		
		else {							//If the password did pass
			if (expectedPass) {			//Expected to pass and did pass
				System.out.println("***Success*** The password <" + inputText + 
						"> is valid, so this is a pass!");
				numPassed++;
			}
			else {							//Expected to be invalid but was valid
				System.out.println("***Failure*** The password <" + inputText + 
						"> was judged as valid" + 
						"\nBut it was supposed to be invalid, so this is a failure!");
				numFailed++;
			}
		}
		displayEvaluation();
	}
	
	private static void displayEvaluation() {		//displays what was invalid of the password 
		if (PasswordEvaluator.foundUpperCase)
			System.out.println("At least one upper case letter - Satisfied");
		else
			System.out.println("At least one upper case letter - Not Satisfied");

		if (PasswordEvaluator.foundLowerCase)
			System.out.println("At least one lower case letter - Satisfied");
		else
			System.out.println("At least one lower case letter - Not Satisfied");
	

		if (PasswordEvaluator.foundNumericDigit)
			System.out.println("At least one digit - Satisfied");
		else
			System.out.println("At least one digit - Not Satisfied");

		if (PasswordEvaluator.foundSpecialChar)
			System.out.println("At least one special character - Satisfied");
		else
			System.out.println("At least one special character - Not Satisfied");

		if (PasswordEvaluator.foundLongEnough)
			System.out.println("At least 8 characters - Satisfied");
		else
			System.out.println("At least 8 characters - Not Satisfied");
	}
}

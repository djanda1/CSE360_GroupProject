package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

public class UserManagementApp extends Application {
	private Map<String, User> users = new HashMap<>();
	private User currentUser;
	public String oneTimePassword;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("User Management Application");
		showLoginPage(primaryStage);
	}
	

	private void showLoginPage(Stage stage) {
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(8);
		grid.setHgap(10);

		Label usernameLabel = new Label("Username:");
		GridPane.setConstraints(usernameLabel, 0, 0);
		TextField usernameInput = new TextField();
		GridPane.setConstraints(usernameInput, 1, 0);

		Label passwordLabel = new Label("Password:");
		GridPane.setConstraints(passwordLabel, 0, 1);
		PasswordField passwordInput = new PasswordField();
		GridPane.setConstraints(passwordInput, 1, 1);

		Button loginButton = new Button("Login");
		GridPane.setConstraints(loginButton, 1, 2);
		loginButton.setOnAction(e -> handleLogin(stage, usernameInput.getText(), passwordInput.getText()));

		Button inviteCodeButton = new Button("Use Invite Code");
		GridPane.setConstraints(inviteCodeButton, 1, 3);
		inviteCodeButton.setOnAction(e -> showInviteCodePage(stage));

		grid.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, loginButton, inviteCodeButton);

		Scene scene = new Scene(grid, 300, 200);
		stage.setScene(scene);
		stage.show();
		if (users.size() == 0) {
			oneTimePassword = generateRandomPassword(8);
			System.out.println("Welcome Admin. One time password is : " + oneTimePassword + 
					"\nThis password expires 12/10/2024");
		}
	}
	
	public static String generateRandomPassword(int length) {		//Generate a random 8 character 1 time password
		int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(length);

	    for (int i = 0; i < length; i++) {
	        int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	        }

	    return buffer.toString();
	}
	
	private void handleLogin(Stage stage, String username, String password) {
		User user = users.get(username);
		if (users.size() == 0 && password.equals(oneTimePassword)) {
			showAccountCreationPage(stage, List.of("Admin"));
		}
		else {
		if (user != null && user.getPassword().equals(password)) {
			currentUser = user;
			if (!user.isSetupComplete()) {
				showAccountSetupPage(stage);
			} else if (user.getRoles().size() > 1) {
				showRoleSelectionPage(stage);
			} else {
				showHomePage(stage, user.getRoles().get(0));
			}
		} else {
			showAlert("Login Failed", "Invalid username or password.");
		}
		}
	}

	private void showInviteCodePage(Stage stage) {
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(20, 20, 20, 20));

		Label inviteCodeLabel = new Label("Enter Invite Code:");
		TextField inviteCodeInput = new TextField();

		Button submitButton = new Button("Submit");
		submitButton.setOnAction(e -> handleInviteCode(stage, inviteCodeInput.getText()));

		layout.getChildren().addAll(inviteCodeLabel, inviteCodeInput, submitButton);
		Scene scene = new Scene(layout, 300, 200);
		stage.setScene(scene);
	}
	

	private void handleInviteCode(Stage stage, String inviteCode) {
		if (inviteCode.equals("admin-invite")) {
			showAccountCreationPage(stage, List.of("Admin"));
		} else if (inviteCode.equals("student-invite")) {
			showAccountCreationPage(stage, List.of("Student"));
		} else if (inviteCode.equals("instructor-invite")) {
			showAccountCreationPage(stage, List.of("Instructor"));
		} else {
			showAlert("Invalid Code", "The invite code is invalid.");
		}
	}

	private void showAccountCreationPage(Stage stage, List<String> roles) {
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(20, 20, 20, 20));

		Label usernameLabel = new Label("Username:");
		TextField usernameInput = new TextField();

		Label passwordLabel = new Label("Password:");
		PasswordField passwordInput = new PasswordField();

		Label confirmPasswordLabel = new Label("Confirm Password:");
		PasswordField confirmPasswordInput = new PasswordField();

		Button createAccountButton = new Button("Create Account");
		createAccountButton.setOnAction(e -> {
			if (passwordInput.getText().equals(confirmPasswordInput.getText())) {
				User newUser = new User(usernameInput.getText(), passwordInput.getText(), roles);
				users.put(usernameInput.getText(), newUser);
				showLoginPage(stage);
			} else {
				showAlert("Password Mismatch", "Passwords do not match.");
			}
		});

		layout.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, confirmPasswordLabel, confirmPasswordInput, createAccountButton);
		Scene scene = new Scene(layout, 300, 300);
		stage.setScene(scene);
	}

	private void showAccountSetupPage(Stage stage) {
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(20, 20, 20, 20));

		Label firstNameLabel = new Label("First Name:");
		TextField firstNameInput = new TextField();

		Label middleNameLabel = new Label("Middle Name:");
		TextField middleNameInput = new TextField();

		Label lastNameLabel = new Label("Last Name:");
		TextField lastNameInput = new TextField();

		Label preferredNameLabel = new Label("Preferred First Name (Optional):");
		TextField preferredNameInput = new TextField();

		Label emailLabel = new Label("Email:");
		TextField emailInput = new TextField();

		Button finishSetupButton = new Button("Finish Setup");
		finishSetupButton.setOnAction(e -> {
			currentUser.setFirstName(firstNameInput.getText());
			currentUser.setMiddleName(middleNameInput.getText());
			currentUser.setLastName(lastNameInput.getText());
			currentUser.setPreferredName(preferredNameInput.getText());
			currentUser.setEmail(emailInput.getText());
			currentUser.setSetupComplete(true);
			showHomePage(stage, currentUser.getRoles().get(0));
		});

		layout.getChildren().addAll(firstNameLabel, firstNameInput, middleNameLabel, middleNameInput, lastNameLabel, lastNameInput, preferredNameLabel, preferredNameInput, emailLabel, emailInput, finishSetupButton);
		Scene scene = new Scene(layout, 400, 400);
		stage.setScene(scene);
	}

	private void showRoleSelectionPage(Stage stage) {
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(20, 20, 20, 20));

		Label roleLabel = new Label("Select Role for this Session:");
		ComboBox<String> roleComboBox = new ComboBox<>();
		roleComboBox.getItems().addAll(currentUser.getRoles());

		Button selectRoleButton = new Button("Select Role");
		selectRoleButton.setOnAction(e -> showHomePage(stage, roleComboBox.getValue()));

		layout.getChildren().addAll(roleLabel, roleComboBox, selectRoleButton);
		Scene scene = new Scene(layout, 300, 200);
		stage.setScene(scene);
	}

	private void showHomePage(Stage stage, String role) {
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(20, 20, 20, 20));

		Label welcomeLabel = new Label("Welcome, " + currentUser.getDisplayName() + " (" + role + ")");
		Button logoutButton = new Button("Log Out");
		logoutButton.setOnAction(e -> {
			currentUser = null;
			showLoginPage(stage);
		});

		layout.getChildren().addAll(welcomeLabel, logoutButton);
		Scene scene = new Scene(layout, 300, 200);
		stage.setScene(scene);
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}


	private static class User {
		private String username;
		private String password;
		private List<String> roles;
		private boolean setupComplete;
		private String firstName;
		private String middleName;
		private String lastName;
		private String preferredName;
		private String email;

		public User(String username, String password, List<String> roles) {
			this.username = username;
			this.password = password;
			this.roles = roles;
			this.setupComplete = false;
		}

		public String getPassword() {
			return password;
		}

		public List<String> getRoles() {
			return roles;
		}

		public boolean isSetupComplete() {
			return setupComplete;
		}

		public void setSetupComplete(boolean setupComplete) {
			this.setupComplete = setupComplete;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public void setMiddleName(String middleName) {
			this.middleName = middleName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public void setPreferredName(String preferredName) {
			this.preferredName = preferredName;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getDisplayName() {
			return preferredName != null && !preferredName.isEmpty() ? preferredName : firstName;
		}
	}
}

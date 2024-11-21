PROJECT NAME: FundMe - Group Savings Application

PROJECT SUMMARY:
FundMe is a group savings application designed to help users collaborate toward shared financial goals. Users can create, join, and manage savings groups, where each group has a defined target savings amount, frequency, and contributions from each member. FundMe allows members to track progress, contribute towards goals, and view their group’s financial achievements through a user-friendly dashboard.

KEY BENEFITS:
**Collaborative Savings**: Supports a group-based approach to saving, making it easier for users to reach shared financial goals.
**Accountability and Transparency**: Each transaction is recorded, ensuring transparency within the group.
**Progress Tracking**: A dashboard feature lets users monitor their savings progress in real-time.

CORE FEATURES:
**User Authentication**: Signup and login with email and password.
**Group Management:** Create a group with a target, savings frequency, and defined member count.
**Join Existing Groups**: Join a group with a unique group code.
**Savings Contribution:** Make contributions towards a group’s savings target.
**Dashboard**: View group progress, member contributions, and overall savings progress.
**Email Notifications:** Receive email confirmations after successful funding.

LIBRARIES AND FRAMEWORKS:
**JavaFX**: Used for the application's UI, enabling interactive forms, tables, and alerts.
**Jakarta Mail**: Manages email notifications for group funding activities.
**JDBC:** Saves and retrieves user, group, and contribution data from  Postgres SQL database.
**Java Streams and Lambda Expressions**: Used for streamlined data processing, like filtering and transforming data collections.

KEY CONCEPTS AND CODING TOPICS EMPLOYED:
**OOP Principles**: Encapsulation, inheritance, and abstraction are applied to organize the project into manageable classes (e.g., User, Group, GroupManager).
**Singleton Design Pattern:** Ensures only one instance of GroupManager exists, which centralizes data handling.
**SOLID Principles:** The Single Responsibility and Dependency Inversion principles are applied to separate responsibilities among classes and improve modularity.
**Functional Programming:** Utilizes Java streams, lambdas, and method references for efficient data filtering and transformation
****JUnit Test****: Tested some of the methods like writing and reading from file 


![flowchartFM](https://github.com/user-attachments/assets/c596051d-c40e-4f34-8e5d-b7ee0066e829)



PROJECT WORKFLOW (Pseudo-Code)
**Initialization**
Load User Data from CSV file into UserManager.
Load Group Data and contributions from CSV files into GroupManager.
Main Menu Navigation
Depending on the user's action, the app will route to the following flows but will first launch sign up scene: User can navigate to login from signu up if user already exist;

1.** Signup Flow**
Input: Name, Email, Password, Password Confirmation
Validations:
Check if email format is correct.
Check if email already exists in UserManager.
Confirm if passwords match.
Success: Save new user to file and redirect to login.
Error: Display specific error messages for each invalid condition.

2.** Login Flow**
Input: Email, Password
Validations:
Check if credentials match a user in UserManager.
Success: Set logged-in user and redirect to dashboard.
Error: Display login error

3**. Create Group Flow**
Input: Group Name, Member Count, Savings Target, Savings Period, Start Date, Purpose
Validations:
Ensure all inputs are complete.
Verify Member Count, Savings Target, Savings Period are positive numbers.
Success:
Generate a unique group code.
Create a new group with the creator as the first member.
Save group details to file.
Error: Display specific error message based on the validation failure.

4. **Join Group Flow**
Input: Group Code
Validations:
Check if group exists.
Verify user isn’t already a member of another group.
Check if group has space for additional members.
Success:
Add user to group.
Update and save group member list to file.
Error: Display appropriate error message.

5. **Fund Savings Flow**
Input: Group Code
Validations:
Check if user is a member of the group.
Ensure that contributions do not exceed group target.
Success:
Record the contribution.
Update total savings and save to contribution file.
Send confirmation email.
Error: Display error (e.g., "Target Reached", "You dont belong to this group").

6. **Dashboard View Flow**
Fetch and display:
Group details.
List of member contributions.
Total and target savings

                                             ** USER STORY AND ACCEPTANCE CRITERIA**



1. User Signup
**User Story**
As a new user, I want to sign up with my name, email, and password so that I can create or join savings groups.

        **Acceptance Criteria**
Given I am a new user when I enter my name, valid email, and password, then I should be able to successfully create an account.
Given my email is already registered when I try to sign up, then I should receive a message stating that the email is already in use.
Given I enter a password that does not meet requirements (e.g., too short) when I try to sign up, then I should receive a message detailing the password requirements.

2. User Login
**User Story**
As a user, I want to log in with my email and password so that I can access my groups and dashboard.

       **Acceptance Criteria**
Given I have an account when I enter the correct email and password, then I should be able to log in and access the main menu.
Given I enter an incorrect email or password when I try to log in, then I should receive an error message stating that the credentials are incorrect.

3. Create Group
**User Story**
As a logged-in user, I want to create a savings group by specifying the group name, target savings, member count, frequency, start date, and purpose so that I can invite others to save with me.

       **Acceptance Criteria**
Given I am logged in when I fill in all required group details with valid information, then I should be able to create a new group successfully and receive a unique group code.
Given I leave any required field empty or enter invalid data (e.g., negative target savings) when I try to create a group, then I should receive an error message specifying which fields need correction.
Given I successfully create a group when the group is created, then I should be added as the first member automatically.

4. Join Group
**User Story**
As a user, I want to join an existing savings group using a unique group code so that I can contribute towards a shared financial goal.

        **Acceptance Criteria**
Given I am logged in and not part of any group when I enter a valid group code, then I should be added to the group and see a success message.
Given I am already part of a group when I try to join another group using a valid group code, then I should receive an error message indicating I cannot join multiple groups.
Given I enter an invalid group code when I try to join a group, then I should see an error message indicating the group code is incorrect or does not exist.
Given the group I try to join has already reached its maximum member count when I enter the code, then I should see an error message indicating the group is full.

5. Fund Savings
**User Story**
As a group member, I want to contribute funds towards the group’s savings goal so that I can help achieve the target amount.

          **Acceptance Criteria**
Given I am a member of a group when I enter my contribution then the contribution should be recorded and added to the group’s total savings.
Given I try to contribute more than the group’s target savings when I submit the contribution, then I should receive an error message stating the target has been met.
Given I have already made a contribution for the current period when I try to contribute again, then I should receive an error message indicating that I have already met my contribution quota.
Given my contribution is successfully recorded when it is saved, then I should receive a confirmation email indicating the contribution was successful.

6. View Group Dashboard
**User Story**
As a group member, I want to view my group’s dashboard so that I can see the group’s savings progress and each member’s contributions.

         **Acceptance Criteria**
Given I am logged in and a member of a group when I navigate to the dashboard, then I should see the group’s details, including the group name, code, start date, target savings, and total savings.
Given there are multiple contributions in the group when I view the dashboard, then I should see a list of member contributions, including each member’s name, amount contributed, and date.
Given the group has not yet received any contributions when I view the dashboard, then I should not see any contributions

7. Email Notification for Savings Contribution
**User Story**
As a user who has contributed to a group’s savings, I want to receive a confirmation email so that I can have a record of my contribution.

        **Acceptance Criteria**
Given I make a successful contribution when the contribution is recorded, then an email should be sent to me with the contribution details (amount and group name).
Given the email server is unavailable when I make a contribution, then I should receive an error message in the application stating that the email confirmation could not be sent.





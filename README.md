PROJECT NAME: FundMe - Group Savings Application

PROJECT SUMMARY:
FundMe is a group savings application designed to help users collaborate towards shared financial goals. Users can create, join, and manage savings groups, where each group has a defined target savings amount, frequency, and contributions from each member. FundMe allows members to track progress, contribute towards goals, and view their group’s financial achievements through a user-friendly dashboard.

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
**File I/O (java.io):** Saves and retrieves user, group, and contribution data from CSV files.
**Java Streams and Lambda Expressions**: Used for streamlined data processing, like filtering and transforming data collections.

KEY CONCEPTS AND CODING TOPICS EMPLOYED:
**OOP Principles**: Encapsulation, inheritance, and abstraction are applied to organize the project into manageable classes (e.g., User, Group, GroupManager).
**Singleton Design Pattern:** Ensures only one instance of GroupManager exists, which centralizes data handling.
**SOLID Principles:** The Single Responsibility and Dependency Inversion principles are applied to separate responsibilities among classes and improve modularity.
**Functional Programming:** Utilizes Java streams, lambdas, and method references for efficient data filtering and transformation.
**Error Handling**: Catches and handles IO and format errors during file read/write operations, and provides user feedback on incorrect inputs.
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



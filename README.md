# KidloLand Assignment

A simple Android app that collects a user's details (name, date of birth, and email), stores them locally, and displays all registered users in a swipeable list — built as an assignment project.

## Features

- **Splash Screen** — shows briefly on launch before navigating to the registration form.
- **User Registration** — collects name, date of birth (via a date picker, capped at the current date), and email, with input validation:
  - Name and DOB are required.
  - Email is required and validated against a standard email pattern.
- **Local Storage** — user details are saved to a local SQLite database (`UserDB`) so they persist across app restarts.
- **User List** — after submitting, the app shows every registered user in a scrollable list:
  - The current user's card is visually highlighted with a light purple tint to match the app's button color; other users appear in a softer, lighter tint of the same purple.
  - Swipe left on any card to reveal a delete action, with a confirmation dialog before removing a user.

## Tech Stack

- **Language:** Java
- **UI:** Android XML layouts (`LinearLayout`, `RecyclerView`, `CardView`)
- **Storage:** SQLite (`SQLiteDatabase`, raw SQL)
- **Architecture components used:** `RecyclerView.Adapter`, `ItemTouchHelper`, `AppCompatActivity`, `EdgeToEdge`

## Project Structure

```
app/src/main/
├── java/com/example/kidloland_assignment/
│   ├── SplashScreen.java       # Initial splash screen, navigates to MainActivity
│   ├── MainActivity.java       # Registration form (name, DOB, email)
│   ├── UserDetails.java        # Displays list of all registered users
│   ├── UserAdapter.java        # RecyclerView adapter for the user list
│   └── UserModel.java          # Simple data model for a user
└── res/layout/
    ├── activity_splash_screen.xml
    ├── activity_main.xml
    ├── activity_user_details.xml
    ├── item_user.xml           # Single row layout in the user list
    └── swipe_delete.xml        # Background shown while swiping to delete
```

## How It Works

1. **`SplashScreen`** displays for a few seconds, then launches `MainActivity`.
2. **`MainActivity`** presents the registration form. On submit, it validates all fields, inserts the new user into the `users` table in SQLite, and navigates to `UserDetails`, passing along the submitted details.
3. **`UserDetails`** loads every saved user from the database, marks whichever one matches the just-submitted details as the "current user," and displays them all in a `RecyclerView` via `UserAdapter`.
4. Swiping a row left, or tapping the delete icon, opens a confirmation dialog. Confirming removes that user from both the database and the on-screen list.

## Getting Started

1. Clone the repository and open it in Android Studio.
2. Let Gradle sync and download dependencies.
3. Run the app on an emulator or physical device (minimum SDK as configured in `app/build.gradle`).

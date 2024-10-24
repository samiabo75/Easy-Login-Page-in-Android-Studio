package com.ls.loginpage.DashboardActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ls.loginpage.R;
import com.ls.loginpage.data.DatabaseHelper;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, usernameInput, passwordInput;
    private Button signUpButton;
    private TextView loginRedirectText;
    private DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // חיבור ה-UI לשדות קלט והכפתורים
        nameInput = findViewById(R.id.signup_name);
        emailInput = findViewById(R.id.signup_email);
        usernameInput = findViewById(R.id.signup_username);
        passwordInput = findViewById(R.id.signup_password);
        signUpButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        // יצירת מופע של DatabaseHelper
        myDB = new DatabaseHelper(SignUpActivity.this);

        // מאזין לכפתור ההרשמה
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String username = usernameInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                // בדיקה אם כל השדות מלאים
                if (name.isEmpty()) {
                    nameInput.setError("יש להזין שם");
                    return;
                }
                else if (email.isEmpty()) {
                    emailInput.setError("יש להזין אימייל");
                    return;
                }
                else if (username.isEmpty()) {
                    usernameInput.setError("יש להזין שם משתמש");
                    return;
                }
                else if (password.isEmpty()) {
                    passwordInput.setError("יש להזין סיסמה");
                    return;
                }
                else if (! myDB.checkEmail(email)){
                    // הוספת המשתמש למסד הנתונים
                    Boolean isInserted = myDB.addUser(name, email, username, password);
                    if (isInserted) {
                        Toast.makeText(SignUpActivity.this, "המשתמש נוסף בהצלחה!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignUpActivity.this, "הוספת המשתמש נכשלה!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(SignUpActivity.this, "המשתמש כבר קיים במערכת!", Toast.LENGTH_SHORT).show();
                }
           }
        });

        // הפניה למסך ההתחברות
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

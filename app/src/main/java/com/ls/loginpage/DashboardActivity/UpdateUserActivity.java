package com.ls.loginpage.DashboardActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.ls.loginpage.R;
import com.ls.loginpage.data.DatabaseHelper;

public class UpdateUserActivity extends AppCompatActivity {

    // רכיבי הממשק לעדכון נתוני המשתמשים
    EditText name_input, email_input, username_input;
    Spinner role_spinner;
    Button update_button, delete_button;

    // משתנים לאחסון הנתונים המועברים מה-Intent
    String id, name, email, username, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        // אתחול רכיבי ה-View מה-XML
        name_input = findViewById(R.id.name_input);
        email_input = findViewById(R.id.email_input);
        username_input = findViewById(R.id.username_input);
        role_spinner = findViewById(R.id.role_spinner);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);

        // אתחול Spinner עם רשימת תפקידים
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.user_roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role_spinner.setAdapter(adapter);

        // הגדרת "Registered User" כברירת מחדל בתור ערך התפקיד
        int defaultPosition = adapter.getPosition("Registered User");
        role_spinner.setSelection(defaultPosition);  // קובע את הערך כברירת מחדל

        // קריאת הנתונים מה-Intent והצגתם בשדות הקלט
        getAndSetIntentData();

        // הגדרת כותרת ה-ActionBar על פי שם המשתמש
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(name);  // הגדרת כותרת בהתאם לשם המשתמש
        }

        // מאזין לאירוע לחיצה על כפתור העדכון
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // יצירת מופע של DatabaseHelper לצורך עדכון המשתמש במסד הנתונים
                DatabaseHelper myDB = new DatabaseHelper(UpdateUserActivity.this);

                // קבלת ערכים מעודכנים מהשדות
                name = name_input.getText().toString().trim();
                email = email_input.getText().toString().trim();
                username = username_input.getText().toString().trim();
                role = role_spinner.getSelectedItem().toString();  // קבלת התפקיד שנבחר מה-Spinner

                // קריאה למתודה לעדכון הנתונים במסד הנתונים
                myDB.updateUser(id, name, email, username, role); // עדכון המשתמש במסד הנתונים
            }
        });

        // מאזין לאירוע לחיצה על כפתור המחיקה
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // פתיחת דיאלוג אישור למחיקת המשתמש
                confirmDialog();
            }
        });
    }

    // מתודה לקבלת הנתונים מה-Intent והצגתם בשדות הקלט
    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("name") &&
                getIntent().hasExtra("email") && getIntent().hasExtra("username") && getIntent().hasExtra("role")) {
            // קבלת הנתונים מה-Intent והצגתם בשדות הקלט
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            username = getIntent().getStringExtra("username");
            role = getIntent().getStringExtra("role");

            // הצגת הנתונים בשדות ה-EditText
            name_input.setText(name);
            email_input.setText(email);
            username_input.setText(username);

            // קביעת הערך המתאים בתפריט ה-Spinner לפי התפקיד
            ArrayAdapter adapter = (ArrayAdapter) role_spinner.getAdapter();
            int spinnerPosition = adapter.getPosition(role);
            role_spinner.setSelection(spinnerPosition);

        } else {
            // הודעה למשתמש במקרה שאין נתונים מה-Intent
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    // פתיחת דיאלוג אישור למחיקת המשתמש
    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + " ?");  // הצגת שם המשתמש בכותרת הדיאלוג
        builder.setMessage("Are you sure you want to delete " + name + " ?");  // הצגת הודעת אישור למחיקה

        // לחיצה על כפתור "Yes" תמחק את המשתמש
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // מחיקת המשתמש ממסד הנתונים וסגירת הפעילות
                DatabaseHelper myDB = new DatabaseHelper(UpdateUserActivity.this);
                myDB.deleteOneUser(id);  // קריאה לפונקציה למחיקת השורה
                finish();  // סגירת הפעילות לאחר המחיקה
            }
        });

        // לחיצה על כפתור "No" תסגור את הדיאלוג
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // אין פעולה נוספת במקרה של לחיצה על "No"
            }
        });
        // הצגת הדיאלוג
        builder.create().show();
    }
}

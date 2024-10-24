package com.ls.loginpage.DashboardActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ls.loginpage.R;
import com.ls.loginpage.adapter.CustomAdapter;
import com.ls.loginpage.data.DatabaseHelper;

import java.util.ArrayList;

public class ManagerUserActivity extends AppCompatActivity {

    RecyclerView recyclerView; // הפניה ל-RecyclerView להצגת נתוני המשתמשים
    FloatingActionButton add_button; // כפתור להוספת משתמש חדש
    ImageView empty_imageview; // תמונה שמוצגת כשאין נתונים
    TextView no_data; // טקסט שמוצג כשאין נתונים

    DatabaseHelper myDB; // מחלקת עזר לניהול מסד הנתונים
    ArrayList<String> user_id, user_name, user_email, user_username, user_role; // אוספים לאחסון פרטי המשתמשים
    CustomAdapter customAdapter; // ה-Adapter ל-RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_user); // תצוגת המחלקה

        recyclerView = findViewById(R.id.recyclerView); // הפניה ל-RecyclerView מממשק המשתמש
        add_button = findViewById(R.id.add_button); // כפתור להוספת משתמש חדש
        empty_imageview = findViewById(R.id.empty_imageview); // תמונה כשאין נתונים
        no_data = findViewById(R.id.no_data); // טקסט כשאין נתונים

        // מאזין ללחיצה על כפתור ההוספה, מעביר לפעילות הוספת משתמש חדש
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerUserActivity.this, SignUpActivity.class); // מעבר לעמוד הוספת משתמש
                startActivity(intent); // התחלת הפעילות SignUpActivity
            }
        });

        myDB = new DatabaseHelper(ManagerUserActivity.this); // יצירת מופע של MyDatabaseHelper
        user_id = new ArrayList<>(); // רשימת מזהי המשתמשים
        user_name = new ArrayList<>(); // רשימת שמות המשתמשים
        user_email = new ArrayList<>(); // רשימת אימיילים
        user_username = new ArrayList<>(); // רשימת שמות המשתמשים
        user_role = new ArrayList<>(); // רשימת תפקידים

        storeDataInArrays(); // קריאה למתודה לשליפת הנתונים ואחסונם באוספים

        // יצירת ה-Adapter עבור ה-RecyclerView
        customAdapter = new CustomAdapter(ManagerUserActivity.this, this, user_id, user_name, user_email, user_username, user_role);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ManagerUserActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate(); // רענון הפעילות לאחר הוספת או עדכון נתונים
        }
    }

    // מתודה לשליפת הנתונים ממסד הנתונים ואחסונם באוספים
    void storeDataInArrays() {
        Cursor cursor = myDB.readAllUsers(); // הפניה לקריאת כל המשתמשים מהמסד
        if(cursor.getCount() == 0) {
            empty_imageview.setVisibility(View.VISIBLE); // הצגת תמונת 'אין נתונים'
            no_data.setVisibility(View.VISIBLE); // הצגת טקסט 'אין נתונים'
        } else {
            while (cursor.moveToNext()) {
                user_id.add(cursor.getString(0)); // הוספת מזהה משתמש לאוסף
                user_name.add(cursor.getString(1)); // הוספת שם משתמש לאוסף
                user_email.add(cursor.getString(2)); // הוספת אימייל לאוסף
                user_username.add(cursor.getString(3)); // הוספת שם משתמש לאוסף
                user_role.add(cursor.getString(4)); // הוספת תפקיד לאוסף
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu); // הוספת פריטי התפריט מקובץ ה-XML
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_all) {
            confirmDialog(); // פתיחת דיאלוג לאישור מחיקת כל הנתונים
        }
        return super.onOptionsItemSelected(item);
    }

    // פתיחת דיאלוג לאישור מחיקת כל המשתמשים
    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all users?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper myDB = new DatabaseHelper(ManagerUserActivity.this);
                myDB.deleteAllUsers(); // מחיקת כל המשתמשים
                Intent intent = new Intent(ManagerUserActivity.this, ManagerUserActivity.class);
                startActivity(intent); // התחלת הפעילות מחדש כדי לעדכן את התצוגה
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // ביטול
            }
        });
        builder.create().show();
    }
}

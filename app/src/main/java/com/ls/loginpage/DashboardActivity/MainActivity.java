package com.ls.loginpage.DashboardActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ls.loginpage.R;
import com.ls.loginpage.data.DatabaseHelper;

import java.text.BreakIterator;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button loginButton;
    private TextView signupText;
    private DatabaseHelper databaseHelper;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signupText = findViewById(R.id.signupText);

        // יצירת מופע של DatabaseHelper לניהול מסד הנתונים
        databaseHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em = email.getText().toString();
                String pass = password.getText().toString();

                if (em.equals("") || pass.equals(""))
                    Toast.makeText(MainActivity.this, "כל השדות הם חובה", Toast.LENGTH_SHORT).show();
                else {
                    Boolean check = databaseHelper.checkEmail(em);
                    if (check) {

                        role = databaseHelper.getUserRole(em, pass);

                        if (role.equals("Manager")) {
                            // אם המשתמש הוא מנהל, מעבירים אותו לאקטיביטי של מנהל
                            Toast.makeText(MainActivity.this, "התחבר בהצלחה כמנהל!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (role.equals("Employee")) {
                            // אחרת, מעבירים אותו לאקטיביטי הראשי
                            Toast.makeText(MainActivity.this, "התחבר בהצלחה כעובד!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, RestaurantDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else if (role.equals("Delivery")) {
                            // אחרת, מעבירים אותו לאקטיביטי הראשי
                            Toast.makeText(MainActivity.this, "התחבר בהצלחה כשליח!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, DeliveryDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else if (role.equals("Guest")) {
                            // אחרת, מעבירים אותו לאקטיביטי הראשי
                            Toast.makeText(MainActivity.this, "ממתין לאישור!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    else {
                        Toast.makeText(MainActivity.this, "פרטי ההתחברות שגויים", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}

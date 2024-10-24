package com.ls.loginpage.DashboardActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ls.loginpage.R;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {
    CardView PassengerDashboard, RestaurantDashboard, DeliveryDashboard,ManagerUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        PassengerDashboard = findViewById(R.id.PassengerDashboard);
        RestaurantDashboard = findViewById(R.id.RestaurantDashboard);
        DeliveryDashboard = findViewById(R.id.DeliveryDashboard);
        ManagerUser=findViewById(R.id.Manager_Dashboard);

        ManagerUser.setOnClickListener(this);
        RestaurantDashboard.setOnClickListener(this);
        DeliveryDashboard.setOnClickListener(this);
        PassengerDashboard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.RestaurantDashboard) {
            // Handle RestaurantDashboard click
            Intent restaurantIntent = new Intent(this, RestaurantDashboardActivity.class);
            startActivity(restaurantIntent);

        } else if (id == R.id.DeliveryDashboard) {
            // Handle DeliveryDashboard click
            Intent deliveryIntent = new Intent(this, DeliveryDashboardActivity.class);
            startActivity(deliveryIntent);

        } else if (id == R.id.PassengerDashboard) {
            // Handle PassengerDashboard click
            Intent passengerIntent = new Intent(this, PassengerDashboardActivity.class);
            startActivity(passengerIntent);

        } else if (id == R.id.Manager_Dashboard) {
            // Handle PassengerDashboard click
            Intent passengerIntent = new Intent(this, ManagerUserActivity.class);
            startActivity(passengerIntent);

        }
        else {
            // Default case, if needed
        }
    }
}



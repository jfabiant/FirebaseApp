package electron.jfabiant.firebaseapp.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import electron.jfabiant.firebaseapp.R;
import electron.jfabiant.firebaseapp.models.User;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAnalytics mFirebaseAnalytics;

    //GPS

    //end GPS



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString("fullname", "Danilo Lopez");

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);
        mFirebaseAnalytics.setUserProperty("username", "dlopez");

        // Get currentuser from FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "currentUser: " + currentUser);

        //Get Latitude and Logitude From user:

        //Toast.makeText(this, "Lat: "+latitude+"\n"+"Long: "+longitude, Toast.LENGTH_SHORT).show();


        // Save/Update current user to Firebase Database
        User user = new User();
        user.setUid(currentUser.getUid());
        user.setEmail(currentUser.getEmail());
        user.setLocationLat(null);
        user.setLocationLon(null);
        // user.setEtc...

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.child(user.getUid()).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onSuccess");
                        }else{
                            Log.e(TAG, "onFailure", task.getException());
                        }
                    }
                });

        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "user: " + user1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                callLogout(null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callLogout(View view){
        Log.d(TAG, "Ssign out user");
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

}


package accntkeeper.com.ph;

import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {

    private GestureLibrary store;
    GestureOverlayView gestureOverlayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        store = new CustomGesturesLibrary().getStore(this);

        gestureOverlayView = (GestureOverlayView) findViewById(R.id.loginPad);
        gestureOverlayView.addOnGesturePerformedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.loginFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        boolean ok = false;
        ArrayList<Prediction> predictions = store.recognize(gesture);
        for (Prediction prediction : predictions) {
            if (prediction.score > 5.0) {
                ok = true;
                Toast.makeText(this, "Welcome "+prediction.name+"!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                this.finish();
                break;
            }
        }

        if(!ok) Toast.makeText(this, "No match found!", Toast.LENGTH_SHORT).show();
    }
}

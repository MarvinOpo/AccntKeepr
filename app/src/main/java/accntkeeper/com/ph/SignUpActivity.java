package accntkeeper.com.ph;

import android.app.AlertDialog;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv1, iv2, iv3;
    Gesture[] gestures = new Gesture[3];
    Button register, cancel;
    EditText name;
    private GestureLibrary store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        name = (EditText) findViewById(R.id.name);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        register = (Button) findViewById(R.id.register);
        cancel = (Button) findViewById(R.id.cancel);

        register.setEnabled(false);
        register.setBackgroundResource(R.drawable.rounded_button_disable);

        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv3.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv1:
                showDialog(v);
                break;
            case R.id.iv2:
                showDialog(v);
                break;
            case R.id.iv3:
                showDialog(v);
                break;
            case R.id.register:
                if(name.getText().toString().trim().isEmpty() || name.getText().toString().length() > 50){
                    name.setError("Required! Must be less than 50 characters");
                    name.requestFocus();
                }else {
//                    DBHelper db = new DBHelper(this);
//                    if(db.nameTaken(name.getText().toString())){
//                        name.setError("Name Already Taken!");
//                        name.requestFocus();
//                    }
//                    else
                    addGesture();
                }
                break;
            case R.id.cancel:
                this.finish();
                break;
        }
    }

    public void showDialog(final View iv){
        View view = getLayoutInflater().inflate(R.layout.sign_up_dialog, null);
        final GestureOverlayView gestureView = (GestureOverlayView) view.findViewById(R.id.dialogPad);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setView(view);
        builder.setTitle("Add Signature");
        builder.setNegativeButton("Back", null);
        builder.setPositiveButton("Done", null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gestureView.getGesture()!=null && gestureView.getGesture().getLength()>0) {

                    gestureView.setDrawingCacheEnabled(true);
                    Bitmap bm = Bitmap.createBitmap(gestureView.getDrawingCache());
                    ((ImageView) iv).setImageBitmap(bm);

                    switch(iv.getId()){
                        case R.id.iv1:
                            gestures[0] = gestureView.getGesture();
                            break;
                        case R.id.iv2:
                            gestures[1] = gestureView.getGesture();
                            break;
                        case R.id.iv3:
                            gestures[2] = gestureView.getGesture();
                            register.setEnabled(true);
                            register.setBackgroundResource(R.drawable.rounded_edittext);
                            register.setOnClickListener(SignUpActivity.this);
                            break;
                    }

                    alertDialog.dismiss();
                }else{
                    Toast.makeText(SignUpActivity.this, "Please put some signature", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addGesture() {
        for(int i = 0; i < gestures.length; i++){
            store = new CustomGesturesLibrary().getStore(this);
            store.addGesture(name.getText().toString(), gestures[i]);
            store.save();
            setResult(RESULT_OK);

            final String path = new File(Environment.getExternalStorageDirectory(),
                    "gesturesHackathon").getAbsolutePath();
            Toast.makeText(this,"Success! You can now try to Login", Toast.LENGTH_LONG).show();
            this.finish();
        }
    }
}

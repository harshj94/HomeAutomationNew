package beproject.homeautomationsystem;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends Activity {

    EditText oldPassword,newPassword;
    Button change;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);

        oldPassword=(EditText)findViewById(R.id.oldPassword);
        newPassword=(EditText)findViewById(R.id.newPassword);
        change=(Button)findViewById(R.id.changePassword);
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPreferences", MODE_PRIVATE);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPassword.getText().toString().isEmpty()||newPassword.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Old Password or New password is empty",Toast.LENGTH_SHORT).show();
                }
                else if(!Login.password.equals(oldPassword.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"Old Password is Incorrect",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("password", newPassword.getText().toString());
                    Login.password=newPassword.getText().toString();
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"Password successfully changed",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}

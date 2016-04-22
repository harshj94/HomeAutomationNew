package beproject.homeautomationsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static String password;
    EditText username, pass;
    String initialPassword="admin";
    String initialUsername="admin";
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPreferences", MODE_PRIVATE);
        password = sharedPreferences.getString("password", null);
        if (password == null) {
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("password", initialPassword);
            password=initialPassword;
            editor.apply();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals(initialUsername)) {
                    if (pass.getText().toString().equals(password)) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid UserID or password!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

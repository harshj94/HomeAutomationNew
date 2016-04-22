package beproject.homeautomationsystem;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    BluetoothDevice device;
    ImageView tubeIcon, fanIcon, switchIcon;
    RelativeLayout tube, fan, switchh;
    TextView tubeText, fanText, switchText;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public String newAddress = null;
    SharedPreferences sharedPreferences;
    int i;
    boolean tubeState, fanState, switchState;
    RatingBar ratingBar, ratingBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        tubeIcon = (ImageView) findViewById(R.id.tube_icon);
        tubeText = (TextView) findViewById(R.id.tube_text);
        tube = (RelativeLayout) findViewById(R.id.tube);

        fanIcon = (ImageView) findViewById(R.id.fan_icon);
        fanText = (TextView) findViewById(R.id.fan_text);
        fan = (RelativeLayout) findViewById(R.id.fan);

        switchIcon = (ImageView) findViewById(R.id.switchh_icon);
        switchText = (TextView) findViewById(R.id.switchh_text);
        switchh = (RelativeLayout) findViewById(R.id.switchh);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar1 = (RatingBar) findViewById(R.id.ratingBar1);

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPreferences", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BluetoothConnect.class));
            }
        });

        tubeState = sharedPreferences.getBoolean("tubeState", false);
        if (!tubeState) {
            tubeIcon.setImageResource(R.mipmap.tubeoff);
            tubeText.setTextColor(getResources().getColor(R.color.colorAccent));
            ratingBar.setEnabled(false);
        } else {
            tubeIcon.setImageResource(R.mipmap.tubeon);
            tubeText.setTextColor(getResources().getColor(R.color.colorPrimary));
            ratingBar.setEnabled(true);
        }

        fanState = sharedPreferences.getBoolean("fanState", false);
        if (!fanState) {
            fanIcon.setImageResource(R.mipmap.fanoff);
            fanText.setTextColor(getResources().getColor(R.color.colorAccent));
            ratingBar1.setEnabled(false);
        } else {
            fanIcon.setImageResource(R.mipmap.fanon);
            fanText.setTextColor(getResources().getColor(R.color.colorPrimary));
            ratingBar1.setEnabled(true);
        }

        switchState = sharedPreferences.getBoolean("switchState", false);
        if (!switchState) {
            switchIcon.setImageResource(R.mipmap.switchoff);
            switchText.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            switchIcon.setImageResource(R.mipmap.switchon);
            switchText.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                i = (int) rating;
                i = i * 51;
                i = sendData(i + "");

                if (i != 1) {
                    Toast.makeText(getApplicationContext(), "ERROR: Device not Connected", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                i = (int) rating;
                i = i * 51;
                i = i + 256;
                i = sendData(i + "");

                if (i != 1) {
                    Toast.makeText(getApplicationContext(), "ERROR: Device not Connected", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tubeState) {
                    i = sendData("0");
                    if (i == 1) {
                        tubeIcon.setImageResource(R.mipmap.tubeon);
                        tubeText.setTextColor(getResources().getColor(R.color.colorPrimary));
                        editor.putBoolean("tubeState", true);
                        tubeState = true;
                        ratingBar.setEnabled(true);
                        ratingBar.setProgress(5);
                        editor.apply();
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR: Device not Connected", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    i = sendData("0");
                    if (i == 1) {
                        tubeIcon.setImageResource(R.mipmap.tubeoff);
                        tubeText.setTextColor(getResources().getColor(R.color.colorAccent));
                        editor.putBoolean("tubeState", false);
                        tubeState = false;
                        ratingBar.setProgress(0);
                        ratingBar.setEnabled(false);
                        editor.apply();
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR: Device not Connected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fanState) {
                    i = sendData("0");
                    if (i == 1) {
                        fanIcon.setImageResource(R.mipmap.fanon);
                        fanText.setTextColor(getResources().getColor(R.color.colorPrimary));
                        editor.putBoolean("fanState", true);
                        fanState = true;
                        ratingBar1.setEnabled(true);
                        ratingBar1.setProgress(5);
                        editor.apply();
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR: Device not Connected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    i = sendData("0");
                    if (i == 1) {
                        fanIcon.setImageResource(R.mipmap.fanoff);
                        fanText.setTextColor(getResources().getColor(R.color.colorAccent));
                        editor.putBoolean("fanState", false);
                        fanState = false;
                        ratingBar1.setProgress(0);
                        ratingBar1.setEnabled(false);
                        editor.apply();
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR: Device not Connected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        switchh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!switchState) {
                    i = sendData("0");
                    if (i == 1) {
                        switchIcon.setImageResource(R.mipmap.switchon);
                        switchText.setTextColor(getResources().getColor(R.color.colorPrimary));
                        editor.putBoolean("switchState", true);
                        switchState = true;
                        sendData("706");
                        editor.apply();
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR: Device not Connected", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    i = sendData("0");
                    if (i == 1) {
                        switchIcon.setImageResource(R.mipmap.switchoff);
                        switchText.setTextColor(getResources().getColor(R.color.colorAccent));
                        editor.putBoolean("switchState", false);
                        switchState = false;
                        sendData("707");
                        editor.apply();
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR: Device not Connected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent intent = getIntent();
        newAddress = intent.getStringExtra(BluetoothConnect.EXTRA_DEVICE_ADDRESS);

        if (newAddress != null) {
            device = btAdapter.getRemoteDevice(newAddress);

            try {
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e1) {
                Toast.makeText(getBaseContext(), "ERROR - Could not create Bluetooth socket", Toast.LENGTH_SHORT).show();
            }
            try {
                btSocket.connect();
            } catch (IOException e) {
                try {
                    btSocket.close();
                } catch (IOException e2) {
                    Toast.makeText(getBaseContext(), "ERROR - Could not close Bluetooth socket", Toast.LENGTH_SHORT).show();
                }
            }
            try {
                outStream = btSocket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "ERROR - Could not create bluetooth outstream", Toast.LENGTH_SHORT).show();
            }
            sendData("0");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if (newAddress != null)
                btSocket.close();
        } catch (IOException e2) {
            Toast.makeText(getBaseContext(), "ERROR - Failed to close Bluetooth socket", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkBTState() {
        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "ERROR - Device does not support bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private int sendData(String message) {
        byte[] msgBuffer = message.getBytes();
        try {
            if (newAddress != null) {
                outStream.write(msgBuffer);
                return 1;
            }
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "ERROR - Device not found", Toast.LENGTH_SHORT).show();
            finish();
            return 0;
        }
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    boolean birthsort = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.automode: {
                if (birthsort) {
                    i = sendData("704");
                    if (i == 1) {
                        tube.setEnabled(true);
                        fan.setEnabled(true);
                        tube.setClickable(true);
                        fan.setClickable(true);
                        ratingBar.setEnabled(true);
                        ratingBar1.setEnabled(true);
                        item.setIcon(R.drawable.ic_action_switch);
                        birthsort = false;
                    } else {
                        Toast.makeText(getBaseContext(), "ERROR: Device not Connected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    i = sendData("703");
                    if (i == 1) {
                        tube.setEnabled(false);
                        fan.setEnabled(false);
                        tube.setClickable(false);
                        fan.setClickable(false);
                        ratingBar.setEnabled(false);
                        ratingBar1.setEnabled(false);
                        item.setIcon(R.drawable.ic_action_switch_1);
                        birthsort = true;
                        i = sendData("703");
                    } else {
                        Toast.makeText(getBaseContext(), "ERROR: Device not Connected", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }

            case R.id.changePassword: {
                startActivity(new Intent(getApplicationContext(),ChangePassword.class));
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

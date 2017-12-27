package com.ftl.sdporzio.readaccelerometer;

// Basic imports
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
// Hardware imports (accelerometer)
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
// Interface imports
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager senSensorManager; // Class which manages the sensor
    private Sensor senAccelerometer; // Class of the actual sensor

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); // Initialize sensor manager
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // Initialize the sensor (accelerometer)
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL); // Tell manager to listen to sensor
    }

    public void updateReadings(float x, float y, float z)
    {
        TextView tv_xValue = findViewById(R.id.tv_xValue);
        TextView tv_yValue = findViewById(R.id.tv_yValue);
        TextView tv_zValue = findViewById(R.id.tv_zValue);

        tv_xValue.setText(String.valueOf(x));
        tv_yValue.setText(String.valueOf(y));
        tv_zValue.setText(String.valueOf(z));
    }

    protected void onPause()
    {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume()
    {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    // Class called whenever the accelerometer detects a change
    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        Sensor mySensor = sensorEvent.sensor;
        // Make sure we're grabbing the actual accelerometer and not some other sensor
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Grab the accelerometer coordinates
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            // Grab the current time
            long curTime = System.currentTimeMillis();

            // Make sure enough time has passed to get an update
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                updateReadings(x,y,z);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }
}
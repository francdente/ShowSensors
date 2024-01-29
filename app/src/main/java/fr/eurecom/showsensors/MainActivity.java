package fr.eurecom.showsensors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor smLight, smGravity, smMotion, smStepCounter, smAccelerometer, smMagneticField, smPressure, smChangeAudio;
    private TextView textLight, textGravity, textMotion, textStepCounter, textAccelerometer1, textAccelerometer2, textAccelerometer3, textMagneticField1, textMagneticField2, textMagneticField3, textPressure, textChangeAudio;
    private float sLightCurrentValue, sGravityCurrentValue, sMotionCurrentValue, sStepCounterCurrentValue, sAccelerometerCurrentValue1, sAccelerometerCurrentValue2, sAccelerometerCurrentValue3, sMagneticFieldCurrentValue1, sMagneticFieldCurrentValue2, sMagneticFieldCurrentValue3, sPressureCurrentValue, sChangeAudioCurrentValue;
    private String sensorError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get the sensor service and retrieve the list of sensors within onCreate lifecycle method
        sm = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        sensorError = getResources().getString(R.string.error_no_sensor);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO}, 0);
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }
        initializeLightBtn();
        initializeGravityBtn();
        initializeMotionBtn();
        initializeStepCounterBtn();
        initializeAccelerometerBtn();
        initializeMagneticFieldBtn();
        initializePressureBtn();
        initializeChangeAudioBtn();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Register the listener for all the sensors
        if (smLight != null) {
            // for a faster rate, use SENSOR_DELAY_GAME or SENSOR_DELAY_FASTEST
            sm.registerListener(this, smLight,SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (smGravity != null) {
            sm.registerListener(this, smGravity,SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (smMotion != null) {
            sm.registerListener(this, smMotion,SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (smStepCounter != null) {
            sm.registerListener(this, smStepCounter,SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (smAccelerometer != null) {
            sm.registerListener(this, smAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (smMagneticField != null) {
            sm.registerListener(this, smMagneticField,SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (smPressure != null) {
            sm.registerListener(this, smPressure,SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (smChangeAudio != null) {
            sm.registerListener(this, smChangeAudio,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        sm.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            sLightCurrentValue = event.values[0];
            // Adjust the background color based on the light level
            float normalizedLightValue = sLightCurrentValue / 40000;
            int backgroundColor = interpolateColor(normalizedLightValue);

            // Set the background color of the app
            getWindow().getDecorView().setBackgroundColor(backgroundColor);
        } else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            sGravityCurrentValue = event.values[0];
        } else if (event.sensor.getType() == Sensor.TYPE_SIGNIFICANT_MOTION) {
            sMotionCurrentValue = event.values[0];
        } else if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            sStepCounterCurrentValue = event.values[0];
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            sAccelerometerCurrentValue1 = event.values[0];
            sAccelerometerCurrentValue2 = event.values[1];
            sAccelerometerCurrentValue3 = event.values[2];
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            sMagneticFieldCurrentValue1 = event.values[0];
            sMagneticFieldCurrentValue2 = event.values[1];
            sMagneticFieldCurrentValue3 = event.values[2];
        } else if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            sPressureCurrentValue = event.values[0];
        } else if (event.sensor.getType() == Sensor.TYPE_DEVICE_PRIVATE_BASE) {
            sChangeAudioCurrentValue = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private int interpolateColor(float value) {
        // This method interpolates the color based on the light level

        int colorStart = Color.BLACK; // Start color (e.g., for low light)
        int colorEnd = Color.WHITE;  // End color (e.g., for high light)

        float[] hsvStart = new float[3];
        float[] hsvEnd = new float[3];

        Color.colorToHSV(colorStart, hsvStart);
        Color.colorToHSV(colorEnd, hsvEnd);

        // Interpolate between start and end colors based on the light value
        float[] interpolatedHsv = new float[3];
        for (int i = 0; i < 3; i++) {
            interpolatedHsv[i] = (1 - value) * hsvStart[i] + value * hsvEnd[i];
        }

        return Color.HSVToColor(interpolatedHsv);
    }

    private void initializeChangeAudioBtn() {
        textChangeAudio = findViewById(R.id.change_audio_text);
        sChangeAudioCurrentValue = 0.0f;
        smChangeAudio = sm.getDefaultSensor(Sensor.TYPE_DEVICE_PRIVATE_BASE);
        findViewById(R.id.change_audio_btn).setOnClickListener(this::getData);
    }

    private void initializePressureBtn() {
        textPressure = findViewById(R.id.pressure_text);
        sPressureCurrentValue = 0.0f;
        smPressure = sm.getDefaultSensor(Sensor.TYPE_PRESSURE);
        findViewById(R.id.pressure_btn).setOnClickListener(this::getData);
    }

    private void initializeMagneticFieldBtn() {
        textMagneticField1 = findViewById(R.id.text_mag_1);
        textMagneticField2 = findViewById(R.id.text_mag_2);
        textMagneticField3 = findViewById(R.id.text_mag_3);
        sMagneticFieldCurrentValue1 = 0.0f;
        sMagneticFieldCurrentValue2 = 0.0f;
        sMagneticFieldCurrentValue3 = 0.0f;
        smMagneticField = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        findViewById(R.id.magnetic_field_btn).setOnClickListener(this::getData);
    }

    private void initializeAccelerometerBtn() {
        textAccelerometer1 = findViewById(R.id.text_acc_1);
        textAccelerometer2 = findViewById(R.id.text_acc_2);
        textAccelerometer3 = findViewById(R.id.text_acc_3);
        sAccelerometerCurrentValue1 = 0.0f;
        sAccelerometerCurrentValue2 = 0.0f;
        sAccelerometerCurrentValue3 = 0.0f;
        smAccelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        findViewById(R.id.accelerometer_btn).setOnClickListener(this::getData);
    }

    private void initializeStepCounterBtn() {
        textStepCounter = findViewById(R.id.step_counter_text);
        sStepCounterCurrentValue = 0.0f;
        smStepCounter = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        findViewById(R.id.step_counter_btn).setOnClickListener(this::getData);
    }

    private void initializeMotionBtn() {
        textMotion = findViewById(R.id.motion_text);
        sMotionCurrentValue = 0.0f;
        smMotion = sm.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        findViewById(R.id.motion_btn).setOnClickListener(this::getData);
    }

    private void initializeGravityBtn() {
        textGravity = findViewById(R.id.gravity_text);
        sGravityCurrentValue = 0.0f;
        smGravity = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
        findViewById(R.id.gravity_btn).setOnClickListener(this::getData);
    }

    private void initializeLightBtn() {
        textLight = findViewById(R.id.light_text);
        sLightCurrentValue = 0.0f;
        smLight = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        findViewById(R.id.light_btn).setOnClickListener(this::getData);
    }
    private void getData(View view) {
        int id = view.getId();
        if (id == R.id.light_btn) {
            if (smLight == null) {
                textLight.setText(sensorError);
            } else {
                textLight.setText(String.valueOf(sLightCurrentValue));
            }
        } else if (id == R.id.gravity_btn) {
            if (smGravity == null) {
                textGravity.setText(sensorError);
            } else {
                textGravity.setText(String.valueOf(sGravityCurrentValue));
            }
        } else if (id == R.id.motion_btn) {
            if (smMotion == null) {
                textMotion.setText(sensorError);
            } else {
                textMotion.setText(String.valueOf(sMotionCurrentValue));
            }
        } else if (id == R.id.step_counter_btn) {
            if (smStepCounter == null) {
                textStepCounter.setText(sensorError);
            } else {
                textStepCounter.setText(String.valueOf(sStepCounterCurrentValue));
            }
        } else if (id == R.id.accelerometer_btn) {
            if (smAccelerometer == null) {
                textAccelerometer1.setText(sensorError);
                textAccelerometer2.setText(sensorError);
                textAccelerometer3.setText(sensorError);
            } else {
                textAccelerometer1.setText(String.valueOf(sAccelerometerCurrentValue1));
                textAccelerometer2.setText(String.valueOf(sAccelerometerCurrentValue2));
                textAccelerometer3.setText(String.valueOf(sAccelerometerCurrentValue3));
            }
        } else if (id == R.id.magnetic_field_btn) {
            if (smMagneticField == null) {
                textMagneticField1.setText(sensorError);
                textMagneticField2.setText(sensorError);
                textMagneticField3.setText(sensorError);
            } else {
                textMagneticField1.setText(String.valueOf(sMagneticFieldCurrentValue1));
                textMagneticField2.setText(String.valueOf(sMagneticFieldCurrentValue2));
                textMagneticField3.setText(String.valueOf(sMagneticFieldCurrentValue3));
            }
        } else if (id == R.id.pressure_btn) {
            if (smPressure == null) {
                textPressure.setText(sensorError);
            } else {
                textPressure.setText(String.valueOf(sPressureCurrentValue));
            }
        } else if (id == R.id.change_audio_btn) {
            if (smChangeAudio == null) {
                textChangeAudio.setText(sensorError);
            } else {
                textChangeAudio.setText(String.valueOf(sChangeAudioCurrentValue));
            }
        }
    }


}
package org.simonevans.touristdash.library.sensor;

import java.util.List;

import org.simonevans.touristdash.library.TouristDash;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.util.Log;

public class RollReader {

	private static SensorManager sensorManager;
	private static RollListener listener;
	private static Boolean supported;
	private static boolean running = false;
	
	/**
	 * @param accelerometerListener - a class that has implemented the AccelerometerListener interface
	 * 
	 * Registers interest in required listeners (TYPE_ACCELEROMETER & TYPE_MAGNETIC_FIELD)
	 * 
	 * 
	 */
	public static void startListening(RollListener accelerometerListener) {
		sensorManager = (SensorManager) TouristDash.getContext().getSystemService(Context.SENSOR_SERVICE);
		Sensor accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		Sensor magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		running = (sensorManager.registerListener(sensorEventListener, accSensor, SensorManager.SENSOR_DELAY_GAME) &&
				sensorManager.registerListener(sensorEventListener, magSensor, SensorManager.SENSOR_DELAY_NORMAL));
		listener = accelerometerListener;
	}
	
	public static boolean isListening() {
		return running;
	}
	
	public static void stopListening() {
		running = false;
		try {
			if(sensorManager != null && sensorEventListener != null) {
				sensorManager.unregisterListener(sensorEventListener);
			}
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}
	}
	
	/**
	 * 
	 * We need both Accelerometer and Magnetic Field readings to compute the yaw/pitch/(!!roll!!) of the device
	 * 
	 * @return boolean supported - Whether required sensors are available on device 
	 */
	
	public static boolean isSupported() {
		if (supported == null || true) {
			if(TouristDash.getContext() != null) {
				sensorManager = (SensorManager) TouristDash.getContext().getSystemService(Context.SENSOR_SERVICE);
				List<Sensor> accSensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
				List<Sensor> magSensors = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
				Log.w("SIMON", accSensors.size() + ", " + magSensors.size());
				supported = new Boolean(accSensors.size() > 0 && magSensors.size() > 0);
			}
			else {
				supported = false;
			}
		}
		return supported;
	}
	
	/**
	 * 
	 * Listens for system calls detailing an update of the accelerometer or magnetic field
	 * 
	 */
	
	private static SensorEventListener sensorEventListener = new SensorEventListener() {

		private float[] rotationMatrix = new float[9];
		private float[] orientation = new float[3];
		private float[] accValues = new float[3];
		private float[] magValues = new float[3];
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				accValues = event.values;
			} else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				magValues = event.values;
			}
			if(accValues != null && magValues != null) {
				if(SensorManager.getRotationMatrix(rotationMatrix, null, accValues, magValues)) {
					orientation = SensorManager.getOrientation(rotationMatrix, orientation);
					listener.onRollChanged(orientation[2]);
				}
			}
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) { }
	};
	
}

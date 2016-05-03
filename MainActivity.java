public class MainActivity extends Activity implements SensorEventListener{
	
	//change these values according to your threshold requirement
	    	private static final int FORCE_THRESHOLD = 800;
	    	private static final int TIME_THRESHOLD = 150;
	    	private static final int SHAKE_TIMEOUT = 500;
	    	private static final int SHAKE_DURATION = 1500;
	    	private static final int SHAKE_COUNT = 3;
	
	
	private SensorManager sm;
	private int mShakeCount = 0;
	private long mLastShake;
	private long mLastForce;
	private float mLastX = -1.0f, mLastY = -1.0f,mLastZ = -1.0f;
	private float x, y, z;
	private long mLastTime;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		//setSensorManager
		sensor_manager_online();
	}
	
	private void sensor_manager_online() {
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            //register listener for Accelerometer 
			sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            
        } else {
			//missing Accelerometer
            Toast.makeText(MainActivity.this, "Phone doesn't support shake!!", Toast.LENGTH_SHORT).show();
        }

    }
	
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
			
			//Check for Shake with current Values.
			checkShake(x, y, z);
        }
    }

    private void checkShake(float a, float b, float c) {
        long now = System.currentTimeMillis();
        if ((now - mLastForce) > SHAKE_TIMEOUT) {
            mShakeCount = 0;
        }
        if ((now - mLastTime) > TIME_THRESHOLD) {
            long diff = now - mLastTime;
            float speed = Math.abs(a + b + c - mLastX - mLastY - mLastZ) / diff * 10000;
            if (speed > FORCE_THRESHOLD) {
                if ((++mShakeCount >= SHAKE_COUNT) && (now - mLastShake > SHAKE_DURATION)) {
                    mLastShake = now;
                    mShakeCount = 0;
                    
					//Do Something.
					
                }
                mLastForce = now;
            }
            mLastTime = now;
            mLastX = a;
            mLastY = b;
            mLastZ = c;
        }
    }
	
	
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
	
    @Override
    protected void onPause() {
        super.onPause();
        if (sm != null) {
			//unregister listener for sensor
            sm.unregisterListener(this);
            sm = null;
        }
	}
	
	
    @Override
    protected void onResume() {
        super.onResume();
		//register listener for sensor
		sensor_manager_online();
	}
}

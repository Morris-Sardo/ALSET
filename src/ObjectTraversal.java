import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class ObjectTraversal implements Behavior{

	@Override
	public boolean takeControl() {
		return false;
		
	}

	@Override
	public void action() {
		
	}

	@Override
	public void suppress() {
		
	}
	
	// NOTE: 
	//	- To make robot rotate ~90 degrees on the spot, make it rotate by 200 amounts.
	//	- The ultrasonic sensor motor's turn is clockwise relative to the top of the robot,
	//	  meaning that 90 makes it turn to its right, 0 to the front and so on. 
	//	- REMEMBER, ultrasonic's motor is set 0 as when PROGRAM STARTS, there is no absolute.
	
	
	final static float WHEEL_DIAMETER = 51; // The diameter (mm) of the wheels
	final static float AXLE_LENGTH = 44; // The distance (mm) your two driven wheels
	final static float ANGULAR_SPEED = 100; // How fast around corners (degrees/sec)
	final static float LINEAR_SPEED = 70; // How fast in a straight line (mm/sec)
	
	final static int SENSORMOTOR = 100 ;
	
	public static void main(String[] args) {
		// ULTRASONIC SENSOR PARTS
		// assign tacho to vars
		int tach_start, tach_end ;
		
		ObjectDetection OD = new ObjectDetection();
		OD.start();
		
		// ULTRASONIC SENSOR MOTOR
		BaseRegulatedMotor distanceSensorMotor = new EV3MediumRegulatedMotor(MotorPort.C) ;
		// /ULTRASONIT SENSOR MOTOR
		
		
		// WHEEL PARTS
		BaseRegulatedMotor mLeft = new EV3LargeRegulatedMotor(MotorPort.A) ;
		BaseRegulatedMotor mRight = new EV3LargeRegulatedMotor(MotorPort.D) ;
		
		mLeft.synchronizeWith(new BaseRegulatedMotor[] {mRight}) ;
		
		// Create a ”Wheel” with Diameter 51mm and offset 22mm left of centre.
		Wheel wLeft = WheeledChassis.modelWheel(mLeft, WHEEL_DIAMETER).offset(-AXLE_LENGTH / 2);
				
		// Create a ”Wheel” with Diameter 51mm and offset 22mm right of centre.
		Wheel wRight = WheeledChassis.modelWheel(mRight, WHEEL_DIAMETER).offset(AXLE_LENGTH / 2);
		
		// Create a ”Chassis” with two wheels on it.
		Chassis chassis = new WheeledChassis( (new Wheel[] {wRight, wLeft}),
											   WheeledChassis.TYPE_DIFFERENTIAL);
		
		// Finally create a pilot which can drive using this chassis.
		MovePilot pilot = new MovePilot(chassis);
		
		pilot.setAngularSpeed(ANGULAR_SPEED) ;
		pilot.setLinearSpeed(LINEAR_SPEED) ;		
		// /WHEEL PARTS
	
		
		// Turn 90 degrees on the stop
		pilot.rotate(200) ;
		
		// Also turn distance detection so that it points
		// towards wall.
		distanceSensorMotor.rotateTo(SENSORMOTOR) ;		
		
		
		// 		KEEP GOING UNTIL WALL IS NO LONGER DETECTED
		// Get starting tacho count, this will be used later
		tach_start = mLeft.getTachoCount() ;
		
		
		
		// while loop, keep going until does not detect wall beside it
		pilot.forward() ;		
		
		float current_distance ;
		while(true) {
			current_distance = movement.getDistanceFromObject();
			if (current_distance >= 100) {
				break;
			}	
		}
		
	
		// keep going forwards for a little bit
		pilot.travel(210) ;
		
		// assign tacho count to a var
		tach_end = mLeft.getTachoCount() ;
		
		System.out.println(tach_end - tach_start) ;
		
		// turn -90, do NOT change distance sensor position
		pilot.rotate(-200) ;
		
		
//		// keep going until wall is not detected on the side
//		// as obstruction may be a box.
//		while (true) {
//			sp.fetchSample(sample, 0) ;
//			
//			if (sample[0] >= 100) {
//				break ;
//			}
//		}
//		
//		// turn -90 again
//		pilot.rotate(-200) ;
//		
//		// go forward the amount of tacho count we assigned before
//		float distance_travelled = WHEEL_DIAMETER*3.14159f ;
//		pilot.travel(distance_travelled) ;
//		
//		// then rotate 90. In theory, we should be back where robot 
//		// was when it was behind the box/obstruction.
//		pilot.rotate(200) ;
//		
//		
//		distanceSensorMotor.close() ;
		

	}

}

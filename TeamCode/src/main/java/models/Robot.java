package models;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.vuforia.TrackableResult;

public class Robot {
    public Wheels wheels;
    public Arms arms;
    public DistanceSensor distanceSensor;
    public EncoderConverter encoderConverter;

    Robot (DcMotor leftWheel,
           DcMotor rightWheel,
           Servo servoDeposit,
           Servo servoCollect,
           Servo servoWrist,
           DcMotor motorExpansion,
           DcMotor motorLander,
           DistanceSensor distanceSensor,
           float wheelDiameter,
           float gearRatio,
           float distanceBetweenWheels) {

        this.wheels = new Wheels(leftWheel, rightWheel);

        this.arms = new Arms(servoDeposit,
                             servoCollect,
                             servoWrist,
                             motorExpansion,
                             motorLander);

        this.distanceSensor = distanceSensor;

        this.encoderConverter = new EncoderConverter(wheelDiameter,
                                                     gearRatio,
                                                     distanceBetweenWheels);
    }


}

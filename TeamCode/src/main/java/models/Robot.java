package models;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.vuforia.TrackableResult;

public class Robot {
    private Wheels wheels;
    private Arms arms;
    private DistanceSensor distanceSensor;
    private EncoderConverter encoderConverter;

    Robot (Wheels wheels, Arms arms, DistanceSensor distanceSensor, EncoderConverter encoderConverter) {

        this.wheels = new Wheels(wheels.leftWheel, wheels.rightWheel);

        this.arms = new Arms(arms.servoDeposit,
                             arms.servoCollect,
                             arms.servoWrist,
                             arms.motorExpansion,
                             arms.motorLander);

        this.distanceSensor = distanceSensor;

        this.encoderConverter = new EncoderConverter(encoderConverter.wheelDiameter,
                                                     encoderConverter.gearRatio,
                                                     encoderConverter.distanceBetweenWheels);
    }

}

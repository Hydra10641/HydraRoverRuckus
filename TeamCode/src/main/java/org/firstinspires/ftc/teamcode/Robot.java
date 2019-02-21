package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Robot {
    public Wheels wheels;
    public Omni omni;
    public Arms arms;
    public DistanceSensor distanceSensor;

    //Robot with normal wheels

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

        this.wheels = new Wheels(leftWheel,
                                 rightWheel,
                                 wheelDiameter,
                                 gearRatio,
                                 distanceBetweenWheels);

        this.arms = new Arms(servoDeposit,
                             servoCollect,
                             servoWrist,
                             motorExpansion,
                             motorLander);

        this.distanceSensor = distanceSensor;

    }

    //Robot with omnidirectional wheels

    Robot (DcMotor leftFrontWheel,
           DcMotor leftBackWheel,
           DcMotor rightFrontWheel,
           DcMotor rightBackWheel,
           Servo servoDeposit,
           Servo servoCollect,
           Servo servoWrist,
           DcMotor motorExpansion,
           DcMotor motorLander,
           DistanceSensor distanceSensor,
           float wheelDiameter,
           float gearRatio,
           float distanceBetweenWheels) {

        this.omni = new Omni(leftFrontWheel,
                             leftBackWheel,
                             rightFrontWheel,
                             rightBackWheel,
                             wheelDiameter,
                             gearRatio,
                             distanceBetweenWheels);

        this.arms = new Arms(servoDeposit,
                servoCollect,
                servoWrist,
                motorExpansion,
                motorLander);

        this.distanceSensor = distanceSensor;

    }
}

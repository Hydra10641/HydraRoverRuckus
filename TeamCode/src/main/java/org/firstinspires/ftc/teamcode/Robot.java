package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Robot {
    public Wheels wheels;
    public Arms arms;
    public LynxI2cColorRangeSensor  distanceSensor;

    //Robot with normal wheels

    Robot (DcMotor leftWheel,
           DcMotor rightWheel,
           Servo servoCollectWrist,
           DcMotor motorCollectSlide,
           DcMotor motorDepositSlide,
           float wheelDiameter,
           float gearRatio,
           float distanceBetweenWheels) {

        this.wheels = new Wheels(leftWheel,
                                 rightWheel,
                                 wheelDiameter,
                                 gearRatio,
                                 distanceBetweenWheels);

        this.arms = new Arms(servoCollectWrist,
                             motorCollectSlide,
                             motorDepositSlide);
    }

}
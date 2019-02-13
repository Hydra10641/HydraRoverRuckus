package models;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DistanceSensor;


public class Robot {
    private Omni wheels;
    private Arms arms;
    private DistanceSensor distanceSensor;

    public Robot ( DcMotor leftFrontMotor, DcMotor leftBackMotor, DcMotor rightFrontMotor, DcMotor righBackMotor,
    Servo colectServo, Servo colectExpansionServo, Servo depositServo, Servo depositExpansionServo) {
        this.wheels = new Omni(leftFrontMotor, leftBackMotor, rightFrontMotor, righBackMotor);
        this.arms = new Arms(colectServo, colectExpansionServo, depositServo, depositExpansionServo);
        this.distanceSensor = distanceSensor;
    }

}

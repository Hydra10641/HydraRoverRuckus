package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Arms {

    /*Welcome, this is the team 10641(Hydra) code for the arms system, first applied in the Rover Ruckus season.
    First of all, we declare 4 attributes of type Servo, corresponding to the servos used on the arms system.*/

    public Servo servoCollectWrist, servoDepositWrist;
    public CRServo crServoCollect;
    public DcMotor motorCollectSlide, motorDepositSlide;

    Arms (CRServo crServoCollect, Servo servoCollectWrist, Servo servoDepositWrist, DcMotor motorCollectSlide, DcMotor motorDepositSlide){

        this.crServoCollect = crServoCollect;
        this.servoCollectWrist = servoCollectWrist;
        this.servoDepositWrist = servoDepositWrist;
        this.motorCollectSlide = motorCollectSlide;
        this.motorDepositSlide = motorDepositSlide;
    }
    /*Here we create one method that we will use to set the arms position.*/

    public void moveOnBy(float speedOrPosition, String moveType){
        switch (moveType){
            case "collect_slide":
                this.motorCollectSlide.setPower(speedOrPosition);
                break;
            case "deposit_slide":
                this.motorDepositSlide.setPower(speedOrPosition);
                break;
            case "collect_wrist":
                this.servoCollectWrist.setPosition(speedOrPosition);
                break;
            case "deposit_wrist":
                this.servoDepositWrist.setPosition(speedOrPosition);
                break;
        }

    }

}
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Arms {

    /*Welcome, this is the team 10641(Hydra) code for the arms system, first applied in the Rover Ruckus season.
    First of all, we declare 4 attributes of type Servo, corresponding to the servos used on the arms system.*/

    public Servo servoDepositWrist, servoCollectWrist;
    public CRServo crServoCollect;
    public DcMotor motorCollectSlide, motorDepositSlide;

    Arms (CRServo crServoCollect, Servo servoDepositWrist, Servo servoCollectWrist, DcMotor motorCollectSlide, DcMotor motorDepositSlide){

        this.crServoCollect = crServoCollect;
        this.servoDepositWrist = servoDepositWrist;
        this.servoCollectWrist = servoCollectWrist;
        this.motorCollectSlide = motorCollectSlide;
        this.motorDepositSlide = motorDepositSlide;
    }
    /*Here we create one method that we will use to set the arms position.*/

    public void moveOnBy(double position, String moveType){
        switch (moveType){
            case "collect_slide":
                this.motorCollectSlide.setPower(position);
                break;
            case "deposit_slide":
                this.motorDepositSlide.setPower(position);
                break;
            case "collect_wrist":
                this.servoDepositWrist.setPosition(position);
                break;
            case "deposit_wrist":
                this.servoCollectWrist.setPosition(position);
                break;
        }

    }

}
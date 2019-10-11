package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Arms {

    /*Welcome, this is the team 16052(Hydra) code for the arms system, first applied in the Rover Ruckus season.
    First of all, we declare 4 attributes of type Servo, corresponding to the servos used on the arms system.*/

    public Servo leftServoCollectWrist, rightServoCollectWrist;
    public DcMotor motorCollectSlide, motorDepositSlide;

    Arms (Servo rightServoCollectWrist, Servo leftServoCollectWrist, DcMotor motorCollectSlide, DcMotor motorDepositSlide){
        this.rightServoCollectWrist = rightServoCollectWrist;
        this.leftServoCollectWrist = leftServoCollectWrist;
        this.motorCollectSlide = motorCollectSlide;
        this.motorDepositSlide = motorDepositSlide;
    }

    Arms (DcMotor motorCollectSlide){
        this.motorCollectSlide = motorCollectSlide;
    }
    /*Here we create one method that we will use to set the arms position.*/

    public void moveOnBy(double position, String moveType){
        switch (moveType){
            case "collect_slide":
                this.motorCollectSlide.setPower(position);
                motorCollectSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                break;
            /*case "deposit_slide":
                this.motorDepositSlide.setPower(position);
                motorDepositSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                break;
            case "collect_wrist":
                this.leftServoCollectWrist.setPosition(position);
                this.rightServoCollectWrist.setPosition(2.4 - position);
                break;*/
        }

    }

}
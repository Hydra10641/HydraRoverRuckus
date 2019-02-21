package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Arms {

    /*Welcome, this is the team 10641(Hydra) code for the arms system, first applied in the Rover Ruckus season.
    First of all, we declare 4 attributes of type Servo, corresponding to the servos used on the arms system.*/

    public Servo servoDeposit, servoCollect, servoWrist;
    public DcMotor motorExpansion, motorLander;

    Arms (Servo servoDeposit, Servo servoCollect, Servo servoWrist, DcMotor motorExpansion, DcMotor motorLander){

        this.servoDeposit = servoDeposit;
        this.servoCollect = servoCollect;
        this.servoWrist = servoWrist;
        this.motorExpansion = motorExpansion;
        this.motorLander = motorLander;
    }
    /*Here we create one method that we will use to set the arms position.*/

    public void moveOnBy(int speedOrPosition, String moveType){
        switch (moveType){
            case "expand":
                this.motorExpansion.setPower(speedOrPosition/100.0);
                break;
            case "wrist":
                this.servoWrist.setPosition(speedOrPosition/100.0);
                break;
            case "deposit":
                this.servoDeposit.setPosition(speedOrPosition/100.0);
                break;
        }

    }

}
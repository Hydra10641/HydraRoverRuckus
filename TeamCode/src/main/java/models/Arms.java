package models;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Arms {

    /*Welcome, this is the team 10641(Hydra) code for the arms system, first applied in the Rover Ruckus season.
    First of all, we declare 4 attributes of type Servo, corresponding to the servos used on the arms system.*/

    protected Servo servoDeposit, servoCollect, servoWrist;
    protected DcMotor motorExpansion, motorLander;

    Arms (Servo servoDeposit, Servo servoCollect, Servo servoWrist, DcMotor motorExpansion, DcMotor motorLander){

        this.servoDeposit = servoDeposit;
        this.servoCollect = servoCollect;
        this.servoWrist = servoWrist;
        this.motorExpansion = motorExpansion;
        this.motorLander = motorLander;
    }
    /*Here we create one method that we will use to set the arms position.*/

    public void setArmsPosition(float position[]){
        /*We have the method set position to each one of servos.*/
        servoDeposit.setPosition(position[1]);
        servoCollect.setPosition(position[2]);
        servoWrist.setPosition(position[3]);
        motorExpansion.setPower(position[4]);
    }

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

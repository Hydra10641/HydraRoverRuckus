package models;

import com.qualcomm.robotcore.hardware.Servo;

public class Arms {

    /*Welcome, this is the team 10641(Hydra) code for the arms system, first applied in the Rover Ruckus season.
    First of all, we declare 4 attributes of type Servo, corresponding to the servos used on the arms system.*/

    private Servo servoDepositArmHand = null, servoDepositArmExpansion = null, servoColectArmHand = null, servoColectArmExpansion = null;

    public void arms (Servo servoDepositArmHand, Servo servoDepositArmExpansion, Servo servoColectArmHand, Servo servoColectArmExpansion){

        this.servoDepositArmHand = servoDepositArmHand;
        this.servoDepositArmExpansion = servoDepositArmExpansion;
        this.servoColectArmHand = servoColectArmHand;
        this.servoColectArmExpansion = servoColectArmExpansion;
    }
    /*Here we create one method that we will use to set the arms position.*/
    public void setPosition(double position[]){
        /*We have the method set position to each one of servos.*/
        servoDepositArmHand.setPosition(position[1]);
        servoDepositArmExpansion.setPosition(position[2]);
        servoColectArmHand.setPosition(position[3]);
        servoColectArmExpansion.setPosition(position[4]);
    }

}

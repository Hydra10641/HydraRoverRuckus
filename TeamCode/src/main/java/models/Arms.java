package models;

import com.qualcomm.robotcore.hardware.Servo;

public class Arms {

    private Servo servoDepositArmHand = null,
                  servoDepositArmExpansion = null,
                  servoColectArmHand = null,
                  servoColectArmExpansion = null;

    public void arms (Servo servoDepositArmHand,
                      Servo servoDepositArmExpansion,
                      Servo servoColectArmHand,
                      Servo servoColectArmExpansion){

        this.servoDepositArmHand = servoDepositArmHand;
        this.servoDepositArmExpansion = servoDepositArmExpansion;
        this.servoColectArmHand = servoColectArmHand;
        this.servoColectArmExpansion = servoColectArmExpansion;
    }

    public void setPosition(double position[]){
        servoDepositArmHand.setPosition(position[1]);
        servoDepositArmExpansion.setPosition(position[2]);
        servoColectArmHand.setPosition(position[3]);
        servoColectArmExpansion.setPosition(position[4]);
    }

}

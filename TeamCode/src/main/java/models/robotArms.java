package models;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;


public class robotArms extends  LinearOpMode{
    private Servo servoDepositArmHand = null;
    private Servo servoDepositArmExpansion = null;
    private Servo servoColectArmHand = null;
    private Servo servoColectArmExpansion = null;


        //
    private double expansionDeposit;
    private double expansionColect;


    public void runOpMode(){
        setDepositArmExpansion();
        setDepositArmHand();
        setColetArmExpansion();
        setColetArmHand();
    }


    public void setDepositArmHand() {
        double angleDepositHand = -gamepad2.left_stick_y;

        setServoPosition(servoDepositArmHand, "servoDepositArmHand", angleDepositHand);
    }


    public void setDepositArmExpansion() {
        if(gamepad2.left_bumper == true){
            if(expansionDeposit <= 1) {
                expansionDeposit += 0.1;
            }
        }
        if(gamepad2.left_trigger >= 0.5){
            if(expansionDeposit >= 0) {
                expansionDeposit -= 0.1;
            }
        }
        setServoPosition(servoDepositArmExpansion, "servoDepositArmExpansion", expansionDeposit);
    }

    public void setColetArmHand() {
        double angleColectHand = -gamepad2.right_stick_y;

        setServoPosition(servoColectArmHand, "servoColectArmHand", angleColectHand);
    }


    public void setColetArmExpansion() {
        if(gamepad2.right_bumper == true){
            if(expansionColect <= 1) {
                expansionColect += 0.1;
            }
        }
        if(gamepad2.right_trigger >= 0.5){
            if(expansionColect >= 0) {
                expansionColect -= 0.1;
            }
        }
        setServoPosition(servoColectArmExpansion, "servoColectArmExpansion", expansionColect);
    }



    public void setServoPosition (Servo servo, String servoName, double angle) {
        servo = hardwareMap.get(Servo.class, servoName);
        double positionArm = Range.clip(angle, 0, 1);
        servo.setPosition(positionArm);
    }

}

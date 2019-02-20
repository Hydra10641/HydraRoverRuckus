package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DcMotor;

public class Omni {

    /*Welcome, this is the team 10641(Hydra) code for the OMNI locomotion system, first applied in the Rover Ruckus season.
     *First of all, we declare 4 attributes of type Dc Motor, corresponding to the motors used on the robot locomotion.*/

    public DcMotor leftFrontWheel, leftBackWheel, rightFrontWheel, rightBackWheel;

    Omni(DcMotor leftFrontWheel, DcMotor leftBackWheel,
         DcMotor rightFrontWheel, DcMotor rightBackWheel){
        this.leftFrontWheel = leftFrontWheel;
        this.leftBackWheel = leftBackWheel;
        this.rightFrontWheel = rightFrontWheel;
        this.rightBackWheel = rightBackWheel;
    }

    /* Here we create three methods that we will use repetitively in the methods for locomotion. The description of each
     * one is within the same.
     * */

    public void resetMotorAndEncoder(){
        // This method will stop all the locomotion motors and reset their encoders
        setMotorsPower(0, 0, 0, 0);
        leftFrontWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }
    public void setMotorsPower(int leftFrontPower, int leftBackPower, int rightFrontPower, int rightBackPower){
        // This method is responsible to energize the motors
        leftFrontWheel.setPower(leftFrontPower/100.0);
        leftBackWheel.setPower(leftBackPower/100.0);
        rightFrontWheel.setPower(rightFrontPower/100.0);
        rightBackWheel.setPower(rightBackPower/100.0);
    }
    public void waitEncoderCount(float encoderCount){
        /* This method is responsible to wait the encoder be equals to value required.
         *The encoder parameter (Count encoder) can be positive or negative, so we use the absolute value of the
         *supplied parameter and the encoder of the motors to limit the amount of rotation we want. We use the empty while
         *structure to serve as a hold structure */
        while(Math.abs(leftFrontWheel.getCurrentPosition()) <= Math.abs(encoderCount)||
                Math.abs(leftBackWheel.getCurrentPosition()) <= Math.abs(encoderCount)||
                Math.abs(rightFrontWheel.getCurrentPosition()) <= Math.abs(encoderCount)||
                Math.abs(rightBackWheel.getCurrentPosition()) <= Math.abs(encoderCount)){ }
    }

    /* We will use a pattern for the methods, where we will create a method to call in the configuration proposed by the
     * programmer (in the order: resetMotorAndEncoder (), setMotorsPower ()), and another similar method that will use the
     * encoder (in order: standardEntry (), waitEncoderCount , resetMotorAndEncoder ())
     * */

    /* walkOnBy() and walkCount() are responsible for the robot movements.
     *
     * COMMANDS:
     * standard = moves forward and backward
     * spin = rotates on its central axis
     * sidewalk = does the sidewalk move (Y axis)
     * diagonalLeft & diagonalRight = moves on diagonal (XY)
     * spinSideLeft & spinSideRight = rotates only one side of robot
     * */
    public void walkOnBy(int power, String walkType ) {
        resetMotorAndEncoder();
        switch(walkType) {
            case "standard":
                setMotorsPower(power, power, power, power);
                break;
            case "spin":
                setMotorsPower(power, power, -power, -power);
                break;
            case "sideWalk":
                setMotorsPower(-power, power, power, -power);
                break;
            case "diagonalLeft":
                setMotorsPower(power, 0, 0, power);
                break;
            case "diagonalRight":
                setMotorsPower(0, power, power, 0);
                break;
            case "spinSideLeft":
                setMotorsPower(power,power, 0, 0);
                break;
            case "spinSideRight":
                setMotorsPower(0, 0, power, power);
                break;
        }
    }

    public void walkCount (int power, float encoderCount, String walkType ){
        walkOnBy(power, walkType);
        waitEncoderCount(encoderCount);
        resetMotorAndEncoder();
    }

}
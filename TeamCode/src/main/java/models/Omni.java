package models;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Omni {

    //Welcome, this is the team 10641(Hydra) code for the OMNI locomotion system, first applied in the Rover Ruckus season.
    //First of all, we declare 4 attributes of type Dc Motor, corresponding to the motors used on the robot locomotion.

    private DcMotor leftFrontWheel = null, leftBackWheel = null, rightFrontWheel = null, rightBackWheel = null;

    public Omni(DcMotor leftFrontWheel, DcMotor leftBackWheel,
                DcMotor rightFrontWheel, DcMotor rightBackWheel){
        this.leftFrontWheel = leftFrontWheel;
        this.leftBackWheel = leftBackWheel;
        this.rightFrontWheel = rightFrontWheel;
        this.rightBackWheel = rightBackWheel;
    }

    /*Here we create three methods that we will use repetitively in the methods for locomotion. The description of each
     *one is within the same.*/

    public void resetMotorAndEncoder(){
        // This method will stop all the locomotion motors and reset their encoders
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

    /*We will use a pattern for the methods, where we will create a method to call in the configuration proposed by the
     *programmer (in the order: resetMotorAndEncoder (), setMotorsPower ()), and another similar method that will use the
     *encoder (in order: standardEntry (), waitEncoderCount , resetMotorAndEncoder ()).*/

    //standard() and standardOn() are responsible for the robot standard movements (front and back).

    public void standardOn(int power){
        resetMotorAndEncoder();
        setMotorsPower(power, power, power, power);
    }
    public void standard(int power, float encoderCount){
        standardOn(power);
        waitEncoderCount(encoderCount);
        resetMotorAndEncoder();
    }

    // spin() and spinOn() are responsible for robot spin.

    public void spinOn(int power){
        resetMotorAndEncoder();
        setMotorsPower(power, power, -power, -power);
    }
    public void spin(int power, float encoderCount){
        spinOn(power);
        waitEncoderCount(encoderCount);
        resetMotorAndEncoder();
    }

    // sidewalk() and sidewalkOn() are responsible for the robot sidewalks (moves on Y axis).

    public void sidewalkOn(int power){
        resetMotorAndEncoder();
        setMotorsPower(-power, power, power, -power);
    }
    public void sidewalk (int power, float encoderCount){
        sidewalkOn(power);
        waitEncoderCount(encoderCount);
        resetMotorAndEncoder();
    }

    /* As the next moves will have four directions, we will work with an extra parameter, a string that will define the
     *direction of movement (right or left)*/

    // spinSide() and spinSideOn() are responsible for robot spin with only one side pair of motors

    public void spinSideOn(int power, String side){
        resetMotorAndEncoder();
        if(side.equals("Right")){
            setMotorsPower(0, 0, power, power);
        } else {
            setMotorsPower(power, power, 0, 0);
        }
    }
    public void spinSide (int power, String side, float encoderCount){
        spinSideOn(power, side);
        waitEncoderCount(encoderCount);
        resetMotorAndEncoder();
    }

    // diagonalOn() and diagonal() are responsible for the robot XY movements

    public void diagonalOn(int power, String direction){
        resetMotorAndEncoder();
        if(direction.equals("Right")){
            setMotorsPower(0, power, power, 0);
        } else{
            setMotorsPower(power, 0, 0, power);
        }
    }
    public void diagonal(int power, String direction, float encoderCount){
        diagonalOn(power, direction);
        waitEncoderCount(encoderCount);
        resetMotorAndEncoder();
    }
}

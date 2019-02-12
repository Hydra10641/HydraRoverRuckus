package models;

import com.qualcomm.robotcore.hardware.DcMotor;

public class NormalWheels {

    /*Welcome, this is the team 10641(Hydra) code for the normals wheels locomotion system, first applied in the Rover Ruckus season.
    First of all, we declare 2 attributes of type Dc Motor, corresponding to the motors used on the robot locomotion.*/

    private DcMotor rightWheel = null, leftWheel = null;

    public void normalWheels(DcMotor leftWheel, DcMotor rightWheel) {
        this.rightWheel = rightWheel;
        this.leftWheel = this.leftWheel;
    }

    /*Here we create three methods that we will use repetitively in the methods for locomotion. The description of each
     *one is within the same.*/

    public void resetMotorAndEncoder() {
        // This method will stop all the locomotion motors and reset their encoders
        setMotorsPower(0, 0);
        leftWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void setMotorsPower(int leftPower, int rightPower) {
        // This method is responsible to energize the motors
        leftWheel.setPower(leftPower / 100.0);
        rightWheel.setPower(rightPower / 100.0);
    }

    public void waitEncoderCount(float encoderCount) {
        /* This method is responsible to wait the encoder be equals to value required.
         *The encoder parameter (Count encoder) can be positive or negative, so we use the absolute value of the
         *supplied parameter and the encoder of the motors to limit the amount of rotation we want. We use the empty while
         *structure to serve as a hold structure */
        while (Math.abs(leftWheel.getCurrentPosition()) <= Math.abs(encoderCount) ||
                Math.abs(rightWheel.getCurrentPosition()) <= Math.abs(encoderCount)) {
        }
    }

    public void standardOn(int power) {
        resetMotorAndEncoder();
        setMotorsPower(power, power);
    }

    public void standardCount(int power, float encoderCount) {
        standardOn(power);
        waitEncoderCount(encoderCount);
        resetMotorAndEncoder();
    }

    // spin() and spinOn() are responsible for robot spin.

    public void spinOn(int power) {
        resetMotorAndEncoder();
        setMotorsPower(power, -power);
    }

    public void spinCount(int power, float encoderCount) {
        spinOn(power);
        waitEncoderCount(encoderCount);
        resetMotorAndEncoder();
    }
}
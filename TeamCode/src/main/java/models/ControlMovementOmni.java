package models;

import com.qualcomm.robotcore.hardware.DcMotor;

public class ControlMovementOmni {

    private DcMotor leftFrontWheel, leftBackWheel, rightFrontWheel, rightBackWheel = null;

    public void resetMotorAndEncoder(){

        // This method will stop all the locomotion motors and reset their encoders

        leftFrontWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void moveX (int power, float encoderCount){

        // This method is responsible for the common linear movement of the robot (front and back)

        // First, we stop the motors and reset the encoders

        resetMotorAndEncoder();

        // After that, we set the force of the motor based on the force provided in the parameter

        leftFrontWheel.setPower(power/100.0);
        leftBackWheel.setPower(power/100.0);
        rightFrontWheel.setPower(power/100.0);
        rightBackWheel.setPower(power/100.0);

        /*Since the rotary counter parameter (Count encoder) can be positive or negative, we use the absolute value of the supplied
         parameter and the encoder of the motors to limit the amount of rotation we want. We use the empty while structure to serve
          as a hold structure */

        while(Math.abs(leftFrontWheel.getCurrentPosition()) <= Math.abs(encoderCount)||
                Math.abs(leftBackWheel.getCurrentPosition()) <= Math.abs(encoderCount)||
                Math.abs(rightFrontWheel.getCurrentPosition()) <= Math.abs(encoderCount)||
                Math.abs(rightBackWheel.getCurrentPosition()) <= Math.abs(encoderCount)){}
                resetMotorAndEncoder();
    }

}

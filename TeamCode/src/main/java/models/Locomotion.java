package models;

import android.graphics.Path;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Locomotion {

    private DcMotor leftFrontWheel = null, leftBackWheel = null, rightFrontWheel = null, rightBackWheel = null;

    public void locomotion (DcMotor leftFrontWheel, DcMotor leftBackWheel, DcMotor rightFrontWheel, DcMotor rightBackWheel){
        this.leftFrontWheel = leftFrontWheel;
        this.leftBackWheel = leftBackWheel;
        this.rightFrontWheel = rightFrontWheel;
        this.rightBackWheel = rightBackWheel;
    }

    //Running without a encoder
    //Running using a core hex motor and the gamepad stick

    public void setForwardBackward (Double power){
        leftFrontWheel.setPower(-power);
        leftBackWheel.setPower(-power);
        rightFrontWheel.setPower(-power);
        rightBackWheel.setPower(-power);
    }

    public void setTurn (Double rightPower, Double leftPower){
        leftFrontWheel.setPower(leftPower);
        leftBackWheel.setPower(leftPower);
        rightFrontWheel.setPower(-rightPower);
        rightBackWheel.setPower(-rightPower);
    }

    //Running using encoder

    public  void initMotorsAndEncoder (){
        leftFrontWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void resetMotorsAndEncoder (){
        leftFrontWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void runToPosition (int position){
        resetMotorsAndEncoder();
        initMotorsAndEncoder();
        leftFrontWheel.setTargetPosition(position);
        leftBackWheel.setTargetPosition(position);
        rightFrontWheel.setTargetPosition(position);
        rightBackWheel.setTargetPosition(position);
    }
}

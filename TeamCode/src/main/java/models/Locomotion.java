package models;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

public class Locomotion{

    private DcMotor leftFrontWheel = null, leftBackWheel = null, rightFrontWheel = null, rightBackWheel = null;

    public void locomotion (DcMotor leftFrontWheel, DcMotor leftBackWheel, DcMotor rightFrontWheel, DcMotor rightBackWheel){
        this.leftFrontWheel = leftFrontWheel;
        this.leftBackWheel = leftBackWheel;
        this.rightFrontWheel = rightFrontWheel;
        this.rightBackWheel = rightBackWheel;
    }

    //Running without a encoder
    //Running using a core hex motor and the gamepad stick

    public void setForwardBackward (double power){

        power = Range.clip(power,-1.0,1.0);

        leftFrontWheel.setPower(power);
        leftBackWheel.setPower(power);
        rightFrontWheel.setPower(power);
        rightBackWheel.setPower(power);
    }

    public void setTurnAtSameAxis (double power){

        power = Range.clip(power,-1.0,1.0);

        leftFrontWheel.setPower(power);
        leftBackWheel.setPower(power);
        rightFrontWheel.setPower(-power);
        rightBackWheel.setPower(-power);
    }

    public void setTurn (double gampadX, double gamepadY){

        double leftPower   = Range.clip(gamepadY + gampadX, -1.0, 1.0) ;
        double rightPower  = Range.clip(gamepadY - gampadX, -1.0, 1.0) ;

        leftFrontWheel.setPower(leftPower);
        leftBackWheel.setPower(leftPower);
        rightFrontWheel.setPower(rightPower);
        rightBackWheel.setPower(rightPower);
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

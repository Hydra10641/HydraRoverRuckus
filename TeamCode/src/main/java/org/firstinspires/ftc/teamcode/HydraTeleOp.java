package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

public class HydraTeleOp extends LinearOpMode {

    protected Robot tesseract;

    protected int encoderCollectSlide;
    protected int encoderDepositSlide;
    protected float collectExpansion = 0;
    protected float depositExpansion = 0;

    protected double turn;
    protected double drive;
    protected double speed = 0;

    @Override
    public void runOpMode() throws InterruptedException {}

    protected void initMotors() {
        tesseract.wheels.leftWheel.setDirection(DcMotorSimple.Direction.REVERSE);
        tesseract.wheels.rightWheel.setDirection(DcMotorSimple.Direction.FORWARD);

        tesseract.arms.motorCollectSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        tesseract.arms.motorCollectSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        tesseract.arms.motorDepositSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        tesseract.arms.motorDepositSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    protected void defineLimitServos() {
        tesseract.arms.moveOnBy(Range.clip(-gamepad2.left_stick_y, 0.7f, 1.6f), "deposit_wrist");
        tesseract.arms.moveOnBy(Range.clip(-gamepad2.right_stick_y, 0, 0.3f), "collect_wrist");
    }

    protected void refreshEncoderPosition() {
        encoderCollectSlide = tesseract.arms.motorCollectSlide.getCurrentPosition();
        encoderDepositSlide = tesseract.arms.motorDepositSlide.getCurrentPosition();
    }

    protected void moveRobot() {
        if(gamepad1.left_stick_x != 0.0 || gamepad1.left_stick_y != 0.0){
            turn = gamepad1.left_stick_x;
            drive = -gamepad1.left_stick_y;
            tesseract.wheels.setMotorsPower(Range.clip(drive + turn,-0.75,0.75),
                    Range.clip(drive - turn,-0.75,0.75));
        } else if (gamepad1.dpad_up || gamepad1.dpad_down || gamepad1.dpad_left || gamepad1.dpad_right){
            moveByDpad();
        } else if (gamepad1.left_bumper || gamepad1.right_bumper ||
                gamepad1.left_trigger > 0.3f || gamepad1.right_trigger > 0.3f){
            moveByTrigger();
        } else {
            tesseract.wheels.setMotorsPower(0, 0);
        }
    }

    protected void setWheelsSpeed() {

        /*This method controls the variable "speed", used in locomotion by d-pad, bumpers and triggers. The method has 4 standard velocities,
         *being: off (0%), low (25%), medium (50%) and high (100%)*/

        if (gamepad1.a == true){
            speed = 0;
        }
        if (gamepad1.x == true){
            speed = 0.5;
        }
        if (gamepad1.y == true){
            speed = 0.5f;
        }
        if (gamepad1.b == true){
            speed = 1;
        }
    }

    protected void moveByDpad() {

        //This method controls the motors based on the state of the d-pad's buttons, returning the "standard" and "spin"

        if (gamepad1.dpad_up == true){
            tesseract.wheels.setMotorsPower(speed, speed);
            telemetry.addData("EncoderLeftWheel:",tesseract.wheels.leftWheel.getCurrentPosition());
            telemetry.addLine();
            telemetry.addData("EncoderRightWheel:",tesseract.wheels.rightWheel.getCurrentPosition());
            telemetry.update();
        }
        if (gamepad1.dpad_down == true){
            tesseract.wheels.setMotorsPower(- speed, - speed);
        }
        if (gamepad1.dpad_left == true){
            tesseract.wheels.setMotorsPower(- speed, speed);
        }
        if (gamepad1.dpad_right == true){
            tesseract.wheels.setMotorsPower(speed, - speed);
        }
    }

    protected void moveByTrigger() {

        /*This method controls the engines based on the state of the bumpers and triggers, returning the methods
        "spinSideLeft" and "spinSideRight"*/

        if (gamepad1.left_bumper == true){
            tesseract.wheels.leftWheel.setPower(speed);
        }
        else if (gamepad1.left_trigger >= 0.3f){
            tesseract.wheels.leftWheel.setPower(-speed);
        } else {
            tesseract.wheels.leftWheel.setPower(0);
        }
        if (gamepad1.right_bumper == true){
            tesseract.wheels.rightWheel.setPower(speed);
        }
        else if (gamepad1.right_trigger >= 0.3f){
            tesseract.wheels.rightWheel.setPower(-speed);
        } else {
            tesseract.wheels.rightWheel.setPower(0);
        }
    }

    protected void crServoCollectControls(){

        //This method controls the direction of the continuous rotation servo of the collection system

        if(gamepad2.x == true){
            tesseract.arms.crServoCollect.setPower(-0.79);
        }
        if(gamepad2.b == true){
            tesseract.arms.crServoCollect.setPower(0.79);
        }
    }
}

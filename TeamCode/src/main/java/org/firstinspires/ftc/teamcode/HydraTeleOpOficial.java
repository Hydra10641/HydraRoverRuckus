package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;

@TeleOp  (name = "HydraTeleOpOficial")

public class HydraTeleOpOficial extends LinearOpMode {

    Robot tesseract;

    int MAX_RANGE_COLLECT = 25000;
    int MAX_RANGE_DEPOSIT = 20000;

    int encoderCollectSlide;
    int encoderDepositSlide;
    float collectExpansion = 0;
    float depositExpansion = 0;

    double turn;
    double drive;
    double speed = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        /*Here we declare the devices of our robot (servos, motors and sensors)
         *Or the locomotion system with two traction engines (Wheels)
         */
        float wheelDiameter = 10.0f;
        float gearRatio = 1.0f;
        float distanceBetweenWheels = 35.0f;

        tesseract = new Robot(hardwareMap.get(DcMotor.class, "leftWheel"),
                                hardwareMap.get(DcMotor.class, "rightWheel"),
                                hardwareMap.get(CRServo.class, "crServoCollect"),
                                hardwareMap.get(Servo.class, "servoCollectWristLeft"),
                                hardwareMap.get(Servo.class, "servoCollectWristRight"),
                                hardwareMap.get(Servo.class, "servoDepositWrist"),
                                hardwareMap.get(DcMotor.class, "motorCollectSlide"),
                                hardwareMap.get(DcMotor.class, "motorDepositSlide"),
                                hardwareMap.get(LynxI2cColorRangeSensor.class, "distanceSensor"),
                                wheelDiameter, gearRatio, distanceBetweenWheels);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        //runtime.reset();

        tesseract.wheels.leftWheel.setDirection(DcMotorSimple.Direction.REVERSE);
        tesseract.wheels.rightWheel.setDirection(DcMotorSimple.Direction.FORWARD);

        tesseract.arms.motorCollectSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        tesseract.arms.motorCollectSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        tesseract.arms.motorDepositSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        tesseract.arms.motorDepositSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()){

            //Locomotion movement system

            encoderCollectSlide = tesseract.arms.motorCollectSlide.getCurrentPosition();
            encoderDepositSlide = tesseract.arms.motorDepositSlide.getCurrentPosition();

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

            //Arms movement system
            collectArmControls();
            depositArmsControls();
            crServoCollectControls();

            tesseract.arms.moveOnBy(Range.clip(-gamepad2.left_stick_y, 0, 0.75f), "deposit_wrist");
            tesseract.arms.moveOnBy(Range.clip(-gamepad2.right_stick_y, 0, 0.75f), "collect_wrist");

            setWheelsSpeed();
        }
        stop();
    }

   private void setWheelsSpeed() {

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

    private void moveByDpad() {

        //This method controls the motors based on the state of the d-pad's buttons, returning the "standard" and "spin"

        if (gamepad1.dpad_up == true){
            tesseract.wheels.setMotorsPower(speed - 0.1, speed);
            telemetry.addData("EncoderLeftWheel:",tesseract.wheels.leftWheel.getCurrentPosition());
            telemetry.addLine();
            telemetry.addData("EncoderRightWheel:",tesseract.wheels.rightWheel.getCurrentPosition());
            telemetry.update();
        }
        if (gamepad1.dpad_down == true){
            tesseract.wheels.setMotorsPower(- speed + 0.1, - speed);
        }
        if (gamepad1.dpad_left == true){
            tesseract.wheels.setMotorsPower(- speed, speed);
        }
        if (gamepad1.dpad_right == true){
            tesseract.wheels.setMotorsPower(speed, - speed);
        }
    }

    private void moveByTrigger() {

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

    private void collectArmControls() {

        // This method controls the expansion and retraction system of the collection arm

        if (encoderCollectSlide <= 0 || gamepad2.left_bumper == true || gamepad2.dpad_up == true){
            if (encoderCollectSlide > MAX_RANGE_COLLECT){
                collectExpansion = -1.0f;
            } else {
                collectExpansion = 1.0f;
            }
        }
        else if (encoderCollectSlide >= MAX_RANGE_COLLECT || gamepad2.left_trigger >= 0.3f || gamepad2.dpad_down == true){
            collectExpansion = -1.0f;
        }
        else {
            collectExpansion = 0;
        }
        tesseract.arms.moveOnBy(collectExpansion, "collect_slide");
        telemetry.addData("EncoderCollect:", encoderCollectSlide);
        telemetry.update();
    }

    private void depositArmsControls() {

        // This method controls the expansion and retraction system of the deposit arm

        if (encoderDepositSlide < 0 || gamepad2.right_bumper == true || gamepad2.y == true){
            if (encoderDepositSlide > MAX_RANGE_DEPOSIT){
                depositExpansion = -1.0f;
            } else {
                depositExpansion = 1.0f;
            }
        }
        else if (encoderDepositSlide > MAX_RANGE_DEPOSIT || gamepad2.right_trigger >= 0.3f || gamepad2.a == true){
            depositExpansion = -1.0f;
        }
        else {
            depositExpansion = 0;
        }
        tesseract.arms.moveOnBy(depositExpansion, "deposit_slide");
        telemetry.addData("EncoderDeposit:", encoderDepositSlide);
        telemetry.addLine();
        telemetry.addData("EncoderDeposit:",encoderDepositSlide);
        telemetry.update();
    }

    private void crServoCollectControls(){

        //This method controls the direction of the continuous rotation servo of the collection system

        if(gamepad2.x == true){
            tesseract.arms.crServoCollect.setPower(-0.79);
        }
        if(gamepad2.b == true){
            tesseract.arms.crServoCollect.setPower(0.79);
        }
    }
}

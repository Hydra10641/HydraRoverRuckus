package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;

@TeleOp  (name = "HydraTeleOp")

public class HydraTeleOp extends LinearOpMode {
    Robot tesseract;

    float speed = 0;
    float collectExpansion = 0;
    float depositExpansion = 0;
    float increment = 0.1f;

    double turn;
    double drive;

    @Override
    public void runOpMode() throws InterruptedException {
        /*Here we declare the devices of our robot (servos, motors and sensors)
         *You can choose between the Omni locomotion system (Omni)
         *Or the locomotion system with two traction engines (Wheels)
         */
        float wheelDiameter = 10.0f;
        float gearRatio = 1.0f;
        float distanceBetweenWheels = 35.0f;

        tesseract = new Robot(hardwareMap.get(DcMotor.class, "leftWheel"),
                                hardwareMap.get(DcMotor.class, "rightWheel"),
                                hardwareMap.get(CRServo.class, "crServoCollect"),
                                hardwareMap.get(Servo.class, "servoCollectWrist"),
                                hardwareMap.get(Servo.class, "servoDepositWrist"),
                                hardwareMap.get(DcMotor.class, "motorCollectSlide"),
                                hardwareMap.get(DcMotor.class, "motorDepositSlide"),
                                hardwareMap.get(DistanceSensor.class, "distanceSensor"),
                                wheelDiameter, gearRatio, distanceBetweenWheels);

        // wheels.leftWheel.setPower((double) encoderConverter.centimeter(20.f));

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        //runtime.reset();

        tesseract.wheels.leftWheel.setDirection(DcMotorSimple.Direction.REVERSE);
        tesseract.wheels.rightWheel.setDirection(DcMotorSimple.Direction.FORWARD);

        tesseract.arms.motorCollectSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        tesseract.arms.motorDepositSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()){
            // Turn on the collect servo motor

            tesseract.arms.crServoCollect.setPower(1);

            //Locomotion movement system

            setWheelsSpeed();
            speed = Range.clip(speed, 0, 1);
            if(gamepad1.left_stick_x != 0.0 || gamepad1.left_stick_y != 0.0){
               turn = gamepad1.left_stick_x;
               drive = -gamepad1.left_stick_y;
               tesseract.wheels.setMotorsPower(drive + turn, drive - turn);
            } else if (gamepad1.dpad_up || gamepad1.dpad_down || gamepad1.dpad_left || gamepad1.dpad_right){
               moveByDpad();
               tesseract.wheels.setMotorsPower(drive + turn, drive - turn);
            } else if (gamepad1.left_bumper || gamepad1.right_bumper ||
                       gamepad1.left_trigger > 0.3f || gamepad1.right_trigger > 0.3f){
               moveByTrigger();
            } else {
                tesseract.wheels.setMotorsPower(0, 0);
            }
            telemetry.addData("Stick position", -gamepad1.left_stick_y);
            telemetry.update();

            //Arms moviment system
            collectArmControls();
            depositArmsControls();

            tesseract.arms.moveOnBy(Range.clip(-gamepad2.left_stick_y, -1, 1), "collect_wrist");
            tesseract.arms.moveOnBy(Range.clip(-gamepad2.right_stick_y, 0, 1), "deposit_wrist");
        }
    }

   private void setWheelsSpeed() {
        if (gamepad1.y == true){
            speed += increment;
        }
        if (gamepad1.a == true){
            speed -= increment;
        }
    }

    private void moveByDpad() {
        if (gamepad1.dpad_up == true){
            drive = speed;
            turn = 0;
        }
        if (gamepad1.dpad_down == true){
            drive = -speed;
            turn = 0;
        }
        if (gamepad1.dpad_left == true){
            turn = -speed;
            drive = 0;
        }
        if (gamepad1.dpad_right == true){
            turn = speed;
            drive = 0;
        }
    }

    private void moveByTrigger() {
        if (gamepad1.left_bumper == true){
            tesseract.wheels.leftWheel.setPower(speed);
        }
        if (gamepad1.right_bumper == true){
            tesseract.wheels.rightWheel.setPower(speed);
        }
        if(gamepad1.left_trigger >= 0.3f){
            tesseract.wheels.leftWheel.setPower(-speed);
        }
        if(gamepad1.right_trigger >= 0.3f){
            tesseract.wheels.rightWheel.setPower(-speed);
        }
    }

    private void collectArmControls() {
        if (gamepad2.left_bumper == true || gamepad2.dpad_up == true){
            collectExpansion = 0.5f;
        }
        else if (gamepad2.left_trigger >= 0.3f || gamepad2.dpad_down == true){
            collectExpansion = -0.5f;
        }
        else {
            collectExpansion = 0;
        }
        tesseract.arms.moveOnBy(collectExpansion, "collect_slide");

    }

    private void depositArmsControls() {
        if (gamepad2.right_bumper == true || gamepad2.y == true){
            depositExpansion = 0.5f;
        }
        else if (gamepad2.right_trigger >= 0.3f || gamepad2.a == true){
            depositExpansion = -0.5f;
        }
        else {
            depositExpansion = 0;
        }
        tesseract.arms.moveOnBy(depositExpansion, "deposit_slide");
    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;

@TeleOp

public class HydraTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        /*Here we declare the devices of our robot (servos, motors and sensors)
         *You can choose between the Omni locomotion system (Omni)
         *Or the locomotion system with two traction engines (Wheels)
         */
        float wheelDiameter = 10.0f;
        float gearRatio = 1.0f;
        float distanceBetweenWheels = 35.0f;

        Robot tesseract = new Robot(hardwareMap.get(DcMotor.class, "leftWheel"),
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

        float speed = 0;
        float collectExpansion = 0;
        float depositExpansion = 0;
        float increment = 0.01f;
        tesseract.arms.crServoCollect.setPower(1); // Turn on the collect servo motor

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()){

            //WHEELS MOVEMENT CONTROL (STICK)
            float turn = gamepad1.left_stick_x;
            float drive = -gamepad1.left_stick_y;
            //WHEELS SPEED CONTROL (BUTTONS)
            if (gamepad1.y == true){
                speed += increment;
            }
            if (gamepad1.a == true){
                speed -= increment;
            }
            speed = Range.clip(speed, 0, 1);
            //WHEELS MOVEMENT CONTROL (DPAD)
            if (gamepad1.dpad_up == true){
                drive = speed;
            }
            if (gamepad1.dpad_down == true){
                drive = -speed;
            }
            if (gamepad1.dpad_left == true){
                turn = -speed;
            }
            if (gamepad1.dpad_right == true){
                turn = speed;
            }
            //WHEELS MOVEMENT IMPLEMENT (DPAD AND STICK)
            tesseract.wheels.setMotorsPower(drive + turn, drive - turn);

            //WHEELS MOVEMENT CONTROL AND IMPLEMENT(BUMPERS AND TRIGGERS)
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

            //COLLECT ARMS CONTROL AND IMPLEMENTS (BUMPERS, TRIGGERS AND DPAD)
            if (gamepad2.left_bumper == true || gamepad2.dpad_up == true){
                collectExpansion -= increment;
            }
            if (gamepad2.left_trigger >= 0.3f || gamepad2.dpad_down == true){
                collectExpansion += increment;
            }
            collectExpansion = Range.clip(collectExpansion, 0,1);
            tesseract.arms.moveOnBy(collectExpansion, "collect_slide");

            //DEPOSIT ARMS CONTROL AND IMPLEMENTS (BUMPERS, TRIGGERS AND DPAD)
            if (gamepad2.right_bumper == true || gamepad2.y == true){
                depositExpansion -= increment;
            }
            if (gamepad2.right_trigger >= 0.3f || gamepad2.a == true){
                depositExpansion += increment;
            }
            depositExpansion = Range.clip(depositExpansion, 0,1);
            tesseract.arms.moveOnBy(depositExpansion, "deposit_slide");

            //WRIST CONTROL (STICKS)
            tesseract.arms.moveOnBy(Range.clip(-gamepad2.left_stick_y, 0, 1), "collect_wrist");
            tesseract.arms.moveOnBy(Range.clip(-gamepad2.right_stick_y, 0, 1), "deposit_wrist");
        }
    }
}

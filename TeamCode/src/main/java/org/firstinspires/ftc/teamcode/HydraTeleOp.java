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

        Robot hydraBot = new Robot(hardwareMap.get(DcMotor.class, "leftWheel"),
                                hardwareMap.get(DcMotor.class, "rightWheel"),
                                hardwareMap.get(CRServo.class, "crServoCollect"),
                                hardwareMap.get(Servo.class, "servoCollectWrist"),
                                hardwareMap.get(Servo.class, "servoDepositWrist"),
                                hardwareMap.get(DcMotor.class, "motorExpansion"),
                                hardwareMap.get(DcMotor.class, "motorLander"),
                                hardwareMap.get(DistanceSensor.class, "distanceSensor"),
                                wheelDiameter, gearRatio, distanceBetweenWheels);

        // wheels.leftWheel.setPower((double) encoderConverter.centimeter(20.f));

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        float speed = 0;
        float expansion = 0;
        float increment = 0.01f;
        hydraBot.arms.crServoCollect.setPower(1); // Turn on the collect servo motor

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
            hydraBot.wheels.setMotorsPower(drive + turn, drive - turn);

            //WHEELS MOVEMENT CONTROL AND IMPLEMENT(BUMPERS AND TRIGGERS)
            if (gamepad1.left_bumper == true){
                hydraBot.wheels.leftWheel.setPower(speed);
            }
            if (gamepad1.right_bumper == true){
                hydraBot.wheels.rightWheel.setPower(speed);
            }
            if(gamepad1.left_trigger >= 0.3f){
                hydraBot.wheels.leftWheel.setPower(-speed);
            }
            if(gamepad1.right_trigger >= 0.3f){
                hydraBot.wheels.rightWheel.setPower(-speed);
            }

            //ARMS CONTROL AND IMPLEMENTS (BUMPERS, TRIGGERS AND DPAD)
            if (gamepad2.left_bumper == true || gamepad2.left_trigger >= 0.3f || gamepad2.dpad_up == true){
                expansion -= increment;
            }
            if (gamepad2.right_bumper == true || gamepad2.right_trigger >= 0.3f || gamepad2.dpad_down == true){
                expansion += increment;
            }
            expansion = Range.clip(expansion, 0,1);
            hydraBot.arms.moveOnBy(expansion, "expand");
            //WRIST CONTROL (STICKS)
            hydraBot.arms.moveOnBy(Range.clip(-gamepad2.left_stick_y, 0, 1), "collect_wrist");
            hydraBot.arms.moveOnBy(Range.clip(-gamepad2.right_stick_y, 0, 1), "deposit_wrist");
            //LATCH/DOCK (BUTTON)
            if (gamepad2.b == true){
                hydraBot.arms.motorLander.setPower(1);
            }
        }
    }
}

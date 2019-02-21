package org.firstinspires.ftc.teamcode;

import android.util.Range;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

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
                                hardwareMap.get(Servo.class, "servoCollect"),
                                hardwareMap.get(Servo.class, "servoCollectWrist"),
                                hardwareMap.get(Servo.class, "servoDepositWrist"),
                                hardwareMap.get(DcMotor.class, "motorExpansion"),
                                hardwareMap.get(DcMotor.class, "motorLander"),
                                hardwareMap.get(DistanceSensor.class, "distanceSensor"),
                                wheelDiameter, gearRatio, distanceBetweenWheels);

        // wheels.leftWheel.setPower((double) encoderConverter.centimeter(20.f));

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        float expansion = 0;
        float increment = 0.01f;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()){
            float turn = gamepad1.left_stick_x;
            float drive = -gamepad1.left_stick_y;
            hydraBot.wheels.setMotorsPower(drive + turn, drive - turn);

            if (gamepad1.left_bumper == true){
                hydraBot.wheels.leftWheel.setPower(1);
            }
            if (gamepad1.right_bumper == true){
                hydraBot.wheels.rightWheel.setPower(1);
            }


            if (gamepad2.left_bumper == true){
                expansion -= increment;
            }
            if (gamepad2.right_bumper == true){
                expansion += increment;
            }
            expansion = com.qualcomm.robotcore.util.Range.clip(expansion, 0,1);
            hydraBot.arms.moveOnBy(expansion, "expand");

            hydraBot.arms.moveOnBy(-gamepad2.left_stick_y, "collect_wrist");
            hydraBot.arms.moveOnBy(-gamepad2.right_stick_y, "deposit_wrist");

        }
    }
}

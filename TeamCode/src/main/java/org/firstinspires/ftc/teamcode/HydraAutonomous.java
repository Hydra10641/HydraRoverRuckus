package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous

public class HydraAutonomous extends LinearOpMode {
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
                                hardwareMap.get(Servo.class, "servoDeposit"),
                                hardwareMap.get(Servo.class, "servoCollect"),
                                hardwareMap.get(Servo.class, "servoWrist"),
                                hardwareMap.get(DcMotor.class, "motorExpansion"),
                                hardwareMap.get(DcMotor.class, "motorLander"),
                                hardwareMap.get(DistanceSensor.class, "distanceSensor"),
                                wheelDiameter, gearRatio, distanceBetweenWheels);

        // wheels.leftWheel.setPower((double) encoderConverter.centimeter(20.f));

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

    }

}

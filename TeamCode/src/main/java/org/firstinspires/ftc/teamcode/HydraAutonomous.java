package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
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
                                hardwareMap.get(CRServo.class, "servoCollect"),
                                hardwareMap.get(Servo.class, "servoCollectWrist"),
                                hardwareMap.get(Servo.class, "servoDepositWrist"),
                                hardwareMap.get(DcMotor.class, "motorExpansion"),
                                hardwareMap.get(DcMotor.class, "motorLander"),
                                hardwareMap.get(DistanceSensor.class, "distanceSensor"),
                                wheelDiameter, gearRatio, distanceBetweenWheels);

        // wheels.leftWheel.setPower((double) encoderConverter.centimeter(20.f));

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        hydraBot.arms.motorLander.setTargetPosition(1152);
        hydraBot.arms.motorLander.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hydraBot.wheels.walkCount(0.5f, 90, "spin");
        hydraBot.wheels.walkCount(-0.75f, 10.0f, "standard");
        hydraBot.wheels.walkCount(-0.5f, 90, "spin");
        hydraBot.wheels.walkCount(0.75f, 15.0f, "standard");
        // Inserir leitura do mineral, e depois disso inserir método para girar, coletar o ouro e girar de volta
        hydraBot.wheels.walkCount(-0.5f, 90, "spin");
        hydraBot.wheels.walkCount(1f, 30.0f, "standard");
        hydraBot.wheels.walkCount(0.5f, 45, "spin");
        hydraBot.wheels.walkCount(1f, 20.0f, "standard");
        int direcao;
        // Inserir leitura da Imagem
        /*if(){
            direcao = -1;
        } else {
            direcao = 1;
        }
        hydraBot.wheels.walkCount(0.5f, 90*direcao, "spin");*/
        hydraBot.wheels.walkCount(1f, 40.0f, "standard");
        hydraBot.arms.servoCollect.setPower(-1);

    }

}

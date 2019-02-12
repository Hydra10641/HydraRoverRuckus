package models;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;


public class OpModeClass extends LinearOpMode{

    @Override
    public void runOpMode() throws InterruptedException {
        /*Here we declare the devices of our robot (servos, motors and sensors)*/
        Arms servos = new Arms();
        servos.arms(hardwareMap.get(Servo.class, "servoDepositArmHand"),
                     hardwareMap.get(Servo.class, "servoDepositArmExpansion"),
                     hardwareMap.get(Servo.class, "servoColectArmHand"),
                     hardwareMap.get(Servo.class, "servoColectArmExpansion"));

        Omni omniWheels = new Omni();
        omniWheels.omni(hardwareMap.get(DcMotor.class, "leftFrontWheel"),
                        hardwareMap.get(DcMotor.class, "leftBackWheel"),
                        hardwareMap.get(DcMotor.class, "rightFrontWheel"),
                        hardwareMap.get(DcMotor.class, "rightBackWheel"));

        NormalWheels normalWheels = new NormalWheels();
        normalWheels.normalWheels(hardwareMap.get(DcMotor.class, "leftWheel"),
                                  hardwareMap.get(DcMotor.class, "rightWheel"));
    }
}

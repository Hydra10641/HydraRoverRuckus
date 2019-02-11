package models;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
    import com.qualcomm.robotcore.util.Range;


public class OpModeClass extends LinearOpMode{

    @Override
    public void runOpMode() throws InterruptedException {
        Arms blalala = new Arms();
        blalala.arms(hardwareMap.get(Servo.class, "servoDepositArmHand"),
                     hardwareMap.get(Servo.class, "servoDepositArmExpansion"),
                     hardwareMap.get(Servo.class, "servoColectArmHand"),
                     hardwareMap.get(Servo.class, "servoColectArmExpansion"));


    }
}

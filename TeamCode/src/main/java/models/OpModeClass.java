package models;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;

public class OpModeClass extends LinearOpMode{

    @Override
    public void runOpMode() throws InterruptedException {
        /*Here we declare the devices of our robot (servos, motors and sensors)
         *You can choose between the Omni locomotion system (Omni)
         *Or the locomotion system with two traction engines (Wheels)
         */
        Robot robot = initializeRobot();
        // wheels.leftWheel.setPower((double) encoderConverter.centimeter(20.f));
    }

    public Robot initializeRobot() {

        Arms servos = new Arms(hardwareMap.get(Servo.class, "servoDeposit"),
                               hardwareMap.get(Servo.class, "servoCollect"),
                               hardwareMap.get(Servo.class, "servoWrist"),
                               hardwareMap.get(DcMotor.class, "motorExpansion"),
                               hardwareMap.get(DcMotor.class, "motorLander"));

        Wheels wheels = new Wheels(hardwareMap.get(DcMotor.class, "leftWheel"),
                                   hardwareMap.get(DcMotor.class, "rightWheel"));

        EncoderConverter encoderConverter = new EncoderConverter(10.2f, 1.2f, 30.2f);


        return new Robot(wheels, servos, null, encoderConverter);
    }
}

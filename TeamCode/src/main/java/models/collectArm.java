package models;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public abstract class collectArm extends LinearOpMode {

    static final double maxPosition = 1.0;
    static final double minPosition = 0.0;
    static final double increment = 0.01;

    public void setPosition (Servo servoName, String deviceName, double angle){
        servoName = hardwareMap.get(Servo.class, deviceName);
        double position = Range.clip(angle,minPosition, maxPosition);
        servoName.setPosition(position);
    }

    public void setExpansion (Servo servoName, String deviceName, double angle){
        if(gamepad1.y == true){
            if(angle < maxPosition || angle == minPosition){
                angle += increment;
            }
            setPosition(servoName, deviceName, angle);
        }
        if(gamepad1.b == true){
            if(angle == maxPosition){
                angle -= increment;
            }
            setPosition(servoName, deviceName, angle);
        }
    }
}

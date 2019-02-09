package models;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Movimento {
    private DcMotor leftMotor, rightMotor;

    public void move (double leftpower, double rightpower){
        leftMotor.setPower(leftpower);
        rightMotor.setPower(rightpower);
    }
}

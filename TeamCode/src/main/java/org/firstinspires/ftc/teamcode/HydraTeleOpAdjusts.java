/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="HydraTeleOpAdjusts")
public class HydraTeleOpAdjusts extends LinearOpMode {

    // Declare OpMode members.
    Robot tesseract;

    float speed = 0;
    float collectExpansion = 0;
    float depositExpansion = 0;
    float increment = 0.01f;

    int Max_Position = 288;
    int Min_Position = 0;
    int depositPosition, collectPosition;

    double turn, drive;

    @Override
    public void runOpMode() {
        /*Here we declare the devices of our robot (servos, motors and sensors)
         *Or the locomotion system with two traction engines (Wheels)
         */
        float wheelDiameter = 10.0f;
        float gearRatio = 1.0f;
        float distanceBetweenWheels = 35.0f;

        tesseract = new Robot(hardwareMap.get(DcMotor.class, "leftWheel"),
                hardwareMap.get(DcMotor.class, "rightWheel"),
                hardwareMap.get(CRServo.class, "crServoCollect"),
                hardwareMap.get(Servo.class, "servoCollectWristRight"),
                hardwareMap.get(Servo.class, "servoCollectWristLeft"),
                hardwareMap.get(Servo.class, "servoDepositWrist"),
                hardwareMap.get(DcMotor.class, "motorCollectSlide"),
                hardwareMap.get(DcMotor.class, "motorDepositSlide"),
                hardwareMap.get(LynxI2cColorRangeSensor.class, "distanceSensor"),
                wheelDiameter, gearRatio, distanceBetweenWheels);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        //runtime.reset();

        tesseract.wheels.leftWheel.setDirection(DcMotorSimple.Direction.REVERSE);
        tesseract.wheels.rightWheel.setDirection(DcMotorSimple.Direction.FORWARD);

        tesseract.arms.motorCollectSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        tesseract.arms.motorDepositSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            //Locomotion movement system

            if(gamepad1.left_stick_y <= -0.04f || gamepad1.left_stick_y >= 0.04f ||
               gamepad1.left_stick_x <= -0.04f || gamepad1.left_stick_x >= 0.04f){
                turn = gamepad1.left_stick_x;
                drive = -gamepad1.left_stick_y;

                tesseract.wheels.setMotorsPower(drive + turn, drive - turn);
            }
            else if(gamepad1.dpad_up == true){
                setWheelsSpeed();
                speed = Range.clip(speed, 0, 1);
                tesseract.wheels.setMotorsPower(speed,speed);
            }
            else if(gamepad1.dpad_down == true){
                setWheelsSpeed();
                speed = Range.clip(speed, 0, 1);
                tesseract.wheels.setMotorsPower(-speed,-speed);
            }
            else if(gamepad1.dpad_right == true){
                setWheelsSpeed();
                speed = Range.clip(speed, 0, 1);
                tesseract.wheels.setMotorsPower(speed,-speed);
            }
            else if(gamepad1.dpad_left == true){
                setWheelsSpeed();
                speed = Range.clip(speed, 0, 1);
                tesseract.wheels.setMotorsPower(-speed,speed);
            }
            if(gamepad1.left_bumper == true){
                setWheelsSpeed();
                speed = Range.clip(speed, 0, 1);
                tesseract.wheels.leftWheel.setPower(speed);
            }
            if(gamepad1.right_bumper == true){
                setWheelsSpeed();
                speed = Range.clip(speed, 0, 1);
                tesseract.wheels.rightWheel.setPower(speed);
            }
            if(gamepad1.left_trigger >= 0.04f){
                setWheelsSpeed();
                speed = Range.clip(speed, 0, 1);
                tesseract.wheels.leftWheel.setPower(-speed);
            }
            if(gamepad1.right_trigger >= 0.04f){
                setWheelsSpeed();
                speed = Range.clip(speed, 0, 1);
                tesseract.wheels.rightWheel.setPower(-speed);
            }
            else if(gamepad1.left_stick_y >= -0.04f || gamepad1.left_stick_y <= 0.04f ||
                      gamepad1.left_stick_x >= -0.04f || gamepad1.left_stick_x <= 0.04f ||
                      gamepad1.dpad_up == false || gamepad1.dpad_down == false ||
                      gamepad1.dpad_right == false || gamepad1.dpad_left == false){
                tesseract.wheels.setMotorsPower(0,0);
            }

            //Arms moviment system
            collectArmsControls();
            depositArmsControls();
            crServoColectControls();

            tesseract.arms.moveOnBy(Range.clip(-gamepad2.left_stick_y, 0, 1), "collect_wrist");
            telemetry.addData("Servo positon: ", tesseract.arms.servoCollectWristLeft.getPosition());
            telemetry.update();
            tesseract.arms.moveOnBy(Range.clip(-gamepad2.right_stick_y, 0, 1), "deposit_wrist");

        }
        stop();
    }

    private void setWheelsSpeed() {
        if (gamepad1.a == true){
            speed = 0f;
        }
        if (gamepad1.x == true){
            speed = 0.25f;
        }
        if (gamepad1.y == true){
            speed = 0.5f;
        }
        if (gamepad1.b == true){
            speed = 1f;
        }
    }

    public void collectArmsControls(){
        if(gamepad2.right_bumper == true || gamepad2.y){
            if(collectPosition <= Max_Position){
                collectExpansion = -0.5f;
            }

        }
        else if(gamepad2.right_trigger > 0.3f || gamepad2.a){
            if(collectPosition >= Min_Position){
                collectExpansion = 0.5f;
            }

        }
        else if(gamepad2.right_bumper == false || gamepad2.right_trigger <= 0.3f){
            collectExpansion = 0f;
        }
        tesseract.arms.moveOnBy(collectExpansion, "collect_slide");
        telemetry.addData("CollectExpansion", collectExpansion);
        telemetry.update();
    }

    public void depositArmsControls(){
        if(gamepad2.left_bumper == true || gamepad2.dpad_up){
            if(depositPosition <= Max_Position){
            depositExpansion = 0.5f;
            }
        }
        else if(gamepad2.left_trigger > 0.3f || gamepad2.dpad_down){
            if(depositPosition <= Max_Position) {
                depositExpansion = -0.5f;
            }
        }
        else if(gamepad2.left_bumper == false || gamepad2.left_trigger <= 0.3f){
            depositExpansion = 0f;
        }
        tesseract.arms.moveOnBy(depositExpansion, "deposit_slide");
        telemetry.addData("DepositExpansion", depositExpansion);
        telemetry.update();
    }

    public void crServoColectControls(){
        if (gamepad2.x == true) {
            tesseract.arms.crServoCollect.setPower(0.79);
        }
        if (gamepad2.b == true) {
            tesseract.arms.crServoCollect.setPower(-0.79);
        }
    }

}

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
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="HydraTeleOpAdjusts")
public class HydraTeleOpAdjusts extends HydraTeleOp {

    @Override
    public void runOpMode() throws InterruptedException {
        /*Here we declare the devices of our robot (servos, motors and sensors)
         *Or the locomotion system with two traction engines (Wheels)
         */
        float wheelDiameter = 10.0f;
        float gearRatio = 1.0f;
        float distanceBetweenWheels = 35.0f;

        tesseract = new Robot(hardwareMap.get(DcMotor.class, "leftWheel"),
                hardwareMap.get(DcMotor.class, "rightWheel"),
                hardwareMap.get(CRServo.class, "crServoCollect"),
                hardwareMap.get(Servo.class, "servoCollectWrist"),
                hardwareMap.get(Servo.class, "servoDepositWrist"),
                hardwareMap.get(DcMotor.class, "motorCollectSlide"),
                hardwareMap.get(DcMotor.class, "motorDepositSlide"),
                hardwareMap.get(LynxI2cColorRangeSensor.class, "distanceSensor"),
                wheelDiameter, gearRatio, distanceBetweenWheels);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        //runtime.reset();
        initMotors();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()){

            //Locomotion movement system
            refreshEncoderPosition();
            moveRobot();

            //Arms movement system
            collectArmControls();
            depositArmsControls();
            crServoCollectControls();

            defineLimitServos();

            setWheelsSpeed();
        }
        stop();
    }

    private void collectArmControls() {

        // This method controls the expansion and retraction system of the collection arm

        if (gamepad2.left_bumper == true || gamepad2.dpad_up == true){
            collectExpansion = 1.0f;

        }
        else if (gamepad2.left_trigger >= 0.3f || gamepad2.dpad_down == true){
            collectExpansion = -1.0f;
        }
        else {
            collectExpansion = 0;
        }
        tesseract.arms.moveOnBy(collectExpansion, "collect_slide");
        telemetry.addData("EncoderCollect:", encoderCollectSlide);
        telemetry.update();
    }

    private void depositArmsControls() {

        // This method controls the expansion and retraction system of the deposit arm

        if (gamepad2.right_bumper == true || gamepad2.y == true){
            depositExpansion = -1.0f;
        }
        else if (gamepad2.right_trigger >= 0.3f || gamepad2.a == true){
            depositExpansion = 1.0f;
        }
        else {
            depositExpansion = 0;
        }
        tesseract.arms.moveOnBy(depositExpansion, "deposit_slide");
        telemetry.addData("EncoderDeposit:", encoderDepositSlide);
        telemetry.addLine();
        telemetry.addData("EncoderDeposit:",encoderDepositSlide);
        telemetry.update();
    }
}
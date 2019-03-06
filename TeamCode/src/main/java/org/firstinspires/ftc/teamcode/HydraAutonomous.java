package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.AR.ObjectReco;
import org.firstinspires.ftc.teamcode.AR.VuforiaImageTarget;

@Autonomous  (name = "HydraAutonomous")

public class HydraAutonomous extends LinearOpMode {
    private Robot tesseract;
    private VuforiaImageTarget vuforia;
    private ObjectReco objectReco;

    private ObjectReco.Position positionMineral;
    private String imageTarget;

    private double recognitionTime = 3;

    @Override
    public void runOpMode() throws InterruptedException {
        /* Here we declare the devices of our robot (servos, motors and sensors)
         * and the Vuforia's localizer.
         * You can choose between the Omni locomotion system (Omni)
         * Or the locomotion system with two traction engines (Wheels)
         */
        float wheelDiameter = 10.0f;
        float gearRatio = 1.0f;
        float distanceBetweenWheels = 34.5f;

        tesseract = new Robot(hardwareMap.get(DcMotor.class, "leftWheel"),
                                hardwareMap.get(DcMotor.class, "rightWheel"),
                                hardwareMap.get(CRServo.class, "crServoCollect"),
                                hardwareMap.get(Servo.class, "servoCollectWrist"),
                                hardwareMap.get(Servo.class, "servoDepositWrist"),
                                hardwareMap.get(DcMotor.class, "motorCollectSlide"),
                                hardwareMap.get(DcMotor.class, "motorDepositSlide"),
                                hardwareMap.get(LynxI2cColorRangeSensor.class, "distanceSensor"),
                                wheelDiameter, gearRatio, distanceBetweenWheels);

        // wheels.leftWheel.setPower((double) encoderConverter.centimeter(20.f));

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        downLander();
        removeHookLander();

        initAr();

        captureMineral();
        areaRecognition();
        depositOfObjects();
        idle();
    }

    private void downLander() {
        tesseract.arms.motorDepositSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        tesseract.arms.motorDepositSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        tesseract.arms.motorDepositSlide.setPower(0.5);
        tesseract.arms.motorDepositSlide.setTargetPosition(1152);
        while(tesseract.arms.motorDepositSlide.getCurrentPosition() < 1152){
            telemtryUpdate("Posição Motor", tesseract.arms.motorDepositSlide.getCurrentPosition());
        }
    }

    private void removeHookLander() {
        tesseract.wheels.walkCount(0.25, 90, "spin");
        tesseract.wheels.walkCount(-0.25, 10.0f, "standard");
        tesseract.wheels.walkCount(-0.25, 90, "spin");
        tesseract.wheels.walkCount(0.25, 15.0f, "standard");
    }

    private void initAr() {
        vuforia = new VuforiaImageTarget();
        objectReco = new ObjectReco(this, vuforia.getLocalizer());
    }

    private void captureMineral() {
        mineralRecognition();
        // Turn on the collect servo motor
        tesseract.arms.crServoCollect.setPower(0.79);
        tesseract.arms.motorCollectSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        tesseract.arms.motorCollectSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        tesseract.arms.moveOnBy(0.5f, "collect_slide");
        tesseract.arms.motorCollectSlide.setTargetPosition(3000);
        tesseract.arms.moveOnBy(-0.5f, "collect_slide");
        tesseract.arms.motorCollectSlide.setTargetPosition(0);
    }

    private void mineralRecognition() {
        objectReco.activate();
        objectReco.sample();

        String walkType = null;
        float encoderCount = 0;

        ElapsedTime recognitionTimer = new ElapsedTime();
        recognitionTimer.reset();
        do {
            positionMineral = objectReco.getPos();
            if (positionMineral != null) {
                telemtryUpdate("Mineral", positionMineral);
            }

            switch (positionMineral) {
                case UNKNOWN:
                    walkType = null;
                    break;
                case CENTER:
                    walkType = "standard";
                    encoderCount = 0;
                    break;

                case LEFT:
                    walkType = "spinSideLeft";
                    encoderCount = 10;
                    break;

                case RIGHT:
                    walkType = "spinSideRight";
                    encoderCount = 10;
                    break;
            }
        } while (isEndOfRecognition(walkType, recognitionTimer.time()));

        if (walkType != null) {
            tesseract.wheels.walkCount(0.75, encoderCount, walkType);
        }
    }

    private void areaRecognition() {
        vuforia.activate();

        String walkType = null;
        int encoderCount = 0;

        ElapsedTime recognitionTimer = new ElapsedTime();
        recognitionTimer.reset();
        do {
            if (!vuforia.isVisible()) {
                telemtryUpdate("Image target:", "Não há imagem a vista");
                vuforia.getVuMark();
            }
            else {
                imageTarget = vuforia.getVuMarkName();
                telemtryUpdate("Image target:", imageTarget);

                encoderCount = 90;
                switch (imageTarget) {
                    case "Blue-Rover":
                    case "Red-Footprint":
                        walkType = "spinSideLeft";
                        break;
                    case "Front-Craters":
                    case "Back-Space":
                        walkType = "spinSideRight";
                        break;
                }
            }
        } while (isEndOfRecognition(walkType, recognitionTimer.time()));
        telemtryUpdate("walkType", walkType);
        if (walkType != null) {
            tesseract.wheels.walkCount(0.75, encoderCount, walkType);
        }
    }

    private boolean isEndOfRecognition (String objectRA, double time) {
        return (objectRA == null && time < recognitionTime);
    }

    private void telemtryUpdate(String caption, Object data) {
        telemetry.addData(caption, data);
        telemetry.update();
    }

    private void depositOfObjects() {
        telemtryUpdate("Distancia", getDistanceInCm());
        while (getDistanceInCm() > 10){
            telemtryUpdate("Distancia", getDistanceInCm());
            tesseract.wheels.walkOnBy(0.75, "standard");
        }
        tesseract.wheels.setMotorsPower(0, 0);
        tesseract.arms.crServoCollect.setPower(-1);
        sleep(2000);
        tesseract.arms.crServoCollect.setPower(0);
    }

    private double getDistanceInCm() {
        return tesseract.distanceSensor.getDistance(DistanceUnit.CM);
    }

}

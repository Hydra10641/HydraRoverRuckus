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
    // Declaring attributes
    private Robot tesseract;
    private VuforiaImageTarget vuforia;
    private ObjectReco objectReco;

    private ObjectReco.Position positionMineral;
    private String imageTarget;

    private double recognitionTime = 2;
    private int imageSearchTurn = 135;

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
                                hardwareMap.get(Servo.class, "servoCollectWristLeft"),
                                hardwareMap.get(Servo.class, "servoCollectWristRight"),
                                hardwareMap.get(Servo.class, "servoDepositWrist"),
                                hardwareMap.get(DcMotor.class, "motorCollectSlide"),
                                hardwareMap.get(DcMotor.class, "motorDepositSlide"),
                                hardwareMap.get(LynxI2cColorRangeSensor.class, "distanceSensor"),
                                wheelDiameter, gearRatio, distanceBetweenWheels);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Landing
        downLander();
        removeHookLander();
        // Starting augmented reality and collecting gold
        initAr();
        captureMineral();
        // Identifying the position of the robot in the arena
        searchImage();
        areaRecognition();
        // Depositing the gold and the marker and ending the autonomous opmode
        depositOfObjects();
        idle();
    }

    private void downLander() {
        tesseract.arms.motorDepositSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        tesseract.arms.motorDepositSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        tesseract.arms.motorDepositSlide.setPower(0.5);
        tesseract.arms.motorDepositSlide.setTargetPosition(21000);
        while(tesseract.arms.motorDepositSlide.getCurrentPosition() < 21000){
            telemtryUpdate("Posição Motor", tesseract.arms.motorDepositSlide.getCurrentPosition());
        }
    }

    private void removeHookLander() {
        tesseract.wheels.walkCount(0.25, 180, "spin");
        tesseract.wheels.walkCount(-0.25, 10.0f, "standard");
        tesseract.wheels.walkCount(-0.25, 180, "spin");
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
        int encoderCount = 0;

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
                    walkType = "spin";
                    encoderCount = - 10;
                    break;

                case RIGHT:
                    walkType = "spin";
                    encoderCount = 10;
                    break;
            }
        } while (isEndOfRecognition(walkType, recognitionTimer.time()));
        imageSearchTurn = imageSearchTurn + encoderCount;
        if (walkType != null) {
            tesseract.wheels.walkCount(0.75, encoderCount, walkType);
        }
    }

    private void searchImage(){
        tesseract.wheels.walkCount(0.4, -imageSearchTurn, "spin");
        tesseract.wheels.walkCount(0.6, 30, "standard");
        tesseract.wheels.walkCount(0.4, 90, "spin");
        tesseract.wheels.walkOnBy(0.6, "standard");
        while(getDistanceInCm() > 20){
        }
        tesseract.wheels.resetMotorAndEncoder();
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

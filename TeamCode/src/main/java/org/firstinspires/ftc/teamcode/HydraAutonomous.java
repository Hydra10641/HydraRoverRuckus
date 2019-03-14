package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.AR.ObjectReco;
import org.firstinspires.ftc.teamcode.AR.VuforiaImageTarget;

import static java.lang.Double.NaN;

@Autonomous  (name = "HydraAutonomous")

public class HydraAutonomous extends LinearOpMode {
    // Declaring attributes
    private Robot tesseract;
    private VuforiaImageTarget vuforia;
    private ObjectReco objectReco;

    private ObjectReco.Position positionMineral;
    private String imageTarget;

    private double recognitionTime = 2;
    private int encoderCount = 0;


    @Override
    public void runOpMode() throws InterruptedException {
        /* Here we declare the devices of our robot (servos, motors and sensors)
         * and the Vuforia's localizer.
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

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        tesseract.wheels.leftWheel.setDirection(DcMotorSimple.Direction.REVERSE);
        tesseract.wheels.rightWheel.setDirection(DcMotorSimple.Direction.FORWARD);
        // Landing
//        downLa
// HookLander();
        // Starting augmented reality and collecting gold
        initAr();
//        captureMineral();
//        sleep(10000);
//        // Identifying the position of the robot in the arena
//        searchImage();
        areaRecognition();
//        // De
// positing the gold and the marker and ending the autonomous opmode
        depositOfObjects();

        idle();
    }

    private void downLander() {
        tesseract.arms.motorDepositSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        tesseract.arms.motorDepositSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        tesseract.arms.motorDepositSlide.setPower(0.5);
        tesseract.arms.motorDepositSlide.setTargetPosition(17000);
        while(tesseract.arms.motorDepositSlide.getCurrentPosition() < 17000){
            telemtryUpdate("Posição Motor", tesseract.arms.motorDepositSlide.getCurrentPosition());
        }
    }

    private void removeHookLander() {
        tesseract.wheels.walkCount(-0.25, -30, "spin");
        tesseract.arms.motorDepositSlide.setTargetPosition(0);
        while(tesseract.arms.motorDepositSlide.getCurrentPosition() > 15000){}
        tesseract.wheels.walkCount(0.25, 30, "spin");
    }

    private void initAr() {
        vuforia = new VuforiaImageTarget();
        objectReco = new ObjectReco(this, vuforia.getLocalizer());
    }

    private void captureMineral() {
        // mineralRecognition();
        // Turn on the collect servo motor
        tesseract.arms.crServoCollect.setPower(-0.79);
        tesseract.arms.motorCollectSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        tesseract.arms.motorCollectSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        tesseract.arms.moveOnBy(0.5f, "collect_slide");
        tesseract.arms.motorCollectSlide.setTargetPosition(9000);
        while(tesseract.arms.motorCollectSlide.getCurrentPosition() < 9000){}
        tesseract.arms.moveOnBy(-0.5f, "collect_slide");
        tesseract.arms.motorCollectSlide.setTargetPosition(0);
        while(tesseract.arms.motorCollectSlide.getCurrentPosition() > 0){}
        tesseract.wheels.walkCount(1, -encoderCount, "spin");
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
        if (walkType != null) {
            tesseract.wheels.walkCount(0.75, encoderCount, walkType);
        }
    }

    private void searchImage(){
        tesseract.wheels.walkCount(1, 15.0f, "standard");
        tesseract.wheels.walkCount(1, -90, "spin");
        tesseract.wheels.walkCount(1, 40.0f, "standard");
        tesseract.wheels.walkCount(1, 45, "spin");
    }

    private void areaRecognition() {
        vuforia.activate();

        String walkType = null;

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

                encoderCount = 180;
                switch (imageTarget) {
                    case "Blue-Rover":
                    case "Red-Footprint":
                        walkType = "spinSideRig+ht";
                        break;
                    case "Front-Craters":
                    case "Back-Space":
                        walkType = "spinSideLeft";
                        break;
                }
            }
        } while (isEndOfRecognition(walkType, recognitionTimer.time()));
        telemtryUpdate("image", imageTarget);
        if (walkType != null) {
            tesseract.wheels.walkCount(1, 5.0f, "standard");
            tesseract.wheels.walkCount(1, encoderCount, walkType);
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
//        telemtryUpdate("Distancia", Double.isNaN(getDistanceInCm()));
        while (getDistanceInCm() > 20 || Double.isNaN(getDistanceInCm())){
            telemtryUpdate("Distancia", getDistanceInCm());
            tesseract.wheels.walkOnBy(0.50, "standard");
        }
        tesseract.wheels.walkCount(0.75, 75, "standard");
        tesseract.wheels.setMotorsPower(0, 0);
        tesseract.arms.crServoCollect.setPower(-1);
        sleep(2000);
        tesseract.arms.crServoCollect.setPower(0);
    }

    private double getDistanceInCm() {
        return tesseract.distanceSensor.getDistance(DistanceUnit.CM);
    }

}

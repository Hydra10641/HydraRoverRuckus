package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.AR.ObjectReco;
import org.firstinspires.ftc.teamcode.AR.VuforiaImageTarget;

@Autonomous

public class HydraAutonomous extends LinearOpMode {
    private Robot tesseract;
    private VuforiaImageTarget vuforia;
    private ObjectReco objectReco;

    private ObjectReco.Position positionMineral;
    private String imageTarget;

    private Telemetry.Item positionMineralLog;
    private Telemetry.Item imageTargetLog;
    private Telemetry telemetry;

    private float recognitionTime = 10;

    @Override
    public void runOpMode() {
        /* Here we declare the devices of our robot (servos, motors and sensors)
         * and the Vuforia's localizer.
         * You can choose between the Omni locomotion system (Omni)
         * Or the locomotion system with two traction engines (Wheels)
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
                                hardwareMap.get(DistanceSensor.class, "distanceSensor"),
                                wheelDiameter, gearRatio, distanceBetweenWheels);

        // wheels.leftWheel.setPower((double) encoderConverter.centimeter(20.f));

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        dowLander();
        removeHookLander();

        initAr();

        pushMineral();
        areaRecognition();

    }

    private void dowLander() {
        tesseract.arms.motorDepositSlide.setTargetPosition(1152);
        tesseract.arms.motorDepositSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private void removeHookLander() {
        tesseract.wheels.walkCount(0.5f, 90, "spin");
        tesseract.wheels.walkCount(-0.75f, 10.0f, "standard");
        tesseract.wheels.walkCount(-0.5f, 90, "spin");
        tesseract.wheels.walkCount(0.75f, 15.0f, "standard");
    }

    private void initAr() {
        vuforia = new VuforiaImageTarget(this);
        objectReco = new ObjectReco(this, vuforia.getLocalizer());
    }

    private void pushMineral() {
        mineralRecognition();
        // Valor aleatório
        tesseract.arms.moveOnBy(5,"collect_slide");
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
            telemtryItemUpdate(positionMineralLog, positionMineral);

            switch (positionMineral) {
                case UNKNOWN:
                    walkType = null;
                    break;
                case CENTER:
                    walkType = "spin";
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

        tesseract.wheels.walkCount(0.75f, encoderCount, walkType);
    }

    private void areaRecognition() {
        vuforia.activate();

        String walkType = null;
        float encoderCount = 0;

        ElapsedTime recognitionTimer = new ElapsedTime();
        recognitionTimer.reset();
        do {
            if (!vuforia.isVisible()) {
                telemtryItemUpdate(imageTargetLog, "Não há imagem a vista");
                vuforia.getVuMark();
            }
            else {
                imageTarget = vuforia.getVuMarkName();
                telemtryItemUpdate(imageTargetLog, imageTarget);

                encoderCount = 20;
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
        } while (isEndOfRecognition(imageTarget, recognitionTimer.time()));
        tesseract.wheels.walkCount(0.75f, encoderCount, walkType);
    }

    private boolean isEndOfRecognition (String objectRA, double time) {
        return (objectRA.isEmpty() || time > recognitionTime);
    }

    private void telemtryItemUpdate(Telemetry.Item item, Object data) {
        item.setValue(data);
        telemetry.update();
    }

    private void evictionOfMark() {
        tesseract.wheels.walkCount(0.75f, 5, "spin");
    }

}

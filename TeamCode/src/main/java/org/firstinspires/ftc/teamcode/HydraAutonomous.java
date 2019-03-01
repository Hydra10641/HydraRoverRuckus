package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.vuforia.CameraCalibration;
import com.vuforia.Frame;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.AR.ObjectReco;
import org.firstinspires.ftc.teamcode.AR.VuforiaImageTarget;

import java.util.concurrent.BlockingQueue;

@Autonomous

public class HydraAutonomous extends LinearOpMode {
    Robot tesseract;
    VuforiaImageTarget vuforia;
    ObjectReco objectReco;

    private ObjectReco.Position positionMineral;
    private String imageTarget;

    Telemetry.Item positionMineralLog;
    Telemetry.Item imageTargetLog;
    Telemetry telemetry;

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

        // Inserir leitura do mineral, e depois disso inserir método para girar, coletar o ouro e girar de volta
        tesseract.wheels.walkCount(-0.5f, 90, "spin");
        tesseract.wheels.walkCount(1f, 30.0f, "standard");
        tesseract.wheels.walkCount(0.5f, 45, "spin");
        tesseract.wheels.walkCount(1f, 20.0f, "standard");
        int direcao;
        // Inserir leitura da Imagem
        /*if(){
            direcao = -1;
        } else {
            direcao = 1;
        }
        tesseract.wheels.walkCount(0.5f, 90*direcao, "spin");*/
        tesseract.wheels.walkCount(1f, 40.0f, "standard");
        tesseract.arms.crServoCollect.setPower(-1);
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
        objectReco.activate();
        objectReco.sample();

        String walkType = null;
        float encoderCount = 0;

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
        } while (walkType.isEmpty());
        tesseract.wheels.walkCount(0.75f, encoderCount, walkType);
    }

    private void areaRecognition() {
        vuforia.activate();

        do {
            if (!vuforia.isVisible()) {
                vuforia.getVuMark();
            }
            else {
                imageTarget = vuforia.getVuMarkName();
                telemtryItemUpdate(imageTargetLog, imageTarget);
            }
        } while (imageTarget.isEmpty());


        switch (imageTarget) {
            case "Blue-Rover":
                // Ação
                break;
            case "Red-Footprint":
                // Ação
                break;
        }
    }

    private void telemtryItemUpdate(Telemetry.Item item, Object data) {
        item.setValue(data);
        telemetry.update();
    }

}

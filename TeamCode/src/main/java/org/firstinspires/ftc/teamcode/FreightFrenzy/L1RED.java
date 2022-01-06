package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Carousel;

@Autonomous(name = "L1RED", group = "Linear Opmode")
public class L1RED extends LinearOpMode {
    Carousel carousel;

    @Override
    public void runOpMode() {
        carousel = new Carousel(this);
        DistanceSensor leftSensor = carousel.detectionSystem.leftSensor;
        DistanceSensor rightSensor = carousel.detectionSystem.rightSensor;

        waitForStart();

        while (opModeIsActive()) {
            carousel.L1();
            stop();
        }
    }
}

package org.firstinspires.ftc.teamcode.FreightFrenzy.RedAutonomi;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Carousel;

@Autonomous(name = "RedLZNW", group = "RedAutonomousL")
public class RedLZNW extends LinearOpMode {
    Carousel carousel;

    @Override
    public void runOpMode() {
        carousel = new Carousel(this);

        waitForStart();

        while (opModeIsActive()) {
            carousel.LZNW(1);
            stop();
        }
    }
}


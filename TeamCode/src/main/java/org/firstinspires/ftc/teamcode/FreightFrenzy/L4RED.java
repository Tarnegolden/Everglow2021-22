package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Carousel;

@Autonomous(name = "L4RED", group = "Linear Opmode")
public class L4RED extends LinearOpMode {
    Carousel carousel;

    @Override
    public void runOpMode() {
        carousel = new Carousel(this);

        waitForStart();

        while (opModeIsActive()) {
            carousel.L4();
            stop();
        }
    }
}



package org.firstinspires.ftc.teamcode.FreightFrenzy.RedAutonomi;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Paths.Crater;

@Autonomous(name = "RedRBYCO", group = "RedAutonomousR")
public class RedRBYCO extends LinearOpMode {
    Crater crater;

    @Override
    public void runOpMode() {
        crater = new Crater(this);

        waitForStart();

        while (opModeIsActive()) {
            crater.RBYCO(1);
            stop();
        }
    }
}


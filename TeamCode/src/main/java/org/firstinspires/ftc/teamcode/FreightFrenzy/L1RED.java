package org.firstinspires.ftc.teamcode.FreightFrenzy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths.Carrouselle;

@Autonomous(name = "L1RED", group = "Linear Opmode")
public class L1RED extends LinearOpMode {
    Carrouselle carrouselle;

    @Override
    public void runOpMode() {
        carrouselle = new Carrouselle(this);

        waitForStart();

        while (opModeIsActive()) {
            carrouselle.L1();
            stop();
        }
    }
}
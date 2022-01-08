package org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.Paths;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DetectionSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DrivingSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class Crater {
    DrivingSystem   drivingSystem;
    ArmSystem       armSystem;
    LinearOpMode    opMode;
    public DetectionSystem detectionSystem;
    private final DuckSystem duckSystem;


    public Crater(LinearOpMode opMode) {
        this.opMode   = opMode;
        drivingSystem = new DrivingSystem(opMode);
        armSystem     = new ArmSystem(opMode);
        detectionSystem = new DetectionSystem(opMode);
        duckSystem = new DuckSystem(opMode);
    }

    /**
     * Goes to alliance shipping hub and places the loaded freight there.
     */
    public void placeFreight() {
        ArmSystem.Floors floor = detectionSystem.findTargetFloor2();

        //avoid totem
        switch (floor) {
            case FIRST:
                drivingSystem.driveSideways(15, 0.4);
                break;
            case THIRD:
                drivingSystem.driveSideways(5, -0.4);
                break;
        }

        // drive to alliance shipping hub
        drivingSystem.driveStraight(95, 0.5);
        TimeUtils.sleep(200);
        switch (floor) {
            case FIRST:
                drivingSystem.driveSideways(15, -0.4);
                break;
            case THIRD:
                drivingSystem.driveSideways(5, 0.4);
                break;
        }
        drivingSystem.driveSideways(10,0.4);
        drivingSystem.turn(90, 200);
        // place freight on alliance shipping hub
        armSystem.autonomousMoveArm(floor);
        TimeUtils.sleep(500);
        drivingSystem.driveStraight(7, 0.5);
        TimeUtils.sleep(500);
        armSystem.spit();
        TimeUtils.sleep(500);
        armSystem.stop();
        drivingSystem.driveStraight(7, -0.5);
        TimeUtils.sleep(500);
        armSystem.autonomousReload();
    }

    /**
     * Goes to crater.
     */
    public void R1() {
        placeFreight();
        // go to crater and collect
        drivingSystem.driveStraight(180, 0.6);
        drivingSystem.turn(180, 200);
        drivingSystem.driveSideways(121.5, 0.4);
        armSystem.moveArm(-300);
        TimeUtils.sleep(700);
        drivingSystem.driveUntilObstacle(60, 0.4);
    }

    /**
     * Goes to carousel, then to crater.
     */
    public void R2() {
        placeFreight();
        // move to carousel
        drivingSystem.driveSideways(50, 0.6);
        drivingSystem.driveStraight(180, 0.6);
        TimeUtils.sleep(100);
        drivingSystem.driveSideways(120, -0.7);
        drivingSystem.driveSideways(45, -0.4);
        // drop duck
        duckSystem.runFor(4000);
        // move to crater
        drivingSystem.driveSideways(50, 0.6);
        drivingSystem.turn(180, 200);
        armSystem.moveArm(-300);
        TimeUtils.sleep(700);
        drivingSystem.driveUntilObstacle(60, 1);
        armSystem.autonomousReload();
    }

    /**
     * Goes to carousel, then to alliance storage unit.
     */
    public void R3() {
        placeFreight();
        // move to carousel
        drivingSystem.driveSideways(50, 0.4);
        drivingSystem.driveStraight(185, 0.4);
        drivingSystem.driveSideways(170, -0.4);
        // drop duck
        duckSystem.runFor(5000);
        // move to alliance storage unit
        drivingSystem.driveSideways(66, 0.4);
    }

    /**
     * Goes to alliance storage unit.
     */
    public void R4() {
        placeFreight();
        // move to storage unit
        drivingSystem.driveSideways(50, 0.4);
        drivingSystem.driveStraight(185, 0.4);
        drivingSystem.driveSideways(90, -0.4);
    }
}

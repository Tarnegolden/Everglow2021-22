package org.firstinspires.ftc.teamcode.FreightFrenzy.Paths;

import static org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.TotemSystem.driveStraightDistanceForFloor;
import static org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils.isMirrored;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.AllSystems;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.ArmSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.MathUtils;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.TimeUtils;

public class Routes {
    private final AllSystems systems;
    private final int mirror;
    private ArmSystem.Floors floor;


    private static final double INITIAL_DRIVE_STAIGHT_DISTANCE = 30;
    /**
     * At the start of the autonomous period, we need to take drive straight to pick up the totem.
     * This varries based on the floor we are going picking up and the floor we are going to.
     * @param floor The floor we need to go to in order to pick up the Totem. Should be switched if mirrored, call switchIfMirrored(mirror)
     * @param mirror 1 if mirrored, -1 if not
     * @return the distance, in centimeters, the robot should drive to pick up the totem.
     */
    public static double driveStraightDistanceForTotemPickup(ArmSystem.Floors floor, int mirror) {
        switch (floor) {
            case FIRST:
                return 19;
            case SECOND:
                return 19;
            case THIRD:

                return 19 + 6 * MathUtils.isMirrored(mirror);
            default:
                throw new IllegalArgumentException("Floor for driveStraightDistanceForFloor must be FIRST, SECOND, or THIRD.");
        }
    }


    public Routes(AllSystems systems, int mirror) {
        this.systems = systems;
        this.mirror = mirror;
    }

    public Routes(LinearOpMode opMode, int mirror){
        this(AllSystems.init(opMode), mirror);
    }

    /**
     * Picks up the totem and drives INITIAL_DRIVE_STAIGHT_DISTANCE centimeters forward.
     */
    private void pickupTotem(){
        floor = systems.cameraSystem.detectTotem();
        double driveStaightDistanceForTotem = driveStraightDistanceForTotemPickup(floor.switchIfMirrored(mirror), mirror);
        systems.drivingSystem.driveStraight(driveStaightDistanceForTotem, 0.6);
        // pick up totem
        systems.drivingSystem.driveStraight(INITIAL_DRIVE_STAIGHT_DISTANCE - driveStaightDistanceForTotem, 0.6);
    }

    private void craterPlaceFreight(boolean toPoint){
        pickupTotem();
        if (floor == ArmSystem.Floors.FIRST) {
            // because the the totem system blocks the armSystem, we can't use the autonomousPlaceFreight, so we turn 180 degrees adn use placeFreight instead.
            systems.armSystem.autonomousMoveArm(floor);
            systems.drivingSystem.driveToPoint(5 * mirror, -60 + driveStraightDistanceForFloor(floor.switchIfMirrored(mirror),mirror), -90 * mirror, 0.5, 0.7);
            systems.armSystem.awaitArmArrival();
            TimeUtils.sleep(50);
            systems.armSystem.spit();
            TimeUtils.sleep(300);
            if (toPoint) {
                systems.drivingSystem.driveToPoint((20 + -25 * isMirrored(mirror)) * mirror, 65, 90 * mirror, 0.5, 1);
                if (floor != ArmSystem.Floors.FIRST){
                    systems.drivingSystem.driveStraight(8, 0.6);
                }
//                drivingSystem.driveSideways(10, 0.6);
                systems.armSystem.moveArm(0);
            }
        } else {
            systems.armSystem.moveArm(floor);
            TimeUtils.sleep(700);
            systems.drivingSystem.driveToPoint(2 * mirror, -37 + driveStraightDistanceForFloor(floor.switchIfMirrored(mirror),mirror), 50 * mirror, 0.5, 0.7);
            systems.armSystem.awaitArmArrival();
            TimeUtils.sleep(50);
            systems.armSystem.spit();
            TimeUtils.sleep(300);
            systems.armSystem.moveArm(0);
            if (toPoint) {
                systems.drivingSystem.driveToPoint(0 * mirror, 70, 90 * mirror, 0.5, 1);
//                drivingSystem.driveSideways(10, 0.6);
            }
        }
    }

    private void RZNCXLoop(int i, int mirror) {
        double extraDistanceForBlue = 10;
        double initialDistance = 65 + 10 * i;
        systems.drivingSystem.driveStraight(initialDistance, 0.5, false);
        systems.drivingSystem.driveUntilCollect(100, 0.2);
        TimeUtils.sleep(200);
        systems.armSystem.moveArm(ArmSystem.Floors.THIRD);
        systems.drivingSystem.driveToPoint(15, 15, 90 * mirror, 0.5, 0.5);
        systems.drivingSystem.driveUntilWhite(-0.5,false);
        systems.drivingSystem.driveStraight(55 +extraDistanceForBlue * MathUtils.isMirrored(mirror),-0.6);
        systems.drivingSystem.driveToPoint(0 * mirror, -75, 60 * mirror, 0.5, 0.5);
        TimeUtils.sleep(300);
        systems.armSystem.spit();
        TimeUtils.sleep(600);
        systems.armSystem.stop();
        systems.armSystem.moveArm(0);
        systems.drivingSystem.driveToPoint(0 * mirror, 80, 90 * mirror, 0.5, 0.5);
    }


    public void RZNCX(){
        craterPlaceFreight(true);
        for (int i = 0; i < 2; i++) {
            RZNCXLoop(i, mirror);
        }
        systems.armSystem.collect();
        systems.drivingSystem.driveStraight(100, 0.7);

    }
}

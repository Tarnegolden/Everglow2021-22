package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

public class TotemSystem2 {
    private final LinearOpMode  opMode;
    public final Servo         altitude1;
    public final Servo         altitude2;
    private final CRServo       meterLeft;
    private final CRServo       meterRight;
    private final DrivingSystem drivingSystem;

    private final double ALTITUDE1_ZERO = 0.5;
    private final double ALTITUDE2_ZERO = 0.5;
    private final double ALTITUDE1_MAX = 0.29;
    private final double ALTITUDE2_MAX = 0.67;

    public TotemSystem2(LinearOpMode opMode) {
        this.opMode   = opMode;
        altitude1     = opMode.hardwareMap.get(Servo.class, "altitude1");
        altitude2     = opMode.hardwareMap.get(Servo.class, "altitude2");
        meterLeft     = opMode.hardwareMap.get(CRServo.class, "meter_left");
        meterRight    = opMode.hardwareMap.get(CRServo.class, "meter_right");
        drivingSystem = new DrivingSystem(opMode);
        altitude1.setPosition(ALTITUDE1_ZERO);
        altitude2.setPosition(ALTITUDE2_ZERO);
    }

    public void extendLeft(double power) {
        meterLeft.setPower(power);
    }

    public void extendRight(double power) {
        meterRight.setPower(power);
    }

    public void stopLeft() {
        meterLeft.setPower(0);
    }

    public void stopRight() {
        meterRight.setPower(0);
    }

    public void moveAltitude(double delta) {
//        if(altitude1.getPosition() + delta < ALTITUDE1_MAX && altitude2.getPosition() - delta > ALTITUDE2_MAX) {
            altitude1.setPosition(altitude1.getPosition() + delta);
            altitude2.setPosition(altitude2.getPosition() - delta);
//        }
    }

    public void setAltitude(double position) {
        altitude1.setPosition(position);
        altitude2.setPosition(1 - position);
    }
}

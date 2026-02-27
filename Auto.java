package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.CRServo;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "leatAuto2026 ")
public class leatAuto2026 extends LinearOpMode {

    private double BACK_DISTANCE_INCHES = 12;
    private double DRIVE_POWER = 0.4;

    private double SHOOT_VELOCITY = 750;
    private int BALL_COUNT = 3;
    private double VELOCITY_TOLERANCE = 30;

    private long SPINUP_TIME_MS = 1500;
    private long FEED_TIME_MS = 400;
    private long RECOVERY_TIME_MS = 600;

    private static final double TICKS_PER_REV = 560;
    private static final double WHEEL_DIAMETER = 3.78;
    private static final double GEAR_RATIO = 3.0;

    private Blinker control_Hub;

    private DcMotor leftFrontMotor;
    private DcMotor rightFrontMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightBackMotor;

    private DcMotor guideMotor;
    private DcMotor accelMotor;
    private Servo feedServo;

    @Override
    public void runOpMode() {

        initHardware();

        waitForStart();

        if (isStopRequested()) return;

        spinUpShooter(SHOOT_VELOCITY);
        driveBackward(BACK_DISTANCE_INCHES, DRIVE_POWER);
        shootBalls(BALL_COUNT);

        stopAll();
    }


    private void initHardware() {
        leftFrontMotor = hardwareMap.get(DcMotor.class, "leftFrontMotor");
        leftBackMotor = hardwareMap.get(DcMotor.class, "leftBackMotor");
        rightFrontMotor = hardwareMap.get(DcMotor.class, "rightFrontMotor");
        rightBackMotor = hardwareMap.get(DcMotor.class, "rightBackMotor");

        feedServo = hardwareMap.get(Servo.class, "feedServo");
        guideMotor = hardwareMap.get(DcMotor.class, "guideMotor");
        accelMotor = hardwareMap.get(DcMotor.class, "accelMotor");

        rightFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBackMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        accelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setVelocityPIDFCoefficients(5, 0, 0, 12);
    }


    private void driveBackward(double inches, double power) {

        int ticks = inchesToTicks(inches);

        leftBackMotor.setTargetPosition(-ticks);
        leftFrontMotor.setTargetPosition(-ticks);
        rightBackMotor.setTargetPosition(-ticks);
        rightFrontMotor.setTargetPosition(-ticks);

        leftBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftBackMotor.setPower(power);
        leftFrontMotor.setPower(power);
        rightBackMotor.setPower(power);
        rightFrontMotor.setPower(power);

        while (opModeIsActive() && (leftBackMotor.isBusy() || leftFrontMotor.isBusy() || rightBackMotor.isBusy() || rightFrontMotor.isBusy())
        {
            telemetry.addLine("Status: Moving Back");
            telemetry.update();
        }

        leftBackMotor.setPower(0);
        leftFrontMotor.setPower(0);
        rightBackMotor.setPower(0);
        rightFrontMotor.setPower(0);

        leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private int inchesToTicks(double inches) {
        double circumference = Math.PI * WHEEL_DIAMETER;
        double rotations = inches / circumference;
        return (int)(rotations * TICKS_PER_REV * GEAR_RATIO);
    }


    private void spinUpShooter(double velocity) {
        accelMotor.setVelocity(velocity);
        sleep(SPINUP_TIME_MS);
    }

    private void shootBalls(int count) {

        for (int i = 0; i < count; i++) {

            waitForVelocity(SHOOT_VELOCITY);

            feedBall();
            sleep(FEED_TIME_MS);

            telemetry.addData("ShotBall", i);
            telemetry.update();

            resetFeeder();
            sleep(RECOVERY_TIME_MS);
        }
    }

    private void waitForVelocity(double target) {
        while (opModeIsActive() && Math.abs(shooter.getVelocity() - target) > VELOCITY_TOLERANCE) {
            telemetry.addData("Velocity", shooter.getVelocity());
            telemetry.update();
        }
    }

    private void feedBall() {
        feedServo.setPosition(1.0);
    }

    private void resetFeeder() {
        feedServo.setPosition(0.0);
    }


    private void stopAll() {
        accelMotor.setVelocity(0);
        leftBackMotor.setPower(0);
        leftFrontMotor.setPower(0);
        rightBackMotor.setPower(0);
        rightFrontMotor.setPower(0);
    }
}

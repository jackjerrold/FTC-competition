package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.AngleUnit;

@TeleOp(name = "LAE TeleOp 2026 (Field Centric)")
public class LAETeleop2026 extends LinearOpMode {

    private DcMotor leftFrontMotor;
    private DcMotor rightFrontMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightBackMotor;

    private DcMotor guideMotor;
    private DcMotor accelMotor;
    private CRServo feedServo;

    private IMU imu;

    boolean dpadUpPrev = false;
    boolean dpadDownPrev = false;
    boolean armGuide = false;
    boolean armAccel = false;

    double guideSpeed = -0.5;
    double accelSpeed = 0.8;

    double fastSpeed = 1.0;
    double slowSpeed = 0.4;

    @Override
    public void runOpMode() {

        leftFrontMotor = hardwareMap.get(DcMotor.class, "leftFrontMotor");
        leftBackMotor = hardwareMap.get(DcMotor.class, "leftBackMotor");
        rightFrontMotor = hardwareMap.get(DcMotor.class, "rightFrontMotor");
        rightBackMotor = hardwareMap.get(DcMotor.class, "rightBackMotor");

        guideMotor = hardwareMap.get(DcMotor.class, "guideMotor");
        accelMotor = hardwareMap.get(DcMotor.class, "accelMotor");
        feedServo = hardwareMap.get(CRServo.class, "feedServo");

        imu = hardwareMap.get(IMU.class, "IMU");

        rightFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBackMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        guideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        accelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        imu.resetYaw();

        telemetry.addLine("Status: Initialized");
        telemetry.addLine("Press START");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            if (!gamepad1.dpad_up && dpadUpPrev) {armGuide = !armGuide;}
            dpadUpPrev = gamepad1.dpad_up;

            if (!gamepad1.dpad_down && dpadDownPrev) {armAccel = !armAccel;}
            dpadDownPrev = gamepad1.dpad_down;

            double driveSpeed = fastSpeed;
            if (gamepad1.left_bumper) driveSpeed = slowSpeed;
            if (gamepad1.right_bumper) driveSpeed = fastSpeed;

            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double z = gamepad1.right_stick_x;

            // Deadzone
            double deadzone = 0.05;
            y = Math.abs(y) > deadzone ? y : 0;
            x = Math.abs(x) > deadzone ? x : 0;
            z = Math.abs(z) > deadzone ? z : 0;

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            double rf = rotY + rotX - z;
            double lf = rotY - rotX - z;
            double rb = rotY - rotX + z;
            double lb = rotY + rotX + z;

            double max = Math.max(1.0,
                    Math.max(Math.abs(rf),
                    Math.max(Math.abs(lf),
                    Math.max(Math.abs(rb), Math.abs(lb)))));

            rf /= max;
            lf /= max;
            rb /= max;
            lb /= max;

            rightFrontMotor.setPower(rf * driveSpeed);
            leftFrontMotor.setPower(lf * driveSpeed);
            rightBackMotor.setPower(rb * driveSpeed);
            leftBackMotor.setPower(lb * driveSpeed);

            guideMotor.setPower(armGuide ? guideSpeed : 0);
            accelMotor.setPower(armAccel ? accelSpeed : 0);
            feedServo.setPower(gamepad1.a ? 1 : 0);

            telemetry.addData("Drive Mode:", driveSpeed == fastSpeed ? "FAST" : "SLOW");
            telemetry.addData("Heading:", Math.toDegrees(botHeading));
            telemetry.addData("Left Stick:", "x: %.2f y: %.2f", x, y);
            telemetry.addData("Rotate:", "%.2f", z);
            telemetry.addData("Guide Motor:", armGuide ? "ARMED" : "IDLE");
            telemetry.addData("Accel Motor:", armAccel ? "ARMED" : "IDLE");
            telemetry.addData("Feed Servo:", gamepad1.a ? "ARMED" : "IDLE");
            telemetry.update();
        }
    }
}

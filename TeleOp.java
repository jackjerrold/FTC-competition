package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp

public class LAETeleop2026 extends LinearOpMode {
    private Blinker control_Hub;
    private DcMotor arm;
    private DcMotor leftFrontMotor;
    private DcMotor rightFrontMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightBackMotor;
    private CRServo feedServo;
    private DcMotor guideMotor;
    private DcMotor accelMotor;

    boolean dpad_upPrevious = false;
    boolean dpad_downPrevious = false;
    boolean trigger = false;
    boolean armGuide = false;
    boolean armAccel = false;

    private double guideSpeed = -0.5;
    private double accelSpeed = 0.8;

    @Override
    
public void runOpMode() {
        control_Hub = hardwareMap.get(Blinker.class, "Control Hub");
        leftFrontMotor = hardwareMap.get(DcMotor.class, "leftFrontMotor");
        leftBackMotor = hardwareMap.get(DcMotor.class, "leftBackMotor");
        rightBackMotor = hardwareMap.get(DcMotor.class, "rightBackMotor");
        rightFrontMotor = hardwareMap.get(DcMotor.class, "rightFrontMotor");
        feedServo = hardwareMap.get(CRServo.class, "feedServo");//CHANGE TO CR
        guideMotor = hardwareMap.get(DcMotor.class, "guideMotor");
        accelMotor = hardwareMap.get(DcMotor.class, "accelMotor");

        
        rightFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBackMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        double x;
        double y;
        double z;
        double theta, power;
        double sin, cos, max;
        double LF, RF, LB, RB;
        
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

        if (gamepad1.dpad_up == false && dpad_upPrevious == true){armGuide = !armGuide;}
        dpad_upPrevious = gamepad1.dpad_up;
        if (gamepad1.dpad_down == false && dpad_downPrevious == true){armAccel = !armAccel;}
        dpad_downPrevious = gamepad1.dpad_down;
        
        trigger = gamepad1.a;
        x = gamepad1.left_stick_x;
        y = -gamepad1.left_stick_y;
        z = gamepad1.right_stick_x;

        theta = Math.atan2(y, x);
        power = Math.hypot(x, y);

        sin = Math.sin(theta - Math.PI/4);
        cos = Math.cos(theta - Math.PI/4);
        max = Math.max(Math.abs(sin), Math.abs(cos));

        LF = power * cos/max + z;
        RF = power * sin/max - z;
        LB = power * sin/max + z;
        RB = power * cos/max - z;

        if (armGuide){guideMotor.setPower(guideSpeed);}
        else{guideMotor.setPower(0);}

        if (armAccel){accelMotor.setPower(accelSpeed);}
        else{accelMotor.setPower(0);}
        
        if (trigger){feedServo.setPower(1);}
        else{feedServo.setPower(0);}
        
        rightFrontMotor.setPower(RF);
        leftFrontMotor.setPower(LF);
        rightBackMotor.setPower(RB);
        leftBackMotor.setPower(LB);
        
        telemetry.addData("Status", "Running");
        telemetry.addData("GuideMotor", armGuide);
        telemetry.addData("Accelorator", armAccel);
        telemetry.addData("Trigger", trigger);
        telemetry.update();
        }
    }
}

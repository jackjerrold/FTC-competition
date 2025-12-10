package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp

public class HelloRobot_TeleOp extends LinearOpMode {
    private Blinker control_Hub;
    private DcMotor arm;
    private DcMotor leftFrontMotor;
    private DcMotor rightFrontMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightBackMotor;
    private Servo test_servo;


    @Override
public void runOpMode() {
        control_Hub = hardwareMap.get(Blinker.class, "Control Hub");
        accel = hardwareMap.get(DcMotor.class, "accel");
        leftmotor = hardwareMap.get(DcMotor.class, "leftmotor");
        rightmotor = hardwareMap.get(DcMotor.class, "rightmotor");
        feed_servo = hardwareMap.get(Servo.class, "feed_servo");
        
        rightFrontmotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBackmotor.setDirection(DcMotorSimple.Direction.REVERSE);
        double x;
        double y;
        boolean trigger;
        
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

        
        trigger = gamepad1.a;
        x = gamepad1.left_stick_x;
        y = -gamepad1.left_stick_y - gamepad1.right_stick_y;
        z = gamaepad1.right_stick_x;
        
        if (trigger){
            feed_servo.setPower(1);
        }
        else{
            feed_servo.setPower(0);
        }
        rightFrontMotor.setPower(y-x)-z;
        leftFrontMotor.setPower(y-x)-z;
        rightBackMotor.setPower(y+x)+z;
        leftBackMotor.setPower(y+x)+z;
        
        telemetry.addData("Status", "Running");
        telemetry.update();
        }
    }
}
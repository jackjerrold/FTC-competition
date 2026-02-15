package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.mechanisms.AprilTagWebcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
@Autonomous
class AprilTagWebcamTest extends OpMode {
    AprilTagWebcam aprilTagWebcamExample = new AprilTagWebcam(); // starts camera

    @Override
    public void init() {
        aprilTagWebcamExample.init(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        aprilTagWebcamExample.update();
        AprilTagDetection id20 = aprilTagWebcamExample.getTagBySpecificId(20);
        telemetry.addData("id20 String", id20.toString());
    }
}
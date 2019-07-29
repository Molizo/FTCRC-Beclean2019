package eu.qrobotics.beclean2019.drive;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import eu.qrobotics.beclean2019.util.StickyGamepad;

@TeleOp(name = "Drive")
public class Drive extends OpMode {

    double SPEED_COEFFICIENT = 0.25;

    StickyGamepad stickyGamepad1 = null;
    StickyGamepad stickyGamepad2 = null;

    DcMotorEx leftFront = null;
    DcMotorEx rightFront = null;
    DcMotorEx leftRear = null;
    DcMotorEx rightRear = null;
    DcMotorEx sliderMotor = null;

    Servo flipServoLeft = null;
    Servo flipServoRight = null;
    Servo grabberServo = null;

    boolean IsGrabberEngaged = true;
    boolean IsFlipped = false;

    double sliderPower = 0;

    @Override public void init(){
        stickyGamepad1 = new StickyGamepad(gamepad1);
        stickyGamepad2 = new StickyGamepad(gamepad2);

        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        rightFront = hardwareMap.get(DcMotorEx.class,"rightFront");
        leftRear = hardwareMap.get(DcMotorEx.class,"leftRear");
        rightRear = hardwareMap.get(DcMotorEx.class,"rightRear");

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        sliderMotor = hardwareMap.get(DcMotorEx.class,"sliderMotor");
        sliderMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sliderMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flipServoLeft = hardwareMap.get(Servo.class,"flipServoLeft");
        flipServoRight = hardwareMap.get(Servo.class,"flipServoRight");
        grabberServo = hardwareMap.get(Servo.class,"grabberServo");

        flipServoLeft.setPosition(0.85);
        flipServoRight.setPosition(0.35);
        grabberServo.setPosition(0.9);

        telemetry.addLine("Initialized");
        telemetry.update();
    }

    @Override public void loop() {

        stickyGamepad1.update();
        stickyGamepad2.update();

        if(stickyGamepad1.x || stickyGamepad2.x) {
            if (SPEED_COEFFICIENT == 0.25)
                SPEED_COEFFICIENT = 0.5;
            else
                SPEED_COEFFICIENT = 0.25;
            IsGrabberEngaged = !IsGrabberEngaged;
        }

        double leftX, leftY, rightX;
        leftX = stickyGamepad1.leftX + stickyGamepad2.leftX;
        leftY = stickyGamepad1.leftY + stickyGamepad2.leftY;
        rightX = stickyGamepad1.rightX + stickyGamepad2.rightX;

        double leftFrontPower, rightFrontPower, leftRearPower, rightRearPower;
        leftFrontPower = Range.clip((leftX + leftY + rightX)*SPEED_COEFFICIENT,-1,1);
        rightFrontPower = Range.clip((leftX - leftY + rightX)*SPEED_COEFFICIENT,-1,1);
        leftRearPower = Range.clip((-leftX + leftY + rightX)*SPEED_COEFFICIENT,-1,1);
        rightRearPower = Range.clip((-leftX - leftY + rightX)*SPEED_COEFFICIENT,-1,1);

        if(stickyGamepad1.a || stickyGamepad2.a) {
            if (IsGrabberEngaged)
                grabberServo.setPosition(0.8);
            else
                grabberServo.setPosition(1);
            IsGrabberEngaged = !IsGrabberEngaged;
        }

        if(stickyGamepad1.b || stickyGamepad2.b)
        {
            if(IsFlipped)
            {
                flipServoLeft.setPosition(0.85);
                flipServoRight.setPosition(0.35);
            }
            else
            {
                flipServoLeft.setPosition(0.2);
                flipServoRight.setPosition(0.975);
            }
            IsFlipped = !IsFlipped;
        }

        sliderPower = gamepad1.dpad_up || gamepad2.dpad_up ? 0.25 : 0;
        sliderPower -= gamepad1.dpad_down || gamepad2.dpad_down ? 0.25 : 0;

        sliderMotor.setPower(sliderPower);
        leftFront.setPower(leftFrontPower);
        rightFront.setPower(rightFrontPower);
        leftRear.setPower(leftRearPower);
        rightRear.setPower(rightRearPower);

        telemetry.addData("Gamepad", leftX + " " + leftY + " " + rightX);
        telemetry.addData("Motor Power", leftFrontPower + " " + rightFrontPower + " " + leftRearPower + " " + rightRearPower);
        telemetry.addData("Slider Power",sliderPower);
        telemetry.update();
    }
}

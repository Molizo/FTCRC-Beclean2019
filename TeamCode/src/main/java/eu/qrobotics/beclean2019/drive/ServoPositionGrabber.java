package eu.qrobotics.beclean2019.drive;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ServoPositionGrabber")
public class ServoPositionGrabber extends OpMode {

    Servo servo = null;
    Servo servo2 = null;

    double servoPosition1 = 0.85;
    double servoPosition2 = 0.35;

    @Override public void init(){
        servo = hardwareMap.servo.get("flipServoLeft");
        //servo2 = hardwareMap.servo.get("flipServoRight");

        //servo.setDirection(Servo.Direction.REVERSE);
        telemetry.addLine("Initialized");
        telemetry.update();
    }

    @Override public void loop() {
        telemetry.addData("Servo position",servo.getPosition());
        if(gamepad1.a) {
            servoPosition1 -= 0.025;
            servoPosition2 += 0.025;
            while(gamepad1.a)
            {
                telemetry.update();
            }
        }
        if(gamepad1.b){

            servoPosition1 += 0.025;
            servoPosition2 -= 0.025;
            while(gamepad1.b)
            {
                telemetry.update();
            }
        }
        servo.setPosition(servoPosition1);
        //servo2.setPosition(servoPosition2);
        telemetry.update();
    }
}
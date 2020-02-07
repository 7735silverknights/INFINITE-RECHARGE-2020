/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  // constants
  private final double belt_const = 0.5;

  // driver joystick
  private Joystick driver;
  // private Joystick operator;

  // speed controllers
  private VictorSP leftDrive;
  private VictorSP rightDrive;
  private VictorSP beltDrive;
  private VictorSP hatchDrive;

  // camera
  private UsbCamera camera;

  private UsbCamera camera2;
  // auto start time
  long autoStartTime;

  // speed const
  double speed_const;
  int direct;

  @Override
  public void robotInit() {
    this.driver = new Joystick(0);    // joystick on input 0

    this.leftDrive = new VictorSP(0);
    this.rightDrive = new VictorSP(1);
    this.beltDrive = new VictorSP(2);
    this.hatchDrive = new VictorSP(3);

    //CameraServer.getInstance().startAutomaticCapture();

    CameraServer.getInstance().startAutomaticCapture(0);
    CameraServer.getInstance().startAutomaticCapture(1);



    // try{
    //   this.camera2 = CameraServer.getInstance().startAutomaticCapture(1);
    // } catch(Exception e){
    //   e.printStackTrace();
    // }
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
    
     // arcade drive
    // getting the inputs
    //NOTE: Please name variables so that they are more intuitive! -Khalil Balde team5596
    double driverX = this.driver.getRawAxis(0);
    double driverY = this.driver.getRawAxis(1);

    double beltSpeed = this.driver.getRawAxis(3); // Right Trigger
    double gear3 = this.driver.getRawAxis(2);      // Left Trigger

    // LB/L1 pressed for inverted control
    if (this.driver.getRawButton(5)){
      direct = -1;
    }
    else{
      direct = 1;
    }

    // calculating the outputs;
    double leftOut = driverY * direct + driverX;
    double rightOut = driverY * direct - driverX;

    // RB/R1 pressed for acceleration
    if (this.driver.getRawButton(6)){
      if (speed_const < 0.4)
        speed_const += 0.025;
    }
    else if (gear3 > 0.5){  // LT pressed for further acceleration
      if (speed_const < 1)
        speed_const += 0.1;
    }
    else{
      speed_const = 0.1;
    }


    // setting speed controllers
    setLeftDrive(leftOut * speed_const);
    setRightDrive(rightOut * speed_const);

    this.beltDrive.set(-beltSpeed * belt_const);

    // setting the hatch panel motor speed
    if (this.driver.getRawButton(2))
      this.hatchDrive.set(0.5);
    else if (this.driver.getRawButton(1))
      this.hatchDrive.set(-0.5);
    else
      this.hatchDrive.set(0);
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {    
    // arcade drive
    // getting the inputs
    //NOTE: Please name variables so that they are more intuitive! -Khalil Balde team5596
    double driverX = this.driver.getRawAxis(0);
    double driverY = this.driver.getRawAxis(1);

    double beltSpeed = this.driver.getRawAxis(3); // Right Trigger
    double gear3 = this.driver.getRawAxis(2);

    // LB/L1 pressed for inverted control
    if (this.driver.getRawButton(5)){  
      direct = -1;
    }
    else{
      direct = 1;
    }

    // calculating the outputs;
    double leftOut = driverY * direct + driverX;
    double rightOut = driverY * direct - driverX;

    // RB/R1 pressed for acceleration
    if (this.driver.getRawButton(6)){
      if (speed_const < 0.5)
        speed_const += 0.025;
    }
    else if (gear3 > 0.5){  // LT pressed for further acceleration
      if (speed_const < 1)
        speed_const += 0.1;
    }
    else{
      speed_const = 0.3;
    }

    // setting speed controllers
    setLeftDrive(leftOut * speed_const);
    setRightDrive(rightOut * speed_const);

    this.beltDrive.set(-beltSpeed * belt_const);

    // setting the hatch panel motor speed
    if (this.driver.getRawButton(2))
      this.hatchDrive.set(0.5);
    else if (this.driver.getRawButton(1))
      this.hatchDrive.set(-0.5);
    else
      this.hatchDrive.set(0);
  }

  // this sets the speed of the left-side motors
  private void setLeftDrive(double speed){
    this.leftDrive.set(speed);
  }

  // this sets the speed of the right-side motors
  private void setRightDrive(double speed){
    this.rightDrive.set(speed);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    
    double leftStick = -this.driver.getRawAxis(0);
    double rightStick = -this.driver.getRawAxis(1);

    // setLeftDrive(leftStick);
    setRightDrive(rightStick);
    setLeftDrive(leftStick);

  }

  @Override
  public void disabledPeriodic(){}

  @Override
  public void disabledInit(){}
}

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
 
  private Joystick driver;

  private VictorSP leftDrive1;
  private VictorSP leftDrive2;
  private VictorSP rightDrive1;
  private VictorSP rightDrive2;

  private Solenoid lowGear;
  private Solenoid highGear;

  private long autonStartTime;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    this. driver = new Joystick(0);

    this.leftDrive1 = new VictorSP(0);
    this.leftDrive2 = new VictorSP(1);
    this.rightDrive1 = new VictorSP(2);
    this.rightDrive2 = new VictorSP(3);

    this.lowGear = new Solenoid(0);
    this.highGear = new Solenoid(1);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    this.autonStartTime = System.currentTimeMillis();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    long timeElapsed = System.currentTimeMillis() - this.autonStartTime;


    if(timeElapsed < 3000) { // go forward for three seconds
      this.leftDrive1.set(-0.3);
      this.leftDrive2.set(-0.3);
      this.rightDrive1.set(0.3);
      this.rightDrive2.set(0.3);
    } else { // and then stop
      this.leftDrive1.set(0);
      this.leftDrive2.set(0);
      this.rightDrive1.set(0);
      this.rightDrive2.set(0);
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    // joystick input
    double driverX = this.driver.getRawAxis(0);
    
    // y inverted since fowrdan on the stick is negative
    // we want forward to be positive
    double driverY = -this.driver.getRawAxis(0);

    // drive output calculations for arcade drive
    double leftOutput = driverY + driverX;
    double rightOutput = driverY - driverX;

    // motor outputs
    // one side will need to be inverted - try one and see!
    this.leftDrive1.set(leftOutput);
    this.leftDrive2.set(leftOutput);
    this.rightDrive1.set(rightOutput);
    this.rightDrive2.set(rightOutput);


    // gear shifter
    if(this.driver.getRawButton(4)) {
      this.lowGear.set(true); // low gear
      this.highGear.set(false);
    } 
    if(this.driver.getRawButton(5)) {
      this.lowGear.set(false); // high gear
      this.highGear.set(true);
    }

  }


}

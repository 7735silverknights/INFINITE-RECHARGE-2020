/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import javax.annotation.OverridingMethodsMustInvokeSuper;

// Customized import
// this is sam. Why didn't you import all
import edu.wpi.cscore.UsbCamera;                      // Camera
import edu.wpi.first.cameraserver.CameraServer;       // Creating and keeping camera servers
import edu.wpi.first.wpilibj.Joystick;                // Joystick
import edu.wpi.first.wpilibj.VictorSP;                // Vector SP Speed Control

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  // Constants 
  private final double SPIN_SPEED = 0.5;

  // driverStick joystick
  private  Joystick driver;
  // private Joystick operatorStick;

  // speed controllers
  private VictorSP leftDrive;
  private VictorSP rightDrive;
  private VictorSP spinDrive;
  private VictorSP intakeDrive;
  private VictorSP armDrive;
  private VictorSP liftDrive;

  // switch
  private DigitalInput switch1;
  private DigitalInput switch2;
  private DigitalInput switch3;
  private DigitalInput switch4;
  private DigitalInput switch5;
  private DigitalInput switch6;
  private DigitalInput switch7;
  private DigitalInput switch8;
  private DigitalInput switch9;

  // camera
  private UsbCamera camera1;
  private UsbCamera camera2;

  // auto start time
  long autoStartTime;

  // speed const
  double speed_const;
  int direct;

  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    this.driver = new Joystick(0);                     // joystick on input 0

    this.leftDrive = new VictorSP(0);
    this.rightDrive = new VictorSP(1);
    this.spinDrive = new VictorSP(2);
    this.intakeDrive = new VictorSP(3);
    this.armDrive = new VictorSP(4);
    this.liftDrive = new VictorSP(5);

    this.switch1 = new DigitalInput(0);
    this.switch2 = new DigitalInput(1);
    this.switch3 = new DigitalInput(2);
    this.switch4 = new DigitalInput(4);
    this.switch5 = new DigitalInput(5);
    this.switch6 = new DigitalInput(6);
    this.switch7 = new DigitalInput(7);
    this.switch8 = new DigitalInput(8);
    this.switch9 = new DigitalInput(9);
    // CameraServer.getInstance().startAutomaticCapture();

    CameraServer.getInstance().startAutomaticCapture(0);
    CameraServer.getInstance().startAutomaticCapture(1);

    /*
    try{
      this.camera2 = CameraServer.getInstance().startAutomaticCapture(1);
    } catch(Exception e){
      e.printStackTrace();
    }
    */
  }

  @Override
  public void robotPeriodic() {
  }


  private double startTime = Timer.getFPGATimestamp();
  @Override
  public void autonomousInit() {

    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
      //Starts right in front of hatch, heads to hatch 
  //HOW DO WE SWITCH BETWEEN AUTO AND TELEOP
  @Override
  public void auto1(){
    
  } 

  @Override
  public void auto2(){
    double time = Timer.getFPGATimestamp();
    
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {  

      // arcade drive
      // getting the inputs
      final double driverX = this.driver.getRawAxis(0);
      final double driverY = this.driver.getRawAxis(1);

      final double spinSpeed = this.driver.getRawAxis(3);            // Right Trigger
      final double gear3 = this.driver.getRawAxis(2);                // Left Trigger

      // LB/L1 pressed for inverted control
      if (this.driver.getRawButton(5)) {
        direct = -1;
      } else {
        direct = 1;
      }

      // calculating the outputs;
      final double leftOut = driverY * direct + driverX;
      final double rightOut = driverY * direct - driverX;

      // RB/R1 pressed for acceleration
      if (this.driver.getRawButton(6)) {
        if (speed_const < 0.4) {
          speed_const += 0.025;
        }
      } else if (gear3 > 0.5) {                 // LT pressed for further acceleration
        if (speed_const < 1){
          speed_const += 0.1;
        }
      } else {
        speed_const = 0.1;
      }

      // setting speed controllers
      setLeftDrive(leftOut * speed_const);
      setRightDrive(rightOut * speed_const);

      this.spinDrive.set(-spinSpeed * SPIN_SPEED);

      // setting the intake motor speed
      if (this.driver.getRawButton(2))
        this.intakeDrive.set(0.5);
      else if (this.driver.getRawButton(1))
        this.intakeDrive.set(-0.5);
      else
        this.intakeDrive.set(0);

      // setting the arm motor speed
      if (this.driver.getRawButton(4))
        this.armDrive.set(0.5);
      else if (this.driver.getRawButton(3))
        this.armDrive.set(-0.5);
      else
        this.armDrive.set(0);

        // setting the lift motor speed
      if (this.driver.getRawButton(10))
      this.liftDrive.set(0.5);
    //else if (this.driver.getRawButton(3))
      //this.liftDrive.set(-0.5);
    else
      this.liftDrive.set(0);
  }



  //starting 

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    // arcade drive
    // getting the inputs
    // NOTE: Please name variables so that they are more intuitive! -Khalil Balde
    // team5596
    final double driverX = this.driver.getRawAxis(0);
    final double driverY = this.driver.getRawAxis(1);

    final double spinSpeed = this.driver.getRawAxis(3); // Right Trigger
    final double gear3 = this.driver.getRawAxis(2);

    // LB/L1 pressed for inverted control
    if (this.driver.getRawButton(5)) {
      direct = -1;
    } else {
      direct = 1;
    }

    // calculating the outputs;
    final double leftOut = driverY * direct + driverX;
    final double rightOut = driverY * direct - driverX;

    // RB/R1 pressed for acceleration
    if (this.driver.getRawButton(6)) {
      if (speed_const < 0.5)
        speed_const += 0.025;
    } else if (gear3 > 0.5) { // LT pressed for further acceleration
      if (speed_const < 1)
        speed_const += 0.1;
    } else {
      speed_const = 0.3;
    }

    // setting speed controllers
    setLeftDrive(leftOut * speed_const);
    setRightDrive(rightOut * speed_const);

    this.spinDrive.set(-spinSpeed * SPIN_SPEED);

    // setting the intake motor speed
    if (this.driver.getRawButton(2))
      this.intakeDrive.set(0.5);
    else if (this.driver.getRawButton(1))
      this.intakeDrive.set(-0.5);
    else
      this.intakeDrive.set(0);

    // setting the arm motor speed
    if (this.driver.getRawButton(4))
      this.armDrive.set(0.5);
    else if (this.driver.getRawButton(3))
      this.armDrive.set(-0.5);
    else
      this.armDrive.set(0);

    // setting the lift motor speed
    if (this.driver.getRawButton(10))
    this.liftDrive.set(0.5);
  //else if (this.driver.getRawButton(3))
    //this.liftDrive.set(-0.5);
  else
    this.liftDrive.set(0);
  }

  // this sets the speed of the left-side motors
  private void setLeftDrive(final double speed) {
    this.leftDrive.set(speed);
  }

  // this sets the speed of the right-side motors
  private void setRightDrive(final double speed) {
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

  /* 
   * 0.17=90 DEGREES, 0.3 moving forwards one rebot length, all for one second intervals
   * in one second, at 0.3 speed, drives forwards 45 inches or 114.3cm
   */

  @Override
  public void testInit() {
    double time = Timer.getFPGATimestamp();
    //STARTING RIGHTMOST, DUMP BALLS INTO HATCH
    if (switch1.get()) {
      if (time - startTime < 1) { //drive backwards to cross line
        this.leftDrive.set(-0.3);
        this.rightDrive.set(-0.3);
      } else if(time - startTime < 5) { //driving forward to hatch
        this.leftDrive.set(0.3);  
        this.rightDrive.set(0.3);
      } else if (time - startTime < 10) { //dumping balls
        this.leftDrive.set(0);
        this.rightDrive.set(0);
        this.intakeDrive.set(-0.5);
      } else if (time - startTime < 11) { //turning left (to get out of way)
        this.leftDrive.set(-0.17);
        this.rightDrive.set(0.17);
      } else if (time - startTime < 14) { //driving away from hatch
        this.leftDrive.set(0.3);
        this.rightDrive.set(0.3);
      } 
    // START MIDDLE, DUMP BALLS INTO HATCH
    } else if (switch2.get()) {
      if (time - startTime < 1) { //drive backwards to cross the
        this.leftDrive.set(-0.3);
        this.rightDrive.set(-0.3);
      } else if (time - startTime < 2) { //drive forwards to return to stating position
        this.leftDrive.set(0.3);
        this.rightDrive.set(0.3);
      } else if (time - startTime < 3) { //turn right
        this.leftDrive.set(0.17);
        this.rightDrive.set(-0.17);
      } else if (time - startTime < 7) { //drive forwards
        this.leftDrive.set(0.3);
        this.rightDrive.set(0.3);
      } else if (time - startTime < 8) { //turn left
        this.leftDrive.set(-0.17);
        this.rightDrive.set(0.17);
      } else if (time - startTime < 10) { //drive forwards
        this.leftDrive.set(0.3);
        this.rightDrive.set(0.3);
      } else { //dump balls into hatch
        this.leftDrive.set(0);
        this.rightDrive.set(0);
        this.intakeDrive.set(-0.5);
      }
    //START LEFTMOST, DUMP BALLS INTO HATCH
    } else if (switch3.get()) {
      if (time - startTime < 1) { //drive backwards to cross the line
        this.leftDrive.set(0.3);
        this.rightDrive.set(0.3);
      } else if (time - startTime < 2) {//turn right
        this.leftDrive.set(0.17);
        this.rightDrive.set(-0.17);
      } else if (time - startTime < 7) {//drive forwards 
        this.leftDrive.set(0.3);
        this.rightDrive.set(0.3);
      } else if (time - startTime < 8.5) { //turn left
        this.leftDrive.set(-0.17);
        this.rightDrive.set(0.17);
      } else if (time - startTime < 11) { //drive forwards to hatch
        this.leftDrive.set(0.3);
        this.rightDrive.set(0.3);
      } else {//dump balls into hatch
        this.leftDrive.set(0);
        this.rightDrive.set(0);
        this.intakeDrive.set(-0.5);
      }
    //START MIDDLE, DUMP BALLS TO BACK OF RIGHTMOST SHOOTER ROBOT
    } else if (switch4.get()) {
      if (time - startTime < 1) { //drive backwards enough to cross the starting line
        this.leftDrive.set(-0.3);
        this.rightDrive.set(-0.3);
      } else if (time - startTime < 5) { //turn right
        this.leftDrive.set(0.17);
        this.rightDrive.set(-0.17);
      } else if (time - startTime < 8) { //drive forwards to back of shooter robot
        this.leftDrive.set(0.3);
        this.leftDrive.set(0.3);
      } else if (time - startTime < 9) { //turn left to face robot
        this.leftDrive.set(-0.17);
        this.rightDrive.set(0.17);
      } else if (time - startTime < 9.3) { //drive forwards enough to close gap
        this.leftDrive.set(0.3);
        this.rightDrive.set(0.3);
      } else if (time - startTime < 13) { //dump balls
        this.leftDrive.set(0);
        this.rightDrive.set(0);
        this.intakeDrive.set(-0.5);
      } else{ //back up
        this.leftDrive.set(-0.3);
        this.rightDrive.set(-0.3);
      }
    //START LEFTMOST, DUMP BALLS TO THE BACK OF RIGHTMOST SHOOTER ROBOT 
    } else if (switch5.get()) {
      if (time - startTime < 1) { //drive backwards enough to cross the line
        this.leftDrive.set(-0.3);
        this.rightDrive.set(-0.3);
      } else if (time - startTime < 2) { //turn right
        this.leftDrive.set(0.17);
        this.rightDrive.set(-0.17);
      } else if (time - startTime < 19) { //drive forwards to the back of robot
        this.leftDrive.set(0.3);
        this.rightDrive.set(0.3);
      } else if (time - startTime < 11.5) { //turn left to face the robot
        this.leftDrive.set(-0.17);
        this.rightDrive.set(0.17);
      } else if (time - startTime < 11.75) { //drive forwards enough to close gap
        this.leftDrive.set(0.3);
        this.rightDrive.set(0.3);
      } else if (time - startTime < 15) { //dump balls
        this.intakeDrive.set(-0.5);
      }
    //JUST DRIVE BACKWARDS TO CROSS THE LINE
    } else if (switch6.get()) {
      if (time - startTime < 2) { //drive backwards
        this.leftDrive.set(-0.3);
        this.rightDrive.set(-0.3);
      }
    } else if (switch7.get()) {

    } else if (switch8.get()) {

    } else if (switch9.get()) {

    }
  }
}

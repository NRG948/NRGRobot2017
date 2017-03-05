package org.usfirst.frc.team948.utilities;

import java.util.ArrayList;
//import edu.wpi.first.wpilibj.Timer;
import java.util.Timer;
import java.util.TimerTask;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team948.robot.Robot;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NewVisionProc {
	private static final double TAPE_WIDTH_INCHES = 2.0;
	private static final double INITIAL_DISTANCE = 32.6;
	private static final double INITIAL_HEIGHT = 26.0;
	private static final double INITIAL_WIDTH = 10.5;
	private static final double initialX = 39.5;
	private static final double initialGamma = ((-5) * Math.PI) / 180;
	private ThreadOut gotten;
	private VisionField lastOut;

	private Timer proccessingTimer;
	private CvSink cvSink;
	private CvSource vidOut;
	private TempGripPipeTwo pipeLine;
	private Mat mat;
	Thread processingThread;
	volatile ThreadOut threadObjectData;

	public NewVisionProc() {
	}

	public NewVisionProc start() {
		gotten = new ThreadOut();
		lastOut = new VisionField();
		threadObjectData = new ThreadOut();
		cvSink = CameraServer.getInstance().getVideo();
		vidOut = CameraServer.getInstance().putVideo("Processed", 640, 480);
		mat = new Mat();
		pipeLine = new TempGripPipeTwo();
		proccessingTimer = new Timer();
		proccessingTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				if (cvSink.grabFrame(mat) == 0) {
					vidOut.notifyError(cvSink.getError());
					return;
				}
				pipeLine.process(mat);
				ArrayList<MatOfPoint> cameraIn = pipeLine.findContoursOutput();
				int cont = cameraIn.size();
				int kprime = 0;
				int k = 0;
				double secondMaxSize = 0;
				double maxSize = 0;
				for (int i = 0; i < cont; i++) {
					MatOfPoint temp0 = cameraIn.get(i);
					Rect temp1 = Imgproc.boundingRect(temp0);
					if (temp1.height * temp1.width >= maxSize) {
						kprime = k;
						secondMaxSize = maxSize;
						k = i;
						maxSize = temp1.height * temp1.width;
					}
				}
				if (maxSize > 0) {
					MatOfPoint l = cameraIn.get(k);
					Rect j = Imgproc.boundingRect(l);
					Imgproc.rectangle(mat, j.br(), j.tl(), new Scalar(255, 255, 255), 1);
					ThreadOut temp = new ThreadOut();
					boolean boool = false;
					temp.hasData = true;
					temp.area = l.size().area();
					temp.rectWidth = j.width;
					temp.rectHeight = j.height;
					temp.x = (j.tl().x + j.br().x) / 2;
					temp.y = (j.tl().y + j.br().y) / 2;
					temp.frameWidth = mat.width();
					if (secondMaxSize > 0) {
						boool = true;
						MatOfPoint lprime = cameraIn.get(kprime);
						Rect jprime = Imgproc.boundingRect(lprime);
						ThreadOut nestedTemp = new ThreadOut();
						nestedTemp.hasData = true;
						nestedTemp.area = lprime.size().area();
						nestedTemp.rectWidth = jprime.width;
						nestedTemp.rectHeight = jprime.height;
						nestedTemp.x = (jprime.tl().x + jprime.br().x) / 2;
						nestedTemp.y = (jprime.tl().y + jprime.br().y) / 2;
						nestedTemp.frameWidth = mat.width();
						Imgproc.rectangle(mat, jprime.br(), jprime.tl(), new Scalar(255, 0, 0), 1);
						temp.secondValue = nestedTemp;
						temp.hasSecond = true;
					}
					long end = System.currentTimeMillis();
					long delta = end - start;
					if (boool)
						temp.secondValue.proccessTime = delta;
					temp.proccessTime = delta;
					setFrameData(temp);
				}
				vidOut.putFrame(mat);
			}
		}, 0, 20);
		return this;
	}

	private double rectDistance(ThreadOut in) {
		return INITIAL_DISTANCE * INITIAL_HEIGHT / in.rectHeight;
	}

	private double getTheta(ThreadOut in) {
		double uW = (in.rectHeight / INITIAL_HEIGHT) * INITIAL_WIDTH;
		double theta = Math.acos(in.rectWidth / uW);
		if (in.hasSecond) {
			theta = Math.copySign(theta, in.x - in.secondValue.x);
		}
		return theta;
	}

	private double getCenterDistance(ThreadOut in, double theta) {
		double width = in.hasSecond ? TAPE_WIDTH_INCHES : (INITIAL_WIDTH * INITIAL_DISTANCE) / rectDistance(in);
		return rectDistance(in) + (Math.tan(theta) * (width / 2.0));
	}

	private double getHeadingOffset(ThreadOut in, double theta) {
		double x = getTargetX(in);
		double wF = in.frameWidth;
		double epsilon = x - (wF / 2.0);
		// 2.0 is width in inches of tape
		double gamma = Math.atan((epsilon * 2.0) / (INITIAL_DISTANCE * INITIAL_WIDTH));
		return gamma;
	}

	private double simpleHeading(ThreadOut in) {
		double x = getTargetX(in);
		double wF = in.frameWidth;
		double epsilon = x - (wF / 2.0);
		double zeta = epsilon / (wF / 2.0);
		return zeta;
	}

	private double getOmega(ThreadOut in, double centerDistance) {
		double x = getTargetX(in);
		double wF = in.frameWidth;
		double epsilon = x - (wF / 2);
		double initialEpsilon = initialX - (wF / 2);
		double tanGam = (epsilon / initialEpsilon) * Math.tan(initialGamma);
		double omega = tanGam * centerDistance;
		return omega;
	}

	private double getTargetX(ThreadOut in) {
		return in.hasSecond ? (in.x + in.secondValue.x) / 2.0 : in.x;
	}

	public boolean dataExists() {
		ThreadOut temp = getFrameData();
		if (temp.hasData) {
			gotten = temp;
			SmartDashboard.putNumber("visionArea", temp.area);
			SmartDashboard.putNumber("visionFrameWidth", temp.frameWidth);
			SmartDashboard.putNumber("visionRectHeight", temp.rectHeight);
			SmartDashboard.putNumber("visionRectWidth", temp.rectWidth);
			SmartDashboard.putNumber("visionX", temp.x);
			SmartDashboard.putNumber("visionY", temp.y);
			SmartDashboard.putNumber("visionProccessTime", temp.proccessTime);
			SmartDashboard.putNumber("Distance to target", rectDistance(temp));
			SmartDashboard.putNumber("Angle to peg center", Robot.visionProcessor.getData().theta * 180 / Math.PI);
			return true;
		}
		return false;
	}

	public VisionField getData() {
		VisionField field = new VisionField();
		if (gotten.hasData) {
			field.theta = getTheta(gotten);
			field.zeta = simpleHeading(gotten);
			field.omega = getOmega(gotten, field.v);
			if (gotten.hasSecond) {
				field.v = getCenterDistance(gotten, Math.abs(field.theta));
				field.gamma = getHeadingOffset(gotten, Math.abs(field.theta));
				field.isTape = false;
			} else {
				field.v = getCenterDistance(gotten, field.theta);
				field.gamma = getHeadingOffset(gotten, field.theta);
				field.isTape = true;
			}
			lastOut = field;
		} else if (!lastOut.equals(new ThreadOut())) {
			return lastOut;
		}
		return field;
	}

	public synchronized void setFrameData(ThreadOut in) {
		threadObjectData = in;
	}

	public synchronized ThreadOut getFrameData() {
		return threadObjectData;
	}

	private class ThreadOut {
		public double area;
		public double rectWidth;
		public double rectHeight;
		public double frameWidth;
		public double x;
		public double y;
		public ThreadOut secondValue;
		public long proccessTime;
		public boolean hasSecond = false;
		public boolean hasData = false;
	}
}

package org.usfirst.frc.team948.utilities;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team948.robot.Robot;
import org.usfirst.frc.team948.utilities.TempGripPipe;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.CameraServer;
//import edu.wpi.first.wpilibj.Timer;
import java.util.Timer;
import java.util.TimerTask;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionProc {
	private static final double initialDistance = 32.6;
	private static final double initialHeight = 26.0;
	private static final double initialWidth = 10.5;
	private static final double initialX = 39.5;
	private static final double initialGamma = ((-5) * Math.PI) / 180;
	private ThreadOut gotten;
	private VisionField lastOut;

	private Timer proccessingTimer;
	private CvSink cvSink;
	private CvSource vidOut;
	private TempGripPipe pipeLine;
	private Mat mat;
	Thread processingThread;
	ThreadOut threadObjectData;

	public VisionProc() {
	}

	public VisionProc start() {
		gotten = new ThreadOut();
		lastOut = new VisionField();
		threadObjectData = new ThreadOut();
		cvSink = CameraServer.getInstance().getVideo();
		vidOut = CameraServer.getInstance().putVideo("Processed", 640, 480);
		mat = new Mat();
		pipeLine = new TempGripPipe();
		proccessingTimer = new Timer();
		proccessingTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				if (cvSink.grabFrame(mat) == 0) {
					vidOut.notifyError(cvSink.getError());
				} else {
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
				}
				vidOut.putFrame(mat);
			}
		}, 0, 10);
		return this;
	}

	private double rectDistance(ThreadOut in) {
		if (in.hasData) {
			double H = in.rectHeight;
			return (initialHeight * initialDistance) / H;
		}
		return (Double) null;
	}

	private double getThetaSingleTape(ThreadOut in, boolean peg) {
		if (in.hasData && peg) {
			if (in.hasSecond) {
				double W = in.rectWidth;
				double H = in.rectHeight;
				double uW = (H / initialHeight) * initialWidth;
				double theta = Math.acos(W / uW);
				theta = Math.copySign(theta, in.x - in.secondValue.x);
				return theta;
			}
		} else if (in.hasData) {
			double W = in.rectWidth;
			double H = in.rectHeight;
			double uW = (H / initialHeight) * initialWidth;
			double theta = Math.acos(W / uW);
			return theta;
		}
		return (Double) null;
	}

	private double getCenterDistance(ThreadOut in, double theta, boolean peg) {
		if (in.hasData && peg) {
			if (in.hasSecond) {
				double closestDistance = rectDistance(in);
				return closestDistance + (Math.tan(theta) * (2.0 / 2.0));
			}
		} else if (in.hasData) {
			double closestDistance = rectDistance(in);
			double W = in.rectWidth;
			return closestDistance + (Math.tan(theta) * (W / 2.0));
		}
		return (Double) null;
	}

	private double getHeadingOffeset(threadOut in, double theta, boolean peg){
		if(in.hasData && peg){
			if(in.hasSecond){
				double x = (in.x + in.secondValue.x)/2.0;
				double wF = in.frameWidth;
				double epsilon = x - (wF/2.0);
//				2.0 is width in inches of tape
				double gamma = Math.atan((epsilon*2.0)/(initialDistance*initialWidth));
				return gamma;
			}
		}else if(in.hasData){
			double x = in.x;
			double wF = in.frameWidth;
			double epsilon = x - (wF/2.0);
//			2.0 is width in inches of tape
			double gamma = Math.atan((epsilon*2.0)/(initialDistance*initialWidth));
			return gamma;
		}
		return (Double) null;
	}

	private double simpleHeading(ThreadOut in, boolean peg) {
		if (in.hasData && peg) {
			if (in.hasSecond) {
				double x = (in.x + in.secondValue.x) / 2.0;
				double wF = in.frameWidth;
				double epsilon = x - (wF / 2.0);
				double zeta = epsilon / (wF / 2.0);
				return zeta;
			}
		} else if (in.hasData) {
			double x = in.x;
			double wF = in.frameWidth;
			double epsilon = x - (wF / 2.0);
			double zeta = epsilon / (wF / 2.0);
			return zeta;
		}
		return (Double) null;
	}

	private double getOmega(ThreadOut in, double centerDistance, boolean peg) {
		if (in.hasData && peg) {
			if (in.hasSecond) {
				double x = (in.x + in.secondValue.x) / 2.0;
				double wF = in.frameWidth;
				double epsilon = x - (wF / 2);
				double initialEpsilon = initialX - (wF / 2);
				double tanGam = (epsilon / initialEpsilon) * Math.tan(initialGamma);
				double omega = tanGam * centerDistance;
				return omega;
			}
		} else if (in.hasData) {
			double x = in.x;
			double wF = in.frameWidth;
			double epsilon = x - (wF / 2);
			double initialEpsilon = initialX - (wF / 2);
			double tanGam = (epsilon / initialEpsilon) * Math.tan(initialGamma);
			double omega = tanGam * centerDistance;
			return omega;
		}
		return (Double) null;
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
			SmartDashboard.putNumber("Angle to peg center", Robot.visionProcessor.getData().theta);
			return true;
		}
		return false;
	}

	public VisionField getData() {
		if (gotten.hasData) {
			if (gotten.hasSecond) {
				VisionField out = new VisionField();
				out.theta = getThetaSingleTape(gotten, true);
				out.v = getCenterDistance(gotten, Math.abs(out.theta), true);
				out.zeta = simpleHeading(gotten, true);
				out.omega = getOmega(gotten, out.v, true);
				out.gamma = getHeadingOffeset(gotten, Math.abs(out.theta), true);
				out.isTape = false;
				lastOut = out;
				return out;
			} else {
				VisionField out = new VisionField();
				out.theta = getThetaSingleTape(gotten, false);
				out.v = getCenterDistance(gotten, out.theta, false);
				out.zeta = simpleHeading(gotten, false);
				out.omega = getOmega(gotten, out.v, false);
				out.gamma = getHeadingOffeset(gotten, out.theta, false);
				out.isTape = true;
				lastOut = out;
				return out;
			}
		} else if (!lastOut.equals(new ThreadOut())) {
			return lastOut;
		}
		return new VisionField();
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

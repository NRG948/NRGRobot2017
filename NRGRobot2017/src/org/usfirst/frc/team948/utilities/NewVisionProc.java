package org.usfirst.frc.team948.utilities;

import java.util.ArrayList;
import java.util.Collections;
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
	private static final Scalar WHITE_COLOR = new Scalar(255, 255, 255);
	private static final Scalar BLUE_COLOR = new Scalar(255, 0, 0);
	private static final double TAPE_HEIGHT_INCHES = 5;
	private static final double TAPE_WIDTH_INCHES = 2.0;
	private static final double TAPE_TO_TAPE_OUTER_WIDTH_INCHES = 10.25;
	private static final double TAPE_CENTER_TO_CENTER_INCHES = 8.25;

	// These values were empirically measured from the robot camera

	private static final double TAPE_DISTANCE_INCHES = 32.6;
	private static final double TAPE_HEIGHT_PIXELS = 26.0;
	private static final double TAPE_WIDTH_PIXELS = TAPE_HEIGHT_PIXELS * TAPE_WIDTH_INCHES / TAPE_HEIGHT_INCHES;
	private static final double TAPE_SEPERATION_PIXELS = 69.0;
	private static final double SEPERATION_DISTANCE_INCHES = 14.5;
	private static final double initialX = 39.5;
	private static final double initialGamma = ((-5) * Math.PI) / 180;

	private static final double AREA_THRESHOLD = 20;
	private ProcessedImage lastGoodImage;
	private VisionField lastField;
	private boolean isProcessingEnabled = true;

	private Timer processingTimer;
	private CvSink cvSink;
	private CvSource vidOut;
	private TempGripPipeTwo pipeLine;
	private Mat mat;
	Thread processingThread;
	volatile ProcessedImage threadObjectData;

	public NewVisionProc() {
	}

	public NewVisionProc start() {
		lastGoodImage = new ProcessedImage();
		lastField = new VisionField();
		threadObjectData = new ProcessedImage();
		cvSink = CameraServer.getInstance().getVideo();
		vidOut = CameraServer.getInstance().putVideo("Processed",
				/*
				 * Robot.CAMERA_RESOLUTION_WIDTH, Robot.CAMERA_RESOLUTION_HEIGHT
				 */ 640, 480);
		mat = new Mat();
		pipeLine = new TempGripPipeTwo();
		processingTimer = new Timer();
		processingTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				long start = System.nanoTime();
				if (cvSink.grabFrame(mat) == 0) {
					vidOut.notifyError(cvSink.getError());
					return;
				}
				
				if (!isProcessingEnabled) {
					vidOut.putFrame(mat);
					return;
				}
				pipeLine.process(mat);
				ArrayList<MatOfPoint> cameraIn = pipeLine.findContoursOutput();
				int numOfContoursSeen = cameraIn.size();
				ProcessedImage image = new ProcessedImage();
				image.frameWidth = mat.width();
				for (int i = 0; i < numOfContoursSeen; i++) {
					MatOfPoint matrix = cameraIn.get(i);
					Contour contour = new Contour(matrix);
					if (contour.area > AREA_THRESHOLD) {
						if (contour.boundingRect.br().y < mat.height() - 5) {
							image.contours.add(contour);
						}
					}
				}

				if (image.contours.size() > 0) {
					Collections.sort(image.contours);
					Rect rect = image.contours.get(0).boundingRect;
					Imgproc.rectangle(mat, rect.br(), rect.tl(), WHITE_COLOR, 1);
					if (image.contours.size() > 1) {
						rect = image.contours.get(1).boundingRect;
						Imgproc.rectangle(mat, rect.br(), rect.tl(), BLUE_COLOR, 1);
					}
					image.processTime = (System.nanoTime() - start) / 1000000;
					setFrameData(image);
				}
				vidOut.putFrame(mat);
			}
		}, 0, 10);
		return this;
	}

	private double rectDistance(ProcessedImage image) {
		if (image.contours.size() > 1 && image.contours.get(0).topY <= 2) {
			Contour c1 = image.contours.get(0);
			Contour c2 = image.contours.get(1);
			double pixelDistance = Math.min(Math.abs(c1.leftX - c2.rightX), Math.abs(c1.rightX - c2.leftX));
			if (SmartDashboardGroups.VISION_DATA) SmartDashboard.putNumber("Pixel Seperation", pixelDistance);
			return TAPE_SEPERATION_PIXELS * SEPERATION_DISTANCE_INCHES / pixelDistance;
		}
		return TAPE_DISTANCE_INCHES * TAPE_HEIGHT_PIXELS / image.contours.get(0).height;
	}

	private double getTheta(ProcessedImage image) {
		Contour c1 = image.contours.get(0);
		double theta;
		if (image.contours.size() > 1) {
			Contour c2 = image.contours.get(1);
			double outerEdgeToOuterEdgeWidth = Math.abs(c1.centerX - c2.centerX) + (c1.width - c2.width) / 2;
			double straightOnWidth = (c1.height / TAPE_HEIGHT_INCHES) * TAPE_TO_TAPE_OUTER_WIDTH_INCHES;
			theta = Math.acos(Math.min(1.0, outerEdgeToOuterEdgeWidth / straightOnWidth));
			// TODO verify the validity of the formula.
			theta = Math.copySign(theta, c1.centerX - c2.centerX);
		} else {
			double straightOnWidth = (c1.height / TAPE_HEIGHT_PIXELS) * TAPE_WIDTH_PIXELS;
			theta = Math.acos(Math.min(1.0, c1.width / straightOnWidth));
		}
		return theta;
	}

	public Point2D getRobotLocation(ProcessedImage image) {
		if (image.contours.size() > 1) {
			double r1 = TAPE_DISTANCE_INCHES * TAPE_HEIGHT_PIXELS / image.contours.get(0).height;
			double r2 = TAPE_DISTANCE_INCHES * TAPE_HEIGHT_PIXELS / image.contours.get(1).height;
			double x2 = TAPE_CENTER_TO_CENTER_INCHES;
			double x = (r1 * r1 - r2 * r2 + x2 * x2) / 2 / x2;
			double y = Math.sqrt(r1 * r1 - x * x);
			Point2D robotLocation = new Point2D(x, y);
			SmartDashboard.putString("Robot Location", robotLocation.toString());
			return robotLocation;
		}
		return null;
	}

	private double getCenterDistance(ProcessedImage image, double theta) {
		double width = image.contours.size() > 1 ? TAPE_WIDTH_INCHES
				: (TAPE_WIDTH_PIXELS * TAPE_DISTANCE_INCHES) / rectDistance(image);
		return rectDistance(image) + (Math.tan(theta) * (width / 2.0));
	}

	private double getHeadingOffset(ProcessedImage in, double theta) {
		double x = getTargetX(in);
		double wF = in.frameWidth;
		double epsilon = x - (wF / 2.0);
		// 2.0 is width in inches of tape
		double gamma = Math.atan((epsilon * 2.0) / (TAPE_DISTANCE_INCHES * TAPE_WIDTH_PIXELS));
		return gamma;
	}

	private double simpleHeading(ProcessedImage in) {
		double x = getTargetX(in);
		double wF = in.frameWidth;
		double epsilon = x - (wF / 2.0);
		double zeta = epsilon / (wF / 2.0);
		return zeta;
	}

	private double getOmega(ProcessedImage in, double centerDistance) {
		double x = getTargetX(in);
		double wF = in.frameWidth;
		double epsilon = x - (wF / 2);
		double initialEpsilon = initialX - (wF / 2);
		double tanGam = (epsilon / initialEpsilon) * Math.tan(initialGamma);
		double omega = tanGam * centerDistance;
		return omega;
	}

	private double getTargetX(ProcessedImage image) {
		double x1 = image.contours.get(0).centerX;
		return image.contours.size() > 1 ? (x1 + image.contours.get(1).centerX) / 2.0 : x1;
	}

	public boolean dataExists() {
		ProcessedImage image = getFrameData();
		if (image.contours.size() > 0) {
			lastGoodImage = image;
			Contour c1 = image.contours.get(0);
			if (SmartDashboardGroups.VISION_DATA) {
				SmartDashboard.putNumber("visionArea", c1.area);
				SmartDashboard.putNumber("visionRectHeight", c1.height);
				SmartDashboard.putNumber("visionRectWidth", c1.width);
				SmartDashboard.putNumber("visionX", c1.centerX);
				SmartDashboard.putNumber("visionY", c1.centerX);
				SmartDashboard.putNumber("visionFrameWidth", image.frameWidth);
				SmartDashboard.putNumber("visionProcessTime", image.processTime);
				SmartDashboard.putNumber("Distance to target", rectDistance(image));
			}
			SmartDashboard.putNumber("Angle to peg center", Robot.visionProcessor.getData().theta * 180 / Math.PI);
			return true;
		}
		return false;
	}

	public VisionField getData() {
		if (lastGoodImage.contours.size() > 0) {
			VisionField field = new VisionField();
			getRobotLocation(lastGoodImage);
			field.theta = getTheta(lastGoodImage);
			field.zeta = simpleHeading(lastGoodImage);
			field.omega = getOmega(lastGoodImage, field.v);
			field.distanceToTarget = rectDistance(lastGoodImage);
			if (lastGoodImage.contours.size() > 1) {
				field.v = getCenterDistance(lastGoodImage, Math.abs(field.theta));
				field.gamma = getHeadingOffset(lastGoodImage, Math.abs(field.theta));
				field.isTape = false;
			} else {
				field.v = getCenterDistance(lastGoodImage, field.theta);
				field.gamma = getHeadingOffset(lastGoodImage, field.theta);
				field.isTape = true;
			}
			lastField = field;
			return field;
		}
		return lastField;
	}

	public synchronized void setFrameData(ProcessedImage in) {
		threadObjectData = in;
	}

	public synchronized ProcessedImage getFrameData() {
		return threadObjectData;
	}

	public synchronized void disableProcessing() {
		isProcessingEnabled = false;
	}
	
	public synchronized void enableProcessing() {
		isProcessingEnabled = true;
	}

	private class ProcessedImage {
		public ArrayList<Contour> contours = new ArrayList<Contour>();
		public double frameWidth;
		public long processTime;
	}

	private class Contour implements Comparable<Contour> {
		public double area;
		public double width;
		public double height;
		public double centerX;
		public double rightX;
		public double topY;
		public double leftX;
		public Rect boundingRect;

		public Contour(MatOfPoint mat) {
			boundingRect = Imgproc.boundingRect(mat);
			width = boundingRect.width;
			height = boundingRect.height;
			area = width * height;
			centerX = (boundingRect.tl().x + boundingRect.br().x) / 2;
			leftX = boundingRect.tl().x;
			topY = boundingRect.tl().y;
			rightX = boundingRect.br().x;
		}

		public int compareTo(Contour ci) {
			return (int) Math.signum(ci.area - area);
		}
	}
}

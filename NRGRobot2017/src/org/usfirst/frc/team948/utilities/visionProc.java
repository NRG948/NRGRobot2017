package org.usfirst.frc.team948.utilities;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team948.utilities.tempPipe;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class visionProc {
	private static final double initialDistance = 32.6;
	private static final double initialHeight = 26.0;
	private static final double initialWidth = 10.5;
	private static final double initialX = 39.5;
	private static final double initialGamma = ((-5)*Math.PI)/180;
	private double[] gotten = new double[6];
	private double[] lastOut = new double[5];
	private boolean hasFrame = false;
	private boolean hasRun = false;
	Thread processingThread;
	ConcurrentLinkedDeque<ArrayDeque<double[]>> objects;
	public visionProc(){}
	
	public visionProc start(){
		objects = new ConcurrentLinkedDeque<ArrayDeque<double[]>>();
		processingThread = new Thread(() -> {
			tempPipe pipeLine = new tempPipe();
			CvSink cvSink = CameraServer.getInstance().getVideo();
			CvSource out = CameraServer.getInstance().putVideo("Processed", 640, 480);
			Mat mat = new Mat();
			Timer timer = new Timer();
			timer.start();
			cvSink.grabFrame(mat);
			while (!Thread.interrupted()) {
				if(timer.get() > 0.002){
					if (cvSink.grabFrame(mat) == 0) {
						out.notifyError(cvSink.getError());
						continue;
					}
					ArrayDeque<double[]> output = new ArrayDeque<double[]>();
					pipeLine.process(mat);
					ArrayList<MatOfPoint> cameraIn = pipeLine.findContoursOutput();
					int cont = cameraIn.size();
					double[] properties = new double[6];
					int k = 0;
					for(int i = 0; i < cont;i++){
						MatOfPoint temp0 = cameraIn.get(i);
						Rect temp1 = Imgproc.boundingRect(temp0);
						if(i != 0){
							if(temp1.area() > properties[0]*properties[1]){
								k= i;
								properties[2] = temp0.size().area();
								properties[1] = temp1.height;
								properties[0] = temp1.width;
								properties[3] = (temp1.tl().x + temp1.br().x)/2;
								properties[4] = (temp1.tl().y + temp1.br().y)/2;
							}
						}else{
							properties[2] = temp0.size().area();
							properties[1] = temp1.height;
							properties[0] = temp1.width;
							properties[3] = (temp1.tl().x + temp1.br().x)/2;
							properties[4] = (temp1.tl().y + temp1.br().y)/2;
							properties[5] = mat.width();
						}
					}
					if(cont > 0){
						Rect j = Imgproc.boundingRect(cameraIn.get(k));
						Imgproc.rectangle(mat, j.br(), j.tl(), new Scalar(255, 255, 255), 1);
						output.offerFirst(properties);
					}
					objects.addFirst(output);
//					System.out.println(objects.size());
					timer.reset();
				}
				out.putFrame(mat);
			}
		});
		processingThread.setDaemon(true);
		processingThread.start();
		return this;
	}
	
	private double rectDistance(double[] in){
		double H = in[1];
		return (initialHeight*initialDistance)/H;
	}
	
	private double getThetaSingleTape(double[] in){
		double W = in[0];
		double H = in[1];
		double uW = (H/initialHeight)*initialWidth;
		double theta = Math.acos(W/uW);
		return theta;
	}
	
	private double getCenterDistance(double[] in, double theta){
		double closestDistance = rectDistance(in);
		double W = in[0];
		return closestDistance + (Math.tan(theta)*W/2);
	}
	
	private double getHeadingOffeset(double[] in, double theta){
		double x = in[3];
		double wF = in[5];
		double epsilon = x - (wF/2);
		double initialEpsilon = initialX - (wF/2);
		double gamma = Math.atan((epsilon/initialEpsilon)*Math.tan(initialGamma));
		return gamma;
	}
	
	private double simpleHeading(double[] in){
		double x = in[3];
		double wF = in[5];
		double epsilon = x - (wF/2);
		double zeta = Math.abs(epsilon) > (1/40.0)*wF ? Math.copySign(1.0, epsilon) : 0;
		return zeta;
	}
	
	private double getOmega(double [] in, double centerDistance){
		double x = in[3];
		double wF = in[5];
		double epsilon = x - (wF/2);
		double initialEpsilon = initialX - (wF/2);
		double tanGam = (epsilon/initialEpsilon)*Math.tan(initialGamma);
		double omega = tanGam*centerDistance;
		return omega;
	}
	
	public boolean dataExists(){
		if(hasRun || objects.size() > 0){
			hasRun = true;
			ArrayDeque<double[]> a = objects.peekFirst();
			if(a.size() > 0){
				gotten = a.peekFirst();
				hasFrame = true;
				return true;
			}
		}
		hasFrame = false;
		return false;
	}
	
	public visionField getData(){
		if(hasFrame){
			double[] out = new double[5];
			double theta = getThetaSingleTape(gotten);
			double v = getCenterDistance(gotten, theta);
			double zeta = simpleHeading(gotten);
			double omega = getOmega(gotten, v);
			double gamma = getHeadingOffeset(gotten, theta);
			out[0] = theta;
			out[1] = v;
			out[2] = zeta;
			out[3] = omega;
			out[4] = gamma;
			lastOut = out;
			return new visionField(out);
		}else if(!lastOut.equals(new double[5])){
			return new visionField(lastOut);
		}
		return new visionField();
	}
	
}

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
	private threadOut gotten;
	private visionField lastOut;
	Thread processingThread;
	threadOut threadObjectData;
	
	public visionProc(){}
	
	public visionProc start(){
		gotten = new threadOut();
		lastOut = new visionField();
		threadObjectData = new threadOut();
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
					pipeLine.process(mat);
					ArrayList<MatOfPoint> cameraIn = pipeLine.findContoursOutput();
					int cont = cameraIn.size();
					int k = 0;
					double maxSize = 0;
					for(int i = 0; i < cont;i++){
						MatOfPoint temp0 = cameraIn.get(i);
						Rect temp1 = Imgproc.boundingRect(temp0);
						if(temp1.height*temp1.width > maxSize){
							k = i;
							maxSize = temp1.height*temp1.width;
						}
					}
					if(maxSize > 0){
						MatOfPoint l = cameraIn.get(k);
						Rect j = Imgproc.boundingRect(l);
						Imgproc.rectangle(mat, j.br(), j.tl(), new Scalar(255, 255, 255), 1);
						threadOut temp = new threadOut();
						temp.hasData = true;
						temp.area = l.size().area();
						temp.rectWidth = j.width;
						temp.rectHeight = j.height;
						temp.x = (j.tl().x + j.br().x)/2;
						temp.y = (j.tl().y + j.br().y)/2;
						temp.frameWidth = mat.width();
						setFrameData(temp);
					}
					timer.reset();
				}
				out.putFrame(mat);
			}
		});
		processingThread.setDaemon(true);
		processingThread.start();
		return this;
	}
	
	private double rectDistance(threadOut in){
		if(in.hasData){
			double H = in.rectHeight;
			return (initialHeight*initialDistance)/H;
		}
		return (Double) null;
	}
	
	private double getThetaSingleTape(threadOut in){
		if(in.hasData){
			double W = in.rectWidth;
			double H = in.rectHeight;
			double uW = (H/initialHeight)*initialWidth;
			double theta = Math.acos(W/uW);
			return theta;
		}
		return (Double) null;
	}
	
	private double getCenterDistance(threadOut in, double theta){
		if(in.hasData){
			double closestDistance = rectDistance(in);
			double W = in.rectWidth;
			return closestDistance + (Math.tan(theta)*W/2);
		}
		return (Double) null;
	}
	
	private double getHeadingOffeset(threadOut in, double theta){
		if(in.hasData){
			double x = in.x;
			double wF = in.frameWidth;
			double epsilon = x - (wF/2);
			double initialEpsilon = initialX - (wF/2);
			double gamma = Math.atan((epsilon/initialEpsilon)*Math.tan(initialGamma));
			return gamma;
		}
		return (Double) null;
	}
	
	private double simpleHeading(threadOut in){
		if(in.hasData){
			double x = in.x;
			double wF = in.frameWidth;
			double epsilon = x - (wF/2);
			double zeta = epsilon/(wF/2);
			return zeta;
		}
		return (Double) null;
	}
	
	private double getOmega(threadOut in, double centerDistance){
		if(in.hasData){
			double x = in.x;
			double wF = in.frameWidth;
			double epsilon = x - (wF/2);
			double initialEpsilon = initialX - (wF/2);
			double tanGam = (epsilon/initialEpsilon)*Math.tan(initialGamma);
			double omega = tanGam*centerDistance;
			return omega;
		}
		return (Double) null;
	}
	
	public boolean dataExists(){
		threadOut temp = getFrameData();
		if(temp.hasData){
			gotten = temp;
			return true;
		}
		return false;
	}
	
	public visionField getData(){
		if(gotten.hasData){
			visionField out = new visionField();
			out.theta = getThetaSingleTape(gotten);
			out.v = getCenterDistance(gotten, out.theta);
			out.zeta = simpleHeading(gotten);
			out.omega = getOmega(gotten, out.v);
			out.gamma = getHeadingOffeset(gotten, out.theta);
			lastOut = out;
			return out;
		}else if(!lastOut.equals(new threadOut())){
			return lastOut;
		}
		return new visionField();
	}
	
	public synchronized void setFrameData(threadOut in){
		threadObjectData = in;
	}
	
	public synchronized threadOut getFrameData(){
		return threadObjectData;
	}
	
	private class threadOut{
		public double area;
		public double rectWidth;
		public double rectHeight;
		public double frameWidth;
		public double x;
		public double y;
		boolean hasData = false;
	}
}
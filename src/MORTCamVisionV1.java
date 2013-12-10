import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.CvFont;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import static com.googlecode.javacv.cpp.opencv_imgproc.*;


public class MORTCamVisionV1{
	static CvFont font=new CvFont(CV_FONT_HERSHEY_PLAIN,1,1);
	public static void main(String[] args)throws com.googlecode.javacv.FrameGrabber.Exception{
		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);	
		final CanvasFrame canvas = new CanvasFrame("MORTCamVision1");
		canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		final CanvasFrame canvas2 = new CanvasFrame("MORTCamVision2");
		canvas2.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		grabber.start();		
		while(true){
			try{
				IplImage img=grabber.grab();
				//IplImage img=cvLoadImage("square.png");
				cvFlip(img,img,1);				
				cvSmooth(img,img,CV_BLUR,3);
				IplImage grayImg=IplImage.create(img.width(), img.height(),IPL_DEPTH_8U,1);				
				cvCvtColor(img,grayImg,CV_BGR2GRAY);
				cvThreshold(grayImg,grayImg,235,255,CV_THRESH_BINARY);
				if(grayImg!=null){
					int corner1=getTopCorner(grayImg);
					int corner1X=indexToX(corner1,grayImg.width());
					int yVal=indexToY(corner1,grayImg.width());											
					CvPoint point=new CvPoint(corner1X,yVal);					
					cvPutText(grayImg,"C",point,font,CvScalar.CYAN);
				}
				drawHair(grayImg);
				canvas.showImage(grayImg);
				canvas2.showImage(img);								
			}catch(Exception e){System.out.println(e);} 
		}
	}	
	public static int getTopCorner(IplImage src){	
		int width=src.width();
		int height=src.height();
		int size=width*height;
		CvMat imgMat=src.asCvMat();				
			for(int i=0;i<size;i++){
				double val=imgMat.get(i);
				if(val>=245){					
					return i;					
				}
			}
		return 0;
	}
	public static int indexToY(int index,int width){		
		int yVal=index/width;
		return (yVal+1);		
	}
	public static int indexToX(int index,int width){
			int xVal=index%(width);			
			return(xVal);
	}
	public static void drawHair(IplImage src){
		int width=src.width();
		int height=src.height();
		int size=width*height;
		CvMat mat=src.asCvMat();
		int start=(size/2)-(width/2)-6;
		for(int j=0;j<12;j++){
			mat.put(start,255);
			start++;
		}
		}	
}

package org.firstinspires.ftc.teamcode.FreightFrenzy.Systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.collections.EvictingBlockingQueue;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.FreightFrenzy.RouteCreator.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class CameraSystem3 {
    static class CameraPipeline extends OpenCvPipeline {

        private static final Rect leftArea = new Rect(503, 491, 229, 234);
        private static final Rect centerArea = new Rect(903, 484, 300, 218);
        private static final Rect rightArea = new Rect(1376, 463, 249, 232);

        private static final Scalar low_blue = new Scalar(94, 80, 2);
        private static final Scalar high_blue = new Scalar(126, 255, 255);

        private static final Scalar low_red = new Scalar(161, 155, 84);
        private static final Scalar high_red = new Scalar(179, 255, 255);

        private static final int PIXEL_COUNT_THRESHOLD = 2000;

        private boolean isCapturingImage = false;
        private boolean isDetectingTotem = false;
        private final OpMode opMode;

        private final EvictingBlockingQueue<ArmSystem.Floors> floorResult = new EvictingBlockingQueue<ArmSystem.Floors>(new ArrayBlockingQueue<ArmSystem.Floors>(1));

        private final Mat hsvAll = new Mat();
        private final Mat redMask = new Mat();
        private final Mat blueMask = new Mat();
        private final Mat colorMask = new Mat();


        CameraPipeline(OpMode opMode) {
            this.opMode = opMode;
        }

        public void captureImage() {
            isCapturingImage = true;
        }

        public ArmSystem.Floors detectTotem(){
            floorResult.clear();
            isDetectingTotem = true;
            try {
                return floorResult.poll(1000, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        @Override
        public Mat processFrame(Mat input) {
            if (isCapturingImage) {
                isCapturingImage = false;
                String timeStamp = Utils.timestampString();
                String filepath = new File(AppUtil.ROBOT_DATA_DIR, String.format("img_%s.png", timeStamp)).getAbsolutePath();
                saveMatToDiskFullPath(input, filepath);
            }

            Imgproc.cvtColor(input, hsvAll, Imgproc.COLOR_RGB2HSV);
            Core.inRange(hsvAll, low_blue, high_blue, redMask);
            Core.inRange(hsvAll, low_red, high_red, blueMask);
            Core.bitwise_and(redMask, blueMask, );

            if (isDetectingTotem) {
                isDetectingTotem = false;
                Mat left = input.submat(leftArea);
                Mat center = input.submat(centerArea);
                Mat right = input.submat(rightArea);
                boolean leftTotem = hasTotemHsv(left);
                boolean centerTotem = hasTotemHsv(center);
                boolean rightTotem = hasTotemHsv(right);

                ArmSystem.Floors floor;
                if(leftTotem){
                    floor = ArmSystem.Floors.FIRST;
                }else if (centerTotem){
                    floor = ArmSystem.Floors.SECOND;
                }else {
                    floor = ArmSystem.Floors.THIRD;
                }
                floorResult.offer(floor);
            }



            Imgproc.rectangle(input, leftArea, new Scalar(255,0,0), 7);
            Imgproc.rectangle(input, centerArea, new Scalar(255,0,0), 7);
            Imgproc.rectangle(input, rightArea, new Scalar(255,0,0), 7);

            return input;


//            Imgproc.cvtColor(input, grey, Imgproc.COLOR_RGB2GRAY);
//            Imgproc.blur(grey, blurred, new Size(5, 5));
//            Imgproc.Canny(blurred, edges, 25, 50);
//            Core.hconcat(Arrays.asList(grey, edges), combined);
//            Imgproc.resize(combined, combinedResized, input.size(), 0, 0, Imgproc.INTER_CUBIC);
//            return combinedResized;
        }

        private boolean hasTotemHsv(Mat input){
            Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);
            Core.inRange(hsv, low_blue, high_blue, redMask);
            Core.inRange(hsv, low_red, high_red, blueMask);
            int numPixels = Core.countNonZero(redMask) + Core.countNonZero(blueMask);
            return numPixels < PIXEL_COUNT_THRESHOLD;
        }

//        public boolean hasTotem(Mat input) {
//            Imgproc.blur(input, blurred, new Size(5, 5));
//            Imgproc.Canny(blurred, edges, 50, 100);
//            Imgproc.blur(edges, edgesBlurred, new Size(5, 5));
//            Imgproc.findContours(edgesBlurred, countours, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);
//            for (MatOfPoint contour: countours){
//                MatOfPoint2f contour2f = new MatOfPoint2f(contour);
//                MatOfPoint2f aproxCurve = new MatOfPoint2f();
//                double perimeter = Imgproc.arcLength(contour2f, true);
//                Imgproc.approxPolyDP(contour2f, aproxCurve, 0.075 * perimeter, true);
//                double contourArea = Imgproc.contourArea(aproxCurve);
//                if (contourArea > 4000){
//                    return true;
//                }
//            }
//            return false;
//        }


    }

    private final OpMode opMode;
    private final CameraPipeline cameraPipeline;

    public CameraSystem3(OpMode opMode) {
        this.opMode = opMode;
        cameraPipeline = new CameraPipeline(opMode);
        startCamera();
    }

    private void startCamera() {
        int cameraMonitorViewId = opMode.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", opMode.hardwareMap.appContext.getPackageName());
        WebcamName webcamName = opMode.hardwareMap.get(WebcamName.class, "webcam");
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(1920, 1080, OpenCvCameraRotation.UPRIGHT);
                camera.setPipeline(cameraPipeline);
            }

            @Override
            public void onError(int errorCode) {
                throw new RuntimeException("Camera failed with code: " + errorCode);
            }
        });
    }

    public void captureImage() {
        cameraPipeline.captureImage();
    }

    public ArmSystem.Floors detectTotem(){
        return cameraPipeline.detectTotem();
    }
}

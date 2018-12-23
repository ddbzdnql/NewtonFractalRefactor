import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class NewtonFractal {
    private static Complex calculate(int[] coef, Complex val){
        Complex toRet = new Complex(0,0);
        for (int i = 0; i < coef.length; i++){
            toRet = toRet.add(val.pow(coef.length-i-1).mult(coef[i]));
        }
        return toRet;
    }

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.println("Please input coefficients for polynomial");
        String raw = in.nextLine();
        String[] raw_coefficients = raw.split(" ");
        int order = raw_coefficients.length;
        System.out.println(order);
        if (order > 9){
            System.out.println("Couldn't compute.\nOrder of polynomial: " + order);
            System.exit(0);
        }
        int[] coef = new int[order];
        int[] deri = new int[order];

        deri[0] = 0;
        System.out.println(0);
        for (int i = 0; i < order; i++){
            coef[i] = Integer.parseInt(raw_coefficients[i]);
            if (i < order-1){
                deri[i+1] = coef[i] * (order-i-1);
            }
        }

        double rangeXLeft = -1, rangeXRight = 1, rangeYLow = -1, rangeYHigh = 1, gridsize = 0.001, alpha = 1, threshold = 0.000001;
        int maxite = 150;

        int index = 0;
        ArrayList<ArrayList<double[]>> grid = new ArrayList<>();
        double[] min = new double[3], max = new double[3];
        for (int i = 0; i < 3; i++){
            min[i] = maxite;
            max[i] = 0;
        }
        for (double i = rangeXLeft; i < rangeXRight; i += gridsize) {
            ArrayList<double[]> cur = new ArrayList<>();
            for (double j = rangeYLow; j < rangeYHigh; j += gridsize) {
                Complex toCalc = new Complex(i, j);
                int ite = 0;
                while (ite < maxite && calculate(coef, toCalc).measure() > threshold) {
                    toCalc = toCalc.minus(calculate(coef, toCalc).divideBy(calculate(deri, toCalc)).mult(alpha));
                    ite += 1;
                }
                double[] toAdd = {ite, Math.abs(toCalc.real), Math.abs(toCalc.imag)};
                cur.add(toAdd);
                if (ite > max[0]) {
                    max[0] = ite;
                }
                if (ite < min[0]) {
                    min[0] = ite;
                }

                if (ite < maxite){
                    double angle = Math.atan2(toAdd[2], toAdd[1]);
                    if (angle > max[1]){
                        max[1] = angle;
                    }
                    if (angle < min[1]){
                        min[1] = angle;
                    }

                    double measure = Math.sqrt(toAdd[2]*toAdd[2] + toAdd[1]*toAdd[1]);
                    if (measure > max[2] && measure != Double.POSITIVE_INFINITY){
                        max[2] = measure;
                    }
                    if (measure < min[2]){
                        min[2] = measure;
                    }
                }
                //System.out.print(" " + ite);
            }
            grid.add(cur);
            //System.out.println();
            System.out.print("\r" + index++);
            System.out.flush();
        }

        System.out.println("\n" + max[2] + " " + max[1] + " " + min[2] + " " + min[1]);

        int XSize = (int)Math.ceil((rangeXRight - rangeXLeft) / gridsize);
        int YSize = (int)Math.ceil((rangeYHigh - rangeYLow) / gridsize);

        double[] normal = {max[0]-min[0], max[1]-min[1], max[2]-min[2]};
        System.out.println(normal[0] + " " + normal[1] + " " + normal[2]);

        BufferedImage bImage = new BufferedImage(XSize, YSize, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < XSize; i++){
            for (int j = 0; j < YSize; j++){
                double[] curVal = grid.get(i).get(j);
                double curMeasure = Math.sqrt(curVal[1]*curVal[1]+curVal[2]*curVal[2]);
                double curAngle = Math.atan2(curVal[2], curVal[1]);
                float lig = (float)(((max[0] - curVal[0]) / normal[0])*0.8 + 0.2);
                float sat = (float)((max[2] - curMeasure) / normal[2]);
                float hue = (float)(curAngle / (Math.PI/2));
                //System.out.println(hue + " " + sat + " " + lig);
                bImage.setRGB(i, j, Color.HSBtoRGB(hue, sat, lig));
            }
        }

        File outputImage = new File("out.png");
        try{
            ImageIO.write(bImage, "png", outputImage);
        }
        catch(IOException io){
            System.out.println(io);
        }

    }
}

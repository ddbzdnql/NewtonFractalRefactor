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
            toRet = toRet.add(val.pow(coef.length-i).mult(coef[i]));
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
        int[] deri = new int[order-1];

        deri[0] = 0;
        for (int i = 0; i < order; i++){
            coef[i] = Integer.parseInt(raw_coefficients[i]);
            if (i < order-1){
                deri[i] = coef[i] * (order-i);
            }
        }

        double rangeXLeft = -1, rangeXRight = 1, rangeYLow = -1, rangeYHigh = 1, gridsize = 0.0005, alpha = 1, threshold = 0.0001;
        int maxite = 150;

        ArrayList<ArrayList<Integer>> grid = new ArrayList<>();
        int min = maxite, max = 0;
        for (double i = rangeXLeft; i < rangeXRight; i += gridsize) {
            ArrayList<Integer> cur = new ArrayList<>();
            for (double j = rangeYLow; j < rangeYHigh; j += gridsize) {
                Complex toCalc = new Complex(i, j);
                int ite = 0;
                while (ite < maxite && calculate(coef, toCalc).measure() > threshold) {
                    toCalc = toCalc.minus(calculate(coef, toCalc).divideBy(calculate(deri, toCalc)).mult(alpha));
                    ite += 1;
                }
                //System.out.print(ite + " ");
                cur.add(ite);
                if (ite > max) {
                    max = ite;
                }
                if (ite < min) {
                    min = ite;
                }
            }
            grid.add(cur);

            //System.out.println();
        }

        int XSize = (int)Math.ceil((rangeXRight - rangeXLeft) / gridsize);
        int YSize = (int)Math.ceil((rangeYHigh - rangeYLow) / gridsize);

        int[] bucket = new int[max - min + 1];

        BufferedImage bImage = new BufferedImage(XSize, YSize, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < XSize; i++){
            for (int j = 0; j < YSize; j++){
                int val = grid.get(i).get(j);
                val = (val - min);
                if (bucket[val] == 0){
                    Random rand = new Random();
                    int r = rand.nextInt(256);
                    int g = rand.nextInt(256);
                    int b = rand.nextInt(256);
                    int color = new Color(r, g, b).getRGB();
                    bucket[val] = color;
                }

                //System.out.println(val * step);
                bImage.setRGB(i, j, bucket[val]);
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

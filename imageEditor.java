import java.awt.Color;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.Scanner;

public class imageEditor {

    //GrayScale
    public static BufferedImage convertToGrayScale(BufferedImage inputImage){
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();

        BufferedImage outputImage = new BufferedImage(width , height , BufferedImage.TYPE_BYTE_GRAY);
        for(int i=0 ; i<height ; i++){
            for(int j=0 ; j<width ; j++){
                outputImage.setRGB(j,i, inputImage.getRGB(j,i));
            }
        }
        return outputImage;
    }

    //Clockwise
    public static BufferedImage rotateClockwise(BufferedImage inputImage){
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();

        BufferedImage outputImage = new BufferedImage(height , width , BufferedImage.TYPE_INT_RGB);
        for(int i = height -1; i>=0; i--){
            for(int j=0; j<width; j++){
                outputImage.setRGB(height-i-1, j, inputImage.getRGB(j, i));
            }
        }
        return outputImage;
    }

    //Anti-Clockwise
    public static BufferedImage rotateAntiClockwise(BufferedImage inputImage){
        BufferedImage outputImage = rotateClockwise(inputImage);
        outputImage = rotateClockwise(outputImage);
        outputImage = rotateClockwise(outputImage);
        return outputImage;
    }

    //Adjust-Brightness
    public static BufferedImage adjustBrightness(BufferedImage inputImage, int percentchange) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        percentchange = Math.min(100, Math.max(-100, percentchange));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color pixel = new Color(inputImage.getRGB(j, i));
                int red = pixel.getRed();
                int blue = pixel.getBlue();
                int green = pixel.getGreen();
                red = Math.min(255, Math.max(0, red + (percentchange * red) / 100));
                blue = Math.min(255, Math.max(0, blue + (percentchange * blue) / 100));
                green = Math.min(255, Math.max(0, green + (percentchange * green) / 100));
                Color newPixel = new Color(red, green, blue);
                outputImage.setRGB(j, i, newPixel.getRGB());
            }
        }
        return outputImage;
    }       

    //Invert
    public static BufferedImage invert(BufferedImage inputImage){
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = inputImage.getRGB(j, i);
                int newJ = width - 1 - j;
                int newI = height - 1 - i;
                outputImage.setRGB(newJ, newI, pixel);
            }
        }
        return outputImage;
    }

    //Blur
    public static BufferedImage blur(BufferedImage inputImage , int amountofblur){
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage outputImage = new BufferedImage(width , height , BufferedImage.TYPE_INT_RGB);
        for(int i=0; i<height - amountofblur; i +=amountofblur){
            for(int j=0; j<width - amountofblur; j+=amountofblur){
                BufferedImage temporaryImage = new BufferedImage(amountofblur, amountofblur, BufferedImage.TYPE_INT_RGB);
                int red_sum=0;
                int blue_sum=0;
                int green_sum=0;

                for(int x=0; x<amountofblur; x++){
                    for(int y=0; y<amountofblur; y++){
                        Color pixel = new Color(inputImage.getRGB(j+y,i+x));
                        red_sum += pixel.getRed();
                        blue_sum += pixel.getBlue();
                        green_sum += pixel.getGreen();
                    }
                }
                int avg_red= (int)red_sum/(amountofblur*amountofblur);
                int avg_blue= (int)blue_sum/(amountofblur*amountofblur);
                int avg_green= (int)green_sum/(amountofblur*amountofblur);

                for(int a=0; a<amountofblur; a++){
                    for(int b=0; b<amountofblur; b++){
                        Color newPixel = new Color(avg_red, avg_green, avg_blue);
                        temporaryImage.setRGB(b, a, newPixel.getRGB());
                    }
                }

                for(int m=0; m<amountofblur; m++){  
                    for(int n=0; n<amountofblur; n++){
                        outputImage.setRGB(j+n, i+m, temporaryImage.getRGB(n, m));
                    }
                }
            }
        }
        return outputImage;
    }
        
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("Main Menu:");
            System.out.println("1. Convert to GrayScale");
            System.out.println("2. Rotate 90 degrees Clockwise");
            System.out.println("3. Rotate 90 degrees Anticlockwise");
            System.out.println("4. Adjust Brightness");
            System.out.println("5. Invert the Image");       
            System.out.println("6. Blur the Image");       
            System.out.println("7. Exit");  
            System.out.print("Enter your choice (1-7) :");
            int choice = sc.nextInt();
            if(choice==7){
                break;
            }
            sc.nextLine();
            System.out.print("Enter the Path of Image :");
            String address = sc.nextLine();
            File inputFile = new File(address);
            try{
                BufferedImage inputImage = ImageIO.read(inputFile);
                BufferedImage outputImage = null;
                switch (choice) {
                    case 1:
                        outputImage = convertToGrayScale(inputImage);
                        break;
                    case 2:
                        outputImage = rotateClockwise(inputImage);
                        break;
                    case 3:
                        outputImage = rotateAntiClockwise(inputImage);
                        break;
                    case 4:
                        System.out.print("Enter Brightness Change Percentage: ");
                        int percentChange = sc.nextInt();
                        sc.nextLine(); // Consume the newline character
                        outputImage = adjustBrightness(inputImage, percentChange);
                        break;                
                    case 5:
                        outputImage = invert(inputImage);
                        break;
                    case 6:
                        System.out.print("Enter Pixel Count for Blur: ");
                        int pixelCount = sc.nextInt();
                        sc.nextLine();
                        outputImage = blur(inputImage, pixelCount);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                        continue;
                }

                System.out.print("Enter the Path to Save the Edited Image: ");
                String outputAddress = sc.nextLine();
                File outputFile = new File(outputAddress);

                //Save output Image
                ImageIO.write(outputImage, "jpg", outputFile);
                System.out.println("Image saved successfully.");

            } catch (IOException e) {
                System.out.println("Error reading the input image: " + e.getMessage());
            }
        }
        sc.close();
    }
}
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.UnaryOperator;

public class FromDigits {

   /* public static void main(String[] args) throws IOException {
        digits();
    }*/

    public static void digits() throws IOException {
        UnaryOperator<Double> sigmoid = x -> 1 / (1 + Math.exp(-x));
        UnaryOperator<Double> dsigmoid = y -> y * (1 - y);
        NeuralNetwork nn = new NeuralNetwork(0.001, sigmoid, dsigmoid, 784, 512, 128, 32, 10);

        int samples = 60000;
        BufferedImage[] images = new BufferedImage[samples];
        int[] digits = new int[samples];
        File[] imagesFiles = new File("C:\\Users\\User\\Downloads\\mnist_train\\train").listFiles();
        for (int i = 0; i < samples; i++) {
            images[i] = ImageIO.read(imagesFiles[i]);
            digits[i] = Integer.parseInt(imagesFiles[i].getName().charAt(10) + "");
        }

        double[][] inputs = new double[samples][784];
        for (int i = 0; i < samples; i++) {
            for (int x = 0; x < 28; x++) {
                for (int y = 0; y < 28; y++) {
                    inputs[i][x + y * 28] = (images[i].getRGB(x, y) & 0xff) / 255.0;
                }
            }
        }

        int epochs = 150;
        for (int i = 1; i < epochs; i++) {
            int right = 0;
            double errorSum = 0;
            int batchSize = 100;
            for (int j = 0; j < batchSize; j++) {
                int imgIndex = (int)(Math.random() * samples);
                double[] targets = new double[10];
                int digit = digits[imgIndex];
                targets[digit] = 1;

                double[] outputs = nn.feedForward(inputs[imgIndex]);
                int maxDigit = 0;
                double maxDigitWeight = -1;
                for (int k = 0; k < 10; k++) {
                    if(outputs[k] > maxDigitWeight) {
                        maxDigitWeight = outputs[k];
                        maxDigit = k;
                    }
                }
                if(digit == maxDigit) {
                    right++;
                    //System.out.println(digit);
                }
                for (int k = 0; k < 10; k++) {
                    errorSum += (targets[k] - outputs[k]) * (targets[k] - outputs[k]);
                }
                nn.backpropagation(targets);
            }
            System.out.println("epoch: " + i + ". correct: " + right + ". error: " + errorSum);
        }

        File file = new File("C:\\Users\\User\\Downloads\\mnist_train\\train\\000004-num9.png");
        BufferedImage img = ImageIO.read(file);
        double[] input = new double[784];
        for (int x = 0; x < 28; x++) {
            for (int y = 0; y < 28; y++) {
                input[x + y * 28] = (img.getRGB(x, y) & 0xff) / 255.0;
            }
        }
        double[] output = nn.feedForward(input);
        int maxDigiT = 0;
        double maxDigitWeight = -1;
        for (int k = 0; k < 10; k++) {
            if(output[k] > maxDigitWeight) {
                maxDigitWeight = output[k];
                maxDigiT = k;}
        }
        System.out.println(maxDigiT);

    }
}
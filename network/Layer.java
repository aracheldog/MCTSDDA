/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.ejml.simple.SimpleBase
 *  org.ejml.simple.SimpleMatrix
 */
package network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import network.ActivationFunction;
import org.ejml.simple.SimpleBase;
import org.ejml.simple.SimpleMatrix;

public class Layer {
    ActivationFunction actF;
    SimpleMatrix inputSimpleMatrix;
    SimpleMatrix weightsSimpleMatrix;
    SimpleMatrix outputSimpleMatrix;
    SimpleMatrix biassSimpleMatrix;

    public Layer(int input, int output, ActivationFunction actF) {
        this.inputSimpleMatrix = new SimpleMatrix(input, 1);
        this.weightsSimpleMatrix = new SimpleMatrix(input, output);
        this.outputSimpleMatrix = new SimpleMatrix(output, 1);
        this.biassSimpleMatrix = new SimpleMatrix(output, 1);
        this.actF = actF;
    }

    public void forward() {
        SimpleMatrix inputTransposedMatrix = (SimpleMatrix)this.inputSimpleMatrix.transpose();
        SimpleMatrix result = (SimpleMatrix)inputTransposedMatrix.mult((SimpleBase)this.weightsSimpleMatrix);
        result = (SimpleMatrix)result.plus((SimpleBase)this.biassSimpleMatrix);
        this.outputSimpleMatrix = this.actF.runFunc(result);
    }

    public Layer loadBias(String filename) {
        File file = new File(filename);
        BufferedReader br = null;
        try {
            String str;
            br = new BufferedReader(new FileReader(file));
            while ((str = br.readLine()) != null) {
                String[] tmp = str.split(",");
                if (tmp.length == this.outputSimpleMatrix.numRows()) {
                    for (int i = 0; i < this.outputSimpleMatrix.numRows(); ++i) {
                        this.biassSimpleMatrix.set(i, 0, Double.valueOf(tmp[i]).doubleValue());
                    }
                    continue;
                }
                System.out.println("ERROR: Bias file size is not match");
            }
            this.biassSimpleMatrix = (SimpleMatrix)this.biassSimpleMatrix.transpose();
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        return this;
    }

    public Layer loadWeight(String filename) {
        File file = new File(filename);
        BufferedReader br = null;
        int count = 0;
        try {
            String str;
            br = new BufferedReader(new FileReader(file));
            while ((str = br.readLine()) != null) {
                String[] tmp = str.split(",");
                if (tmp.length == this.inputSimpleMatrix.numRows()) {
                    for (i = 0; i < this.inputSimpleMatrix.numRows(); ++i) {
                        this.weightsSimpleMatrix.set(i, count, Double.valueOf(tmp[i]).doubleValue());
                    }
                } else if (tmp.length == this.outputSimpleMatrix.numRows()) {
                    for (i = 0; i < this.outputSimpleMatrix.numRows(); ++i) {
                        this.weightsSimpleMatrix.set(count, i, Double.valueOf(tmp[i]).doubleValue());
                    }
                } else {
                    System.out.println("ERROR: Weight file size is not match");
                }
                ++count;
            }
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        return this;
    }
}


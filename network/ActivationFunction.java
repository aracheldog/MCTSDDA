/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.ejml.simple.SimpleMatrix
 */
package network;

import org.ejml.simple.SimpleMatrix;

public class ActivationFunction {
    public SimpleMatrix runFunc(SimpleMatrix _outputs) {
        SimpleMatrix outputs = new SimpleMatrix(_outputs.numRows(), _outputs.numCols());
        for (int i = 0; i < _outputs.numRows(); ++i) {
            for (int j = 0; j < _outputs.numCols(); ++j) {
                outputs.set(i, j, this.func(_outputs.get(i, j)));
            }
        }
        return outputs;
    }

    public double func(double output) {
        return output;
    }
}


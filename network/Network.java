/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.ejml.simple.SimpleMatrix
 */
package network;

import java.util.ArrayList;
import network.Layer;
import org.ejml.simple.SimpleMatrix;

public class Network {
    ArrayList<Layer> model = new ArrayList();

    public void addLayer(Layer layer) {
        this.model.add(layer);
    }

    public SimpleMatrix forward(SimpleMatrix inputs) {
        SimpleMatrix outputs = null;
        for (int i = 0; i < this.model.size(); ++i) {
            Layer layer = this.model.get(i);
            if (i == 0) {
                layer.inputSimpleMatrix = inputs;
            }
            layer.forward();
            outputs = (SimpleMatrix)layer.outputSimpleMatrix.transpose();
            if (this.model.size() <= i + 1) continue;
            this.model.get((int)(i + 1)).inputSimpleMatrix = outputs;
        }
        return outputs;
    }
}


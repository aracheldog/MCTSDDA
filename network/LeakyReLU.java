/*
 * Decompiled with CFR 0.150.
 */
package network;

import network.ActivationFunction;

public class LeakyReLU
extends ActivationFunction {
    @Override
    public double func(double outputs) {
        if (outputs >= 0.0) {
            return outputs;
        }
        return 0.01 * outputs;
    }
}


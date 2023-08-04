/*
 * Decompiled with CFR 0.150.
 */
package network;

import network.ActivationFunction;

public class ReLU
extends ActivationFunction {
    @Override
    public double func(double outputs) {
        return Math.max(0.0, outputs);
    }
}


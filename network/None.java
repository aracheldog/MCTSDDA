/*
 * Decompiled with CFR 0.150.
 */
package network;

import network.ActivationFunction;

public class None
extends ActivationFunction {
    @Override
    public double func(double outputs) {
        return outputs;
    }
}


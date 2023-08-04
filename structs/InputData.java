/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  struct.AttackData
 *  struct.CharacterData
 *  struct.FrameData
 */
package structs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import struct.AttackData;
import struct.CharacterData;
import struct.FrameData;

public class InputData {
    public double[] Input;

    public InputData(FrameData frameData, boolean player) {
        this.Input = this.convert(frameData, player);
    }

    double[] convert(FrameData frameData, boolean player) {
        AttackData tmp;
        int i;
        int i2;
        ArrayList<Double> input_list = new ArrayList<Double>();
        CharacterData my = frameData.getCharacter(player);
        CharacterData opp = frameData.getCharacter(!player);
        double myHp = Math.abs((double)my.getHp() / 400.0);
        double myEnergy = (double)my.getEnergy() / 300.0;
        double myX = ((double)my.getLeft() + (double)my.getRight()) / 2.0 / 960.0;
        double myY = ((double)my.getBottom() + (double)my.getTop()) / 2.0 / 640.0;
        double mySpeedX = (double)my.getSpeedX() / 15.0;
        double mySpeedY = (double)my.getSpeedY() / 28.0;
        int myState = my.getAction().ordinal();
        double myRemainingFrame = (double)my.getRemainingFrame() / 70.0;
        double oppHp = Math.abs((double)opp.getHp() / 400.0);
        double oppEnergy = (double)opp.getEnergy() / 300.0;
        double oppX = ((double)opp.getLeft() + (double)opp.getRight()) / 2.0 / 960.0;
        double oppY = ((double)opp.getBottom() + (double)opp.getTop()) / 2.0 / 640.0;
        double oppSpeedX = (double)opp.getSpeedX() / 15.0;
        double oppSpeedY = (double)opp.getSpeedY() / 28.0;
        int oppState = opp.getAction().ordinal();
        double oppRemainingFrame = (double)opp.getRemainingFrame() / 70.0;
        double game_frame_num = (double)frameData.getFramesNumber() / 3600.0;
        input_list.add(myHp);
        input_list.add(myEnergy);
        input_list.add(myX);
        input_list.add(myY);
        if (mySpeedX < 0.0) {
            input_list.add(0.0);
        } else {
            input_list.add(1.0);
        }
        input_list.add(Math.abs(mySpeedX));
        if (mySpeedY < 0.0) {
            input_list.add(0.0);
        } else {
            input_list.add(1.0);
        }
        input_list.add(Math.abs(mySpeedY));
        for (i2 = 0; i2 < 56; ++i2) {
            if (i2 == myState) {
                input_list.add(1.0);
                continue;
            }
            input_list.add(0.0);
        }
        input_list.add(myRemainingFrame);
        input_list.add(oppHp);
        input_list.add(oppEnergy);
        input_list.add(oppX);
        input_list.add(oppY);
        if (oppSpeedX < 0.0) {
            input_list.add(0.0);
        } else {
            input_list.add(1.0);
        }
        input_list.add(Math.abs(oppSpeedX));
        if (oppSpeedY < 0.0) {
            input_list.add(0.0);
        } else {
            input_list.add(1.0);
        }
        input_list.add(Math.abs(oppSpeedY));
        for (i2 = 0; i2 < 56; ++i2) {
            if (i2 == oppState) {
                input_list.add(1.0);
                continue;
            }
            input_list.add(0.0);
        }
        input_list.add(oppRemainingFrame);
        input_list.add(game_frame_num);
        ArrayList myAttack = new ArrayList(player ? frameData.getProjectilesByP1() : frameData.getProjectilesByP2());
        ArrayList oppAttack = new ArrayList(player ? frameData.getProjectilesByP2() : frameData.getProjectilesByP1());
        for (i = 0; i < 2; ++i) {
            if (myAttack.size() > i) {
                tmp = (AttackData)myAttack.get(i);
                input_list.add((double)tmp.getHitDamage() / 200.0);
                input_list.add(((double)tmp.getCurrentHitArea().getLeft() + (double)tmp.getCurrentHitArea().getRight()) / 2.0 / 960.0);
                input_list.add(((double)tmp.getCurrentHitArea().getTop() + (double)tmp.getCurrentHitArea().getBottom()) / 2.0 / 640.0);
                continue;
            }
            input_list.add(0.0);
            input_list.add(0.0);
            input_list.add(0.0);
        }
        for (i = 0; i < 2; ++i) {
            if (oppAttack.size() > i) {
                tmp = (AttackData)oppAttack.get(i);
                input_list.add((double)tmp.getHitDamage() / 200.0);
                input_list.add(((double)tmp.getCurrentHitArea().getLeft() + (double)tmp.getCurrentHitArea().getRight()) / 2.0 / 960.0);
                input_list.add(((double)tmp.getCurrentHitArea().getTop() + (double)tmp.getCurrentHitArea().getBottom()) / 2.0 / 640.0);
                continue;
            }
            input_list.add(0.0);
            input_list.add(0.0);
            input_list.add(0.0);
        }
        for (i = 0; i < input_list.size(); ++i) {
            if (input_list.get(i) > 1.0) {
                input_list.set(i, 1.0);
            }
            if (!(input_list.get(i) < 0.0)) continue;
            input_list.set(i, 0.0);
        }
        this.Input = InputData.toArr(input_list);
        return this.Input;
    }

    public static double[] toArr(List<Double> list) {
        int l = list.size();
        double[] arr = new double[l];
        Iterator<Double> iter = list.iterator();
        for (int i = 0; i < l; ++i) {
            arr[i] = iter.next();
        }
        return arr;
    }
}


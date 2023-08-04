/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  aiinterface.AIInterface
 *  aiinterface.CommandCenter
 *  enumerate.Action
 *  enumerate.State
 *  org.ejml.simple.SimpleMatrix
 *  struct.CharacterData
 *  struct.FrameData
 *  struct.GameData
 *  struct.Key
 *  struct.MotionData
 */
import aiinterface.AIInterface;
import aiinterface.CommandCenter;
import enumerate.Action;
import enumerate.State;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import network.Layer;
import network.LeakyReLU;
import network.Network;
import network.None;
import org.ejml.simple.SimpleMatrix;
import struct.CharacterData;
import struct.FrameData;
import struct.GameData;
import struct.Key;
import struct.MotionData;
import structs.InputData;

public class MCTSDDA
implements AIInterface {
    private boolean isControl;
    private FrameData frameData;
    private CommandCenter commandCenter;
    private boolean playerNumber;
    private Key key;
    private boolean frameskip;
    Network network;
    private boolean isGameJustStarted;
    private LinkedList<Action> myActions;
    private LinkedList<Action> oppActions;
    private Node rootNode;
    private GameData gameData;
    private int top_n = 15;
    public static final List<Integer> chooseable_actions = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 16, 17, 18, 19, 21, 22, 23, 24, 25, 26, 29, 31, 32, 33, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 52, 53);
    public static final Action[] ALL_ACTIONS_INCLUDE_ALL = new Action[]{Action.AIR, Action.AIR_A, Action.AIR_B, Action.AIR_D_DB_BA, Action.AIR_D_DB_BB, Action.AIR_D_DF_FA, Action.AIR_D_DF_FB, Action.AIR_DA, Action.AIR_DB, Action.AIR_F_D_DFA, Action.AIR_F_D_DFB, Action.AIR_FA, Action.AIR_FB, Action.AIR_GUARD, Action.AIR_GUARD_RECOV, Action.AIR_RECOV, Action.AIR_UA, Action.AIR_UB, Action.BACK_JUMP, Action.BACK_STEP, Action.CHANGE_DOWN, Action.CROUCH, Action.CROUCH_A, Action.CROUCH_B, Action.CROUCH_FA, Action.CROUCH_FB, Action.CROUCH_GUARD, Action.CROUCH_GUARD_RECOV, Action.CROUCH_RECOV, Action.DASH, Action.DOWN, Action.FOR_JUMP, Action.FORWARD_WALK, Action.JUMP, Action.LANDING, Action.NEUTRAL, Action.RISE, Action.STAND, Action.STAND_A, Action.STAND_B, Action.STAND_D_DB_BA, Action.STAND_D_DB_BB, Action.STAND_D_DF_FA, Action.STAND_D_DF_FB, Action.STAND_D_DF_FC, Action.STAND_F_D_DFA, Action.STAND_F_D_DFB, Action.STAND_FA, Action.STAND_FB, Action.STAND_GUARD, Action.STAND_GUARD_RECOV, Action.STAND_RECOV, Action.THROW_A, Action.THROW_B, Action.THROW_HIT, Action.THROW_SUFFER};
    public static final Action[] ALL_ACTIONS = new Action[]{Action.AIR_A, Action.AIR_B, Action.AIR_D_DB_BA, Action.AIR_D_DB_BB, Action.AIR_D_DF_FA, Action.AIR_D_DF_FB, Action.AIR_DA, Action.AIR_DB, Action.AIR_F_D_DFA, Action.AIR_F_D_DFB, Action.AIR_FA, Action.AIR_FB, Action.AIR_GUARD, Action.AIR_UA, Action.AIR_UB, Action.BACK_JUMP, Action.BACK_STEP, Action.CROUCH, Action.CROUCH_A, Action.CROUCH_B, Action.CROUCH_FA, Action.CROUCH_FB, Action.CROUCH_GUARD, Action.DASH, Action.FOR_JUMP, Action.FORWARD_WALK, Action.JUMP, Action.STAND_A, Action.STAND_B, Action.STAND_D_DB_BA, Action.STAND_D_DB_BB, Action.STAND_D_DF_FA, Action.STAND_D_DF_FB, Action.STAND_D_DF_FC, Action.STAND_F_D_DFA, Action.STAND_F_D_DFB, Action.STAND_FA, Action.STAND_FB, Action.STAND_GUARD, Action.THROW_A, Action.THROW_B};
    public static final Action[] ACTION_AIR = new Action[]{Action.AIR_GUARD, Action.AIR_A, Action.AIR_B, Action.AIR_DA, Action.AIR_DB, Action.AIR_FA, Action.AIR_FB, Action.AIR_UA, Action.AIR_UB, Action.AIR_D_DF_FA, Action.AIR_D_DF_FB, Action.AIR_F_D_DFA, Action.AIR_F_D_DFB, Action.AIR_D_DB_BA, Action.AIR_D_DB_BB};
    public static final Action[] ACTION_GROUND = new Action[]{Action.STAND_D_DB_BA, Action.BACK_STEP, Action.FORWARD_WALK, Action.DASH, Action.CROUCH, Action.JUMP, Action.FOR_JUMP, Action.BACK_JUMP, Action.STAND_GUARD, Action.CROUCH_GUARD, Action.THROW_A, Action.THROW_B, Action.STAND_A, Action.STAND_B, Action.CROUCH_A, Action.CROUCH_B, Action.STAND_FA, Action.STAND_FB, Action.CROUCH_FA, Action.CROUCH_FB, Action.STAND_D_DF_FA, Action.STAND_D_DF_FB, Action.STAND_F_D_DFA, Action.STAND_F_D_DFB, Action.STAND_D_DB_BB, Action.STAND_D_DF_FC};
    public static final Action[] actionAirOff = new Action[]{Action.AIR, Action.AIR_A, Action.AIR_B, Action.AIR_D_DB_BA, Action.AIR_D_DB_BB, Action.AIR_D_DF_FA, Action.AIR_D_DF_FB, Action.AIR_DA, Action.AIR_DB, Action.AIR_F_D_DFA, Action.AIR_F_D_DFB, Action.AIR_FA, Action.AIR_FB, Action.AIR_GUARD, Action.AIR_GUARD_RECOV, Action.AIR_RECOV, Action.AIR_UA, Action.AIR_UB};
    public static final Action[] actionGroundOff = new Action[]{Action.BACK_JUMP, Action.BACK_STEP, Action.CHANGE_DOWN, Action.CROUCH, Action.CROUCH_A, Action.CROUCH_B, Action.CROUCH_FA, Action.CROUCH_FB, Action.CROUCH_GUARD, Action.CROUCH_GUARD_RECOV, Action.CROUCH_RECOV, Action.DASH, Action.DOWN, Action.FOR_JUMP, Action.FORWARD_WALK, Action.JUMP, Action.LANDING, Action.NEUTRAL, Action.RISE, Action.STAND, Action.STAND_A, Action.STAND_B, Action.STAND_D_DB_BA, Action.STAND_D_DB_BB, Action.STAND_D_DF_FA, Action.STAND_D_DF_FB, Action.STAND_D_DF_FC, Action.STAND_F_D_DFA, Action.STAND_F_D_DFB, Action.STAND_FA, Action.STAND_FB, Action.STAND_GUARD, Action.STAND_GUARD_RECOV, Action.STAND_RECOV, Action.THROW_A, Action.THROW_B, Action.THROW_HIT, Action.THROW_SUFFER};
    public static final Action[] actionAirDef = new Action[]{Action.AIR, Action.AIR_A, Action.AIR_B, Action.AIR_D_DB_BA, Action.AIR_D_DF_FA, Action.AIR_DA, Action.AIR_DB, Action.AIR_F_D_DFA, Action.AIR_FA, Action.AIR_FB, Action.AIR_GUARD, Action.AIR_GUARD_RECOV, Action.AIR_RECOV, Action.AIR_UA};
    public static final Action[] actionGroundDef = new Action[]{Action.BACK_JUMP, Action.BACK_STEP, Action.CHANGE_DOWN, Action.CROUCH, Action.CROUCH_A, Action.CROUCH_B, Action.CROUCH_FA, Action.CROUCH_FB, Action.CROUCH_GUARD, Action.CROUCH_GUARD_RECOV, Action.CROUCH_RECOV, Action.DASH, Action.DOWN, Action.FOR_JUMP, Action.FORWARD_WALK, Action.JUMP, Action.LANDING, Action.NEUTRAL, Action.RISE, Action.STAND, Action.STAND_A, Action.STAND_B, Action.STAND_D_DB_BA, Action.STAND_D_DF_FA, Action.STAND_D_DF_FB, Action.STAND_F_D_DFA, Action.STAND_FA, Action.STAND_FB, Action.STAND_GUARD, Action.STAND_GUARD_RECOV, Action.STAND_RECOV, Action.THROW_A, Action.THROW_B, Action.THROW_HIT, Action.THROW_SUFFER};
    public static final List<Integer> OFF_ACTION_INDEX_AIR = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17);
    public static final List<Integer> OFF_ACTION_INDEX_GROUND = Arrays.asList(18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55);
    public static final List<Integer> OFF_ACTION_INDEX = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55);
    public static final List<Integer> DEF_ACTION_INDEX_AIR = Arrays.asList(0, 1, 2, 3, 5, 7, 8, 9, 11, 12, 13, 14, 15, 16);
    public static final List<Integer> DEF_ACTION_INDEX_GROUND = Arrays.asList(18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 42, 43, 45, 47, 48, 49, 50, 51, 52, 53, 54, 55);
    public static final List<Integer> DEF_ACTION_INDEX = Arrays.asList(0, 1, 2, 3, 5, 7, 8, 9, 11, 12, 13, 14, 15, 16, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 42, 43, 45, 47, 48, 49, 50, 51, 52, 53, 54, 55);
    private CharacterData myCharacter;
    private CharacterData oppCharacter;
    private int myhp;
    private int opphp;
    private ArrayList<MotionData> myMotion;
    private ArrayList<MotionData> oppMotion;
    private List<String[]> myList = new ArrayList<String[]>();

    public static void main(String[] args) {
        Action a;
        int n;
        ArrayList<Integer> test = new ArrayList<Integer>();
        Action[] arraction = actionAirOff;
        int n2 = actionAirOff.length;
        for (n = 0; n < n2; ++n) {
            a = arraction[n];
            test.add(Arrays.asList(ALL_ACTIONS_INCLUDE_ALL).indexOf((Object)a));
        }
        System.out.println();
        arraction = actionGroundOff;
        n2 = actionGroundOff.length;
        for (n = 0; n < n2; ++n) {
            a = arraction[n];
            System.out.println((Object)a);
            System.out.println(Arrays.asList(ALL_ACTIONS_INCLUDE_ALL).indexOf((Object)a));
            test.add(Arrays.asList(ALL_ACTIONS_INCLUDE_ALL).indexOf((Object)a));
        }
        Collections.sort(test);
        for (Integer integer : test) {
            System.out.print(integer + ", ");
        }
    }

    public void close() {
    }

    public void getInformation(FrameData frameData, boolean isControl) {
        this.isControl = isControl;
        this.frameData = frameData;
        this.commandCenter.setFrameData(this.frameData, this.playerNumber);
        this.myCharacter = frameData.getCharacter(this.playerNumber);
        this.oppCharacter = frameData.getCharacter(!this.playerNumber);
        if (frameData.getEmptyFlag()) {
            return;
        }
    }

    public int initialize(GameData gameData, boolean playerNumber) {
        this.isGameJustStarted = true;
        this.frameskip = false;
        this.playerNumber = playerNumber;
        this.key = new Key();
        this.frameData = new FrameData();
        this.gameData = gameData;
        this.commandCenter = new CommandCenter();
        this.network = new Network();
        Layer l1 = new Layer(143, 800, new LeakyReLU());
        Layer l2 = new Layer(800, 600, new LeakyReLU());
        Layer l3 = new Layer(600, 400, new LeakyReLU());
        Layer l4 = new Layer(400, 200, new LeakyReLU());
        Layer l5 = new Layer(200, 100, new LeakyReLU());
        Layer l6 = new Layer(100, 56, new None());
        String root = "data/aiData/MCTSDDA/";
        l1.loadWeight(String.valueOf(root) + "weight_l1.csv");
        l1.loadBias(String.valueOf(root) + "bias_l1.csv");
        l2.loadWeight(String.valueOf(root) + "weight_l2.csv");
        l2.loadBias(String.valueOf(root) + "bias_l2.csv");
        l3.loadWeight(String.valueOf(root) + "weight_l3.csv");
        l3.loadBias(String.valueOf(root) + "bias_l3.csv");
        l4.loadWeight(String.valueOf(root) + "weight_l4.csv");
        l4.loadBias(String.valueOf(root) + "bias_l4.csv");
        l5.loadWeight(String.valueOf(root) + "weight_l5.csv");
        l5.loadBias(String.valueOf(root) + "bias_l5.csv");
        l6.loadWeight(String.valueOf(root) + "weight_l6.csv");
        l6.loadBias(String.valueOf(root) + "bias_l6.csv");
        this.network.addLayer(l1);
        this.network.addLayer(l2);
        this.network.addLayer(l3);
        this.network.addLayer(l4);
        this.network.addLayer(l5);
        this.network.addLayer(l6);
        this.myActions = new LinkedList();
        this.oppActions = new LinkedList();
        this.myMotion = gameData.getMotionData(this.playerNumber);
        this.oppMotion = gameData.getMotionData(!this.playerNumber);
        return 0;
    }

    public Key input() {
        return this.key;
    }

    public void processing() {
        double time = System.nanoTime();
        if (this.frameData.getEmptyFlag()) {
            this.isGameJustStarted = true;
            return;
        }
        if (this.frameskip) {
            if (this.commandCenter.getSkillFlag()) {
                this.key = this.commandCenter.getSkillKey();
                return;
            }
            if (!this.isControl) {
                return;
            }
            this.key.empty();
            this.commandCenter.skillCancel();
        }
        InputData myInformationData = new InputData(this.frameData, this.playerNumber);
        double[] myInformationDataArray = myInformationData.Input;
        SimpleMatrix input = new SimpleMatrix(myInformationDataArray.length, 1, true, myInformationDataArray);
        SimpleMatrix outputSimpleMatrix = this.network.forward(input);
        double[] my_output = this.matrix2array(outputSimpleMatrix);
        time = System.nanoTime();
        HashMap<Integer, Double> actionProbabilities = new HashMap<Integer, Double>();
        for (int i = 0; i < my_output.length; ++i) {
            actionProbabilities.put(i, my_output[i]);
        }
        this.myhp = this.myCharacter.getHp();
        this.opphp = this.oppCharacter.getHp();
        List<Object> topActions = new ArrayList();
        if (this.opphp - this.myhp >= 30 || this.myhp <= 30 && this.opphp <= 30) {
            if (this.myCharacter.getState() == State.AIR) {
                topActions = new ArrayList<Integer>(OFF_ACTION_INDEX_AIR);
                Collections.sort(topActions, (a, b) -> Double.compare((Double)actionProbabilities.get(b), (Double)actionProbabilities.get(a)));
                topActions = topActions.stream().filter(OFF_ACTION_INDEX_AIR::contains).limit(Math.min(this.top_n, OFF_ACTION_INDEX_AIR.size())).collect(Collectors.toList());
            } else {
                topActions = new ArrayList<Integer>(OFF_ACTION_INDEX_GROUND);
                Collections.sort(topActions, (a, b) -> Double.compare((Double)actionProbabilities.get(b), (Double)actionProbabilities.get(a)));
                topActions = topActions.stream().filter(OFF_ACTION_INDEX_GROUND::contains).limit(Math.min(this.top_n, OFF_ACTION_INDEX_GROUND.size())).collect(Collectors.toList());
            }
        } else if (this.myCharacter.getState() == State.AIR) {
            topActions = new ArrayList<Integer>(DEF_ACTION_INDEX_AIR);
            Collections.sort(topActions, (a, b) -> Double.compare((Double)actionProbabilities.get(b), (Double)actionProbabilities.get(a)));
            topActions = topActions.stream().filter(DEF_ACTION_INDEX_AIR::contains).limit(Math.min(this.top_n, DEF_ACTION_INDEX_AIR.size())).collect(Collectors.toList());
        } else {
            topActions = new ArrayList<Integer>(DEF_ACTION_INDEX_GROUND);
            Collections.sort(topActions, (a, b) -> Double.compare((Double)actionProbabilities.get(b), (Double)actionProbabilities.get(a)));
            topActions = topActions.stream().filter(DEF_ACTION_INDEX_GROUND::contains).limit(Math.min(this.top_n, DEF_ACTION_INDEX_GROUND.size())).collect(Collectors.toList());
        }
        this.myActions.clear();
        for (Integer n : topActions) {
            if (Math.abs(this.myMotion.get(Action.valueOf((String)ALL_ACTIONS_INCLUDE_ALL[n].name()).ordinal()).getAttackStartAddEnergy()) > this.myCharacter.getEnergy()) continue;
            this.myActions.add(ALL_ACTIONS_INCLUDE_ALL[n]);
        }
        this.setOppAction();
        this.rootNode = new Node(this.frameData, null, this.myActions, this.oppActions, this.gameData, this.playerNumber, this.commandCenter);
        this.rootNode.createNode(this.myActions, this.oppActions, false);
        Action action = this.rootNode.mcts();
        this.commandCenter.commandCall(action.name());
        if (!this.frameskip) {
            this.key = this.commandCenter.getSkillKey();
        }
        String[] infoStrings = new String[]{Integer.toString(this.frameData.getFramesNumber()), Integer.toString(this.myCharacter.getHp()), Integer.toString(this.oppCharacter.getHp()), action.name()};
        this.myList.add(infoStrings);
    }

    public void roundEnd(int p1Hp, int p2Hp, int frames) {
        File writeFile;
        block10: {
            String filename;
            System.out.println(p1Hp);
            System.out.println(p2Hp);
            System.out.println(p1Hp - p2Hp);
            File directory = new File("mctsdda_" + this.gameData.getAiName(!this.playerNumber).toLowerCase());
            if (!directory.exists()) {
                directory.mkdir();
            }
            if ((writeFile = new File(filename = "mctsdda_" + this.gameData.getAiName(!this.playerNumber).toLowerCase() + "/" + Integer.toString(p1Hp - p2Hp) + "dda.csv")).exists()) {
                int counter = 1;
                while (true) {
                    String newFilename;
                    File newFile;
                    if (!(newFile = new File(newFilename = String.valueOf(filename.substring(0, filename.lastIndexOf(46))) + "_" + counter + filename.substring(filename.lastIndexOf(46)))).exists()) {
                        writeFile.renameTo(newFile);
                        break block10;
                    }
                    ++counter;
                }
            }
            try {
                writeFile.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter writeText = new BufferedWriter(new FileWriter(writeFile));
            for (int i = 0; i < this.myList.size(); ++i) {
                writeText.newLine();
                writeText.write(String.valueOf(this.myList.get(i)[0]) + "," + this.myList.get(i)[1] + "," + this.myList.get(i)[2] + "," + this.myList.get(i)[3]);
            }
            writeText.flush();
            writeText.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("\u6ca1\u6709\u627e\u5230\u6307\u5b9a\u6587\u4ef6");
        }
        catch (IOException e) {
            System.out.println("\u6587\u4ef6\u8bfb\u5199\u51fa\u9519");
        }
    }

    public double[] matrix2array(SimpleMatrix matrix) {
        double[] array = new double[matrix.numRows()];
        for (int r = 0; r < matrix.numRows(); ++r) {
            array[r] = matrix.get(r, 0);
        }
        return array;
    }

    public void setOppAction() {
        this.oppActions.clear();
        int energy = this.oppCharacter.getEnergy();
        if (this.oppCharacter.getState() == State.AIR) {
            for (int i = 0; i < ACTION_AIR.length; ++i) {
                if (Math.abs(this.oppMotion.get(Action.valueOf((String)ACTION_AIR[i].name()).ordinal()).getAttackStartAddEnergy()) > energy) continue;
                this.oppActions.add(ACTION_AIR[i]);
            }
        } else {
            for (int i = 0; i < ACTION_GROUND.length; ++i) {
                if (Math.abs(this.oppMotion.get(Action.valueOf((String)ACTION_GROUND[i].name()).ordinal()).getAttackStartAddEnergy()) > energy) continue;
                this.oppActions.add(ACTION_GROUND[i]);
            }
        }
    }

    public /* synthetic */ void getInformation(FrameData frameData) {
        throw new Error("Unresolved compilation problem: \n\tThe type MCTSDDA must implement the inherited abstract method AIInterface.getInformation(FrameData)\n");
    }
}


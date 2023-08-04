/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  aiinterface.CommandCenter
 *  enumerate.Action
 *  simulator.Simulator
 *  struct.FrameData
 *  struct.GameData
 *  struct.MotionData
 */
import aiinterface.CommandCenter;
import enumerate.Action;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import simulator.Simulator;
import struct.FrameData;
import struct.GameData;
import struct.MotionData;

public class Node {
    public static final Action[] ACTION_AIR = new Action[]{Action.AIR_GUARD, Action.AIR_A, Action.AIR_B, Action.AIR_DA, Action.AIR_DB, Action.AIR_FA, Action.AIR_FB, Action.AIR_UA, Action.AIR_UB, Action.AIR_D_DF_FA, Action.AIR_D_DF_FB, Action.AIR_F_D_DFA, Action.AIR_F_D_DFB, Action.AIR_D_DB_BA, Action.AIR_D_DB_BB};
    public static final Action[] ACTION_GROUND = new Action[]{Action.STAND_D_DB_BA, Action.BACK_STEP, Action.FORWARD_WALK, Action.DASH, Action.CROUCH, Action.JUMP, Action.FOR_JUMP, Action.BACK_JUMP, Action.STAND_GUARD, Action.CROUCH_GUARD, Action.THROW_A, Action.THROW_B, Action.STAND_A, Action.STAND_B, Action.CROUCH_A, Action.CROUCH_B, Action.STAND_FA, Action.STAND_FB, Action.CROUCH_FA, Action.CROUCH_FB, Action.STAND_D_DF_FA, Action.STAND_D_DF_FB, Action.STAND_F_D_DFA, Action.STAND_F_D_DFB, Action.STAND_D_DB_BB};
    public static final Action spSkill = Action.STAND_D_DF_FC;
    public static final List<Integer> chooseable_actions = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 16, 17, 18, 19, 21, 22, 23, 24, 25, 26, 29, 31, 32, 33, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 52, 53);
    public static final Action[] ALL_ACTIONS = new Action[]{Action.AIR_A, Action.AIR_B, Action.AIR_D_DB_BA, Action.AIR_D_DB_BB, Action.AIR_D_DF_FA, Action.AIR_D_DF_FB, Action.AIR_DA, Action.AIR_DB, Action.AIR_F_D_DFA, Action.AIR_F_D_DFB, Action.AIR_FA, Action.AIR_FB, Action.AIR_GUARD, Action.AIR_UA, Action.AIR_UB, Action.BACK_JUMP, Action.BACK_STEP, Action.CROUCH, Action.CROUCH_A, Action.CROUCH_B, Action.CROUCH_FA, Action.CROUCH_FB, Action.CROUCH_GUARD, Action.DASH, Action.FOR_JUMP, Action.FORWARD_WALK, Action.JUMP, Action.STAND_A, Action.STAND_B, Action.STAND_D_DB_BA, Action.STAND_D_DB_BB, Action.STAND_D_DF_FA, Action.STAND_D_DF_FB, Action.STAND_D_DF_FC, Action.STAND_F_D_DFA, Action.STAND_F_D_DFB, Action.STAND_FA, Action.STAND_FB, Action.STAND_GUARD, Action.THROW_A, Action.THROW_B};
    public static final int UCT_TIME = 17500000;
    public static final double UCB_C = 3.0;
    public static final int UCT_TREE_DEPTH = 2;
    public static final int UCT_CREATE_NODE_THRESHOULD = 10;
    public static final int SIMULATION_TIME = 60;
    private Random rnd;
    private Node parent;
    private Node[] children;
    private int depth;
    public int games;
    private double ucb;
    private double score;
    private LinkedList<Action> myActions;
    private LinkedList<Action> oppActions;
    private Simulator simulator;
    private Action selectedMyAction;
    private LinkedList<Action> selectedMyActions;
    private int myOriginalHp;
    private int oppOriginalHp;
    private FrameData frameData;
    private boolean playerNumber;
    private CommandCenter commandCenter;
    private GameData gameData;
    private boolean isCreateNode;
    Deque<Action> mAction;
    Deque<Action> oppAction;
    private ArrayList<MotionData> myMotion;
    private ArrayList<MotionData> oppMotion;

    public Node(FrameData frameData, Node parent, LinkedList<Action> myActions, LinkedList<Action> oppActions, GameData gameData, boolean playerNumber, CommandCenter commandCenter) {
        this.frameData = frameData;
        this.parent = parent;
        this.myActions = myActions;
        this.oppActions = oppActions;
        this.gameData = gameData;
        this.simulator = new Simulator(gameData);
        this.playerNumber = playerNumber;
        this.commandCenter = commandCenter;
        this.myMotion = gameData.getMotionData(this.playerNumber);
        this.oppMotion = gameData.getMotionData(!this.playerNumber);
        this.rnd = new Random();
        this.mAction = new LinkedList<Action>();
        this.oppAction = new LinkedList<Action>();
        this.depth = this.parent != null ? this.parent.depth + 1 : 0;
    }

    public Action mcts() {
        long start = System.nanoTime();
        while (System.nanoTime() - start <= 17500000L) {
            this.uct();
        }
        return this.getBestVisitAction();
    }

    public double playout() {
        int i;
        this.mAction.clear();
        this.oppAction.clear();
        this.mAction.add(this.selectedMyAction);
        for (i = 0; i < 4; ++i) {
            this.mAction.add(this.myActions.get(this.rnd.nextInt(this.myActions.size())));
        }
        for (i = 0; i < 5; ++i) {
            this.oppAction.add(this.oppActions.get(this.rnd.nextInt(this.oppActions.size())));
        }
        FrameData nFrameData = this.simulator.simulate(this.frameData, this.playerNumber, this.mAction, this.oppAction, 60);
        if (this.games >= 9) {
            this.frameData = nFrameData;
        }
        double score = this.getSimulationScore(nFrameData);
        return score;
    }

    public double uct() {
        Node selectedNode = null;
        double bestUcb = -99999.0;
        Node[] arrnode = this.children;
        int n = this.children.length;
        for (int i = 0; i < n; ++i) {
            Node child = arrnode[i];
            child.ucb = child.games == 0 ? (double)(9999 + this.rnd.nextInt(50)) : this.getUcb(child.score / (double)child.games, this.games, child.games);
            if (!(bestUcb < child.ucb)) continue;
            selectedNode = child;
            bestUcb = child.ucb;
        }
        double score = 0.0;
        if (selectedNode.games == 0) {
            score = selectedNode.playout();
        } else if (selectedNode.children == null) {
            if (selectedNode.depth < 2) {
                if (10 <= selectedNode.games) {
                    selectedNode.createNode(this.myActions, this.oppActions, true);
                    selectedNode.isCreateNode = true;
                    score = selectedNode.uct();
                } else {
                    score = selectedNode.playout();
                }
            } else {
                score = selectedNode.playout();
            }
        } else if (selectedNode.depth < 2) {
            score = selectedNode.uct();
        } else {
            selectedNode.playout();
        }
        ++selectedNode.games;
        selectedNode.score += score;
        if (this.depth == 0) {
            ++this.games;
        }
        return score;
    }

    public void createNode(LinkedList<Action> myNewActions, LinkedList<Action> oppNewActions, Boolean rand) {
        this.children = rand == false ? new Node[Math.min(12, myNewActions.size())] : new Node[Math.min(4, myNewActions.size())];
        for (int i = 0; i < this.children.length; ++i) {
            this.children[i] = new Node(this.frameData, this, myNewActions, oppNewActions, this.gameData, this.playerNumber, this.commandCenter);
            this.children[i].selectedMyAction = rand == false ? myNewActions.get(i) : myNewActions.get(this.rnd.nextInt(myNewActions.size()));
        }
    }

    public Action getBestScoreAction() {
        int selected = -1;
        double bestScore = -9999.0;
        for (int i = 0; i < this.children.length; ++i) {
            double meanScore = this.children[i].score / (double)this.children[i].games;
            if (!(bestScore < meanScore)) continue;
            bestScore = meanScore;
            selected = i;
        }
        return this.myActions.get(selected);
    }

    public double getSimulationScore(FrameData fd) {
        return Math.abs(fd.getCharacter(this.playerNumber).getHp() - fd.getCharacter(!this.playerNumber).getHp()) - 15 < 0 ? Math.abs(fd.getCharacter(this.playerNumber).getHp() - fd.getCharacter(!this.playerNumber).getHp()) : -(Math.abs(fd.getCharacter(this.playerNumber).getHp() - fd.getCharacter(!this.playerNumber).getHp()) - 15);
    }

    public double getUcb(double score, int n, int ni) {
        return score / 400.0 + 3.0 * Math.sqrt(2.0 * Math.log(n) / (double)ni);
    }

    public void printNode(Node node) {
        int i;
        System.out.println("The current node has been visited:" + node.games + " times" + "node depth: " + node.depth);
        if (node.depth != 0) {
            System.out.println("parant selected action is: " + node.selectedMyAction.name());
        }
        if (node.children != null) {
            System.out.println("the children nodes have length: " + node.children.length);
        }
        for (i = 0; i < node.children.length; ++i) {
            System.out.println(i + ", num_visited:" + node.children[i].games + ", node_depth:" + node.children[i].depth + ", score:" + node.children[i].score / (double)node.children[i].games + ", ucb:" + node.children[i].ucb + ", actions_selected: " + node.children[i].selectedMyAction.name());
        }
        System.out.println("");
        for (i = 0; i < node.children.length; ++i) {
            if (!node.children[i].isCreateNode) continue;
            this.printNode(node.children[i]);
        }
    }

    public Action getBestVisitAction() {
        int selected = -1;
        double bestGames = -9999.0;
        for (int i = 0; i < this.children.length; ++i) {
            if (!(bestGames < (double)this.children[i].games)) continue;
            bestGames = this.children[i].games;
            selected = i;
        }
        return this.myActions.get(selected);
    }
}


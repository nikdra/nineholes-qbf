package main;

import java.util.List;

public class Transition {
    private List<Moves> moves;
    private List<Frame> frames;
    private String id;
    private String transition;

    public Transition(List<Moves> moves, List<Frame> frames, String id, boolean classic) throws TransitionException{
        this.moves = moves;
        this.frames = frames;
        this.id = id;
        if(moves.size() > 0) {
            gen_transition(classic);
        }
    }

    private void gen_transition(boolean classic) throws TransitionException {
        StringBuilder res = new StringBuilder();
        String[] tr_vars = new String[moves.size() + frames.size()];
        int k = 0;
        for(Moves move : moves) {
            res.append(move.generateMoveBlock());
            tr_vars[k++] = move.getAction_gate();
        }
        for(Frame frame : frames) {
            res.append(frame.generateFrameBlock());
            tr_vars[k++] = frame.getFr_gate();
        }
        if(classic) {
            try {
                String tr = id + " = " + Generator.n_ary(tr_vars, true);
                res.append(tr);
            } catch (OperatorException e) {
                throw new TransitionException("Could not generate transition cause " + e);
            }
        } else {
            res.deleteCharAt(res.length() - 1);
        }
        transition = res.toString();
    }

    public String getTransition() {
        return transition;
    }

}

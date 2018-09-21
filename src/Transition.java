import java.util.List;

public class Transition {
    private List<Moves> moves;
    private List<Frame> frames;
    private String id;
    private String transition;

    public Transition(List<Moves> moves, List<Frame> frames, String id) throws TransitionException{
        this.moves = moves;
        this.frames = frames;
        this.id = id;
        if(moves.size() > 0) {
            gen_transition();
        }
    }

    private void gen_transition() throws TransitionException {
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
        try {
            String tr = id + " = " + Generator.n_ary(tr_vars, true);
            res.append(tr);
            transition = res.toString();
        } catch (OperatorException e) {
            throw new TransitionException("Could not generate transition cause " + e);
        }
    }

    public String getTransition() {
        return transition;
    }

}

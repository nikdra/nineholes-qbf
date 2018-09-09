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
        if(moves.size() != frames.size()) {
            throw new TransitionException("Moves and frames do not have the same number of steps");
        }
        if(moves.size() > 0) {
            gen_transition();
        }
    }

    /**
     * Generate a transition given the clauses for action and frame axioms
     * @throws TransitionException in case something went wrong
     */
    private void gen_transition() throws TransitionException {
        StringBuilder res = new StringBuilder();
        String[] tr_vars = new String[moves.size() + frames.size()];
        int k = 0;
        for(Moves move : moves) {
            res.append(move.getPrecondition());
            res.append('\n');
            res.append(move.getEffect());
            res.append('\n');
            res.append(move.getInvariant());
            res.append('\n');
            res.append(move.getAction());
            res.append('\n');
            tr_vars[k++] = move.getAction_gate();
        }
        for(Frame frame : frames) {
            res.append(frame.getFr1());
            res.append('\n');
            res.append(frame.getFr2());
            res.append('\n');
            res.append(frame.getFr3());
            res.append('\n');
            res.append(frame.getFr());
            res.append('\n');
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

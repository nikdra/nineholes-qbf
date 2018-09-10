public class Moves {
    private int step;
    private String[][] vars = new String[9][9];
    private String[] vars_1d = new String[72];
    private Locations current;
    private Locations next;
    private String precondition;
    private String invariant;
    private String precondition_gate;
    private String effect_gate;
    private String invariant_gate;
    private String action_gate;
    private String action;

    public Moves(int step) throws OperatorException{
        this.step = step;
        current = new Locations(step);
        next = new Locations(step+1);
        precondition_gate = "mpr_" + step;
        effect_gate = "mme_" + step;
        invariant_gate = "min_" + step;
        gen_variables();
        gen_precondition();
        gen_invariant();
        action_gate = "ac_" + step;
        action = action_gate + " = " + "and(" + precondition_gate + "," + invariant_gate +")";
    }

    /**
     * Generate all move variables for this step
     */
    private void gen_variables() {
        int c = 0;
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                if(i != j) {
                    //move variables: m + first index is the step number, second is the current position, third the desired position
                    vars[i][j] = "m_" + step +"_" + i +"_"+ j;
                    vars_1d[c++] = "m_" + step +"_" + i +"_"+ j;
                }
            }
        }
    }

    /**
     * Generate a String representing the preconditions for all moves - it is a block that can be inserted into the encoding
     */
    private void gen_precondition() throws OperatorException {
        StringBuilder res = new StringBuilder();
        String[] prec_vars = new String[72];
        int c = 0;
        //move preconditions: mp + first index is the step number, second is the current position, third the desired position
        //help variables: precondition + he + counter
        if(step % 2 == 0) { //white move
            for(int i = 0; i < 9; i++) {
                for(int j = 0; j < 9; j++) {
                    if(i != j) {
                        String[] p_vars = {current.getWhite()[i], "-" + current.getBlack()[j], "-" + current.getWhite()[j]};
                        String p_help = "mp_" + step + "_" + i + "_" + j +"_he = " + Generator.n_ary(p_vars, true) + "\n";
                        res.append(p_help);
                        String[] p_gate = {"-" + vars[i][j], "mp_" + step + "_" + i + "_" + j +"_he"};
                        String precondition = "mp_" + step + "_" + i + "_" + j + " = " + Generator.n_ary(p_gate, false) + "\n";
                        res.append(precondition);
                        prec_vars[c++] = "mp_" + step + "_" + i + "_" + j;
                    }
                }
            }
        } else { //black move
            for(int i = 0; i < 9; i++) {
                for(int j = 0; j < 9; j++) {
                    if(i != j) {
                        String[] p_vars = {current.getBlack()[i], "-" + current.getWhite()[j], "-" + current.getBlack()[j]};
                        String p_help = "mp_" + step + "_" + i + "_" + j +"_he = " + Generator.n_ary(p_vars, true) + "\n";
                        res.append(p_help);
                        String[] p_gate = {"-" + vars[i][j], "mp_" + step + "_" + i + "_" + j +"_he"};
                        String precondition = "mp_" + step + "_" + i + "_" + j + " = " + Generator.n_ary(p_gate, false) + "\n";
                        res.append(precondition);
                        prec_vars[c++] = "mp_" + step + "_" + i + "_" + j;
                    }
                }
            }
        }
        String pr_s = precondition_gate + " = " + Generator.n_ary(prec_vars, true);
        precondition = res.append(pr_s).toString();
    }

    /**
     * The invariant is independent of whose move it is - it simply states that exactly one move must be made
     */
    private void gen_invariant() throws OperatorException {
        StringBuilder res = new StringBuilder();
        String[] amos = new String[2556];
        int amos_c = 0;
        for(int i = 0; i < vars_1d.length-1; i++) {
            for(int j = i+1; j < vars_1d.length; j++) {
                String[] mu_vars = {"-" + vars_1d[i], "-" + vars_1d[j]};
                String amo = "mu_" + step + "_" + amos_c + " = " + Generator.n_ary(mu_vars, false) + "\n";
                res.append(amo);
                amos[amos_c] =  "mu_" + step + "_" + amos_c;
                amos_c++;
            }
        }
        String amo_s = "amo_" + step + " = " + Generator.n_ary(amos, true) + "\n";
        res.append(amo_s);
        String alo_s = "alo_" + step + " = " + Generator.n_ary(vars_1d, false) + "\n";
        res.append(alo_s);
        String[] inv_vars = {"amo_" + step, "alo_" + step};
        String in_s = invariant_gate + " = " + Generator.n_ary(inv_vars, true);
        invariant = res.append(in_s).toString();
    }

    public String generateMoveBlock() {
        StringBuilder res = new StringBuilder();
        res.append(getPrecondition());
        res.append('\n');
        res.append(getInvariant());
        res.append('\n');
        res.append(getAction());
        res.append('\n');
        return res.toString();
    }

    public String getPrecondition() {
        return precondition;
    }

    public String getInvariant() {
        return invariant;
    }

    public int getStep() {
        return step;
    }

    public String[][] getVars() {
        return vars;
    }

    public String[] getVars_1d() {
        return vars_1d;
    }

    public Locations getCurrent() {
        return current;
    }

    public Locations getNext() {
        return next;
    }

    public String getPrecondition_gate() {
        return precondition_gate;
    }

    public String getEffect_gate() {
        return effect_gate;
    }

    public String getInvariant_gate() {
        return invariant_gate;
    }

    public String getAction_gate() {
        return action_gate;
    }

    public String getAction() {
        return action;
    }
}

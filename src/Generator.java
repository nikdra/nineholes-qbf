import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Generator {

    private static int k = 5; //assumed to be odd

    private enum Goal_mode {
        CLASSIC, NESTED
    }

    private static Goal_mode mode;

    public static void main(String[] args) {
        //location variables: first index is the step number, second index is the position
        //the following two line represent the initial condition on the board
        String[] w_0 = {"-w_0_0", "-w_0_1", "-w_0_2", "-w_0_3", "w_0_4", "w_0_5", "-w_0_6", "w_0_7", "-w_0_8"};
        String[] b_0 = {"b_0_0", "b_0_1", "-b_0_2", "b_0_3", "-b_0_4", "-b_0_5", "-b_0_6", "-b_0_7", "-b_0_8"};

        mode = Goal_mode.NESTED;

        //initial condition
        String i_w;
        String i_b;
        Transition tr_w;
        Transition tr_b;
        Goal g;
        try {
            i_w = "i_w = " + n_ary(w_0, true);
            i_b = "i_b = " + n_ary(b_0, true);
        } catch (OperatorException e) {
            System.err.println(e.getMessage());
            return;
        }
        try {
            List<Moves> m_w = new LinkedList<>();
            List<Frame> fr_w = new LinkedList<>();
            List<Moves> m_b = new LinkedList<>();
            List<Frame> fr_b = new LinkedList<>();
            for (int i = 0; i < k; i++) {
                Moves m = new Moves(i);
                fr_w.add(new Frame(m));
                if (i % 2 == 0) {
                    m_w.add(m);
                } else {
                    m_b.add(m);
                }
            }
            tr_b = new Transition(m_b, fr_b, "tr_b");
            tr_w = new Transition(m_w, fr_w, "tr_w");
            g = new Goal(k);
        } catch (OperatorException | TransitionException e) {
            System.err.println(e.getMessage());
            return;
        }
        String body;
        if (k == 1) { //this is the same for both variations of the matrix
            body = i_w + "\n" + i_b + "\n" + tr_w.getTransition() + "\n" + g.getGoal() + '\n';
            body += "out = and(i_w,i_b,tr_w,g)";
        } else { //generate all necessary gates
            body = i_w + "\n" + i_b + "\n" + tr_b.getTransition() + "\n" + tr_w.getTransition() + "\n" + g.getGoal() + '\n';
        }
        if (mode == Goal_mode.CLASSIC) { //k > 1 and classic formulation
            body += "implication = or(-tr_b,g)\n";
            body += "out = and(i_w,i_b,tr_w,implication)";
        } else {
            int c = 1;
            int steps = k-2;
            body += "ga0 = and(-gb_" + (k-1) + ",fr_gate_" + (steps) + ",ac_" + (k-1) +",fr_" + (k-1) + ",gw_" + k + ")\n"; //innermost clause
            while(steps > 0) {
                body += "ga" + c + "= or(-" + "ac_" + steps + ",ga" + (c-1) + ")\n";
                body += "ga" + (c+1) + "= or(gw_" + steps + ", ga" + c + ")\n";
                steps--;
                c = c + 2;
                if(steps > 0) {
                    body += "ga" + c + " = and(-gb_" + steps + ",fr_gate_" + (steps-1) + ",ac_" + steps + ",fr_" + steps + ",ga" + (c-1) + ")\n";
                    c++;
                    steps--;
                }
            }
            body += "out = and(i_w,i_b,ac_0,fr_0,ga" + --c + ")";
        }

        StringBuilder res = new StringBuilder();
        res.append("#QCIR-G14 \n");
        for (int i = 0; i < k; i++) {
            try {
                Moves turn = new Moves(i);
                if (i % 2 == 0) { //white variables
                    res.append(quantify(turn.getCurrent().getVars(), true));
                    res.append('\n');
                    res.append(quantify(turn.getVars_1d(), true));
                    res.append('\n');
                    res.append(quantify(turn.getNext().getVars(), true));
                    res.append('\n');
                } else { //black variables
                    res.append(quantify(turn.getVars_1d(), false));
                    res.append('\n');
                }
            } catch (OperatorException o) {
                System.err.println(o.getMessage());
                return;
            }
        }
        res.append("output(out) \n");
        res.append(body);
        res.append("\n");
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter("loss_nested_" + k + ".qcir"); //TODO something generic
            bw = new BufferedWriter(fw);
            bw.write(res.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Connect the given variables to an and-clause. Array of variables must not be null, as well as its contents
     *
     * @param vars the vars to be connected
     * @param and  1 if n-ary and-clause, or otherwise
     * @return the String representation of the clause
     * @throws OperatorException in case less than two variables are present
     */
    public static String n_ary(String[] vars, boolean and) throws OperatorException {
        StringBuilder ret = new StringBuilder();
        if (vars == null) {
            throw new OperatorException("Array of given variables was null");
        }
        if (and) {
            ret.append("and(");
        } else {
            ret.append("or(");
        }
        for (int i = 0; i < vars.length; i++) {
            if (vars[i] == null) {
                throw new OperatorException("Variable was null");
            }
            ret.append(vars[i]);
            if (i != vars.length - 1) {
                ret.append(",");
            }
        }
        return ret.append(')').toString();
    }

    /**
     * Connect two variables to an XOR expression.
     *
     * @param var1 the first variable
     * @param var2 the second variable
     * @return the String representation of the clause
     */
    public static String xor(String var1, String var2) {
        return "xor(" + var1 + "," + var2 + ")";
    }

    /**
     * Quantify the given variables in a quantifier block
     *
     * @param vars        the variables to be quantified. must not be null, as well as the contents
     * @param existential true iff existential, universal otherwise
     * @return the String representation of the quantifier block
     */
    public static String quantify(String[] vars, boolean existential) {
        StringBuilder ret = new StringBuilder();
        if (existential) {
            ret.append("exists(");
        } else {
            ret.append("forall(");
        }
        for (int i = 0; i < vars.length; i++) {
            ret.append(vars[i]);
            if (i != vars.length - 1) {
                ret.append(",");
            }
        }
        return ret.append(')').toString();
    }
}

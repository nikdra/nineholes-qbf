import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Generator {

    private static int k = 3; //assumed to be odd

    /**
     * Generate a qbf encoding for nine holes with the upper bound on moves k.
     * The result is written to encoding.qcir in this project's folder
     * @param args //TODO write this program as a command line application
     */
    public static void main(String[] args) {
        //location variables: first index is the step number, second index is the position
        //the following two line represent the initial condition on the board
        String[] w_0 = {"w_0_1", "w_0_2", "-w_0_3", "-w_0_4", "-w_0_5", "w_0_6", "-w_0_7", "-w_0_8", "-w_0_9"};
        String[] b_0 = {"-b_0_1", "-b_0_2", "-b_0_3", "b_0_4", "b_0_5", "-b_0_6", "b_0_7", "-b_0_8", "-b_0_9"};

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
        try { //generate move,transition and goal objects
            List<Moves> m_w = new LinkedList<>();
            List<Frame> fr_w = new LinkedList<>();
            List<Moves> m_b = new LinkedList<>();
            List<Frame> fr_b = new LinkedList<>();
            for (int i = 0; i < k; i++) {
                Moves m = new Moves(i);
                if (i % 2 == 0) {
                    m_w.add(m);
                    fr_w.add(new Frame(m));
                } else {
                    m_b.add(m);
                    fr_b.add(new Frame(m));
                }
            }
            tr_w = new Transition(m_w, fr_w, "tr_w");
            tr_b = new Transition(m_b, fr_b, "tr_b");
            g = new Goal(k);
        } catch (OperatorException | TransitionException e) {
            System.err.println(e.getMessage());
            return;
        }
        String body; //generate the body (or matrix) of the qbf
        if(k != 1) {
            body = i_w + "\n" + i_b + "\n" + tr_w.getTransition() + "\n" + tr_b.getTransition() + "\n"+  g.getGoal() + '\n';
            body += "implication = or(-tr_b,g)\n";
            body += "out = and(i_w,i_b,tr_w,implication)";
        } else {
            body = i_w + "\n" + i_b + "\n" + tr_w.getTransition() + "\n" +  g.getGoal() + '\n';
            body += "out = and(i_w,i_b,tr_w,g)";
        }
        //generate the quantifier block
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
        //write the generated string formula to a file
        try {
            fw = new FileWriter("encoding.qcir");
            bw = new BufferedWriter(fw);
            bw.write(res.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bw != null) {
                    bw.close();
                }
                if(fw != null) {
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
        if (vars.length < 2) {
            throw new OperatorException("Can not connect less than two variables to an and-clause");
        }
        StringBuilder ret = new StringBuilder();
        if (and) {
            ret.append("and(");
        } else {
            ret.append("or(");
        }
        for (int i = 0; i < vars.length; i++) {
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
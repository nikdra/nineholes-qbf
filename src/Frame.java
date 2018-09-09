public class Frame {
    private int step;
    private Moves moves;
    private String fr1_gate;
    private String fr2_gate;
    private String fr3_gate;
    private String fr1;
    private String fr2;
    private String fr3;
    private String fr;
    private String fr_gate;

    public String getFr() {
        return fr;
    }

    public String getFr_gate() {
        return fr_gate;
    }

    public Frame(Moves moves) throws OperatorException {
        this.moves = moves;
        this.step = moves.getStep();
        this.fr1_gate = "fr1_" + step;
        this.fr2_gate = "fr2_" + step;
        this.fr3_gate = "fr3_" + step;
        gen_frame_one();
        gen_frame_two();
        gen_frame_three();
        fr_gate = "fr_" + step;
        fr = fr_gate + " = " + "and(" + fr1_gate + "," + fr2_gate +  "," + fr3_gate + ")";
    }

    /**
     * Generate a string representing the first frame axiom - if a counter is not moved, it remains at its position
     * @throws OperatorException in case something went wrong
     */
    public void gen_frame_one() throws OperatorException {
        StringBuilder res = new StringBuilder();
        String[] f1_vars = new String[9];
        if(step % 2 == 0) { //white move
            for (int i = 0; i < moves.getCurrent().getWhite().length; i++) {
                String[] moves_i = new String[8];
                int j = 1;
                int k = 0;
                while(k < moves_i.length) {
                    if(i != j-1) {
                        moves_i[k] = "-" + moves.getVars()[i+1][j];
                        k++;
                    }
                    j++;
                }
                String neg_neg_move = "fr1_" + step +"_" + (i+1) + "nnm  = " + Generator.n_ary(moves_i, true) +"\n";
                String neg_move = "fr1_" + step +"_" + (i+1) + "nm  = " + "and(" + moves.getCurrent().getWhite()[i] + "," + "fr1_" + step +"_" + (i+1) + "nnm) \n";
                String f1 = "fr1_" + step +"_" + (i+1) + " = " + "or(-" + "fr1_" + step +"_" + (i+1) + "nm," + moves.getNext().getWhite()[i] +") \n";
                res.append(neg_neg_move);
                res.append(neg_move);
                res.append(f1);
                f1_vars[i] = "fr1_" + step +"_" + (i+1);
            }
        } else { //black move
            for (int i = 0; i < moves.getCurrent().getBlack().length; i++) {
                String[] moves_i = new String[8];
                int j = 1;
                int k = 0;
                while(k < moves_i.length) {
                    if(i != j-1) {
                        moves_i[k] = "-" + moves.getVars()[i+1][j];
                        k++;
                    }
                    j++;
                }
                String neg_neg_move = "fr1_" + step +"_" + (i+1) + "nnm  = " + Generator.n_ary(moves_i, true) +"\n";
                String neg_move = "fr1_" + step +"_" + (i+1) + "nm  = " + "and(" + moves.getCurrent().getBlack()[i] + "," + "fr1_" + step +"_" + (i+1) + "nnm) \n";
                String f1 = "fr1_" + step +"_" + (i+1) + " = " + "or(-" + "fr1_" + step +"_" + (i+1) + "nm," + moves.getNext().getBlack()[i] +") \n";
                res.append(neg_neg_move);
                res.append(neg_move);
                res.append(f1);
                f1_vars[i] = "fr1_" + step +"_" + (i+1);
            }
        }
        String fr1_s = fr1_gate + " = " + Generator.n_ary(f1_vars, true);
        fr1 = res.append(fr1_s).toString();
    }

    /**
     * Generate a string representing the second frame axiom - if a counter is not moved to a position, it is not there at the next step
     * @throws OperatorException in case something went wrong
     */
    public void gen_frame_two() throws OperatorException {
        StringBuilder res = new StringBuilder();
        String[] f2_vars = new String[9];
        if(step % 2 == 0) { //white move
            for (int i = 0; i < moves.getCurrent().getWhite().length; i++) {
                String[] moves_i = new String[8];
                int j = 1;
                int k = 0;
                while(k < moves_i.length) {
                    if(i != j-1) {
                        moves_i[k] = "-" + moves.getVars()[j][i+1];
                        k++;
                    }
                    j++;
                }
                String neg_neg_move = "fr2_" + step +"_" + (i+1) + "nnm  = " + Generator.n_ary(moves_i, true) +"\n";
                String neg_move = "fr2_" + step +"_" + (i+1) + "nm  = " + "and(-" + moves.getCurrent().getWhite()[i] + "," + "fr2_" + step +"_" + (i+1) + "nnm) \n";
                String f2 = "fr2_" + step +"_" + (i+1) + " = " + "or(-" + "fr2_" + step +"_" + (i+1) + "nm,-" + moves.getNext().getWhite()[i] +") \n";
                res.append(neg_neg_move);
                res.append(neg_move);
                res.append(f2);
                f2_vars[i] = "fr2_" + step +"_" + (i+1);
            }
        } else { //black move
            for (int i = 0; i < moves.getCurrent().getBlack().length; i++) {
                String[] moves_i = new String[8];
                int j = 1;
                int k = 0;
                while(k < moves_i.length) {
                    if(i != j-1) {
                        moves_i[k] = "-" + moves.getVars()[j][i+1];
                        k++;
                    }
                    j++;
                }
                String neg_neg_move = "fr2_" + step +"_" + (i+1) + "nnm  = " + Generator.n_ary(moves_i, true) +"\n";
                String neg_move = "fr2_" + step +"_" + (i+1) + "nm  = " + "and(-" + moves.getCurrent().getBlack()[i] + "," + "fr2_" + step +"_" + (i+1) + "nnm) \n";
                String f2 = "fr2_" + step +"_" + (i+1) + " = " + "or(-" + "fr2_" + step +"_" + (i+1) + "nm,-" + moves.getNext().getBlack()[i] +") \n";
                res.append(neg_neg_move);
                res.append(neg_move);
                res.append(f2);
                f2_vars[i] = "fr2_" + step +"_" + (i+1);
            }
        }
        String fr2_s = fr2_gate + " = " + Generator.n_ary(f2_vars, true);
        fr2 = res.append(fr2_s).toString();
    }

    /**
     * Generate a string representing the thrid frame axiom - a player's counters remain unchanged when it is not their move
     * @throws OperatorException in case something went wrong
     */
    public void gen_frame_three() throws OperatorException{
        StringBuilder res = new StringBuilder();
        String[] fr3_vars = new String[9];
        if(step % 2 == 0) { //white move - black counters must be unchanged
            for(int i = 0; i < moves.getCurrent().getBlack().length; i++) {
                String un_h = "fr3_" + step + "_" + (i+1) + " = " + Generator.xor(moves.getCurrent().getBlack()[i], moves.getNext().getBlack()[i]) + "\n";
                fr3_vars[i] = "-fr3_" + step + "_" + (i+1);
                res.append(un_h);
            }
        } else { //black move - white counters must be unchanged
            for(int i = 0; i < moves.getCurrent().getWhite().length; i++) {
                String un_h = "fr3_" + step + "_" + (i+1) + " = " + Generator.xor(moves.getCurrent().getWhite()[i], moves.getNext().getWhite()[i]) + "\n";
                fr3_vars[i] = "-fr3_" + step + "_" + (i+1);
                res.append(un_h);
            }
        }
        String fr3_s = fr3_gate + " = " + Generator.n_ary(fr3_vars, true);
        fr3 = res.append(fr3_s).toString();
    }

    public String getFr1_gate() {
        return fr1_gate;
    }

    public String getFr2_gate() {
        return fr2_gate;
    }

    public String getFr3_gate() {
        return fr3_gate;
    }

    public String getFr1() {
        return fr1;
    }

    public String getFr2() {
        return fr2;
    }

    public String getFr3() {
        return fr3;
    }
}

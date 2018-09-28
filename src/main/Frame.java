package main;

public class Frame {
    private int step;
    private Moves moves;
    private String fr1_gate;
    private String fr2_gate;
    private String fr3_gate;
    private String fr4_gate;
    private String fr1;
    private String fr2;
    private String fr3;
    private String fr4;
    private String fr;
    private String fr_gate;

    public Frame(Moves moves, boolean classic) throws OperatorException {
        this.moves = moves;
        this.step = moves.getStep();
        this.fr1_gate = "fr1_" + step;
        this.fr2_gate = "fr2_" + step;
        this.fr3_gate = "fr3_" + step;
        this.fr4_gate = "fr4_" + step;
        gen_frame_one();
        gen_frame_two();
        gen_frame_three();
        gen_frame_four();
        fr_gate = "fr_" + step;
        if(step % 2 == 0 || !classic) {
            fr = fr_gate + " = " + "and(" + fr1_gate + "," + fr2_gate + "," + fr3_gate + "," + fr4_gate + ")";
        } else { //a frame for the black player. The white player only has to check the black player's moves if the move was legal. only necessary in classic encoding
            fr = "fr_gate_" + step + " = " + "and(" + fr1_gate + "," + fr2_gate + "," + fr3_gate + "," + fr4_gate + ")\n";
            fr += fr_gate + " = " + "or(-ac_" + step + "," + "fr_gate_" + step + ")";
        }
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
                int j = 0;
                int k = 0;
                while(k < moves_i.length) {
                    if(i != j) {
                        moves_i[k] = "-" + moves.getVars()[i][j];
                        k++;
                    }
                    j++;
                }
                String neg_neg_move = "fr1_" + step +"_" + i + "nnm  = " + Generator.n_ary(moves_i, true) +"\n";
                String neg_move = "fr1_" + step +"_" + i + "nm  = " + "and(" + moves.getCurrent().getWhite()[i] + "," + "fr1_" + step +"_" + i + "nnm) \n";
                String f1 = "fr1_" + step +"_" + i + " = " + "or(-" + "fr1_" + step +"_" + i + "nm," + moves.getNext().getWhite()[i] +") \n";
                res.append(neg_neg_move);
                res.append(neg_move);
                res.append(f1);
                f1_vars[i] = "fr1_" + step +"_" + i;
            }
        } else { //black move
            for (int i = 0; i < moves.getCurrent().getBlack().length; i++) {
                String[] moves_i = new String[8];
                int j = 0;
                int k = 0;
                while(k < moves_i.length) {
                    if(i != j) {
                        moves_i[k] = "-" + moves.getVars()[i][j];
                        k++;
                    }
                    j++;
                }
                String neg_neg_move = "fr1_" + step +"_" + i + "nnm  = " + Generator.n_ary(moves_i, true) +"\n";
                String neg_move = "fr1_" + step +"_" + i + "nm  = " + "and(" + moves.getCurrent().getBlack()[i] + "," + "fr1_" + step +"_" + i + "nnm) \n";
                String f1 = "fr1_" + step +"_" + i + " = " + "or(-" + "fr1_" + step +"_" + i + "nm," + moves.getNext().getBlack()[i] +") \n";
                res.append(neg_neg_move);
                res.append(neg_move);
                res.append(f1);
                f1_vars[i] = "fr1_" + step +"_" + i;
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
                int j = 0;
                int k = 0;
                while(k < moves_i.length) {
                    if(i != j) {
                        moves_i[k] = "-" + moves.getVars()[j][i];
                        k++;
                    }
                    j++;
                }
                String neg_neg_move = "fr2_" + step +"_" + i + "nnm  = " + Generator.n_ary(moves_i, true) +"\n";
                String neg_move = "fr2_" + step +"_" + i + "nm  = " + "and(-" + moves.getCurrent().getWhite()[i] + "," + "fr2_" + step +"_" + i + "nnm) \n";
                String f2 = "fr2_" + step +"_" + i + " = " + "or(-" + "fr2_" + step +"_" + i + "nm,-" + moves.getNext().getWhite()[i] +") \n";
                res.append(neg_neg_move);
                res.append(neg_move);
                res.append(f2);
                f2_vars[i] = "fr2_" + step +"_" + i;
            }
        } else { //black move
            for (int i = 0; i < moves.getCurrent().getBlack().length; i++) {
                String[] moves_i = new String[8];
                int j = 0;
                int k = 0;
                while(k < moves_i.length) {
                    if(i != j) {
                        moves_i[k] = "-" + moves.getVars()[j][i];
                        k++;
                    }
                    j++;
                }
                String neg_neg_move = "fr2_" + step +"_" + i + "nnm  = " + Generator.n_ary(moves_i, true) +"\n";
                String neg_move = "fr2_" + step +"_" + i + "nm  = " + "and(-" + moves.getCurrent().getBlack()[i] + "," + "fr2_" + step +"_" + i + "nnm) \n";
                String f2 = "fr2_" + step +"_" + i + " = " + "or(-" + "fr2_" + step +"_" + i + "nm,-" + moves.getNext().getBlack()[i] +") \n";
                res.append(neg_neg_move);
                res.append(neg_move);
                res.append(f2);
                f2_vars[i] = "fr2_" + step +"_" + i;
            }
        }
        String fr2_s = fr2_gate + " = " + Generator.n_ary(f2_vars, true);
        fr2 = res.append(fr2_s).toString();
    }

    /**
     * Generate a string representing the third frame axiom - a player's counters remain unchanged when it is not their
     * move. Since the existential player (white) controls the board, they are responsible for the location of pieces on
     * the board.
     * @throws OperatorException in case something went wrong
     */
    public void gen_frame_three() throws OperatorException{
        StringBuilder res = new StringBuilder();
        String[] fr3_vars = new String[9];
        if(step % 2 == 0) { //white move - black counters must be unchanged
            for(int i = 0; i < moves.getCurrent().getBlack().length; i++) {
                String un_h = "fr3_" + step + "_" + i + " = " + Generator.xor(moves.getCurrent().getBlack()[i], moves.getNext().getBlack()[i]) + "\n";
                fr3_vars[i] = "-fr3_" + step + "_" + i;
                res.append(un_h);
            }
        } else { //black move - white counters must be unchanged
            for(int i = 0; i < moves.getCurrent().getWhite().length; i++) {
                String un_h = "fr3_" + step + "_" + i + " = " + Generator.xor(moves.getCurrent().getWhite()[i], moves.getNext().getWhite()[i]) + "\n";
                fr3_vars[i] = "-fr3_" + step + "_" + i;
                res.append(un_h);
            }
        }
        String fr3_s = fr3_gate + " = " + Generator.n_ary(fr3_vars, true);
        fr3 = res.append(fr3_s).toString();
    }

    /**
     * Generate a String representing the effects for all moves - it is a block that can be inserted into the encoding
     */
    public void gen_frame_four() throws OperatorException{
        StringBuilder res = new StringBuilder();
        String[] fr4_vars = new String[72];
        int c = 0;
        //move effect: me + first index is the step number, second is the current position, third the desired position
        //help variables: effect + he + counter
        if(step % 2 == 0) { //white move
            for(int i = 0; i < 9; i++) {
                for(int j = 0; j < 9; j++) {
                    if(i != j) {
                        String[] e_vars = {"-" + moves.getNext().getWhite()[i], moves.getNext().getWhite()[j]};
                        String e_help = "me_" + step + "_" + i + "_" + j +"_he = " + Generator.n_ary(e_vars, true) + "\n";
                        res.append(e_help);
                        String[] e_gate = {"-" + moves.getVars()[i][j], "me_" + step + "_" + i + "_" + j +"_he"};
                        String effect = "me_" + step + "_" + i + "_" + j + " = " + Generator.n_ary(e_gate, false) + "\n";
                        res.append(effect);
                        fr4_vars[c++] = "me_" + step + "_" + i + "_" + j;
                    }
                }
            }
        } else { //black move
            for(int i = 0; i < 9; i++) {
                for(int j = 0; j < 9; j++) {
                    if(i != j) {
                        String[] e_vars = {"-" + moves.getNext().getBlack()[i], moves.getNext().getBlack()[j]};
                        String e_help = "me_" + step + "_" + i + "_" + j +"_he = " + Generator.n_ary(e_vars, true) + "\n";
                        res.append(e_help);
                        String[] e_gate = {"-" + moves.getVars()[i][j], "me_" + step + "_" + i + "_" + j +"_he"};
                        String effect = "me_" + step + "_" + i + "_" + j + " = " + Generator.n_ary(e_gate, false) + "\n";
                        res.append(effect);
                        fr4_vars[c++] = "me_" + step + "_" + i + "_" + j;
                    }
                }
            }
        }
        String fr4_s = fr4_gate + " = " + Generator.n_ary(fr4_vars, true);
        fr4 = res.append(fr4_s).toString();

    }

    public String generateFrameBlock() {
        StringBuilder res = new StringBuilder();
        res.append(getFr1());
        res.append('\n');
        res.append(getFr2());
        res.append('\n');
        res.append(getFr3());
        res.append('\n');
        res.append(getFr4());
        res.append('\n');
        res.append(getFr());
        res.append('\n');
        return res.toString();
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

    public String getFr4() {
        return fr4;
    }

    public String getFr4_gate() {
        return fr4_gate;
    }

    public String getFr() {
        return fr;
    }

    public String getFr_gate() {
        return fr_gate;
    }
}

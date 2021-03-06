package main;

public class Goal {
    private int steps;
    private String goal_gate = "g";
    private String goal;
    private String[] wg_h;
    private String[] bg_h;

    public Goal(int steps, boolean classic) throws OperatorException{
        this.steps = steps;
        wg_h = new String[steps+1];
        bg_h = new String[steps+1];
        if (classic) {
            gen_goal_classic();
        } else {
            gen_goal_nested();
        }
    }

    private void gen_goal_classic() throws OperatorException{
        StringBuilder res = new StringBuilder();
        String[] goal_vars = new String[steps/2+2];
        int k = 0;
        for(int i = 0; i < steps; i = i + 2) { //add clauses for black goal
            res.append(gen_black_goal_step(i));
            res.append('\n');
        }
        res.append(gen_white_goal_step(0)); //white can win on step 0 (if the initial condition is set to a win position)
        res.append('\n');
        String g_0 = "g_" + 0 + " = and(" + wg_h[0] +")\n";
        res.append(g_0);
        goal_vars[k++] = "g_" + 0;
        for(int i = 1; i <= steps; i = i + 2) {
            res.append(gen_white_goal_step(i));
            res.append('\n');
            String g_s = "g_" + i + " =  and(" + wg_h[i];
            res.append(g_s);
            for(int j = 0; j < i; j = j+2) {
                res.append(',');
                res.append(bg_h[j]);
            }
            res.append(")\n");
            goal_vars[k++] = "g_" + i;
        }
        String goal_s;
        if(k != 1) {
            goal_s = goal_gate + " = " + Generator.n_ary(goal_vars, false);
        } else {
            goal_s = goal_gate + " = or(" + goal_vars[0] + "," + goal_vars[1] +")";
        }
        goal = res.append(goal_s).toString();
    }

    private void gen_goal_nested() throws OperatorException{
        StringBuilder res = new StringBuilder();
        int k = 0;
        for(int i = 0; i <= steps; i++) { //add clauses for black goal
            if(i == 0) {
                res.append(gen_black_goal_step(i));
                res.append('\n');
                res.append(gen_white_goal_step(i));
            } else if(i % 2 == 0) {
                res.append(gen_black_goal_step(i));
            } else {
                res.append(gen_white_goal_step(i));
            }
            res.append('\n');
        }
        res.deleteCharAt(res.length() - 1);
        goal = res.toString();
    }

    private String gen_white_goal_step(int step) throws OperatorException {
        StringBuilder res = new StringBuilder();
        Locations locations = new Locations(step);
        String[] l1 = {locations.getWhite()[0], locations.getWhite()[1], locations.getWhite()[2]};
        String[] l2 = {locations.getWhite()[0], locations.getWhite()[3], locations.getWhite()[6]};
        String[] l3 = {locations.getWhite()[3], locations.getWhite()[4], locations.getWhite()[5]};
        String[] l4 = {locations.getWhite()[1], locations.getWhite()[4], locations.getWhite()[7]};
        String[] l5 = {locations.getWhite()[6], locations.getWhite()[7], locations.getWhite()[8]};
        String[] l6 = {locations.getWhite()[2], locations.getWhite()[5], locations.getWhite()[8]};
        String l1_s = "gw_" + step + "_l1" + " = " + Generator.n_ary(l1, true) +"\n";
        String l2_s = "gw_" + step + "_l2" + " = " + Generator.n_ary(l2, true) +"\n";
        String l3_s = "gw_" + step + "_l3" + " = " + Generator.n_ary(l3, true) +"\n";
        String l4_s = "gw_" + step + "_l4" + " = " + Generator.n_ary(l4, true) +"\n";
        String l5_s = "gw_" + step + "_l5" + " = " + Generator.n_ary(l5, true) +"\n";
        String l6_s = "gw_" + step + "_l6" + " = " + Generator.n_ary(l6, true) +"\n";
        res.append(l1_s);
        res.append(l2_s);
        res.append(l3_s);
        res.append(l4_s);
        res.append(l5_s);
        res.append(l6_s);
        String[] gw_s_vars = {"gw_" + step + "_l1", "gw_" + step + "_l2","gw_" + step + "_l3","gw_" + step + "_l4",
                "gw_" + step + "_l5","gw_" + step + "_l6"};
        String gw_s = "gw_" + step + " = " + Generator.n_ary(gw_s_vars, false);
        res.append(gw_s);
        wg_h[step] =  "gw_" + step;
        return res.toString();
    }

    private String gen_black_goal_step(int step) throws OperatorException {
        StringBuilder res = new StringBuilder();
        Locations locations = new Locations(step);
        String[] l1 = {locations.getBlack()[0], locations.getBlack()[1], locations.getBlack()[2]};
        String[] l2 = {locations.getBlack()[0], locations.getBlack()[3], locations.getBlack()[6]};
        String[] l3 = {locations.getBlack()[3], locations.getBlack()[4], locations.getBlack()[5]};
        String[] l4 = {locations.getBlack()[1], locations.getBlack()[4], locations.getBlack()[7]};
        String[] l5 = {locations.getBlack()[6], locations.getBlack()[7], locations.getBlack()[8]};
        String[] l6 = {locations.getBlack()[2], locations.getBlack()[5], locations.getBlack()[8]};
        String l1_s = "gb_" + step + "_l1" + " = " + Generator.n_ary(l1, true) +"\n";
        String l2_s = "gb_" + step + "_l2" + " = " + Generator.n_ary(l2, true) +"\n";
        String l3_s = "gb_" + step + "_l3" + " = " + Generator.n_ary(l3, true) +"\n";
        String l4_s = "gb_" + step + "_l4" + " = " + Generator.n_ary(l4, true) +"\n";
        String l5_s = "gb_" + step + "_l5" + " = " + Generator.n_ary(l5, true) +"\n";
        String l6_s = "gb_" + step + "_l6" + " = " + Generator.n_ary(l6, true) +"\n";
        res.append(l1_s);
        res.append(l2_s);
        res.append(l3_s);
        res.append(l4_s);
        res.append(l5_s);
        res.append(l6_s);
        String[] gw_s_vars = {"gb_" + step + "_l1", "gb_" + step + "_l2","gb_" + step + "_l3","gb_" + step + "_l4",
                "gb_" + step + "_l5","gb_" + step + "_l6"};
        String gw_s = "gb_" + step + " = " + Generator.n_ary(gw_s_vars, false);
        res.append(gw_s);
        bg_h[step] =  "-gb_" + step;
        return res.toString();
    }

    public String getGoal_gate() {
        return goal_gate;
    }

    public String getGoal() {
        return goal;
    }
}

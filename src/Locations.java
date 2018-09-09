public class Locations {

    private String[] vars = new String[18];
    private String[] white = new String[9];
    private String[] black = new String[9];

    public Locations(int step) {
        for(int i = 0; i < 9; i++) {
           white[i] = "w_" + step + "_" + i;
           black[i] = "b_" + step + "_" + i;
        }
        System.arraycopy(white, 0, vars, 0, 9);
        System.arraycopy(black, 0, vars, 9, 9);
    }

    public String[] getVars() {
        return vars;
    }

    public String[] getWhite() {
        return white;
    }

    public String[] getBlack() {
        return black;
    }
}
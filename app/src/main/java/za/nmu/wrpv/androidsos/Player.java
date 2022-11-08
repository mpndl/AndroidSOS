package za.nmu.wrpv.androidsos;

public class Player {
    public int color;
    public boolean isCurrentPlayer = false;
    public int points = 0;
    public int name;
    public Player(int color, int name) {
        this.color = color;
        this.name = name;
    }
}

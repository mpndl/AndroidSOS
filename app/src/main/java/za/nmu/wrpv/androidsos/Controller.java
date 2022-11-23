package za.nmu.wrpv.androidsos;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.gridlayout.widget.GridLayout;
import android.widget.TextView;

import java.util.Locale;

public class Controller {
    public MainActivity context;
    public GridLayout grid;
    public Square[][] squares = new Square[5][5];
    public Player player1 = new Player(Color.BLUE, 1);
    public Player player2 = new Player(Color.YELLOW, 2);
    public TextView player1TV;
    public TextView player2TV;
    public TextView player1Points;
    public TextView player2Points;
    public TextView winner;
    public Button s_button;
    public Button o_button;
    public Button clickedButton;
    public Button reset;
    public int turns = 0;
    public Controller(MainActivity mainActivity) {
        context = mainActivity;
        player1.isCurrentPlayer = true;
        player1TV = context.findViewById(R.id.player1);
        player1TV.setBackgroundColor(Color.GRAY);
        player2TV = context.findViewById(R.id.player2);
        player1TV.setTextColor(player1.color);
        player2TV.setTextColor(player2.color);
        player1Points = context.findViewById(R.id.player1points);
        player2Points = context.findViewById(R.id.player2points);
        winner = context.findViewById(R.id.winner);

        grid = context.findViewById(R.id.grid);
        s_button = context.findViewById(R.id.s_button);
        o_button = context.findViewById(R.id.o_button);
        reset = context.findViewById(R.id.reset);
        reset.setOnClickListener(this::onResetClicked);

        s_button.setOnClickListener(this::onSOButtonClicked);
        o_button.setOnClickListener(this::onSOButtonClicked);

        populate();
    }

    private void populate() {
        grid.removeAllViews();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Square square = new Square();
                Button button = new Button(context);
                button.setOnClickListener(this::onSquareClicked);

                square.color = Color.WHITE;
                square.button = button;
                //square.id = i + "" + j;

                squares[i][j] = square;
                grid.addView(square.button);
            }
        }
    }

    private void changeCurrentPlayer() {
        if(player1.isCurrentPlayer) {
            player1.isCurrentPlayer = false;
            player2.isCurrentPlayer = true;

            player2TV.setBackgroundColor(Color.GRAY);
            player1TV.setBackgroundColor(Color.TRANSPARENT);
        }
        else {
            player1.isCurrentPlayer = true;
            player2.isCurrentPlayer = false;
            player1TV.setBackgroundColor(Color.GRAY);
            player2TV.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private int getCurrentPlayerColor() {
        if(player1.isCurrentPlayer) return player1.color;
        else return player2.color;
    }

    private Player getCurrentPlayer() {
        if(player1.isCurrentPlayer) return player1;
        else return player2;
    }

    private void onSOButtonClicked(View view) {
        Button clickedSOButton = (Button) view;
        clickedButton.setText(clickedSOButton.getText());
        clickedButton.setTextColor(getCurrentPlayerColor());
        Square clickedSquare = getSquare(clickedButton);
        if(clickedSquare != null) {
            clickedSquare.played = true;
            clickedSquare.id = clickedSOButton.getText().toString();
            clickedSquare.color = getCurrentPlayerColor();
        }
        s_button.setEnabled(true);
        o_button.setEnabled(true);
        disableAll(false);

        turns++;

        if(turns == 25)
            displayWinner();

        checkHorizontal();
        checkVertical();
        checkDiagonal();
        changeCurrentPlayer();
    }

    public void onResetClicked(View view) {
        if(getCurrentPlayer().name == 2)
            changeCurrentPlayer();
        populate();
        player1.points = 0;
        player2.points = 0;
        player2Points.setText("Points : 0");
        player1Points.setText("Points : 0");
        winner.setText("");
    }

    private void displayWinner() {
        disableAll();
        winner.setTextColor(Color.GREEN);
        if(player1.points > player2.points)
            winner.setText("Player 1 wins!");
        else if(player1.points < player2.points)
            winner.setText("Player 2 wins!");
        else
            winner.setText("Draw!");
    }

    private void onSquareClicked(View view) {
        if(!s_button.isEnabled() && ((Button)view).isEnabled()) {
            disableAll(true);
            clickedButton = (Button) view;
        }
    }

    private void disableAll() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Button button = squares[i][j].button;
                Square square = getSquare(button);
                if(square != null && !square.played)
                    button.setEnabled(false);
            }
        }
        s_button.setEnabled(false);
        o_button.setEnabled(false);
    }

    private void disableAll(boolean b) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Button button = squares[i][j].button;
                Square square = getSquare(button);
                if(square != null && !square.played)
                    button.setEnabled(!b);
            }
        }
        s_button.setEnabled(b);
        o_button.setEnabled(b);
    }

    private Square getSquare(Button button) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Square square = squares[i][j];
                if(square.button == button)
                    return square;
            }
        }
        return null;
    }

    private void checkHorizontal(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(j < 3) {
                    Square sq1 = squares[i][j];
                    Square sq2 = squares[i][j+1];
                    Square sq3 = squares[i][j+2];

                    //if(sq1.color != -1 && sq1.color == sq2.color && sq2.color == sq3.color && sq1.color == getCurrentPlayerColor())
                        if(!(sq1.sos && sq2.sos && sq3.sos))
                            setSOS(sq1, sq2, sq3);
                }
            }
        }
    }

    private void checkVertical(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(i < 3) {
                    Square sq1 = squares[i][j];
                    Square sq2 = squares[i+1][j];
                    Square sq3 = squares[i+2][j];

                    //if(sq1.color != -1 && sq1.color == sq2.color && sq2.color == sq3.color && sq1.color == getCurrentPlayerColor())
                        if(!(sq1.sos && sq2.sos && sq3.sos))
                            setSOS(sq1, sq2, sq3);
                }
            }
        }
    }

    private void checkDiagonal(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(i < 3 && j < 3) {
                    Square sq1 = squares[i][j];
                    Square sq2 = squares[i+1][j+1];
                    Square sq3 = squares[i+2][j+2];

                    //if(sq1.color != -1 && sq1.color == sq2.color && sq2.color == sq3.color && sq1.color == getCurrentPlayerColor())
                        if(!(sq1.sos && sq2.sos && sq3.sos))
                            setSOS(sq1, sq2, sq3);
                }
            }
        }
        checkDiagonal2();
    }

    private void checkDiagonal2(){
        for (int i = 0; i < 5; i++) {
            for (int j = 4; j >= 0; j--) {
                if(i < 3 && j >= 2) {
                    Square sq1 = squares[i][j];
                    Square sq2 = squares[i+1][j-1];
                    Square sq3 = squares[i+2][j-2];

                    //if(sq1.color != -1 && sq1.color == sq2.color && sq2.color == sq3.color && sq1.color == getCurrentPlayerColor())
                        if(!(sq1.sos && sq2.sos && sq3.sos))
                            setSOS(sq1, sq2, sq3);
                }
            }
        }
    }

    private void setSOS(Square sq1, Square sq2, Square sq3) {
        if((sq1.id + sq2.id + sq3.id).toLowerCase(Locale.ROOT).equals("sos")) {
            sq1.sos = true;
            sq2.sos = true;
            sq3.sos = true;
            sq1.button.setBackgroundColor(Color.TRANSPARENT);
            sq2.button.setBackgroundColor(Color.TRANSPARENT);
            sq3.button.setBackgroundColor(Color.TRANSPARENT);
            Player currentPlayer = getCurrentPlayer();
            currentPlayer.points++;
            if(currentPlayer.name == 1)
                player1Points.setText("Points" + " : " + currentPlayer.points);
            else
                player2Points.setText("Points" + " : " + currentPlayer.points);
        }
    }

    public void log(String message) {
        Log.i("Error Tag", message);
    }
}

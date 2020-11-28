import java.util.*;

import static java.lang.Character.isDigit;

class Position {
    public int x;
    public int y;

    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class TicTacToe {
    private final char[][] field = new char[3][3];

    private static final char emptyChar = '#';
    private static final char playerChar = 'X';
    private static final char aiChar = 'O';
    private boolean gameIsRunning = true;

    private final HashMap<Character, Integer> scores = new HashMap<>();

    public boolean getGame() {
        return gameIsRunning;
    }

    TicTacToe() {
        for(int i = 0; i < 3; ++i) {
            for(int a = 0; a < 3;++a)
                this.field[i][a] = emptyChar;
        }

        scores.put(playerChar, 1);
        scores.put(aiChar, 3);
    }

    public void resetField() {
        for(int i = 0; i < 3; ++i)
            for(int a = 0; a < 3; ++a)
                field[i][a] = emptyChar;
    }

    public void setGameStatus(boolean status) {
        gameIsRunning = status;
    }

    public void printField() {
        System.out.println("----------------------------------\n");
        for(char[] i : this.field) {
            for(char a : i) {
                System.out.print(a + "  ");
            }
            System.out.println();
        }

        if(gameIsRunning) {
            System.out.print("\nInput: ");
        }else {
            if(hasWon() == 1) {
                System.out.println("Player has won");
            }else if(hasWon() == 3) {
                System.out.println("AI has won.");
            }else if(hasWon() == 2) {
                System.out.println("No one has won");
            }
        }
    }

    private boolean isPositionBlocked(int x, int y) {
        return this.field[x][y] != emptyChar;
    }

    public boolean nextAIMove(boolean firstMove) {
        int bestScore = 1;
        Map<Position, Integer> moves = new HashMap<>();
        List<Position> bestMoves = new ArrayList<>();

        if(firstMove)
            bestScore = 3;

        for(int i = 0; i < 3; ++i) {
            for(int a = 0; a < 3; ++a) {
                if(!isPositionBlocked(i, a)) {
                    setPosition(i, a, firstMove);
                    int score = minimax(0,firstMove);
                    deletePosition(i, a);
                    moves.put(new Position(i, a), score);
                    if(firstMove) {
                        if(score < bestScore) {
                            bestScore = score;
                        }
                    }else {
                        if(score > bestScore) {
                            bestScore = score;
                        }
                    }
                }
            }
        }

        for(java.util.Map.Entry<Position, Integer> entry : moves.entrySet()) {
            if(entry.getValue() == bestScore)
                bestMoves.add(entry.getKey());
        }

        Position random = bestMoves.get((int)(Math.random() * bestMoves.size()));
        setPosition(random.x, random.y, firstMove);

        if(hasWon() != -2) {
            gameIsRunning = false;
            return false;
        }

        return true;
    }

    private int minimax(int depth, boolean isMax) {
        int bestScore = 1;
        int result = hasWon();

        if(result != -2) {
            return result;
        }

        if(!isMax)
            bestScore = 3;

        for(int i = 0; i < 3; ++i) {
            for(int a = 0; a < 3; ++a) {
                if(!isPositionBlocked(i, a)) {
                    setPosition(i, a, !isMax);
                    int score = minimax(depth + 1, !isMax);
                    deletePosition(i, a);

                    if(isMax)
                        bestScore = Math.max(score, bestScore);
                    else
                        bestScore = Math.min(score, bestScore);
                }
            }
        }


        return bestScore ;
    }

    public static boolean isNumber(char input) {
        return input >= '0' && input <= '9';
    }

    public static boolean strIsNumber(String str) {
        for(int i = 0; i < str.length(); ++i)
            if(!isDigit(str.charAt(i)))
                return false;
            return true;
    }

    private void setPosition(int x, int y, boolean player) {
        char mark = aiChar;

        if(player)
            mark = playerChar;

        if(!(x >= 0 && x <= 2 || y >= 0 && y <= 2))
            return;

        this.field[x][y] = mark;
    }

    private void deletePosition(int x, int y) {
        field[x][y] = emptyChar;
    }

    private boolean fieldIsFull() {
        for(char[] i : field)
            for(char a : i)
                if(a == emptyChar)
                    return false;
        return true;
    }

    public int hasWon() {

        for(int i = 0; i < 3; ++i) {
            if(this.field[0][i] == this.field[1][i] && this.field[1][i] == this.field[2][i]) {
                if(field[0][i] != emptyChar)
                    return scores.get(field[0][i]);
            }
            if(this.field[i][0] == this.field[i][1] && this.field[i][1] == this.field[i][2]) {
                if(field[i][0] != emptyChar)
                    return scores.get(field[i][0]);
            }
        }

        if(field[1][1]!= emptyChar && ((this.field[0][0] == this.field[1][1] && this.field[1][1] == this.field[2][2]) || (this.field[0][2] == this.field[1][1] && this.field[1][1] == this.field[2][0])))
            return scores.get(field[1][1]);

        if(fieldIsFull()) {
            return 2;
        }

        return -2;
    }

    public void performMoves() {
        if(nextAIMove(true))
            nextAIMove(false);

        if(hasWon() == 2)
            gameIsRunning = false;
    }
}

class Main {

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();

        if((int)(Math.random() * 2) == 0)
            game.nextAIMove(false);

        int playerWon = 0, aiWon = 0, noWon = 0;
        int counter = 0;
        int end = 0;

        if(args.length == 1) {
            if(TicTacToe.strIsNumber((args[0]))) {
                end = Integer.parseInt(args[0]);
            }else {
                System.out.println("Argument must be a number!");
                return;
            }
        }else {
            System.out.println("usage: [arg1]\narg1 (required): Number of games.");
            return;
        }

        while(counter < end) {
            game.performMoves();

            int status = game.hasWon();
            if(status != -2) {
                switch (status) {
                    case 1 -> ++playerWon;
                    case 2 -> ++noWon;
                    case 3 -> ++aiWon;
                }

                game.resetField();
                game.setGameStatus(true);
                ++counter;
            }
        }

        System.out.println("Player: " + playerWon + "\nAI: " + aiWon + "\nNo: " + noWon);
    }
}

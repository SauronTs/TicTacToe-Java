import java.util.*;

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

        scores.put(playerChar, -1);
        scores.put(aiChar, 1);
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
            if(hasWon() == -1) {
                System.out.println("Player has won");
            }else if(hasWon() == 1) {
                System.out.println("AI has won.");
            }else if(hasWon() == 0) {
                System.out.println("No one has won");
            }
        }
    }

    private boolean isPositionBlocked(int x, int y) {
        return this.field[x][y] != emptyChar;
    }

    public void nextAIMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestX = 0, bestY = 0;

        for(int i = 0; i < 3; ++i) {
            for(int a = 0; a < 3; ++a) {
                if(!isPositionBlocked(i, a)) {
                    setPosition(i, a, false);
                    int score = minimax(0, false);
                    deletePosition(i, a);
                    if(score > bestScore) {
                        bestScore = score;
                        bestX = i;
                        bestY = a;
                    }
                }
            }
        }

        setPosition(bestX, bestY, false);

        if(hasWon() != -2)
            gameIsRunning = false;
    }

    private int minimax(int depth, boolean isMax) {
        int bestScore = Integer.MIN_VALUE;
        int result = hasWon();

        if(result != -2) {
            return result;
        }

        if(!isMax)
            bestScore = Integer.MAX_VALUE;

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


        return bestScore;
    }

    private boolean isNumber(char input) {
        return input >= '0' && input <= '9';
    }

    private boolean nextPlayerMove(int number) {
        int x = (number - 1) / 3;
        int y = (number - 1) - (number - 1) / 3 * 3;

        if(isPositionBlocked(x, y)) {
            System.out.println("Player move was illegal");
            return false;
        }

        setPosition(x, y, true);

        if(hasWon() != -2) {
            gameIsRunning = false;
            return false;
        }

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
            return 0;
        }

        return -2;
    }

    //TODO: Optimize
    public void getInput(char input) {
        if(!isNumber(input)) {
            System.out.println("Input is not a valid number");
            return;
        }

        if(nextPlayerMove(input - '0')) {
            nextAIMove();
        }

        if(hasWon() == 0)
            gameIsRunning = false;
    }
}

class Main {

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();

        if((int)(Math.random() * 2) == 0)
            game.nextAIMove();

        game.printField();

        Scanner scanner = new Scanner(System.in);
        char input = ' ';

        while(input != 'q' && game.getGame()) {
            String rawInput = scanner.nextLine();
            if(rawInput.length() != 0)
                input = rawInput.charAt(0);

            game.getInput(input);
            game.printField();
        }
    }
}
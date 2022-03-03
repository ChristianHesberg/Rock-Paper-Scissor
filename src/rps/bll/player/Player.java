package rps.bll.player;

//Project imports
import rps.bll.game.IGameState;
import rps.bll.game.Move;
import rps.bll.game.Result;
import rps.bll.game.ResultType;

//Java imports
import java.util.ArrayList;
import java.util.Random;

/**
 * Example implementation of a player.
 *
 * @author smsj
 */
public class Player implements IPlayer {

    private String name;
    private PlayerType type;
    private int rowAmount = 3;
    private int columnAmount = 9;
    private int[][] transitionMatrix ={
            //RR, RP, RS, PR, PP, PS, SR, SP, SS
            {0, 0, 0, 0, 0, 0, 0, 0, 0}, //R
            {0, 0, 0, 0, 0, 0, 0, 0, 0}, //P
            {0, 0, 0, 0, 0, 0, 0, 0, 0}, //S
    };

    /**
     * @param name
     */
    public Player(String name, PlayerType type) {
        this.name = name;
        this.type = type;
    }


    @Override
    public String getPlayerName() {
        return name;
    }


    @Override
    public PlayerType getPlayerType() {
        return type;
    }

    /**
     * Decides the next move for the bot...
     * @return Next move
     */

    private int calculateMatrixColumn(Result result)
    {
        if(result.getType().equals(ResultType.Win))
        {
            if(result.getWinnerPlayer().getPlayerType().equals(PlayerType.AI))
            {
                if(result.getWinnerMove().equals(Move.Rock))
                {
                    return 2;
                }
                if(result.getWinnerMove().equals(Move.Paper))
                {
                    return 3;
                }
                if(result.getWinnerMove().equals(Move.Scissor))
                {
                    return 7;
                }
            }
            if(result.getWinnerPlayer().getPlayerType().equals(PlayerType.Human))
            {
                if(result.getWinnerMove().equals(Move.Rock))
                {
                    return 6;
                }
                if(result.getWinnerMove().equals(Move.Paper))
                {
                    return 1;
                }
                if(result.getWinnerMove().equals(Move.Scissor))
                {
                    return 5;
                }
            }
        }
        if(result.getType().equals(ResultType.Tie))
        {
            if(result.getWinnerMove().equals(Move.Rock))
            {
                return 0;
            }
            if(result.getWinnerMove().equals(Move.Paper))
            {
                return 4;
            }
            if(result.getWinnerMove().equals(Move.Scissor))
            {
                return 8;
            }
        }
        return -100;
    }

    private int calculateMatrixRow(Result result)
    {
        if(result.getType().equals(ResultType.Win)|| result.getType().equals(ResultType.Tie))
        {
            if(result.getWinnerPlayer().getPlayerType().equals(PlayerType.Human))
            {
                if(result.getWinnerMove().equals(Move.Rock))
                {
                     return 0;
                }
                if(result.getWinnerMove().equals(Move.Paper))
                {
                    return 1;
                }
                if(result.getWinnerMove().equals(Move.Scissor))
                {
                    return 2;
                }
            }
            if(result.getLoserPlayer().getPlayerType().equals(PlayerType.Human))
            {
                if(result.getLoserMove().equals(Move.Rock))
                {
                    return 0;
                }
                if(result.getLoserMove().equals(Move.Paper))
                {
                    return 1;
                }
                if(result.getLoserMove().equals(Move.Scissor))
                {
                    return 2;
                }
            }
        }
        return -100;
    }

    @Override
    public Move doMove(IGameState state) {
        //Historic data to analyze and decide next move...
        ArrayList<Result> results = (ArrayList<Result>) state.getHistoricResults();

        //Implement better AI here...

        int roundNumber = state.getRoundNumber();

        if (roundNumber <= 2) {
            Random random = new Random();
            int randNumber = random.nextInt(3);
            if (randNumber == 2) {
                return Move.Rock;
            }
            if (randNumber == 1) {
                return Move.Paper;
            }
            if (randNumber == 0) {
                return Move.Scissor;
            }
        }

        if (roundNumber > 2) {
            Result movePair = results.get(roundNumber - 3);
            Result transition = results.get(roundNumber - 2);

            int row = calculateMatrixRow(transition);
            int column = calculateMatrixColumn(movePair);

            int startingValue= -1;
            int selectRow = -1;
            for(int i=0; i<rowAmount; i++)
            {
                if(transitionMatrix[i][column]>startingValue)
                {
                    startingValue = transitionMatrix[i][column];
                    selectRow = i;
                }
            }

            transitionMatrix[row][column] = transitionMatrix[row][column] +1;

            System.out.println("Select Row: "+selectRow);
            if (selectRow == 0) {

                printMatrix();
                return Move.Paper;
            }
            if (selectRow == 1) {

                printMatrix();
                return Move.Scissor;
            }
            if (selectRow == 2) {
                printMatrix();
                return Move.Rock;
            }
        }
        return null;
    }

    private void printMatrix()
    {
        for(int i=0; i<rowAmount; i++)
        {
            for(int k=0; k<columnAmount; k++)
            {
                System.out.println(transitionMatrix[i][k]);
            }
        }
    }
}

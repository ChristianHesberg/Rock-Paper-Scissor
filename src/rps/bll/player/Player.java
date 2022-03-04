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
            return randomMove();
        }

        if (roundNumber > 2) {
            Result twoBack = results.get(roundNumber - 3);
            Result oneBack = results.get(roundNumber - 2);

            int row = calculateMatrixRow(oneBack);
            int column = calculateMatrixColumn(twoBack);
            int newColumn = calculateMatrixColumn(oneBack);

            transitionMatrix[row][column] = transitionMatrix[row][column] +1;

            int highestValue= 0;
            int selectRow = -1;
            for(int i=0; i<rowAmount; i++)
            {
                if(transitionMatrix[i][newColumn]>highestValue)
                {
                    highestValue = transitionMatrix[i][newColumn];
                    selectRow = i;
                }
            }

            if(tieBreaker(transitionMatrix, newColumn, highestValue)!= -1)
            {
                selectRow = tieBreaker(transitionMatrix, newColumn, highestValue);
            }

            if(selectRow == -1)
            {
                Random random = new Random();
                selectRow = random.nextInt(3);
            }
            if (selectRow == 0) {
                return Move.Paper;
            }
            if (selectRow == 1) {
                return Move.Scissor;
            }
            if (selectRow == 2) {
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

    private Move randomMove()
    {
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
        else
        {
            return null;
        }
    }

    private int tieBreaker(int[][] matrix, int column, int highestValue)
    {
        if(matrix[0][column] == highestValue && highestValue!= 0 && matrix[0][column] == matrix[1][column] && matrix[0][column] == matrix[2][column])
        {
            Random random = new Random();
            return random.nextInt(3);
        }
        if(matrix[0][column] == highestValue && matrix[0][column]!= 0 && matrix[0][column] == matrix[1][column])
        {
            return 0;
        }
        if(matrix[0][column] == highestValue && matrix[0][column]!= 0 && matrix[0][column] == matrix[2][column])
        {
            return 2;
        }
        if(matrix[1][column] == highestValue && matrix[1][column]!= 0 && matrix[1][column] == matrix[2][column])
        {
            return 1;
        }
        else
        {
            return -1;
        }

    }

        /*private int checkMostPlayed(int[][] matrix)
    {
        int row1 = 0;
        int row2 = 0;
        int row3 = 0;
        for(int i=0; i<rowAmount; i++)
        {
            for(int k=0; k<columnAmount; i++)
            {
                if(i==0)
                {
                    row1 += matrix[i][k];
                }
                if(i==1)
                {
                    row2 += matrix[i][k];
                }
                if(i==2)
                {
                    row3 += matrix[i][k];
                }
            }
        }
        if(row1 > row2 && row1 > row3)
        {
            return 0;
        }
        if(row2 > row1 && row2 >row3)
        {
            return 1;
        }
        if(row3 > row1 && row3 > row2)
        {
            return 2;
        }
        else
        {
            return -1;
        }
    }

     */
}

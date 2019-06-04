import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date
 */
public class MyWorld extends World
{
    private String[][] grid;
    private int playerTurn;
    private String playerBlack;
    private String playerRed;
    private String playerBlackInitial;
    private String playerRedInitial;
    private int playerBlackWinTotal;
    private int playerRedWinTotal;
    private double playerBlackWinPercentage;
    private double playerRedWinPercentage;
    private int goalInARow;
    private int firstPlayerTurn;
    private boolean firstPlay;
    private String lastPlayedInitial;
    private int tieCounter;
    private int gameCounter;
    
    
    
    private int turnCounter;
    private int col1x, col1y, colDiff;
    private int actCounter;
    
    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public MyWorld() 
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1); 
        grid = new String[6][7];    //to keep track of the game board
        firstPlayerTurn = (int)(Math.random()*2+1);   //randomize who starts the game
        playerBlack = "PlayerBlack";
        playerRed = "PlayerRed";
        playerBlackInitial = "b";  //to fill the 2d array grid to know which checker is where
        playerRedInitial = "r";   //to fill the 2d array grid to know which checker is where
        playerBlackWinTotal = 0;  //scoreboard
        playerRedWinTotal = 0;    //scoreboard
        goalInARow = 5;//because it is Connect 4
        actCounter = 20;//sets the speed of the game
        firstPlay = true; //determines if it's first play of new game, so we can alternate first turn each game
        lastPlayedInitial = ""; //keeps track of who played last
        tieCounter = 0;
        gameCounter = 0;
        
        
        
        turnCounter = 1; //used to keep track of number of plays made
        col1x=176; //to place checkers in the correct location
        col1y=72; //to place checkers in the correct location
        colDiff=41; //to place checke rs in the correct location
        
        addObject(new Board(),300,200); //adds game board
        
        
        
        
        
        
    }
    
    public void act(){
        
        
        String winner = checkForWinner();  //see if there is a winner
        if(winner != null || tie()){ //if there is a winner or a tie
                
                
            
                if(winner != null && winner.equals("r"))  //red is the winner
                {
                    showPlayer2Winner();
                    
                }
                else if(winner != null && winner.equals("b")) //black is the winner
                {
                    showPlayer1Winner();
                    
                }
                
                //resetGrid();
                
                if(turnCounter % (actCounter) == 0) //after a win,allows last checker to fall before the grid is cleared
                {
                    Greenfoot.delay(500); //allows the highlighted winner more time to be seen
                    resetGame();  //resets game
                    
                }
                
            }
            
            
            if(turnCounter % actCounter == 0){  //this controls the speed of the game
                if(firstPlay && firstPlayerTurn == 1){ //runs only first time of game, to alternate first play of each game between players

                    playerBlackStrategy();   //runs player1 code
                    firstPlayerTurn = 2;    //alternates who plays first next game
                    playerTurn = 2;  //switches to other player
                    firstPlay = false;  //will be set back to true when next game starts
                    
                }
                else if(firstPlay && firstPlayerTurn == 2){//runs only first time of game, to alternate first play of each game between players
                    
                    playerRedStrategy();    //runs player2 code
                    firstPlayerTurn = 1;    //alternates who plays first next game
                    playerTurn = 1;   //switches to other player
                    firstPlay = false;  //will be set back to true when next game starts
                    
                }
                else if(playerTurn==1){  //alternates players
                    playerBlackStrategy();   //runs player1 code
                    
                    playerTurn = 2;  //switches to other player
                }
                else if(playerTurn==2){  //alternates players
                    playerRedStrategy();    //runs player2 code
                    
                    playerTurn = 1;   //switches to other player
                }
                
               }
        
      
            
                turnCounter++;    //adds to turn counter
              
                    
    }
    
    public void playerBlackStrategy(){
        
        //leave these top 4 lines at the top
        playerBlack = "Sashka";  //put player1 name here
        showText(playerBlack,70,100); //shows your name
        GreenfootImage image1 = new GreenfootImage("black.png"); //sets image to black checker
        String colorInitial = new String("b"); //fills the 2d array grid with this value
        
        
        
        
        //replace the following code with your strategy
        //but the second line shows you how to place your checker
        
        //checking the grid for groups of three and whether a thing seems to be growing.
        
        /*
         * Strategies:
         * go from an origin point, once blocked, change origin point.
         * record order of placements in a 2d array of your own????? > when op changes, go in order of placed for checking.
         * when choosing new origin point, choose one with the most openings
         * or one that can use already placed tokens in new line. > more calculations
         * 
         * if the other player has already made 3 or more turns, start checking for lines of 3, actively get to blocking once lines of 3 are found
         * 
         * KEEP INITIATIVE
         * 
         * order of importance when blocking:
         * row of 3
         * row of 2
         * place your own additions to lines when rows of 2 or single checkers are just on board.
         * 
         * 1. check for horizontal spaces of length 4, aim for those. Once blocked, strat 2
         * 2. check for vertical spaces from original point. Once blocked, change to strat 3
         * 3. check for diagonal spaces from origin point. (if right and left open, random and continue from there).
         */
        
        int colToPlace = 0;
        
        //first to play
        if (turnCounter < 120){
            colToPlace = 3;
        } else if (turnCounter > 120 && (check3R() != -1)){
            colToPlace = check3R();
        } else if (check3B() != -1){
            colToPlace = check3B();
        } else {
            colToPlace = (int)(Math.random()*7);
        }
        
        placeCol(colToPlace,colorInitial,image1);  //plays in the specified col and fills the 2D array with the 2nd parameter String
        
        
    }
    
    
      public int check3B(){
        
        //easier to build a String from the 2d array grid, and then check for consecutive initials
        String checkersAsAString = new String();
        
        int row = 0;
        int col = 0;
        
        //check each row for a winner, starting at bottom
        for(int r = grid.length - 1; r >= 0; r--){
            checkersAsAString = ""; //reset String for each row
            for(int c = 0; c < grid[0].length; c++){
                if(grid[r][c] == null)
                    checkersAsAString+="-";//adds dash to the String if cell is empty
                else{
                    checkersAsAString+= grid[r][c]; //adds player initial to String
                    if(checkersAsAString.contains(playerRedInitial+playerRedInitial+playerRedInitial))
                   {
                        if ((c - 1) > -1 && grid[r][c - 1] == null){
                            return c - 1;
                        }
                    }
                }
            }
        }
        
        
        //check each column for a winner
        for(int c = 0; c < grid[0].length; c++){
            checkersAsAString = "";  //reset String for each column
            for(int r = grid.length - 1; r >= 0; r--){
                if(grid[r][c] == null)
                    checkersAsAString+="-"; //adds dash to the String if cell is empty
                else{
                    checkersAsAString+= grid[r][c]; //adds the initial to the String
                    if(checkersAsAString.contains(playerRedInitial+playerRedInitial+playerRedInitial))
                   {
                        if ((r - 1) > -1 && grid[r - 1][c] == null){
                            return c;
                        }
                    }
                }
            }
        }
        
        
        //checks for diagonal winner
        //this checks for diagonal starting at bottom right, moving to top left
        String diag1 = new String();  //Six new Strings to be built
        String diag2 = new String();  //for each possible diagonal to win 
        String diag3 = new String();
        String diag4 = new String();
        String diag5 = new String();
        String diag6 = new String();
        for(int r = 0; r < grid.length; r++){
            for(int c = 0; c < grid[0].length; c++){
                
                if(r-c==2){ //checks two diagonals below the middle diagonal
                    if(grid[r][c] == null)
                         diag1 += "-"; //adds a dash if cell is blank
                    else{
                         diag1 += grid[r][c]; //adds the player initial to String
                    }
                }
                
                if(r-c==1){ //checks one diagonal below the middle diagonal
                    if(grid[r][c] == null)
                         diag2 += "-"; //adds a dash if cell is blank
                    else{
                         diag2 += grid[r][c];  //adds the player initial to String
                        
                    }
                }
                
                if(r-c==0){//checks the middle diagonal
                    if(grid[r][c] == null)
                         diag3 += "-";
                    else{
                         diag3 += grid[r][c];
                         
                    }
                }
                
                if(r-c==-1){ //checks one diagonal above the middle diagonal
                    if(grid[r][c] == null)
                         diag4 += "-";
                    else{
                         diag4 += grid[r][c];
                         
                    }
                }
                
                if(r-c==-2){ //checks two diagonals above the middle diagonal
                    if(grid[r][c] == null)
                         diag5 += "-";
                    else{
                         diag5 += grid[r][c];
                          
                    }
                }
                
                if(r-c==-3){  //checks three diagonals above the middle diagonal
                    if(grid[r][c] == null)
                         diag6 += "-";
                    else{
                         diag6 += grid[r][c];
                        
                     }
                }
                
                
            }
            
            
        }
        
        //checking eah diagonal for placements
         if (diag1.contains(playerRedInitial+playerRedInitial+playerRedInitial)){
             if (grid[2][0] == null){
                 return 0;
             } else if (grid[5][3] == null){
                 return 3; 
             }
         }
          else if (diag2.contains(playerRedInitial+playerRedInitial+playerRedInitial)){
                             if (grid[2][1] == null){
                                 return 1;
                            }else if (grid[4][3] == null){
                                return 3;
                            } else if (grid[5][4] == null){
                                return 4;
                            } else if (grid[1][0] == null){
                                return 0;
                            } 
                        }
         else if (diag3.contains(playerRedInitial+playerRedInitial+playerRedInitial)){
                            if (grid[2][2] == null){
                                return 2;
                            } else if (grid[3][3] == null){
                                return 2;
                            } else if (grid[4][4] == null){
                                return 4;
                            } else if (grid[1][1] == null){
                                return 1;
                            } else if (grid[5][5] == null){
                                return 5;
                            }else if (grid[0][0] == null){
                                 return 0;
                            }
                        }
         else if (diag4.contains(playerRedInitial+playerRedInitial+playerRedInitial)){
                            if (grid[2][3] == null){
                                return 3;
                            } else if (grid[3][4] == null){
                                return 4;
                            } else if (grid[4][5] == null){
                                return 5;
                            } else if (grid[1][2] == null){
                                return 2;
                            } else if (grid[5][6] == null){
                                return 6;
                            }else if (grid[0][1] == null){
                                 return 1;
                            }
                        }
         else if (diag5.contains(playerRedInitial+playerRedInitial+playerRedInitial)){
                             if (grid[1][3] == null){
                                return 3;
                            } else if (grid[3][5] == null){
                                return 5;
                            } else if (grid[4][6] == null){
                                return 6;
                            }else  if (grid[0][2] == null){
                                 return 2;
                            } 
                        }
         else if (diag6.contains(playerRedInitial+playerRedInitial+playerRedInitial)){
                             if (grid[0][3] == null){
                                 return 3;
                            } else if (grid[1][4] == null){
                                return 4;
                            }
                        }
        
        //reset diagonal Strings so we can now check for diagonal wins starting
        //at bottom left moving up to top right
        diag1="";
        diag2="";
        diag3="";
        diag4="";
        diag5="";
        diag6="";
        //check for diagonal wins starting
        //at bottom left moving up to top right
        for(int r = 0; r < grid.length; r++){
            for(int c = 0; c < grid[0].length; c++){
                
                if(r+c==3){ //checks the diagonal 2 diagonals above the middle diagonal
                    if(grid[r][c] == null)
                         diag1 += "-";
                    else{
                         diag1 += grid[r][c]; //adds the player initial to String
                         
                    }
                }
                
                if(r+c==4){  //checks the diagonal 1 diagonal above the middle diagonal
                    if(grid[r][c] == null)
                         diag2 += "-";
                    else
                         diag2 += grid[r][c];
                }
                
                if(r+c==5){  ////checks the middle diagonal 
                    if(grid[r][c] == null)
                         diag3 += "-";
                    else
                         diag3 += grid[r][c];
                }
                
                if(r+c==6){ //checks the diagonal 1 diagonal below the middle diagonal
                    if(grid[r][c] == null)
                         diag4 += "-";
                    else
                         diag4 += grid[r][c];
                }
                
                if(r+c==7){ //checks the diagonal 2 diagonals below the middle diagonal
                    if(grid[r][c] == null)
                         diag5 += "-";
                    else
                         diag5 += grid[r][c];
                }
                
                if(r+c==8){  //checks the diagonal 3 diagonals below the middle diagonal
                    if(grid[r][c] == null)
                         diag6 += "-";
                    else
                         diag6 += grid[r][c];
                }
            }
        }
        
        if (diag1.contains(playerRedInitial+playerRedInitial+playerRedInitial)){
             if (grid[3][0] == null){
                 return 0;
             } else if (grid[0][3] == null){
                 return 3;
                
             }
         }
          else if (diag2.contains(playerRedInitial+playerRedInitial+playerRedInitial)){
                            if (grid[3][1] == null){
                                return 1;
                            } else if (grid[1][3] == null){
                                return 3;
                            } else if (grid[0][4] == null){
                                return 4;
                            }else if (grid[4][0] == null){
                                 return 0;
                            }
                        }
         else if (diag3.contains(playerRedInitial+playerRedInitial+playerRedInitial)){
                            if (grid[3][2] == null){
                                return 2;
                            } else if (grid[2][3] == null){
                                return 2;
                            } else if (grid[1][4] == null){
                                return 4;
                            } else if (grid[4][1] == null){
                                return 1;
                            } else if (grid[0][5] == null){
                                return 5;
                            } else if (grid[5][0] == null){
                                 return 0;
                            }
                        }
         else if (diag4.contains(playerRedInitial+playerRedInitial+playerRedInitial)){
                            if (grid[3][3] == null){
                                return 3;
                            } else if (grid[2][4] == null){
                                return 4;
                            } else if (grid[1][5] == null){
                                return 5;
                            } else if (grid[4][2] == null){
                                return 2;
                            } else if (grid[0][6] == null){
                                return 6;
                            }else if (grid[5][1] == null){
                                 return 1;
                            }
                        }
         else if (diag5.contains(playerRedInitial+playerRedInitial+playerRedInitial)){
                            if (grid[4][3] == null){
                                return 3;
                            } else if (grid[2][5] == null){
                                return 5;
                            } else if (grid[1][6] == null){
                                return 6;
                            }else if (grid[5][2] == null){
                                 return 2;
                            }
                        }
         else if (diag6.contains(playerRedInitial+playerRedInitial+playerRedInitial)){
                             if (grid[5][3] == null){
                                 return 3;
                            } else if (grid[2][4] == null){
                                return 4;
                            }
                        }
        
        return -1; //returns null if there is no winner
    
    }
    
    public int check3R(){
        
        //easier to build a String from the 2d array grid, and then check for consecutive initials
        String checkersAsAString = new String();
       
        //check each row for a winner, starting at bottom
        for(int r = grid.length - 1; r >= 0; r--){
            checkersAsAString = ""; //reset String for each row
            for(int c = 0; c < grid[0].length; c++){
                if(grid[r][c] == null)
                    checkersAsAString+="-";//adds dash to the String if cell is empty
                else{
                    checkersAsAString+= grid[r][c]; //adds player initial to String
                    if(checkersAsAString.contains(playerBlackInitial+playerBlackInitial+playerBlackInitial))
                   {
                        if ((c - 1) > -1 && grid[r][c - 1] == null){
                            return c - 1; 
                        }
                    }
                }
            }
        }
        
        
        //check each column for a winner
        for(int c = 0; c < grid[0].length; c++){
            checkersAsAString = "";  //reset String for each column
            for(int r = grid.length - 1; r >= 0; r--){
                if(grid[r][c] == null)
                    checkersAsAString+="-"; //adds dash to the String if cell is empty
                else{
                    checkersAsAString+= grid[r][c]; //adds the initial to the String
                    if(checkersAsAString.contains(playerBlackInitial+playerBlackInitial+playerBlackInitial))
                    {
                        if ((r - 1) > -1 && grid[r - 1][c] == null){
                            return c;
                        }
                    }
                }
            }
        }
        
        
        //checks for diagonal winner
        //this checks for diagonal starting at bottom right, moving to top left
        String diag1 = new String();  //Six new Strings to be built
        String diag2 = new String();  //for each possible diagonal to win 
        String diag3 = new String();
        String diag4 = new String();
        String diag5 = new String();
        String diag6 = new String();
        for(int r = 0; r < grid.length; r++){
            for(int c = 0; c < grid[0].length; c++){
                
                if(r-c==2){ //checks two diagonals below the middle diagonal
                    if(grid[r][c] == null)
                         diag1 += "-"; //adds a dash if cell is blank
                    else{
                         diag1 += grid[r][c]; //adds the player initial to String
                    }
                }
                
                if(r-c==1){ //checks one diagonal below the middle diagonal
                    if(grid[r][c] == null)
                         diag2 += "-"; //adds a dash if cell is blank
                    else{
                         diag2 += grid[r][c];  //adds the player initial to String
                        
                    }
                }
                
                if(r-c==0){//checks the middle diagonal
                    if(grid[r][c] == null)
                         diag3 += "-";
                    else{
                         diag3 += grid[r][c];
                         
                    }
                }
                
                if(r-c==-1){ //checks one diagonal above the middle diagonal
                    if(grid[r][c] == null)
                         diag4 += "-";
                    else{
                         diag4 += grid[r][c];
                         
                    }
                }
                
                if(r-c==-2){ //checks two diagonals above the middle diagonal
                    if(grid[r][c] == null)
                         diag5 += "-";
                    else{
                         diag5 += grid[r][c];
                          
                    }
                }
                
                if(r-c==-3){  //checks three diagonals above the middle diagonal
                    if(grid[r][c] == null)
                         diag6 += "-";
                    else{
                         diag6 += grid[r][c];
                        
                     }
                }
                
                
            }
            
            
        }
        
        //checking eah diagonal for placements
         if (diag1.contains(playerBlackInitial+playerBlackInitial+playerBlackInitial)){
             if (grid[2][0] == null){
                 return 0;
             } else if (grid[5][3] == null){
                 return 3;  
             }
         }
          else if (diag2.contains(playerBlackInitial+playerBlackInitial+playerBlackInitial)){
                             if (grid[2][1] == null){
                                 return 1;
                            }else if (grid[4][3] == null){
                                return 3;
                            } else if (grid[5][4] == null){
                                return 4;
                            } else if (grid[1][0] == null){
                                return 0;
                            } 
                        }
         else if (diag3.contains(playerBlackInitial+playerBlackInitial+playerBlackInitial)){
                            if (grid[2][2] == null){
                                return 2;
                            } else if (grid[3][3] == null){
                                return 2;
                            } else if (grid[4][4] == null){
                                return 4;
                            } else if (grid[1][1] == null){
                                return 1;
                            } else if (grid[5][5] == null){
                                return 5;
                            }else if (grid[0][0] == null){
                                 return 0;
                            }
                        }
         else if (diag4.contains(playerBlackInitial+playerBlackInitial+playerBlackInitial)){
                            if (grid[2][3] == null){
                                return 3;
                            } else if (grid[3][4] == null){
                                return 4;
                            } else if (grid[4][5] == null){
                                return 5;
                            } else if (grid[1][2] == null){
                                return 2;
                            } else if (grid[5][6] == null){
                                return 6;
                            }else if (grid[0][1] == null){
                                 return 1;
                            }
                        }
         else if (diag5.contains(playerBlackInitial+playerBlackInitial+playerBlackInitial)){
                             if (grid[1][3] == null){
                                return 3;
                            } else if (grid[3][5] == null){
                                return 5;
                            } else if (grid[4][6] == null){
                                return 6;
                            }else  if (grid[0][2] == null){
                                 return 2;
                            } 
                        }
         else if (diag6.contains(playerBlackInitial+playerBlackInitial+playerBlackInitial)){
                             if (grid[0][3] == null){
                                 return 3;
                            } else if (grid[1][4] == null){
                                return 4;
                            }
                        }
        
        //reset diagonal Strings so we can now check for diagonal wins starting
        //at bottom left moving up to top right
        diag1="";
        diag2="";
        diag3="";
        diag4="";
        diag5="";
        diag6="";
        //check for diagonal wins starting
        //at bottom left moving up to top right
        for(int r = 0; r < grid.length; r++){
            for(int c = 0; c < grid[0].length; c++){
                
                if(r+c==3){ //checks the diagonal 2 diagonals above the middle diagonal
                    if(grid[r][c] == null)
                         diag1 += "-";
                    else{
                         diag1 += grid[r][c]; //adds the player initial to String
                         
                    }
                }
                
                if(r+c==4){  //checks the diagonal 1 diagonal above the middle diagonal
                    if(grid[r][c] == null)
                         diag2 += "-";
                    else
                         diag2 += grid[r][c];
                }
                
                if(r+c==5){  ////checks the middle diagonal 
                    if(grid[r][c] == null)
                         diag3 += "-";
                    else
                         diag3 += grid[r][c];
                }
                
                if(r+c==6){ //checks the diagonal 1 diagonal below the middle diagonal
                    if(grid[r][c] == null)
                         diag4 += "-";
                    else
                         diag4 += grid[r][c];
                }
                
                if(r+c==7){ //checks the diagonal 2 diagonals below the middle diagonal
                    if(grid[r][c] == null)
                         diag5 += "-";
                    else
                         diag5 += grid[r][c];
                }
                
                if(r+c==8){  //checks the diagonal 3 diagonals below the middle diagonal
                    if(grid[r][c] == null)
                         diag6 += "-";
                    else
                         diag6 += grid[r][c];
                }
            }
        }
        
        if (diag1.contains(playerBlackInitial+playerBlackInitial+playerBlackInitial)){
             if (grid[3][0] == null){
                 return 0;
             } else if (grid[0][3] == null){
                 return 3;
                
             }
         }
          else if (diag2.contains(playerBlackInitial+playerBlackInitial+playerBlackInitial)){
                            if (grid[3][1] == null){
                                return 1;
                            } else if (grid[1][3] == null){
                                return 3;
                            } else if (grid[0][4] == null){
                                return 4;
                            }else if (grid[4][0] == null){
                                 return 0;
                            }
                        }
         else if (diag3.contains(playerBlackInitial+playerBlackInitial+playerBlackInitial)){
                            if (grid[3][2] == null){
                                return 2;
                            } else if (grid[2][3] == null){
                                return 2;
                            } else if (grid[1][4] == null){
                                return 4;
                            } else if (grid[4][1] == null){
                                return 1;
                            } else if (grid[0][5] == null){
                                return 5;
                            } else if (grid[5][0] == null){
                                 return 0;
                            }
                        }
         else if (diag4.contains(playerBlackInitial+playerBlackInitial+playerBlackInitial)){
                            if (grid[3][3] == null){
                                return 3;
                            } else if (grid[2][4] == null){
                                return 4;
                            } else if (grid[1][5] == null){
                                return 5;
                            } else if (grid[4][2] == null){
                                return 2;
                            } else if (grid[0][6] == null){
                                return 6;
                            }else if (grid[5][1] == null){
                                 return 1;
                            }
                        }
         else if (diag5.contains(playerBlackInitial+playerBlackInitial+playerBlackInitial)){
                            if (grid[4][3] == null){
                                return 3;
                            } else if (grid[2][5] == null){
                                return 5;
                            } else if (grid[1][6] == null){
                                return 6;
                            }else if (grid[5][2] == null){
                                 return 2;
                            }
                        }
         else if (diag6.contains(playerBlackInitial+playerBlackInitial+playerBlackInitial)){
                             if (grid[5][3] == null){
                                 return 3;
                            } else if (grid[2][4] == null){
                                return 4;
                            }
                        }
        
        return -1; //returns null if there is no winner
    
    }
    
    
    public void playerRedStrategy(){
        //leave these top 3 lines at the top
        
        
        playerRed = "Sashka";  //put your name here
        showText(playerRed,getWidth()-70,100); //shows your name
        GreenfootImage image2 = new GreenfootImage("red.png");  //sets image to red checker
        String colorInitial = new String("r");  //fills the 2d array grid with this value
        
        
        
        
        
        //replace the following code with your strategy
        //but the second line shows you how to place your checker
        int colToPlace = 0;
        
        //first to play
        if (turnCounter < 120){
            colToPlace = 3;
        } else if (turnCounter > 120 && check3B() != -1){
            colToPlace = check3B();
        } else if (check3R() != -1){
            colToPlace = check3R();
        } else {
            colToPlace = (int)(Math.random()*7);
        }
        placeCol(colToPlace,colorInitial,image2);  //plays in the specified col and fills the 2D array with the 2nd parameter String
         
    }
    
    
    public void placeCol(int column,String teamInitial, GreenfootImage color){
        
        
            
        if(!lastPlayedInitial.equals(teamInitial)){//ensures placeCol() can only be called once per player per turn
        
        //places checker and updates grid
            if(grid[0][column] == null){//makes sure there is a place to play, if not a random col is selected
                addObject(new Checker(column,color),col1x+(column * colDiff),col1y);
                grid[0][column] = teamInitial; //adds initial to 2d grid
                dropGrid(); //drops teamInitial down the column to bottom
            }
            else{//a random column is selected
                
                placeCol((int)(Math.random()*7),teamInitial, color);
                
            }
        
        }
    
        
        
        
        
        lastPlayedInitial = teamInitial;
        
    }
    
    private void dropGrid()
    {
        
        //drops new "b" or "r" to its new location in grid
        int moveToRow = 0;
        for(int c = 0; c < grid[0].length;c++)
        {
            for(int r = 0; r < grid.length - 1;r++)
            {
                if(grid[r][c] != null && grid[r + 1][c] == null)
                {
                    grid[r+1][c] = grid[r][c];
                    grid[r][c] = null;
                }
                else if(grid[r][c] != null && grid[r + 1][c] != null)
                {
                    r+=8;
                }
            }
        }
        
        
        
        
    }
    
    public String checkForWinner(){
        
        //easier to build a String from the 2d array grid, and then check for consecutive initials
        String checkersAsAString = new String();
       
        //check each row for a winner, starting at bottom
        for(int r = grid.length - 1; r >= 0; r--){
            checkersAsAString = ""; //reset String for each row
            for(int c = 0; c < grid[0].length; c++){
                if(grid[r][c] == null)
                    checkersAsAString+="-";//adds dash to the String if cell is empty
                else
                    checkersAsAString+= grid[r][c]; //adds player initial to String
            
                if(checkersAsAString.contains(playerBlackInitial+playerBlackInitial+playerBlackInitial+playerBlackInitial))
                {
                    circleRow(r,checkersAsAString, playerBlackInitial);  //calls the winning row to be circled
                    return playerBlackInitial;
                }
                else if(checkersAsAString.contains(playerRedInitial+playerRedInitial+playerRedInitial+playerRedInitial))
                {
                    circleRow(r,checkersAsAString, playerRedInitial); //calls the winning row to be circled
                    return playerRedInitial;
                }
            }
        }
        
        
        //check each column for a winner
        for(int c = 0; c < grid[0].length; c++){
            checkersAsAString = "";  //reset String for each column
            for(int r = grid.length - 1; r >= 0; r--){
                if(grid[r][c] == null)
                    checkersAsAString+="-"; //adds dash to the String if cell is empty
                else
                    checkersAsAString+= grid[r][c]; //adds the initial to the String
            
                if(checkersAsAString.contains(playerBlackInitial+playerBlackInitial+playerBlackInitial+playerBlackInitial))
                {
                    circleColumn(c,checkersAsAString,playerBlackInitial);   //highlights winning column
                    return playerBlackInitial;
                }
                else if(checkersAsAString.contains(playerRedInitial+playerRedInitial+playerRedInitial+playerRedInitial))
                {
                    circleColumn(c,checkersAsAString,playerRedInitial);  //highlights winning column
                    return playerRedInitial;
                }
            }
        }
        
        
        //checks for diagonal winner
        //this checks for diagonal starting at bottom right, moving to top left
        String diag1 = new String();  //Six new Strings to be built
        String diag2 = new String();  //for each possible diagonal to win 
        String diag3 = new String();
        String diag4 = new String();
        String diag5 = new String();
        String diag6 = new String();
        for(int r = 0; r < grid.length; r++){
            for(int c = 0; c < grid[0].length; c++){
                
                if(r-c==2){ //checks two diagonals below the middle diagonal
                    if(grid[r][c] == null)
                         diag1 += "-"; //adds a dash if cell is blank
                    else
                         diag1 += grid[r][c]; //adds the player initial to String
                }
                
                if(r-c==1){ //checks one diagonal below the middle diagonal
                    if(grid[r][c] == null)
                         diag2 += "-"; //adds a dash if cell is blank
                    else
                         diag2 += grid[r][c];  //adds the player initial to String
                }
                
                if(r-c==0){//checks the middle diagonal
                    if(grid[r][c] == null)
                         diag3 += "-";
                    else
                         diag3 += grid[r][c];
                }
                
                if(r-c==-1){ //checks one diagonal above the middle diagonal
                    if(grid[r][c] == null)
                         diag4 += "-";
                    else
                         diag4 += grid[r][c];
                }
                
                if(r-c==-2){ //checks two diagonals above the middle diagonal
                    if(grid[r][c] == null)
                         diag5 += "-";
                    else
                         diag5 += grid[r][c];
                }
                
                if(r-c==-3){  //checks three diagonals above the middle diagonal
                    if(grid[r][c] == null)
                         diag6 += "-";
                    else
                         diag6 += grid[r][c];
                }
                
                // //check each concatenated diag String to see if it contains a winning streak for black
                // //if so, call the correct diagonal to be highlighted
                if(containsWinningString(diag1, playerBlackInitial)){
                    circleDiagonal(1,diag1,playerBlackInitial);  //highlight the winning diagonal
                    return playerBlackInitial;
                }
                else if(containsWinningString(diag2, playerBlackInitial)){
                    circleDiagonal(2,diag2,playerBlackInitial);
                    return playerBlackInitial;
                }
                else if(containsWinningString(diag3, playerBlackInitial)){
                    circleDiagonal(3,diag3,playerBlackInitial);
                    return playerBlackInitial;
                }
                else if(containsWinningString(diag4, playerBlackInitial)){
                    circleDiagonal(4,diag4,playerBlackInitial);
                    return playerBlackInitial;
                }
                else if(containsWinningString(diag5, playerBlackInitial)){
                    circleDiagonal(5,diag5,playerBlackInitial);
                    return playerBlackInitial;
                }
                else if(containsWinningString(diag6, playerBlackInitial)){
                    circleDiagonal(6,diag6,playerBlackInitial);
                    return playerBlackInitial;
                }
                
                //same as above but checking to see if red is the winner
                if(containsWinningString(diag1, playerRedInitial)){
                    circleDiagonal(1,diag1,playerRedInitial);
                    return playerRedInitial;
                }
                else if(containsWinningString(diag2, playerRedInitial)){
                    circleDiagonal(2,diag2,playerRedInitial);
                    return playerRedInitial;
                }
                else if(containsWinningString(diag3, playerRedInitial)){
                    circleDiagonal(3,diag3,playerRedInitial);
                    return playerRedInitial;
                }
                else if(containsWinningString(diag4, playerRedInitial)){
                    circleDiagonal(4,diag4,playerRedInitial);
                    return playerRedInitial;
                }
                else if(containsWinningString(diag5, playerRedInitial)){
                    circleDiagonal(5,diag5,playerRedInitial);
                    return playerRedInitial;
                }
                else if(containsWinningString(diag6, playerBlackInitial)){
                    circleDiagonal(6,diag6,playerRedInitial);
                    return playerRedInitial;
                }
                
            }
            
            
        }
        
        
        //reset diagonal Strings so we can now check for diagonal wins starting
        //at bottom left moving up to top right
        diag1="";
        diag2="";
        diag3="";
        diag4="";
        diag5="";
        diag6="";
        //check for diagonal wins starting
        //at bottom left moving up to top right
        for(int r = 0; r < grid.length; r++){
            for(int c = 0; c < grid[0].length; c++){
                
                if(r+c==3){ //checks the diagonal 2 diagonals above the middle diagonal
                    if(grid[r][c] == null)
                         diag1 += "-";
                    else
                         diag1 += grid[r][c];
                }
                
                if(r+c==4){  //checks the diagonal 1 diagonal above the middle diagonal
                    if(grid[r][c] == null)
                         diag2 += "-";
                    else
                         diag2 += grid[r][c];
                }
                
                if(r+c==5){  ////checks the middle diagonal 
                    if(grid[r][c] == null)
                         diag3 += "-";
                    else
                         diag3 += grid[r][c];
                }
                
                if(r+c==6){ //checks the diagonal 1 diagonal below the middle diagonal
                    if(grid[r][c] == null)
                         diag4 += "-";
                    else
                         diag4 += grid[r][c];
                }
                
                if(r+c==7){ //checks the diagonal 2 diagonals below the middle diagonal
                    if(grid[r][c] == null)
                         diag5 += "-";
                    else
                         diag5 += grid[r][c];
                }
                
                if(r+c==8){  //checks the diagonal 3 diagonals below the middle diagonal
                    if(grid[r][c] == null)
                         diag6 += "-";
                    else
                         diag6 += grid[r][c];
                }
                
                
                //checks each concatenated String to see if it contains a win 
                //streak for black
                if(containsWinningString(diag1, playerBlackInitial)){
                    circleBackwardsDiagonal(1,diag1,playerBlackInitial);
                    return playerBlackInitial;
                }
                else if(containsWinningString(diag2, playerBlackInitial)){
                    circleBackwardsDiagonal(2,diag2,playerBlackInitial);
                    return playerBlackInitial;
                }
                else if(containsWinningString(diag3, playerBlackInitial)){
                    circleBackwardsDiagonal(3,diag3,playerBlackInitial);
                    return playerBlackInitial;
                }
                else if(containsWinningString(diag4, playerBlackInitial)){
                    circleBackwardsDiagonal(4,diag4,playerBlackInitial);
                    return playerBlackInitial;
                }
                else if(containsWinningString(diag5, playerBlackInitial)){
                    circleBackwardsDiagonal(5,diag5,playerBlackInitial);
                    return playerBlackInitial;
                }
                else if(containsWinningString(diag6, playerBlackInitial)){
                    circleBackwardsDiagonal(6,diag6,playerBlackInitial);
                    return playerBlackInitial;
                }
                
                //checks each concatenated String to see if it contains a win 
                //streak for red
                if(containsWinningString(diag1, playerRedInitial)){
                    circleBackwardsDiagonal(1,diag1,playerRedInitial);
                    return playerRedInitial;
                }
                else if(containsWinningString(diag2, playerRedInitial)){
                    circleBackwardsDiagonal(2,diag2,playerRedInitial);
                    return playerRedInitial;
                }
                else if(containsWinningString(diag3, playerRedInitial)){
                    circleBackwardsDiagonal(3,diag3,playerRedInitial);
                    return playerRedInitial;
                }
                else if(containsWinningString(diag4, playerRedInitial)){
                    circleBackwardsDiagonal(4,diag4,playerRedInitial);
                    return playerRedInitial;
                }
                else if(containsWinningString(diag5, playerRedInitial)){
                    circleBackwardsDiagonal(5,diag5,playerRedInitial);
                    return playerRedInitial;
                }
                else if(containsWinningString(diag6, playerBlackInitial)){
                    circleBackwardsDiagonal(6,diag6,playerRedInitial);
                    return playerRedInitial;
                }
                
            }
        
        
         }
    
        return null; //returns null if there is no winner
    
    }

    public void circleDiagonal(int diag,String str, String initial){
        //adds the rainbow rings to the correct locations after a win
        
        int c = 0; //dictates the first column in the diagonal to be circled
        int r = 0; //dicates the first row in the diagonal to be circled
        
        int start = str.indexOf(initial+initial+initial+initial);
        
        
        //the steps below set the starting row and column,
        //depending on which diagonal and which indexOf the streak was found
        if(diag==1){
            r=2;
            c=0;
        }
        else if(diag==2 && start == 0){
            r=1;
            c=0;
        }
        else if(diag==2 && start == 1){
            r=2;
            c=1;
        }
        else if(diag==3 && start == 0){
            r=0;
            c=0;
        }
        else if(diag==3 && start == 1){
            r=1;
            c=1;
        }
        else if(diag==3 && start == 2){
            r=2;
            c=2;
        }
        else if(diag==4 && start == 0){
            r=0;
            c=1;
        }
        else if(diag==4 && start == 1){
            r=1;
            c=2;
        }
        else if(diag==4 && start == 2){
            r=2;
            c=3;
        }
        else if(diag==5 && start == 0){
            r=0;
            c=2;
        }
        else if(diag==5 && start == 1){
            r=1;
            c=3;
        }
        else if(diag==6){
            r=0;
            c=3;
        }
        
        
        
        for(int i=start; i < start + 4; i++){
            //places a ring at each correct x,y location
            //according to the rows and columns needed
            addObject(new Ring(),col1x+(c * colDiff),col1y+(r+1)*38);
            c++;
            r++;
        }
        //}
    }
    
    
    public void circleBackwardsDiagonal(int diag, String str, String initial){
        //adds the rainbow rings to the correct locations after a win
        
        int c = 0; //dictates the first column in the diagonal to be circled
        int r = 5; //dicates the first row in the diagonal to be circled
        int repeat = 6; //dicates how many rainbow rings are needed
        int start = str.indexOf(initial+initial+initial+initial);
        
        
        
        //the steps below set the starting row and column,
        //depending on which diagonal and which indexOf the streak was found
        if(diag==1){
            r=3;
            c=0;
        }
        else if(diag==2 && start==0){
            r=3;
            c=1;
        }
        else if(diag==2 && start==1){
            r=4;
            c=0;
        }
        else if(diag==3 && start==0){
            r=3;
            c=2;
        }
        else if(diag==3 && start==1){
            r=4;
            c=1;
        }
        else if(diag==3 && start==2){
            r=5;
            c=0;
        }
        else if(diag==4 && start==2){
            r=5;
            c=1;
        }
        else if(diag==4 && start==1){
            r=4;
            c=2;
        }
        else if(diag==4 && start==0){
            r=3;
            c=3;
        }
        else if(diag==5 && start==0){
            r=4;
            c=3;
        }
        else if(diag==5 && start==1){
            r=5;
            c=2;
        }
        else if(diag==6){
            r=5;
            c=3;
        }
        
        
         
        for(int i=start; i < start+4; i++){
            //places a ring at each correct x,y location
            //according to the rows and columns needed
            addObject(new Ring(),col1x+(c * colDiff),col1y+(r+1)*38);
            c++;
            r--;
        }
        //}
    }
    
    
    
    
    public void circleRow(int r, String str, String initial){
        //adds the rainbow rings to the correct locations after a win
        
        //check the indexOf so we know where the winner rings should start
        int start = str.indexOf(initial+initial+initial+initial);
        
        for(int c = start; c < start+4; c++){
            addObject(new Ring(),col1x+(c * colDiff),col1y+(r+1)*38);
            
        }
    }
    
    
    public void circleColumn(int c, String str, String initial){
        
        //adds the rainbow rings to the correct locations after a win
        
        int start = str.indexOf(initial+initial+initial+initial);
        if(start==2)
            start = 0;
        else if(start==0)
            start = 2;
        
        
            
        for(int r = start; r < start + 4; r++){
            addObject(new Ring(),col1x+(c * colDiff),col1y+(r+1)*38);
            
        }
    }
    
    
    
    public boolean containsWinningString(String str, String playInit){
        
        //checks if a string contains four identical initials in a row
        return str.contains(playInit+playInit+playInit+playInit);
        
    }
    
    
    public boolean tie()
    {
        //if there is a null in the grid, there is not a tie
        for(int i = 0;i<grid[0].length;i++)
        {
            if(grid[0][i]==null)
            {
                return false;
            }
        }
        tieCounter++;
        
        showText("Ties: " + tieCounter/actCounter,getWidth()/2,370); //shows number of ties
        
        
        
        
        
        return true;
    }
    
    public void showPlayer1Winner(){
        //adds 1 to player1 total wins
        playerBlackWinTotal+=1;
        
        //divides by actCounter since this code 
        //runs actCounter times before game is cleared
        int wins = playerBlackWinTotal/actCounter;
        int wins1 = playerRedWinTotal/actCounter;
        int games = wins + wins1 ;
        
        
    
        //calculates and shows all the stats
        showText("Wins: " + wins,70,130); //shows the total wins
        double winPercentage = (double)(wins)/(games);
        double winPercentageRounded = 100*Math.round(winPercentage*1000)/1000.0;
        showText(winPercentageRounded + "%",70,160); //shows the total win %
        double opponentPercentage = Math.round((100.0-winPercentageRounded)*1000)/1000.0;
        showText(opponentPercentage + "%",getWidth()-70,160); //shows the total win % for other player
    
        addObject(new BigRing(),70,130); //highlights winning name
        
        
    
        
        
    }
    
    public void showPlayer2Winner(){
        
        //adds 1 to player2 total wins
        playerRedWinTotal+=1;
        
        //divides by actCounter since this code 
        //runs actCounter times before game is cleared
        int wins = playerBlackWinTotal/actCounter;
        int wins1 = playerRedWinTotal/actCounter;
        int games = wins + wins1 ;
        
        
        
        //calculates and shows all the stats
        showText("Wins: " + wins1,getWidth()-70,130); //shows the total wins
        double winPercentage = (double)(wins1)/(games);
        double winPercentageRounded = 100*Math.round(winPercentage*1000)/1000.0;
        showText(winPercentageRounded + "%",getWidth()-70,160); //shows the total win %
        double opponentPercentage = Math.round((100.0-winPercentageRounded)*1000)/1000.0;
        showText(opponentPercentage + "%",70,160); //shows the total win % for other player
    
        addObject(new BigRing(),getWidth()-70,130); //highlights winning name
        
        
    
    }
    
    public void resetGrid(){
        //resets 2d array after a victory
         for(int r = 0; r < grid.length; r++){
            for(int c = 0; c < grid[0].length; c++){
            
            grid[r][c] = null;
            
            
            }
            
        }
        
        
    }
    
    public void resetGame(){
        
        gameCounter++;
        showText("Games played : " + gameCounter,getWidth()/2,15); //shows number of games
        
        
        //clears all checkers, rings, and board
        List objects = getObjects(null);
        removeObjects(objects);
        
        addObject(new Board(),300,200); //adds game board
        
        resetGrid(); //clears 2d array
        firstPlay = true; //resets firstPlay to true
        lastPlayedInitial = ""; //allows the first player of the next game to alternate
        playerTurn = 0;
        //so the first player of each game will alternate
        
    }
    
    
    public String[][] getGrid(){
        return this.grid;
    }
}

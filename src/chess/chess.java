package chess;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.io.IOException;

import board.*;
import piece.*;

/**
 * 
 * @author Chris Zachariah
 *
 */

public class chess {
	
	static boolean drawAsked;	// true one of the players has asked for a draw during their turn
	
	/**
	 * This is the main method where a loop is started to play the chess game.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args []) throws IOException {
		// create a welcome message before starting the game
		System.out.println("---------------------------------------------------------------");
		System.out.println("----- Hello! Welcome to 2-Player Chess by Chris Zachariah -----");
		System.out.println("------------- Player 1 = White | Player 2 = Black -------------");
		System.out.println("---------------------- Good Luck and START! -------------------");
		System.out.println("---------------------------------------------------------------\n");
		
		// build the chess board
		Piece[][] board = Board.buildBoard();
		Board.printTable(board);
		
		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));	// used to read in the players' moves
		
		boolean gameOver = true;	// determine when the game is done...
		boolean turn = true;		// true = white's turn | false = black's turn
		
		String nextMove;			// this will be the String where the player's next moves will be stored
		King king = null;			// use this pointer to find the king
		
		while(gameOver) {
			// white's turn
			if (turn) {
				// find the king, then see if he is in check, if yes, then also check for check-mate ... else if not in check , check for stale-mate
				for (int i = 0; i <= 7; i++) {
					for (int j = 0; j<= 7; j++) {
						if ((board[i][j] != null) && (board[i][j].isWhite == turn) && (board[i][j] instanceof King)) {
							king = (King) board[i][j];
							// check if the king is in check
							if (king.isInCheck(board,i,j,turn)) {
								//check for check-mate
								if (king.isInCheckMate(board,i,j,turn)) {
									System.out.println("Checkmate\n");
									endGameNow(false,!turn);
								} else {
									System.out.println("Check\n");
								}
							} else {
								// check for stale-mate
								if (king.isInStalemate(board,i,j,turn)) {
									System.out.println("Stalemate\n");
									endGameNow(true,!turn);
								}
							}
						}
					}
				}
							
				// reset all enPassants for the current team
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						if (board[i][j] instanceof Pawn && board[i][j].isWhite == turn) {
							Pawn check = (Pawn)board[i][j];
							check.canEnpassant = false;
						}
					}
				}
				
				System.out.print("White's move: ");
				nextMove = read.readLine().trim().toLowerCase();		// take the input, get rid of leading and trailing spaces and make all letters lower case
				System.out.println();									// skip to next line to keep the formatting consistent
				
				//check if there was a draw asked last move and if so check the current input to see if the draw is accepted or not ... 
				if (drawAsked) {
					if (nextMove.equals("draw")) {
						endGameNow(true,turn);
					}
					drawAsked = false;
				}
				
				// check the input and make the necessary moves 
				if (checkInput(board , nextMove , turn)) {
					int[] newMove = translateMoves(nextMove);
					
					if ((board[newMove[0]][newMove[1]] == null) || (board[newMove[0]][newMove[1]].isWhite != turn)) {
						System.out.println("Illegal move, try again");
						System.out.println();
						continue;
						
					} else if (board[newMove[0]][newMove[1]].isValidMove(board,newMove[0],newMove[1],newMove[2],newMove[3])) {
						board = board[newMove[0]][newMove[1]].makeMove(board,newMove[0],newMove[1],newMove[2],newMove[3]);
						Board.printTable(board);
					} else {
						System.out.println("Illegal move, try again");
						System.out.println();
						continue;
					}
					turn = !turn;
				// the input is not correct and the player must re-do the turn
				} else {
					System.out.println("Illegal move, try again");
					System.out.println();
					continue;
				}
			
			// black's turn
			} else {
				// find the king, then see if he is in check, if yes, then also check for check-mate ... else if not in check , check for stale-mate
				for (int i = 0; i <= 7; i++) {
					for (int j = 0; j<= 7; j++) {
						if ((board[i][j] != null) && (board[i][j].isWhite == turn) && (board[i][j] instanceof King)) {
							king = (King) board[i][j];
							// check if the king is in check
							if (king.isInCheck(board,i,j,turn)) {
								//check for check-mate
								if (king.isInCheckMate(board,i,j,turn)) {
									System.out.println("Checkmate\n");
									endGameNow(false,!turn);
								} else {
									System.out.println("Check\n");
								}
							} else {
								// check for stale-mate
								if (king.isInStalemate(board,i,j,turn)) {
									System.out.println("Stalemate\n");
									endGameNow(true,!turn);
								}
							}
						}
					}
				}
				
				// reset all enPassant's for the team
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						if (board[i][j] instanceof Pawn && board[i][j].isWhite == turn) {
							Pawn check = (Pawn)board[i][j];
							check.canEnpassant = false;
						}
					}
				}
				
				System.out.print("Black's move: ");
				nextMove = read.readLine().trim().toLowerCase();		// take the input, get rid of leading and trailing spaces and make all letters lower case
				System.out.println();									// skip to next line to keep the formatting consistent
				
				//check if there was a draw asked last move and if so check the current input to see if the draw is accepted or not ... 
				if (drawAsked) {
					if (nextMove.equals("draw")) {
						endGameNow(true,turn);
					}
					drawAsked = false;
				}
				
				// check the input and make the necessary moves 
				if (checkInput(board , nextMove , turn)) {
					int[] newMove = translateMoves(nextMove);
					
					if ((board[newMove[0]][newMove[1]] == null) || (board[newMove[0]][newMove[1]].isWhite != turn)) {
						System.out.println("Illegal move, try again");
						System.out.println();
						continue;
						
					} else if (board[newMove[0]][newMove[1]].isValidMove(board,newMove[0],newMove[1],newMove[2],newMove[3])) {
						board = board[newMove[0]][newMove[1]].makeMove(board,newMove[0],newMove[1],newMove[2],newMove[3]);
						Board.printTable(board);
					} else {
						System.out.println("Illegal move, try again");
						System.out.println();
						continue;
					}
					
					turn = !turn;
				// the input is not correct and the player must re-do the turn
				} else {
					System.out.println("Illegal move, try again");
					System.out.println();
					continue;
				}
			}
		} // ends the while loop
	} // ends the main() method
	
	/**
	 * Will check to see if the input is valid or not.
	 * @param board => compare the pieces on the board to make sure that right one's are chosen
	 * @param s	=> the input from the user
	 * @param turn => who's turn it is (white/black)
	 * @return => true if input is valid , false otherwise
	 */
	public static boolean checkInput(Piece[][] board , String s , boolean turn ) {
		// this is not just a regular set of moves
		if ((s.length() != 5) || (s.charAt(2) != ' ')) {
			if (s.length() >= 6) {
				// check for resignation (other team wins)
				if (s.equals("resign")) { 
					endGameNow(false,!turn);
				}
				// check for draw
				if (checkInput(board,s.substring(0,5),turn) && (s.substring(6).equals("draw?"))) {
					drawAsked = true;
					return true;
				}
				if ((s.length() == 7) && (Character.isLetter(s.charAt(0))) && (Character.isDigit(s.charAt(1))) && (Character.isWhitespace(s.charAt(2))) && (Character.isLetter(s.charAt(3))) && (Character.isDigit(s.charAt(4))) && (Character.isLetter(s.charAt(6)))) {
					// check for pawn promotion (with argument = not queen)
					int[] newMove = translateMoves(s);	// will need to translate the moves so that we can use the numbers to check the pieces on the board easily
					if (board[newMove[0]][newMove[1]] instanceof Pawn) {
						if (((board[newMove[0]][newMove[1]].isWhite == true) && (newMove[2] == 0)) || (((board[newMove[0]][newMove[1]].isWhite == false) && (newMove[2] == 7)))) {
							if ((s.length() == 7) && (checkInput(board,s.substring(0, 5), turn)) && (s.charAt(5) == ' ') && (Character.isLetter(s.charAt(6)))) {
								Pawn check = (Pawn)board[newMove[0]][newMove[1]];
								if ((s.charAt(6) == 'p') || (s.charAt(6) == 'r') || (s.charAt(6) == 'n') || (s.charAt(6) == 'b')) {
									check.readyToPromote = s.charAt(6);
									return true;
								} else {
									return false;
								}
							} 
						}
					}
				}
			}
			return false;
 		}
		// now check if the input is 4 characters in the format: letter,number,space,letter,number
		if ((!Character.isLetter(s.charAt(0))) || (!Character.isDigit(s.charAt(1))) || (!Character.isWhitespace(s.charAt(2))) || (!Character.isLetter(s.charAt(3))) || (!Character.isDigit(s.charAt(4)))) {
			return false;
		}
		// make sure letters are in the correct range
		if ((!(s.charAt(0) >= 'a' && s.charAt(0) <= 'h')) || (!(s.charAt(3) >= 'a' && s.charAt(3) <= 'h'))) {
			return false;
		}
		// make sure numbers are in the correct range
		if ((!(s.charAt(1) >= '1' && s.charAt(1) <= '8')) || (!(s.charAt(4) >= '1' && s.charAt(4) <= '8'))) {
			return false;
		}
		// no duplicate entries
		if ((s.charAt(0) == s.charAt(3)) && (s.charAt(1) == s.charAt(4))) {
			return false;
		}
		// pawn promotion - w/o argument - queen
		int[] newMove = translateMoves(s);
		if (board[newMove[0]][newMove[1]] instanceof Pawn) {
			if (((board[newMove[0]][newMove[1]].isWhite == true) && (newMove[2] == 0)) || (((board[newMove[0]][newMove[1]].isWhite == false) && (newMove[2] == 7)))) {
				Pawn check = (Pawn)board[newMove[0]][newMove[1]];
				check.readyToPromote = 'q';
			}
		}
				
		return true;
		
	} // ends the getInput() method
	
	/**
	 * This method will take the new moves in String from and translate it to a numbered form that can be easily used on the chess board
	 * @param s => the string to be translated
	 * @return => integer array (4 indices) with the new move => (format) oldRank,oldFile,newRank,newFile
	 */
	public static int[] translateMoves(String s) {
		int[] moves = new int[4];
		
		// the ASCII values of (lower-case a through h) - 49 will give the results (0 through 7) respectively
		char oldFile = (char) (s.charAt(0) - 49);		
		moves[1] = Character.getNumericValue(oldFile);
		
		moves[0] = 8 - Character.getNumericValue(s.charAt(1));
		
		// the ASCII values of (lower-case a through h) - 49 will give the results (0 through 7) respectively
		char a3 = (char) (s.charAt(3)-49);
		moves[3] = Character.getNumericValue(a3);
		
		moves[2] = 8 - Character.getNumericValue(s.charAt(4));
		
		return moves;
	}
	
	/**
	 * This method will end the game  given the right input 
	 * @param draw	=> true if there is a draw in the game
	 * @param turn => true if white wins or false if black wins
	 * @return => will end the program and either end the game in draw,white win or black win
	 */
	public static void endGameNow(boolean draw , boolean turn) {
		if (draw) {
			System.out.println("draw");			// there was a draw
		} else if (turn) {
			System.out.println("White wins");	// white wins
		} else {
			System.out.println("Black wins");	// black wins
		}
		System.exit(0);							// now the game can officially end
	} // ends the endGameNow method()
} // ends the chess class

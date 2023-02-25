package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game {
	public static final String SPORTS_CATEGORY = "Sports";
	public static final String SCIENCE_CATEGORY = "Science";
	public static final String POP_CATEGORY = "Pop";
	public static final String ROCK_CATEGORY = "Rock";
	ArrayList players = new ArrayList();
	int[] places = new int[6];
	int[] purses  = new int[6];
	boolean[] inPenaltyBox  = new boolean[6];

	LinkedList popQuestions = new LinkedList();
	LinkedList scienceQuestions = new LinkedList();
	LinkedList sportsQuestions = new LinkedList();
	LinkedList rockQuestions = new LinkedList();

	int currentPlayer = 0;
	boolean isGettingOutOfPenaltyBox;

	public  Game(){
		populateQuestions();
	}

	private void populateQuestions() {
		for (int i = 0; i < 50; i++) {
			addQuestion(i);
		}
	}

	private void addQuestion(int i) {
		popQuestions.addLast("Pop Question " + i);
		scienceQuestions.addLast(("Science Question " + i));
		sportsQuestions.addLast(("Sports Question " + i));
		rockQuestions.addLast(createRockQuestion(i));
	}

	public String createRockQuestion(int index){
		return "Rock Question " + index;
	}

	public boolean isPlayable() {
		return (howManyPlayers() >= 2);
	}

	public boolean add(String playerName) {


		players.add(playerName);
		places[howManyPlayers()] = 0;
		purses[howManyPlayers()] = 0;
		inPenaltyBox[howManyPlayers()] = false;

		System.out.println(playerName + " was added");
		System.out.println("They are player number " + howManyPlayers());
		return true;
	}

	public int howManyPlayers() {
		return players.size();
	}

	public void roll(int roll) {
		System.out.println(getCurrentPlayer() + " is the current player");
		System.out.println("They have rolled a " + roll);

		if (currentPlayerIsInPenaltyBox()) {
			inPenaltyBoxPlay(roll);
		} else {
			commonPlay(roll);
		}

	}

	private void commonPlay(int roll) {
		places[currentPlayer] = places[currentPlayer] + roll;
		if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;

		System.out.println(getCurrentPlayer()
				+ "'s new location is "
				+ places[currentPlayer]);
		System.out.println("The category is " + getCurrentCategory());
		askQuestion();
	}

	private Object getCurrentPlayer() {
		return players.get(currentPlayer);
	}

	private void inPenaltyBoxPlay(int roll) {
		if (roll % 2 != 0) {
			isGettingOutOfPenaltyBox = true;

			System.out.println(getCurrentPlayer() + " is getting out of the penalty box");
			commonPlay(roll);
		} else {
			System.out.println(getCurrentPlayer() + " is not getting out of the penalty box");
			isGettingOutOfPenaltyBox = false;
		}
	}

	private void askQuestion() {
		String currentCategory = getCurrentCategory();
		if (currentCategory.equals(POP_CATEGORY))
			System.out.println(popQuestions.removeFirst());
		if (currentCategory.equals(SCIENCE_CATEGORY))
			System.out.println(scienceQuestions.removeFirst());
		if (currentCategory.equals(SPORTS_CATEGORY))
			System.out.println(sportsQuestions.removeFirst());
		if (currentCategory.equals(ROCK_CATEGORY))
			System.out.println(rockQuestions.removeFirst());
	}


	private String getCurrentCategory() {
		if (places[currentPlayer] == 0) return POP_CATEGORY;
		if (places[currentPlayer] == 4) return POP_CATEGORY;
		if (places[currentPlayer] == 8) return POP_CATEGORY;
		if (places[currentPlayer] == 1) return SCIENCE_CATEGORY;
		if (places[currentPlayer] == 5) return SCIENCE_CATEGORY;
		if (places[currentPlayer] == 9) return SCIENCE_CATEGORY;
		if (places[currentPlayer] == 2) return SPORTS_CATEGORY;
		if (places[currentPlayer] == 6) return SPORTS_CATEGORY;
		if (places[currentPlayer] == 10) return SPORTS_CATEGORY;
		return ROCK_CATEGORY;
	}

	public boolean wasCorrectlyAnswered() {
		if (currentPlayerIsInPenaltyBox()){
			if (isGettingOutOfPenaltyBox) {
				System.out.println("Answer was correct!!!!");
				giveCoinToCurrentPlayer();
				System.out.println(getCurrentPlayer()
						+ " now has "
						+ getCurrentPlayerCoinCount()
						+ " Gold Coins.");

				boolean winner = didPlayerWin();
				goToNextPlayer();

				return winner;
			} else {
				goToNextPlayer();
				return true;
			}
		} else {

			System.out.println("Answer was correct!!!!");
			giveCoinToCurrentPlayer();
			System.out.println(getCurrentPlayer()
					+ " now has "
					+ getCurrentPlayerCoinCount()
					+ " Gold Coins.");

			boolean winner = didPlayerWin();
			goToNextPlayer();

			return winner;
		}
	}

	private int getCurrentPlayerCoinCount() {
		return purses[currentPlayer];
	}

	private void giveCoinToCurrentPlayer() {
		purses[currentPlayer]++;
	}

	private boolean currentPlayerIsInPenaltyBox() {
		return inPenaltyBox[currentPlayer];
	}

	public boolean wrongAnswer(){
		System.out.println("Question was incorrectly answered");
		System.out.println(getCurrentPlayer() + " was sent to the penalty box");
		putCurrentPlayerInPenaltyBox();

		goToNextPlayer();
		return true;
	}

	private void putCurrentPlayerInPenaltyBox() {
		inPenaltyBox[currentPlayer] = true;
	}

	private void goToNextPlayer() {
		currentPlayer++;
		if (currentPlayer == howManyPlayers()) currentPlayer = 0;
	}


	private boolean didPlayerWin() {
		return !(getCurrentPlayerCoinCount() == 6);
	}
}

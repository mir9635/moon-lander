package com.javarush.games.moonlander;

import com.javarush.engine.cell.*;


public class MoonLanderGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    private Rocket rocket;
    private GameObject landscape;
    private GameObject platform;
    private boolean isGameStopped;
    private int score;


    private boolean isUpPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;

    private void  createGame() {
        setTurnTimer(50);
        createGameObjects();
        drawScene();

        isUpPressed = false;
        isLeftPressed = false;
        isRightPressed = false;

        isGameStopped = false;

        score = 1000;
    }

    private void check() {
        if (rocket.isCollision(platform) && rocket.isStopped()) {
            win();
        } else if (rocket.isCollision(landscape)) {
            gameOver();
        }
    }

    private void win() {
        rocket.land();
        isGameStopped = true;
        showMessageDialog(Color.WHEAT, "WIN", Color.BLACK, 24);
        stopTurnTimer();
    }

    private void gameOver(){
        rocket.crash();
        isGameStopped = true;
        showMessageDialog(Color.WHEAT, "GAME OVER", Color.BLACK, 24);
        stopTurnTimer();
        score = 0;
        setScore(0);
    }

    private void createGameObjects() {
        rocket = new Rocket(WIDTH/2., 0);
        landscape = new GameObject(0,25, ShapeMatrix.LANDSCAPE);
        platform = new GameObject(23, HEIGHT-1, ShapeMatrix.PLATFORM);

    }

    private void  drawScene() {
        for (int y = 0; y < HEIGHT; y++){
            for (int x = 0; x < WIDTH; x++) {
                setCellColor(x,y, Color.BLACK);
            }
        }
        rocket.draw(this);
        landscape.draw(this);
    }



    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
        showGrid(false);
    }

    @Override
    public void onTurn(int step) {
        rocket.move(isUpPressed, isLeftPressed, isRightPressed);
        check();
        drawScene();
        if (score > 0) score-=1;
        setScore(score);
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if (x > WIDTH - 1 || x < 0 || y < 0 || y > HEIGHT - 1) {
            return;
        }
        super.setCellColor(x, y, color);
    }

    @Override
    public void onKeyPress(Key key) {

        switch (key){
            case SPACE: if (isGameStopped) createGame(); break;
            case UP: isUpPressed = true; break;
            case LEFT: isLeftPressed = true; isRightPressed = false; break;
            case RIGHT: isRightPressed = true; isLeftPressed = false; break;

        }
    }

    @Override
    public void onKeyReleased(Key key) {
        switch (key){
            case UP: isUpPressed = false; break;
            case LEFT: isLeftPressed = false; break;
            case RIGHT: isRightPressed = false; break;
        }
    }
}

package client;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import java.util.concurrent.ScheduledExecutorService;

public class Updater implements Runnable {
    GUIGameController guiGameController;
    GUILobbyController guiLobbyController;
    static boolean getPlayerlist = false;
    static boolean spyForSabotage = false;
    static boolean getGrouplist = false;
    String controller;
    ScheduledExecutorService scheduler;
    int spyLocation = 0;
    ImageView spy;
    ImageView sittingSpyView;
    ImageView spyWithChairView;
    ImageView spyView;
    boolean spyWithChair1 = true;
    double[][] spyPosition = {{-0.03, 0.360}, {-0.02, 0.365}, {-0.01, 0.370}, {0, 0.375}, {0.01, 0.380}, {0.02, 0.385}, {0.03, 0.390}, {0.04, 0.395},
            {0.05, 0.4}, {0.06, 0.405}, {0.07, 0.41}, {0.08, 0.415}, {0.09, 0.42}, {0.1, 0.425}, {0.11, 0.430}, {0.12, 0.435}, {0.13, 0.440}, {0.14, 0.444}, {0.15, 0.448}, {0.16, 0.452},
            {0.17, 0.456}, {0.18, 0.460}, {0.19, 0.464}, {0.2, 0.467}, {0.21, 0.470}, {0.22, 0.473}, {0.23, 0.476}, {0.24, 0.479}, {0.25, 0.481}, {0.26, 0.483}, {0.27, 0.485}, {0.28, 0.487},
            {0.29, 0.489}, {0.30, 0.491}, {0.31, 0.493}, {0.32, 0.495}, {0.33, 0.497}, {0.34, 0.499}, {0.35, 0.501}, {0.36, 0.503}, {0.37, 0.505}, {0.38, 0.507}, {0.39, 0.512}, {0.40, 0.514},
            {0.41, 0.516}, {0.42, 0.518}, {0.43, 0.520}, {0.44, 0.522}, {0.45, 0.524}, {0.46, 0.526}, {0.47, 0.528}, {0.48, 0.530}, {0.49, 0.532}, {0.5, 0.534}, {0.51, 0.536}, {0.52, 0.538},
            {0.53, 0.540}, {0.54, 0.542}, {0.55, 0.543}, {0.56, 0.544}, {0.57, 0.545}, {0.58, 0.546}, {0.59, 0.547}, {0.6, 0.548}, {0.61, 0.549}, {0.62, 0.55}, {0.63, 0.55}, {0.64, 0.55},
            {0.65, 0.55}, {0.66, 0.55}, {0.67, 0.55}, {0.68, 0.55}, {0.69, 0.55}, {0.70, 0.55}, {0.71, 0.55}, {0.72, 0.55}, {0.73, 0.55}, {0.74, 0.55}, {0.75, 0.55}, {0.76, 0.55}, {0.77, 0.55},
            {0.78, 0.55}, {0.79, 0.55}, {0.80, 0.55}, {0.81, 0.55}, {0.82, 0.55}, {0.83, 0.55}, {0.84, 0.55}, {0.85, 0.55}, {0.86, 0.55}, {0.87, 0.55}, {0.88, 0.55}, {0.89, 0.55}, {0.90, 0.55},
            {0.91, 0.55}, {0.92, 0.55}, {0.93, 0.55}, {0.94, 0.55}, {0.95, 0.55}, {0.96, 0.55}, {0.97, 0.55}, {0.98, 0.55}, {0.99, 0.55}, {1, 0.55}, {1.01, 0.55}, {1.02, 0.55}, {1.03, 0.55},
            {1.04, 0.55}, {1.05, 0.55}, {1.06, 0.55}, {1.07, 0.55}};
    double[] treePosition = {0.69, 0.28};
    double[] leftStartPosition = {spyPosition[0][0], spyPosition[0][1]};
    double[] rightStartPosition = {spyPosition[spyPosition.length - 1][0], spyPosition[spyPosition.length - 1][1]};
    double[][] spyWithChair1Position = new double[50][2];
    double[][] spyWithChair2Position = new double[50][2];

    Updater(GUIGameController guiGameController, ScheduledExecutorService scheduler) {
        this.guiGameController = guiGameController;
        controller = "game";
        this.scheduler = scheduler;
         spy = guiGameController.sittingSpyView;
        for (int i = 0; i < 50; i++) {
            spyWithChair1Position[i][0] = treePosition[0] - (((treePosition[0] - leftStartPosition[0]) / 50) * i);
            spyWithChair1Position[i][1] = treePosition[1] + (((leftStartPosition[1] - treePosition[1]) / 50) * i);
        }
        for (int i = 0; i < 50; i++) {
            spyWithChair2Position[i][0] = rightStartPosition[0] - (((rightStartPosition[0] - treePosition[0]) / 50) * i);
            spyWithChair2Position[i][1] = rightStartPosition[1] - (((rightStartPosition[1] - treePosition[1]) / 50) * i);
        }
    }
    Updater(GUILobbyController guiLobbyController, ScheduledExecutorService scheduler) {
        this.guiLobbyController = guiLobbyController;
        controller = "lobby";
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        if (!Main.connectionHandler.runningSocket) {
            scheduler.shutdown();
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                switch (controller) {
                    case "game":
                        guiGameController.fitBuildingSizes();
                        break;
                    case "lobby":
                        if (Main.connectionHandler.gameIsRunning) {
                            scheduler.shutdown();
                        }
                        guiLobbyController.getPlayerlistAll();
                        break;
                }
            }
        });
    }

    /**
     * sets the spy pictures position so it moves across the screen along the way, waits 5' and starts again
     * @param paneHeight height of the pane
     */
    public void setSpyPosition(double paneHeight) {
        if (spy == sittingSpyView) {
            spy.setFitWidth(guiGameController.paneHeight * 0.04);
            sittingSpyView.setX(paneHeight * treePosition[0]);
            sittingSpyView.setY(paneHeight * treePosition[1]);
            spyLocation++;
            changeSpy();
        } else if (spy == spyWithChairView && spyWithChair1) {
            spy.setFitWidth(guiGameController.paneHeight * 0.04);
            spyWithChairView.setX(paneHeight * spyWithChair1Position[spyLocation][0]);
            spyWithChairView.setY(paneHeight * spyWithChair1Position[spyLocation][1]);
            spyLocation++;
            changeSpy();
        } else if (spy == spyView) {
            spy.setFitWidth(guiGameController.paneHeight * 0.04);
            spyView.setX(paneHeight * spyPosition[spyLocation][0]);
            spyView.setY(paneHeight * spyPosition[spyLocation][1]);
            spyLocation++;
            changeSpy();
        } else if (spy == spyWithChairView && !spyWithChair1) {
            spy.setFitWidth(guiGameController.paneHeight * 0.04);
            spyWithChairView.setX(paneHeight * spyWithChair2Position[spyLocation][0]);
            spyWithChairView.setY(paneHeight * spyWithChair2Position[spyLocation][1]);
            spyLocation++;
            changeSpy();
        }
    }

    public void setSpy() {
        sittingSpyView = guiGameController.sittingSpyView;
        spyView = guiGameController.spyView;
        spyWithChairView = guiGameController.spyWithChairView;
        sittingSpyView.setFitWidth(guiGameController.paneHeight * 0.04);
        sittingSpyView.setX(guiGameController.paneHeight * treePosition[0]);
        sittingSpyView.setY(guiGameController.paneHeight * treePosition[1]);
        sittingSpyView.setVisible(true);
        guiGameController.spyIsActive = true;
    }

    private void changeSpy() {
        if (spy == sittingSpyView && spyLocation == 25) {
            changeVisibility(spyWithChairView, treePosition);
            spyWithChair1 = true;
        } else if (spy == spyWithChairView && spyWithChair1 && spyLocation == 50) {
            changeVisibility(spyView, leftStartPosition);
        } else if (spy == spyView && spyLocation == spyPosition.length) {
            changeVisibility(spyWithChairView, rightStartPosition);
            spyWithChair1 = false;
        } else if (spy == spyWithChairView && !spyWithChair1 && spyLocation == 50) {
            changeVisibility(sittingSpyView, treePosition);
        }
    }

    private void changeVisibility(ImageView newSpy, double[] startPosition) {
        spy.setVisible(false);
        spy = newSpy;
        spy.setFitWidth(guiGameController.paneHeight * 0.04);
        spy.setX(guiGameController.paneHeight * startPosition[0]);
        spy.setY(guiGameController.paneHeight * startPosition[1]);
        spy.setVisible(true);
        spyLocation = 0;
    }

    public void setSaboteur() {
        spyView.setImage(guiGameController.spySabView.getImage());
        spyWithChairView.setImage(guiGameController.spyWithChairSabView.getImage());
        sittingSpyView.setImage(guiGameController.sittingSpySabView.getImage());
        guiGameController.saboteurIsActive = true;
    }
}

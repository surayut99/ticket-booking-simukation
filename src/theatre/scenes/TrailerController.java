package theatre.scenes;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import theatre.tools.EffectController;

public class TrailerController {
    @FXML
    MediaView player;
    @FXML
    ImageView actionImg, fullScreen;

    private Media media;
    private MediaPlayer mediaPlayer;
    private Image playImg, pauseImg;
    private String path;

    @FXML public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                playImg = new Image("/movieData/player_tools_icon/play_icon.png");
                pauseImg = new Image("/movieData/player_tools_icon/pause_icon.png");
                Stage thisStage = (Stage) player.getScene().getWindow();
                thisStage.setOnCloseRequest(e -> mediaPlayer.stop());
                try {
                    String externalPath = getClass().getResource(path).toExternalForm();
                    media = new Media(externalPath);
                    mediaPlayer = new MediaPlayer(media);
                    DoubleProperty width = player.fitWidthProperty();
                    DoubleProperty height = player.fitHeightProperty();
                    width.bind(Bindings.selectDouble(player.sceneProperty(), "width"));
                    height.bind(Bindings.selectDouble(player.sceneProperty(), "height"));
                    player.setMediaPlayer(mediaPlayer);
                    actionImg.setImage(playImg);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setPath(String path) {
        this.path = path;
    }

    @FXML
    public void mouseClickOnAction(MouseEvent event) {
        ImageView action = (ImageView) event.getSource();

        if (action.getImage() == playImg) {
            mediaPlayer.play();
            action.setImage(pauseImg);
        }
        else {
            mediaPlayer.pause();
            action.setImage(playImg);
        }
    }

    @FXML
    private void mouseEnterOnAction(MouseEvent event) {
        ImageView action = (ImageView) event.getSource();
        Stage stage = (Stage) player.getScene().getWindow();
        if (stage.isFullScreen() && action == fullScreen) return;
        EffectController.createFadeTransition(action, 0.5, 1).play();
    }

    @FXML
    private void mouseExitOnAction(MouseEvent event) {
        ImageView action = (ImageView) event.getSource();
        Stage stage = (Stage) player.getScene().getWindow();
        if (action.getImage() == playImg) return;
        if (stage.isFullScreen()) {
            EffectController.createFadeTransition(action, 0.5, 0).play();
            return;
        }
        EffectController.createFadeTransition(action, 0.5, 0.25).play();
    }

    @FXML
    private void mouseClickOnFullScreen(MouseEvent event) {
        Stage stage = (Stage) player.getScene().getWindow();
        if (!stage.isFullScreen()) {
            EffectController.createFadeTransition(actionImg, 0.5, 0).play();
            EffectController.createFadeTransition(fullScreen, 0.5, 0).play();
            stage.setFullScreen(true);
        }
    }
}

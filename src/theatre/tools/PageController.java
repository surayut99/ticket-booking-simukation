package theatre.tools;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.AnchorPane;

import java.util.Stack;

public class PageController {
    public static Stack<Node> stackPages = new Stack<>();
    public static Stack<AnchorPane> stackWaringMessages = new Stack<>();
    public static Button backButton;
    public static AnchorPane mainShowContent;
    public static Label lastHomePage;

    public static void back() {
        if (stackWaringMessages.peek() != null) {
            mainShowContent.getChildren().get(0).setEffect(new ColorAdjust(0,0,-0.5,0));
            ((AnchorPane) stackPages.firstElement()).getChildren().add(stackWaringMessages.peek());
            return;
        }
        stackPages.pop();
        stackWaringMessages.pop();
        mainShowContent.getChildren().clear();
        mainShowContent.getChildren().add(peakPage());
        if (stackPages.size() == 2) {
            EffectController.createFadeTransition(backButton, 0.25, 0).play();
            backButton.setDisable(true);
        }
        EffectController.createFadeTransition(peakPage(), 0.25, 1).play();
    }

    public static void addPage(Node page) {
        stackPages.push(page);
    }

    public static void goHomepage() {
        if (stackWaringMessages.size() == 1) return;

        while (stackPages.size() != 1) {
            stackPages.pop();
            stackWaringMessages.pop();
        }
        EffectController.createFadeTransition(backButton, 0.5, 0).play();
        mainShowContent.getChildren().clear();
        backButton.setDisable(true);
    }

    public static Node peakPage() {
        return stackPages.peek();
    }

    public static AnchorPane createWarningMessage() {
        String topic = "You have just done something on this page.";
        String description = "If you go back, your data you did will lose.";
        return NodeCreator.createWarningAnchorPane(topic, description);
    }
}

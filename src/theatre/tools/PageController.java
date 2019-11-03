package theatre.tools;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

import java.util.Stack;

public class PageController {
    private static Stack<Node> stackPages = new Stack<>();
    private static Stack<AnchorPane> stackWaringMessages = new Stack<>();
    private static Button backButton;
    private static ScrollPane mainShowContent;
    private static Label lastHomePage;

    public static void back() {
        if (stackWaringMessages.peek() != null) {
            WarningController.showWarning(stackWaringMessages.peek());
            return;
        }
        stackPages.pop();
        stackWaringMessages.pop();
        mainShowContent.setContent(peakPage());
        if (stackPages.size() == 2) {
            EffectController.createFadeTransition(backButton, 0.25, 0).play();
            backButton.setDisable(true);
        }
        EffectController.createFadeTransition(peakPage(), 0.25, 1).play();

    }

    public static void goHomepage() {
        if (stackPages.size() == 1) return;

        while (stackPages.size() != 2) {
            stackPages.pop();
            stackWaringMessages.pop();
        }
        EffectController.createFadeTransition(backButton, 0.5, 0).play();
        mainShowContent.setContent(null);
        backButton.setDisable(true);
    }

    public static Node peakPage() {
        return stackPages.peek();
    }

//    public static AnchorPane createWarningMessage() {
//        String topic = "You have just done something on this page.";
//        String description = "If you go back, your data you did will lose.";
//        return NodeCreator.createWarningAnchorPane(topic, description);
//    }

    public static boolean hasWarning() {
        for (AnchorPane anchorPane : stackWaringMessages) {
            if (anchorPane != null) return true;
        }
        return false;
    }


    public static Stack<Node> getStackPages() {
        return stackPages;
    }

    public static Stack<AnchorPane> getStackWaringMessages() {
        return stackWaringMessages;
    }

    public static Button getBackButton() {
        return backButton;
    }

    public static ScrollPane getMainShowContent() {
        return mainShowContent;
    }

    public static Label getLastHomePage() {
        return lastHomePage;
    }

    public static void setBackButton(Button backButton) {
        PageController.backButton = backButton;
    }

    public static void setMainShowContent(ScrollPane mainShowContent) {
        PageController.mainShowContent = mainShowContent;
    }

    public static void setLastHomePage(Label lastHomePage) {
        PageController.lastHomePage = lastHomePage;
    }
}

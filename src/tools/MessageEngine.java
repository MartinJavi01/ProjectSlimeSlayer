package tools;
/*
 * @author Javier Martín
 * @version 1.0
 * @since 1.0
 *
 */
/*This one of the classes I am most proud of, I created this class for showing text in the screen by writing it
* in a String using different special combinations to tell the compiler where should the line or the page
* stop*/
import java.awt.*;
import java.util.ArrayList;

public class MessageEngine {

    private String text;
    private int fontSize;

    private Rectangle textRec;
    private Rectangle innerRec;
    private Rectangle extRec;

    private Color textColor;
    private Color innerRecColor;
    private Color extRecColor;

    private Font textFont;

    private int x;
    private int y;

    private int width;
    private int height;

    private int maxCharsPerLine;
    private int maxCharsPerRow;
    private int maxCharsPerPage;
    private ArrayList<String> lines;

    private int currentPage;
    private int currentLine;
    private int numPages;
    private int numLines;

    private boolean isVisible;
    private boolean shouldRemove;
    private boolean shouldBeVisible;

    public MessageEngine(String s, int fontSize, int x, int y, int width, int height) {
        this.fontSize = fontSize;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = s;

        lines = new ArrayList<String>();
        isVisible = false;
        shouldRemove = false;
        shouldBeVisible = true;
        maxCharsPerLine = width / (fontSize / 2);
        maxCharsPerRow = height / fontSize;
        maxCharsPerPage = maxCharsPerLine * maxCharsPerRow;
        textFont = new Font("", Font.PLAIN, fontSize);
        numPages = 1;
        numLines = 1;

        currentPage = 1;
        currentLine = 0;

        textRec = new Rectangle(x, y, width, height);
        textColor = Color.white;
        innerRec = new Rectangle(x - 2, y - 2, width + 4, height + 4);
        innerRecColor = Color.black;
        extRec = new Rectangle(x - 4, y - 4, width + 8, height + 8);
        extRecColor = Color.white;

        setTextLines();
    }
    //this method deals with the Strings and tells the compiler how the messages should be written
    private void setTextLines(){
        String[] strings = null;
        if(text.contains("/")) {
            strings = text.split("/");
            for(int i = 0;i < strings.length;i++) {
                int spaces = 24 - strings[i].length();
                for(int j = 0;j < spaces;j++) {
                    strings[i] += " ";
                }
            }
        }else{
            strings = new String[]{text};
        }
        numLines = strings.length;
        numPages = (numLines / 3) + 1;
        for (int i = 0; i < strings.length; i++) {
            lines.add(strings[i]);
        }
    }

    /*when rendering, the game will render the text letter by letter
     and positioning this letter in the space it should be*/
    public void render(Graphics2D g) {
        if (!isVisible) {
            return;
        }

        g.setColor(extRecColor);
        g.fill(extRec);
        g.setColor(innerRecColor);
        g.fill(innerRec);
        g.setColor(textColor);
        g.setFont(textFont);

        if (currentPage == 1){
            if(numLines < 3){
                for(int i = 0;i < numLines;i++){
                    g.drawString(lines.get(i), x, y + 10 + i * (fontSize + 5));
                }
            }else {
                for (int i = 0; i < 3; i++) {
                    g.drawString(lines.get(i), x, y + 10 + i * (fontSize + 5));
                }
            }
        } else {
            if (shouldRemove) {
                if(lines.size() < 3){
                    for (int i = 0; i < lines.size(); i++) {
                        lines.remove(lines.get(0));
                    }
                }else {
                    for (int i = 0; i < 3; i++) {
                        lines.remove(lines.get(0));
                    }
                }
                shouldRemove = false;
            }
            if(lines.size() < 3){
                for (int i = 0; i < lines.size(); i++) {
                    g.drawString(lines.get(i), x, y + 10 + i * (fontSize + 5));
                }
            }else {
                for (int i = 0; i < 3; i++) {
                    g.drawString(lines.get(i), x, y + 10 + i * (fontSize + 5));
                }
            }
        }
    }

    /*this methods are used to help the render method doing its task on rendering the letters and
    telling it where to stop*/

    public void setVisible(boolean b) {
        if(isVisible == b){
            return;
        }
        isVisible = b;
    }

    public void increasePage() {
        if (currentPage == numPages || currentLine == numLines) {
            shouldBeVisible = false;
            return;
        }
        shouldRemove = true;
        currentLine += 3;
        currentPage++;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public boolean shouldBeVisible() {
        return shouldBeVisible;
    }
}

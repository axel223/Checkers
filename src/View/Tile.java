package View;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import static Controller.Control.TILE_SIZE;

public class Tile extends Rectangle {

    private Piece piece;
    boolean light;
    Color oldColor;

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Tile(boolean light, int x, int y) {
        this.light = light;
        setWidth(TILE_SIZE);
        setHeight(TILE_SIZE);

        relocate(x * TILE_SIZE, y * TILE_SIZE);

        oldColor = light ? Color.valueOf("#feb") : Color.valueOf("#582");
        setFill(oldColor);
    }

}

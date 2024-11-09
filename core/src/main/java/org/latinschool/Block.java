package org.latinschool;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Block {
    private Vector2 pos;
    private Color color;
    private float health;

    public Block(Vector2 pos, Color color, float health) {
        this.pos = pos;
        this.color = color;
        this.health = health;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 newPos) {
        pos = newPos;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color newColor) {
        color = newColor;
    }
}

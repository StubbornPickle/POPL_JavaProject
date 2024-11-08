package org.latinschool;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Block {
    private Vector2 pos;
    private float health;
    private Color color;

    public Block(Vector2 pos, float health, Color color) {
        this.pos = pos;
        this.health = health;
        this.color = color;
    }

    public Vector2 getPos() {
        return pos;
    }

    public float getHealth() {
        return health;
    }

    public void changeHealth(float by) {
        health = health + by;
    }

    public Color getColor() {
        return color;
    }

}

package org.latinschool;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.ceil;

public class ProceduralTerrain {

    private final int resolution;
    private final Color[] depthColors;
    private final float[] depthThresholds;
    private final Color[] caveLayers;
    private final float caveScale;
    private final Camera camera;

    private final float blockSize;
    private int depth = 0;

    private Block[][] blocks;

    public ProceduralTerrain(float height, int resolution, Color[] depthColors, float[] depthThresholds, Color[] caveLayers, float caveScale, Camera camera) {
        this.resolution = resolution;
        this.depthColors = depthColors;
        this.depthThresholds = depthThresholds;
        this.caveLayers = caveLayers;
        this.caveScale = caveScale;
        this.camera = camera;

        blockSize = camera.viewportWidth / resolution;

        initializeTerrain(height);
    }

    private void initializeTerrain(float height) {
        int rows = ceil(camera.viewportHeight / blockSize) + 1;
        blocks = new Block[rows][resolution];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < resolution; col++) {
                float x = col * blockSize;
                float y = height - (row * blockSize);
                blocks[row][col] = new Block(new Vector2(x, y), getDepthColor(row), 1);
            }
        }
    }

    public void draw(ShapeRenderer shapeRenderer) {
        for (Block[] row : blocks) {
            for (Block block : row) {
                Vector2 pos = block.getPos();
                shapeRenderer.setColor(block.getColor());
                shapeRenderer.rect(pos.x, pos.y, blockSize * 0.9f, -blockSize * 0.9f);
            }
        }
    }

    public void update() {
        for (int i = 0; i < 2; i++) { // Check twice
            float terrainHeight = blocks[0][0].getPos().y;
            float viewportTop = camera.position.y + camera.viewportHeight / 2;
            float distanceToViewport = viewportTop - terrainHeight;

            if (distanceToViewport <= -blockSize) {
                cycleRow();
            } else {
                break;
            }
        }
    }

    private Color getDepthColor(float depth) {
        Random random = new Random();

        for (int i = 0; i < depthThresholds.length; i++) {
            float nextThreshold = (i + 1 < depthThresholds.length) ? depthThresholds[i + 1] : Integer.MAX_VALUE;
            int transitionRange = Math.max(3, Math.min(10, (int) ((nextThreshold - depthThresholds[i]) / 2)));

            if (depth >= depthThresholds[i] && depth < nextThreshold) {
                if (i == 0 || i == 1) { // No transition for first 2 layers
                    return depthColors[i];
                }

                float transitionDepth = depth - depthThresholds[i];

                if (transitionDepth < transitionRange) {
                    float probability = transitionDepth / transitionRange;

                    if (random.nextFloat() < probability) {
                        return depthColors[i]; // Current color layer
                    } else {
                        return depthColors[i - 1]; // Previous color layer
                    }
                } else {
                    // Outside the transition range, use current color
                    return depthColors[i];
                }
            }
        }

        // Default to the last color
        return depthColors[depthColors.length - 1];
    }


    private void cycleRow() {
        depth++;
        float newY = blocks[blocks.length - 1][0].getPos().y - blockSize;

        for (Block block : blocks[0]) {
            block.setPos(new Vector2(block.getPos().x, newY));
            int trueDepth = blocks.length - 1 + depth;
            block.setColor(getDepthColor(trueDepth));
        }

        Block[] topRow = blocks[0];
        System.arraycopy(blocks, 1, blocks, 0, blocks.length - 1);
        blocks[blocks.length - 1] = topRow;
    }
}

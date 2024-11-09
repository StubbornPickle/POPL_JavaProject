package org.latinschool;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends ApplicationAdapter {

    private static final float WORLD_WIDTH = 10;
    private static final float WORLD_HEIGHT = 10;

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Viewport viewport;
    private ProceduralTerrain proceduralTerrain;
    private World world;

    @Override
    public void create() {
        Box2D.init(); // Initialize Box2D
        initializeBox2DWorld();
        initializeCameraAndViewport();
        initializeTerrain();
        initializeMisc();
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        world.dispose(); // Clean up Box2D resources
    }

    private void initializeMisc() {
        shapeRenderer = new ShapeRenderer();
    }

    private void initializeCameraAndViewport() {
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();
        camera.update();
    }

    private void initializeTerrain() {
        proceduralTerrain = new ProceduralTerrain(
            WORLD_HEIGHT / 2,                                                // Starting height
            15,                                                                     // Blocks per row
            new Color[]{Color.GREEN, Color.BROWN, Color.GRAY, Color.DARK_GRAY},     // Terrain depth colors
            new float[]{0, 1, 10, 30},                                              // Depth thresholds for colors
            new Color[]{Color.DARK_GRAY, Color.GRAY},                               // Cave layer colors
            0.01f,                                                                  // Cave noise scale
            camera                                                                  // Reference camera
        );
    }

    private void initializeBox2DWorld() {
        world = new World(new Vector2(0, -9.8f), true); // Gravity set to -9.8 in y-axis
    }

    private void input() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.position.add(0, -2.5f * deltaTime, 0); // Move camera down at 1 meter per second
            camera.update();
        }
    }

    private void logic() {
        world.step(1 / 60f, 6, 2); // Update the physics world
        proceduralTerrain.update();
    }

    private void draw() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f); // Background color

        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        proceduralTerrain.draw(shapeRenderer);
        shapeRenderer.end();
    }
}

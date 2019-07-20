package com.redbay.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.redbay.game.model.Track;
import com.redbay.game.model.TrackSegment;

import java.util.ArrayList;

public class TrackScreen implements Screen
{
  private final DebugConsole debugConsole;

  private final static int SCREEN_SIZE_X = 640;
  private final static int SCREEN_SIZE_Y = 480;
  private final static int SCREEN_HALF_SIZE_X = 320;
  private final static int SCREEN_HALF_SIZE_Y = 240;

  private final static int CAR_MAX_SPEED = 8800;  // CM/sec
  private final static int CAR_ACCEL = 40;
  private final static int CAR_BRAKE = 40;

  private final static int CAMERA_HEIGHT_FROM_GROUND = -500;
  private final static int SCALING_FACTOR_Y = 416;    // (y_res/2)/tan(angle/2) -- using 60(deg)

  private final SpriteBatch batch;
  private final ArrayList<Texture> img = new ArrayList<>();

  private int carSpeed = 4400;
  private int carPos = 0;

  private int xOffset = 0;

  Track track = new Track();

  public TrackScreen()
  {
    // Initialize some shiz
    batch = new SpriteBatch();

    img.add(new Texture("RoadWide.png"));
    img.add(new Texture("RoadWideDark.png"));

    // Initialize new track
    track.addSegment(new TrackSegment(500, 0));
    track.addSegment(new TrackSegment(100, 15));
    track.addSegment(new TrackSegment(300, -15));

    debugConsole = new DebugConsole();
  }

  @Override
  public void show()
  {

  }

  @Override
  public void render(float delta)
  {
    if (Gdx.input.isKeyJustPressed(Input.Keys.D))
    {
      debugConsole.enableConsole(!debugConsole.isConsoleEnabled());
    }

    boolean isGivingThrottle = false;

    // Give throttle.
    if (Gdx.input.isKeyPressed(Input.Keys.UP))
    {
      carSpeed += CAR_ACCEL;

      // Cap speed to car's max speed
      if (carSpeed > CAR_MAX_SPEED)
        carSpeed = CAR_MAX_SPEED;

      isGivingThrottle = true;
    }

    // Give brakes.
    if (Gdx.input.isKeyPressed((Input.Keys.DOWN)))
    {
      carSpeed -= CAR_BRAKE;
    }

    // Steer the car.
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
    {
      xOffset -= 50;
    }

    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
    {
      xOffset += 50;
    }

    // Adjust car speed on input.
    if (!isGivingThrottle)
      carSpeed -= 10;

    if (carSpeed < 0)
      carSpeed = 0;

    // Pointer to which road texture we're going to be using.
    Texture road;

    // Initialize curving state.
    int accumulatedCurve = 0;
    int ddCurve = 0;

    // Determine car position.
    carPos += (int)(delta * carSpeed);

    // Begin redering frame.
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    batch.begin();

    for (int scrY = 0; scrY < 200 ; scrY++)
    {
      // TODO: This is fixed, replace with an array to avoid calculations.
      // Determine Z position
      int z = carPos + (CAMERA_HEIGHT_FROM_GROUND * SCALING_FACTOR_Y) / (scrY - SCREEN_HALF_SIZE_Y);

      // Determine correct road texture.
      // 250cm is the "rumble length" -- each "small" section of road.
      int roadIndex = (z / 250) % 2;
      road = img.get(roadIndex);

      // Center of the image, minus 320 (half the screen width)
      int roadX = (road.getWidth() >> 1) - SCREEN_HALF_SIZE_X; // start off centered
      int roadY = road.getHeight() - scrY  - 1;   // -1, 0 based index.

      // Perspective adjustments.
      // This ratio is multiplied by 1000, to avoid having to store it in floats.  It is then
      //   lineOffsetRatio is linear: y = a*x + b.
      //   Solving for y = 1. x = 0: b = 1.
      //   Solving for y = 0, x = 200: a = -1/200.
      //   Multiply everything by 1000 to scale and avoid floating points.
      //   (lineOffsetRation = (-1/200*scrY + 1) * 1000
      //   We approximate -1/200*1000 by -5.
      int lineOffsetRatio = (scrY * -5 + 1000);
      int horizontalOffset = ((xOffset * lineOffsetRatio) / 1000);

      // Curvature adjustments.
      ddCurve += track.getTrackCurvature(z % track.getTrackLength());;
      accumulatedCurve += ddCurve;

      int curvatureOffset = accumulatedCurve / 1000;

      // Draw the line.
      batch.draw(
          road,
          0,
          scrY,
          roadX + horizontalOffset + curvatureOffset,
          roadY,
          SCREEN_SIZE_X,
          1);
    }

    debugConsole.render(batch);
    batch.end();
  }

  @Override
  public void resize(int width, int height)
  {

  }

  @Override
  public void pause()
  {

  }

  @Override
  public void resume()
  {

  }

  @Override
  public void hide()
  {

  }

  @Override
  public void dispose()
  {
    batch.dispose();

    img.forEach(Texture::dispose);
  }
}

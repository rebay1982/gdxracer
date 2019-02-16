package com.redbay.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class TitleScreen implements Screen
{
  private final static int SCREEN_SIZE_X = 640;
  private final static int SCREEN_SIZE_Y = 480;
  private final static int SCREEN_HALF_SIZE_X = 320;
  private final static int SCREEN_HALF_SIZE_Y = 240;

  private final static int CAR_MAX_SPEED = 8800;  // CM/sec
  private final static int CAR_ACCEL = 40;
  private final static int CAR_BRAKE = 40;

  private final static int CAMERA_HEIGHT_FROM_GROUND = -500;
  private final static int SCALING_FACTOR_Y = 416;    // (y_res/2)/tan(angle/2) -- using 60(deg)

  private SpriteBatch batch;
  private ArrayList<Texture> img = new ArrayList<>();

  private int carSpeed = 4400;
  private int carPos = 0;

  private int xOffset = 0;

  Track track = new Track();

  public TitleScreen()
  {
    // Initialize some shiz
    batch = new SpriteBatch();

    img.add(new Texture("RoadWide.png"));
    img.add(new Texture("RoadWideDark.png"));

    // Initialize new track
    track.addSegment(new TrackSegment(500, 0));
    track.addSegment(new TrackSegment(500, 0));
  }

  @Override
  public void show()
  {

  }

  @Override
  public void render(float delta)
  {
    boolean isGivingThrottle = false;
    if (Gdx.input.isKeyPressed(Input.Keys.UP))
    {
      carSpeed += CAR_ACCEL;

      // Cap speed to car's max speed
      if (carSpeed > CAR_MAX_SPEED)
        carSpeed = CAR_MAX_SPEED;

      isGivingThrottle = true;
    }

    if (Gdx.input.isKeyPressed((Input.Keys.DOWN)))
    {
      carSpeed -= CAR_BRAKE;
    }

    if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
    {
      xOffset -= 20;
    }

    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
    {
      xOffset += 20;
    }


    if (!isGivingThrottle)
      carSpeed -= 10;

    if (carSpeed < 0)
      carSpeed = 0;

    carPos += (int)(delta * carSpeed);

    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    batch.begin();

    Texture road;
    for (int scrY = 0; scrY < 200 ; scrY++)
    {
      int z = carPos + (CAMERA_HEIGHT_FROM_GROUND * SCALING_FACTOR_Y) / (scrY - SCREEN_HALF_SIZE_Y);

      // 250cm is the "rumble length" -- each "small" section of road.
      int roadIndex = (z / 250) % 2;

      track.getTrackCurvature(z % track.getTrackLength());

      road = img.get(roadIndex);

      // TODO: Clean this up a bit.
      // This ratio is multiplied by 1000, to avoid having to store it in floats.  It is then
      // divided by 1000 when it is multiplied by
      // the xOffset.
      int lineOffsetRatio = (scrY * -4 + 1000);
      batch.draw(
          road,
          0,
          scrY,
          (road.getWidth() >> 1) - SCREEN_HALF_SIZE_X + (xOffset * lineOffsetRatio) / 1000,  // Center of the image, minus 320 (half the screen width)
          road.getHeight() - scrY  - 1,  // -1, 0 based index.
          SCREEN_SIZE_X,
          1);
    }

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

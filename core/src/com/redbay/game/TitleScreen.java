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
  private final static int CAR_MAX_SPEED = 8800;  // CM/sec
  private final static int CAR_ACCEL = 40;
  private final static int CAR_BRAKE = 40;

  private final static int CAMERA_HEIGHT_FROM_GROUND = -500;
  private final static int SCALING_FACTOR_Y = 416; // (y_res/2)/tan(angle/2) -- using 60(deg)

  private SpriteBatch batch;
  private ArrayList<Texture> img = new ArrayList<>();

  private int carSpeed = 4400;
  private int carPos = 0;

  Track track = new Track();

  public TitleScreen()
  {
    // Initialize some shiz
    batch = new SpriteBatch();

    img.add(new Texture("Road.png"));
    img.add(new Texture("RoadDark.png"));

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

//    if (!isGivingThrottle)
//      carSpeed -= 10;

    if (carSpeed < 0)
      carSpeed = 0;

    carPos += (int)(delta * carSpeed);

    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    batch.begin();

    Texture road;
    for (int scrY = 0; scrY < 200 ; scrY++)
    {
      int z = carPos + (CAMERA_HEIGHT_FROM_GROUND * SCALING_FACTOR_Y) / (scrY - 240);

      // 250cm is the "rumble length" -- each "small" section of road.
      int roadIndex = (z / 250) % 2;

      road = img.get(roadIndex);
      batch.draw(
          road,
          0,
          scrY,
          0,
          road.getHeight() - 1 - scrY,
          road.getWidth(),
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

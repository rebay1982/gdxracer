package com.redbay.game;

import com.badlogic.gdx.Game;

public class GdxRacer extends Game
{
  @Override
  public void create()
  {
    setScreen(new TitleScreen());
  }
}

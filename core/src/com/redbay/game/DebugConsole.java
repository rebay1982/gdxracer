package com.redbay.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class DebugConsole
{
  private final BitmapFont font;
  private final StringBuilder debugMessage;

  private boolean isConsoleEnabled = false;


  public DebugConsole()
  {
    font = new BitmapFont();
    debugMessage = new StringBuilder();
  }

  public void debug(final String message)
  {
    if (isConsoleEnabled)
    {
      debugMessage.append(message + '\n');
    }
  }

  public void render(final Batch batch)
  {
    if (isConsoleEnabled)
    {
      font.draw(batch, "*** DEBUG ***\n" + debugMessage.toString(), 0, 480);

      // Clear the string builder after the message was rendered to screen.
      debugMessage.setLength(0);

    }
  }

  public boolean isConsoleEnabled()
  {
    return this.isConsoleEnabled;
  }

  public void enableConsole(final boolean isEnabled)
  {
    this.isConsoleEnabled = isEnabled;
  }
}



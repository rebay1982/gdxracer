package com.redbay.game;

/**
 * This class represents the data for a track segment.
 */
public class TrackSegment
{
  // This is stored in CM
  private int length;

  // TODO: Define unit.
  private int curvature;

  /**
   * Create a track segment.
   * @param length The segment's length, in meters.
   * @param curvature The segment's curvature
   */
  public TrackSegment(final int length, final int curvature)
  {
    this.length = length * 100;
    this.curvature = curvature;
  }

  /**
   * Get the segment's length
   * @return The segment's length, in centimeters.
   */
  public int getLength()
  {
    return length;
  }


  /**
   * Get the segment's curvature.
   * @return The segment's curvature.
   */
  public int getCurvature()
  {
    return curvature;
  }
}

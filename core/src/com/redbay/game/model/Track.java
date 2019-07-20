package com.redbay.game.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Track
{
  private int length;

  private List<TrackSegment> segments;

  /**
   * Create an empty track.
   */
  public Track()
  {
    this.length = 0;
    this.segments = new ArrayList<>();
  }

  /**
   * Create a new track from a collection of {@link TrackSegment}s
   * @param segments A collection of {@link TrackSegment}
   */
  public Track(final Collection<TrackSegment> segments)
  {
    this();
    this.segments.addAll(segments);

    length = calculateTrackLength();
  }


  /**
   * Get the total track length.
   * @return The total track lengthX
   */
  public int getTrackLength()
  {
    return length;
  }


  /**
   * Add a track segment to the track.
   * @param segment
   */
  public void addSegment(final TrackSegment segment)
  {
    segments.add(segment);

    length = calculateTrackLength();
  }

  /**
   * Get the track's curvature based on the position.
   *
   * NOTE: Using this might have a negative performance impact.
   *
   * @param position The position for which we want to retrieve the track curvature.
   * @return The track's curvature at the specified position.
   */
  public int getTrackCurvature(final int position)
  {
    int currPosition = position;
    int curvature = 0;

    for (TrackSegment segment : segments)
    {
      currPosition -= segment.getLength();
      if (currPosition <= 0)
      {
        curvature = segment.getCurvature();
        break;
      }
    }

    return curvature;
  }

  /**
   * Utility method used to update the track's length after altering the
   */
  private int calculateTrackLength()
  {
    int totalLength = 0;

    for (TrackSegment segment : segments)
    {
      totalLength += segment.getLength();
    }

    return totalLength;
  }
}

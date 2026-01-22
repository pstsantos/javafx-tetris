package org.depaul.logic.events;

public enum EventType {
    LEFT,    // Move piece left
    RIGHT,     // Move piece right
    DOWN,       // Move piece down (soft drop)
    ROTATE_CW,  // Rotate clockwise
    ROTATE_CCW, // Rotate counterclockwise
    HARD_DROP   // Instantly drop to the bottom
}

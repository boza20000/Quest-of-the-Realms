package com.questoftherealm.characters.player;

import com.fasterxml.jackson.annotation.JsonProperty;


public record PlayTime(@JsonProperty("hours") int hours,
                       @JsonProperty("minutes") int minutes) {
}

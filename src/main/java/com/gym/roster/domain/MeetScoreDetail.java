package com.gym.roster.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeetScoreDetail {
    @JsonProperty("judge")
    private String judge;

    @JsonProperty("startValue")
    private Double startValue;

    @JsonProperty("score")
    private Double score;
}

package com.moviebooking.dto.theatre;

import com.moviebooking.dto.show.ShowResponse;
import lombok.Data;
import java.util.List;

@Data
public class TheatreWithShowsResponse {
    private TheatreResponse theatre;
    private List<ShowResponse> shows;
} 
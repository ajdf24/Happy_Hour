package it.rieger.happyhour.controller.database.firebase;

import java.util.List;

import it.rieger.happyhour.model.Location;

/**
 * Created by sebastian on 28.11.16.
 */

public interface LocationsLoaded {

    void locationsLoaded(List<Location> locations);
}

package me.alvince.android.httptrapdoor.buoy.util;

import android.app.Application;

import androidx.annotation.NonNull;

import me.alvince.android.httptrapdoor.Trapdoor;
import me.alvince.android.httptrapdoor.buoy.TrapdoorBuoy;
import me.alvince.android.httptrapdoor.buoy.TrapdoorBuoyKt;
import me.alvince.android.httptrapdoor.util.Predicates;

public final class TrapdoorBuoyUtil {

    /**
     * Enable floating-buoy for {@link Trapdoor} on {@link Application}
     */
    public static TrapdoorBuoy enableFloatingBuoy(@NonNull Application application, @NonNull Trapdoor source) {
        Predicates.requireNonNull(application);
        Predicates.requireNonNull(source);

        return TrapdoorBuoyKt.enableFloatingBuoy(source, application);
    }

}

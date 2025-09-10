package com.digitalminds.dmssevent.common;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Sandeep.Kumar on 25-01-2018.
 */

public class NotificationID {
    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }
}

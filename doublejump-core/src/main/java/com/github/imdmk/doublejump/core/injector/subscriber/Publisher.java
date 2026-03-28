package com.github.imdmk.doublejump.core.injector.subscriber;

import com.github.imdmk.doublejump.core.injector.subscriber.event.SubscribeEvent;

public interface Publisher {

    void subscribe(Object subscriber);

    <E extends SubscribeEvent> E publish(E event);

}

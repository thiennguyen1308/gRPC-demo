
package com.faber.main;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

/**
 *
 * @author Nguyen Duc Thien
 * @email nguyenducthien@fabercompany.co.jp
 */
public class VertXUtils extends AbstractVerticle {

    public static EventBus eventBus;
    public static Vertx vertxx;

    @Override
    public void start() throws Exception {
        eventBus = vertx.eventBus();
        vertxx = vertx;
    }

}
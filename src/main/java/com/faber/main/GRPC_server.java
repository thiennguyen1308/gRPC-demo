package com.faber.main;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.faber.service.HelloServiceImpl;
import io.grpc.Server;
import io.vertx.core.Vertx;
import io.grpc.netty.NettyServerBuilder;
import io.netty.channel.nio.NioEventLoopGroup;
import io.vertx.core.json.JsonObject;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
//</editor-fold>

/*
 *
 * @author Nguyen Duc Thien
 */
public class GRPC_server {
    
    public static void main(String[] args) throws IOException, InterruptedException {
        Vertx vertx = Vertx.vertx();
        vertx.eventBus().consumer("hello", event -> {
            event.reply(new JsonObject().put("message", event.body().toString()));
        });
        vertx.deployVerticle(VertXUtils.class.getName());
        int coreProcess = Runtime.getRuntime().availableProcessors();// get nums of core processor of os

        //In linux, use epoll instead NIO
        NioEventLoopGroup bossEventGroup = new NioEventLoopGroup(coreProcess);// should be = core process for best performance
        NioEventLoopGroup workerEventGroup = new NioEventLoopGroup(coreProcess * 2);// should be = core process * 2 for best performance

        Server server = NettyServerBuilder//Use netty server instead normal serverBuilder for best performance
                .forPort(8085)
                .flowControlWindow(200000)//enable message size per request
                .executor(new ForkJoinPool(coreProcess * 4, ForkJoinPool.defaultForkJoinWorkerThreadFactory, (Thread t, Throwable e) -> {
                    t.interrupt();
                }, true))//add executor group
                .bossEventLoopGroup(bossEventGroup)// thread for get request
                .workerEventLoopGroup(workerEventGroup)// thread for execute request
                .maxInboundMessageSize(Integer.MAX_VALUE) // 100 mb = 100000000 bytes
                .addService(new HelloServiceImpl())//Must implement 1 or more service
                .keepAliveTime(20, TimeUnit.SECONDS)//send keep alive each 30 seconds
                .keepAliveTimeout(2, TimeUnit.MINUTES)//timeout after keep alive
                .maxConnectionIdle(2, TimeUnit.MINUTES)//allow connection idle in 30 seconds
                .permitKeepAliveWithoutCalls(true)//enable ping to check alive
                .build();
        
        server.start();
        
        server.awaitTermination();//Shutdown hangout
    }
    
}

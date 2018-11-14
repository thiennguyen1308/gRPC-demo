package com.faber.main;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.faber.service.HelloServiceImpl;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import io.netty.channel.nio.NioEventLoopGroup;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
//</editor-fold>

/*
 *
 * @author Nguyen Duc Thien
 */
public class GRPC_server {

    public static void main(String[] args) throws IOException, InterruptedException {
        int coreProcess = Runtime.getRuntime().availableProcessors();// get nums of core processor of os

        //In linux, use epoll instead NIO
        NioEventLoopGroup bossEventGroup = new NioEventLoopGroup(coreProcess);// should be = core process for best performance
        NioEventLoopGroup workerEventGroup = new NioEventLoopGroup(coreProcess * 2);// should be = core process * 2 for best performance
       
        //4 * coreProcess thread is enough for high performance
        Executor executor = Executors.newFixedThreadPool(coreProcess * 4, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "gRPC executor pool");
                thread.setUncaughtExceptionHandler((t, e) -> {
                    t.interrupt();
                });
                return thread;
            }
        });

        Server server = NettyServerBuilder//Use netty server instead normal serverBuilder for best performance
                .forPort(9000)
                .executor(executor)
                .bossEventLoopGroup(bossEventGroup)// thread for get request
                .workerEventLoopGroup(workerEventGroup)// thread for execute request
                .maxInboundMessageSize(100000000) // 100 mb = 100000000 bytes
                .addService(new HelloServiceImpl())//Must implement 1 or more service 
                .build();

        server.start();
        server.awaitTermination();//Shutdown hangout
    }

}

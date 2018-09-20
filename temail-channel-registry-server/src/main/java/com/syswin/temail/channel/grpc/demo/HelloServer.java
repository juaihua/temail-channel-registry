package com.syswin.temail.channel.grpc.demo;

import com.syswin.temail.channel.grpc.servers.GreeterGrpc;
import com.syswin.temail.channel.grpc.servers.HelloReply;
import com.syswin.temail.channel.grpc.servers.HelloRequest;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import io.netty.handler.codec.http2.DefaultHttp2HeadersDecoder;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class HelloServer {

  private  int port = 50051;

  private Server server;

  /**
   * start service
   */
  public void start() throws IOException {
    server = ServerBuilder.forPort(port).addService(new GreeterImpl()).build().start();
    log.info("service start");
    Runtime.getRuntime().addShutdownHook(new Thread(){
      @Override
      public void run() {
        log.error("**** shutting down grpc server since JVM js shutting down ****");
        HelloServer.this.stop();
      }
    });
  }


  /**
   * stop service
   */
  private void stop(){
    if(server != null){
      server.shutdown();
    }
  }

  /**
   * 阻塞直到程序退出 - 集成spring的时候没必要这么做
   * @throws InterruptedException
   */
  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
        server.awaitTermination();
    }
  }


  public static void main(String[] args) throws IOException, InterruptedException {
    DefaultHttp2HeadersDecoder defaultHttp2HeadersDecoder = new DefaultHttp2HeadersDecoder(true, 32);
    HelloServer helloServer = new HelloServer();
    helloServer.start();
    helloServer.blockUntilShutdown();
  }


  private class GreeterImpl extends GreeterGrpc.GreeterImplBase {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
      log.info("接收请求：{} ", request.getName());
      HelloReply.Builder helloRBuilder = HelloReply.newBuilder();
      helloRBuilder.setMessage(" 响应你的： {} 请求！");
      responseObserver.onNext(helloRBuilder.build());
      responseObserver.onCompleted();
    }
  }

}

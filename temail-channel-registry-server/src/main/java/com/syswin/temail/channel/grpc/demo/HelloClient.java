package com.syswin.temail.channel.grpc.demo;

import com.syswin.temail.channel.grpc.servers.GreeterGrpc;
import com.syswin.temail.channel.grpc.servers.HelloReply;
import com.syswin.temail.channel.grpc.servers.HelloRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class HelloClient {

  private ManagedChannel channel;    // 一个grpc信道

  private GreeterGrpc.GreeterBlockingStub greeterBlockingStub;   //阻塞同步存根

  private GreeterGrpc.GreeterFutureStub greeterFutureStub;    //异步调用存根

  private HelloClient(String host, int port){
    this(ManagedChannelBuilder.forAddress(host,port).usePlaintext());
  }

  private HelloClient(ManagedChannelBuilder<?> channelBuilder){
    this.channel = channelBuilder.build();
    this.greeterBlockingStub = GreeterGrpc.newBlockingStub(channel);
  }

  private void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  private void greet(String name){
    log.info("客户端请求：{}", name);
    HelloRequest request = HelloRequest.newBuilder().setName(name).build();
    HelloReply reply = this.greeterBlockingStub.sayHello(request);
    log.info("获取响应： {}", reply.toString());
  }

  public static void main(String[] args) throws InterruptedException {
    HelloClient helloClient = new HelloClient("127.0.0.1", 50051);
   /* for(int i =0; i < 100; i ++){
      helloClient.greet("元亨利贞");
      Thread.currentThread().sleep(500);
    }*/

   Thread.currentThread().sleep(1000000);
  }


}

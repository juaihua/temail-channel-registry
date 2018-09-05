package com.syswin.temail.channel.client;

import com.syswin.temail.channel.client.connection.ChannelManager;
import com.syswin.temail.channel.client.connection.LocalMachineUtil;
import com.syswin.temail.channel.core.entity.BizType;
import com.syswin.temail.channel.core.entity.ServerStatusRequest;
import com.syswin.temail.channel.core.entity.StatusRequest;
import com.syswin.temail.channel.core.entity.TemailAcctStsUpdReq;
import com.syswin.temail.channel.core.entity.TemailAcctStsUpdResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author 姚华成
 * @date 2018-8-20
 */
@Slf4j
public class TemailChannelClient implements InitializingBean {

  private static final String INSTANCE_UNIQUE_TAG_4_HEARTBEAT = "_instance_unique_tag_4_heart_beat_$";

  private ChannelManager channelManager;

  public TemailChannelClient(ChannelManager channelManager) {
    this.channelManager = channelManager;
  }

  public TemailAcctStsUpdResp updateAccountStatus(
      TemailAcctStsUpdReq temailAccountStatusUpdateRequest) {
    // 由于并发请求数量可能比较多，需要进行连接池管理
    // 由于需要等待服务器的返回值，需要对连接进行同步化处理。
    throw new UnsupportedOperationException("当前还不支持这个方法，请使用原来的方法更新用户状态！");
  }

  public void updateServerStatus(ServerStatusRequest serverStatusRequest) {
    log.info("向状态服务器推送当前服务状态信息：{}", serverStatusRequest);
    StatusRequest<ServerStatusRequest> request = new StatusRequest<>(BizType.SERVER_STATUS, serverStatusRequest);
    channelManager.getChannel().writeAndFlush(request);
  }

  @Override
  public void afterPropertiesSet() {
    ServerStatusRequest request = new ServerStatusRequest();
    request.setIp(LocalMachineUtil.getLocalIp());
    String uniqueTagVal = System.getProperty(INSTANCE_UNIQUE_TAG_4_HEARTBEAT);
    request.setProcessId(uniqueTagVal);
    updateServerStatus(request);
  }
}

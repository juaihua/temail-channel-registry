package com.syswin.temail.channel.client;

import com.syswin.temail.channel.core.entity.ServerStatusRequestBody;
import com.syswin.temail.channel.core.entity.TemailAccountStatusUpdateRequest;
import com.syswin.temail.channel.core.entity.TemailAccountStatusUpdateResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 姚华成
 * @date 2018-8-20
 */
@Slf4j
public class CdtpStatusClient {

  private ChannelManager channelManager;

  public CdtpStatusClient(ChannelManager channelManager) {
    this.channelManager = channelManager;
    log.info(String.valueOf(this.channelManager));
  }

  public TemailAccountStatusUpdateResponse updateUserStatus(
      TemailAccountStatusUpdateRequest temailAccountStatusUpdateRequest) {
    // 由于并发请求数量可能比较多，需要进行连接池管理
    // 由于需要等待服务器的返回值，需要对连接进行同步化处理。
    throw new UnsupportedOperationException("当前还不支持这个方法，请使用原来的方法更新用户状态！");
  }

  public void updateServerStatus(ServerStatusRequestBody serverStatusRequest) {
    log.info("向状态服务器推送当前服务状态信息：{}" + serverStatusRequest);
    channelManager.getChannel().writeAndFlush(serverStatusRequest);
  }


}

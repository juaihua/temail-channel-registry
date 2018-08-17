package com.syswin.temail.service.impl;

import com.syswin.temail.beans.TemailAccountStatus;
import com.syswin.temail.beans.TemailAccountStatusLocateResponse;
import com.syswin.temail.beans.TemailAccountStatusUpdateRequest;
import com.syswin.temail.beans.TemailAccountStatusUpdateResponse;
import com.syswin.temail.service.ConnectionStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 *  <pre>
 *  data structure design guide for channels in redis
 *    1、temail account channels info.. (hash)：
 *      redisKey ： temail:status:${temailaccount}
 *      hashKey  ： hostOf + "-" + processId + "-" + devId
 *      hashValue： {@link com.syswin.temail.beans.TemailAccountStatus}
 *    2、processes channels info.. (set)
 *      redisKey：temail:hosts:${hostOf}:${processId}
 *      member构成：{@link com.syswin.temail.beans.TemailAccountStatus}
 *  </pre>
 */
@Component
public class ConnectionStatusServiceImpl implements ConnectionStatusService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionStatusServiceImpl.class);

    private static final String TEMAIL_PREFIX_ON_REDIS = "temail:status:";

    private static final String HOST_PREFIX_ON_REDIS = "temail:hosts:";

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    @Override
    public TemailAccountStatusUpdateResponse updateStatus(TemailAccountStatusUpdateRequest temailAccountStatusUpdateRequest) {
        Boolean isSuccess = redisTemplate.execute(new SessionCallback<Boolean>() {
            @Override
            public Boolean  execute(RedisOperations redisOperations) throws DataAccessException {
                try {
                    switch (temailAccountStatusUpdateRequest.getOptype()){
                        case add:{
                            //update temail/process channels  in redis
                            redisOperations.multi();
                            redisOperations.opsForHash().put((TEMAIL_PREFIX_ON_REDIS+temailAccountStatusUpdateRequest.getAccount()),
                                temailAccountStatusUpdateRequest.getStatus().geneHashKey(),temailAccountStatusUpdateRequest.getStatus());

                            redisOperations.opsForSet().add((HOST_PREFIX_ON_REDIS + (temailAccountStatusUpdateRequest.getStatus().getHostOf())
                                + "-" + (temailAccountStatusUpdateRequest.getStatus().getProcessId())),temailAccountStatusUpdateRequest.getStatus());
                            Object val = redisOperations.exec();
                            break;

                        }case del:{
                            // remove temail/process channels in redis
                            redisOperations.multi();
                            redisOperations.opsForHash().delete((TEMAIL_PREFIX_ON_REDIS+temailAccountStatusUpdateRequest.getAccount()),
                                    temailAccountStatusUpdateRequest.getStatus().geneHashKey());

                            redisOperations.opsForSet().remove((HOST_PREFIX_ON_REDIS + (temailAccountStatusUpdateRequest.getStatus().getHostOf())
                                + "-" + (temailAccountStatusUpdateRequest.getStatus().getProcessId())),temailAccountStatusUpdateRequest.getStatus());

                            Object val = redisOperations.exec();
                            break;
                        }
                    }

                    return true;

                } catch (Exception e) {
                    LOGGER.error("redis update fail.", e);
                    e.printStackTrace();
                    return false;
                }
            }
        });

        return new TemailAccountStatusUpdateResponse(isSuccess.booleanValue());
    }



    @Override
    public TemailAccountStatusLocateResponse locateStatus(String temailAccount) {
        TemailAccountStatusLocateResponse result = new TemailAccountStatusLocateResponse();
        result.setAccount(temailAccount);
        try {
            List<TemailAccountStatus> statusList = new ArrayList<TemailAccountStatus>();
            Map<Object,Object> statusHash = redisTemplate.opsForHash().entries(TEMAIL_PREFIX_ON_REDIS+temailAccount);
            Optional.ofNullable(statusHash).ifPresent(new Consumer<Map>(){
                @Override
                public void accept(Map map) {
                    map.forEach((k,v) -> statusList.add((TemailAccountStatus)v));
                }
            });
            result.setStatusList(statusList);
        } catch (Exception e) {
              LOGGER.error("redis locate fail.", e);
            e.printStackTrace();
        }
        return result;
    }



}

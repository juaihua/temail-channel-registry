package com.syswin.temail.channel.account;

import com.syswin.temail.channel.account.timer.StatusSyncTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by juaihua on 2018/8/22.
 * data gene utils for testing chennels sync
 */
public class CommonDataGeneUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusSyncTimer.class);

    private static final Random RANDOM = new Random(1);

    private static char[] UPPER_CASE = new char[26];

    private static char[] LOWER_CASE = new char[26];

    private static char[] NUMBER = new char[10];

    public static enum ExtractType{UPPER,LOWER,NUM};

    static {

        for(int i = 0; i < 26; i++){
            UPPER_CASE[i] = (char)((int)'A' + i);
            LOWER_CASE[i] = (char)((int)'a' + i);
        }
        for(int i = 0; i < 10; i++){
            NUMBER[i] = (char)((int)'0' + i);
        }
        LOGGER.info("init: ");
        LOGGER.info("UPPER_CASE: " + Arrays.toString(UPPER_CASE));
        LOGGER.info("LOWER_CASE: " + Arrays.toString(LOWER_CASE));
        LOGGER.info("NUMBER" + Arrays.toString(NUMBER));
    }

    public static String extractChar(ExtractType type, int size){
        StringBuilder sbd = new StringBuilder("");
        switch (type){
            case UPPER:{
                for(int i = 0; i < size; i++){
                    sbd.append(UPPER_CASE[RANDOM.nextInt(UPPER_CASE.length)]);
                }
                break;
            }
            case LOWER:{
                for(int i = 0; i < size; i++){
                    sbd.append(LOWER_CASE[RANDOM.nextInt(LOWER_CASE.length)]);
                }
                break;
            }
            case NUM:{
                for(int i = 0; i < size; i++){
                    sbd.append(NUMBER[RANDOM.nextInt(NUMBER.length)]);
                }
                break;
            }
        }
        return sbd.toString();
    }

}

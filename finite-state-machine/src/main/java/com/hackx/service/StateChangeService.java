package com.hackx.service;

import com.hackx.enums.RegEventEnum;
import com.hackx.enums.RegStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

/**
 * Created by 曹磊(Hackx) on 16/11/2017.
 * Email: caolei@mobike.com
 */
@Service
public class StateChangeService implements CommandLineRunner {

    @Autowired
    private StateMachine<RegStatusEnum, RegEventEnum> stateMachine;

    @Override
    public void run(String... strings) throws Exception {
        stateMachine.sendEvent(RegEventEnum.CONNECT);
        stateMachine.sendEvent(RegEventEnum.UN_REGISTER);
    }
}

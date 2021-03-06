package com.hackx.config;

import com.hackx.enums.RegEventEnum;
import com.hackx.enums.RegStatusEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

/**
 * Created by 曹磊(Hackx) on 12/11/2017.
 * Email: caolei@mobike.com
 */
@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<RegStatusEnum, RegEventEnum> {
    /**
     * 初始化状态机状态
     */
    @Override
    public void configure(StateMachineStateConfigurer<RegStatusEnum, RegEventEnum> states) throws Exception {
        states.withStates()
                // 定义初始状态
                .initial(RegStatusEnum.UNCONNECTED)
                // 定义状态机状态
                .states(EnumSet.allOf(RegStatusEnum.class));
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<RegStatusEnum, RegEventEnum> config)
            throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(listener());
    }

    @Bean
    public StateMachineListener<RegStatusEnum, RegEventEnum> listener() {
        return new StateMachineListenerAdapter<RegStatusEnum, RegEventEnum>() {
            @Override
            public void stateChanged(State<RegStatusEnum, RegEventEnum> from, State<RegStatusEnum, RegEventEnum> to) {
                System.out.println("State change to " + to.getId());
            }
        };
    }

    /**
     * 初始化状态迁移事件
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<RegStatusEnum, RegEventEnum> transitions)
            throws Exception {
        transitions
                // 1.连接事件
                // 未连接 -> 已连接
                .withExternal()
                .source(RegStatusEnum.UNCONNECTED)
                .target(RegStatusEnum.CONNECTED)
                .event(RegEventEnum.CONNECT)
                .and()

                // 2.注册事件
                // 已连接 -> 注册中
                .withExternal()
                .source(RegStatusEnum.CONNECTED)
                .target(RegStatusEnum.REGISTERING)
                .event(RegEventEnum.REGISTER)
                .and()

                // 3.注册成功事件
                // 注册中 -> 已注册
                .withExternal()
                .source(RegStatusEnum.REGISTERING)
                .target(RegStatusEnum.REGISTERED)
                .event(RegEventEnum.REGISTER_SUCCESS)
                .and()

                // 5.注销事件
                // 已连接 -> 未连接
                .withExternal()
                .source(RegStatusEnum.CONNECTED)
                .target(RegStatusEnum.UNCONNECTED)
                .event(RegEventEnum.UN_REGISTER)
                .and()
                // 注册中 -> 未连接
                .withExternal()
                .source(RegStatusEnum.REGISTERING)
                .target(RegStatusEnum.UNCONNECTED)
                .event(RegEventEnum.UN_REGISTER)
                .and()
                // 已注册 -> 未连接
                .withExternal()
                .source(RegStatusEnum.REGISTERED)
                .target(RegStatusEnum.UNCONNECTED)
                .event(RegEventEnum.UN_REGISTER)
        ;
    }
}

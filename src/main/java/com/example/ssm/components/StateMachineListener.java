package com.example.ssm.components;

import com.example.ssm.utils.CustomConstants;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

@Component
public class StateMachineListener extends StateMachineListenerAdapter<CustomConstants.OrderStates, CustomConstants.OrderEvents> {
@Override
public void stateChanged(State<CustomConstants.OrderStates, CustomConstants.OrderEvents> from, State<CustomConstants.OrderStates, CustomConstants.OrderEvents> to) {
     System.out.println("state changed from "+from.toString()+"        to"+to.toString());
  }
}
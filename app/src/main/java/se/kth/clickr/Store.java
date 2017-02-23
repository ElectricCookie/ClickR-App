package se.kth.clickr;

/**
 * Created by ElectricCookie on 26.08.2016.
 */

import java.util.ArrayList;
import java.util.List;

public final class Store<Action, State> {

    private State state;


    private List<Middleware<Action,State>> middlewares = new ArrayList<>();
    private List<Action> actionStack = new ArrayList<>();
    private List<Reducer<Action,State>> reducers = new ArrayList<>();
    private List<Subscriber<State>> subscribers = new ArrayList<>();

    private List<Afterware<Action,State>> afterwareList = new ArrayList<>();

    public Store(State intialState){
        this.state = intialState;
    }

    public void registerReducer(Reducer<Action,State> reducer){
        reducers.add(reducer);
    }

    public void registerAfterware(Afterware<Action,State> afterware){
        afterwareList.add(afterware);
    }

    public void removeAfterware(Afterware<Action,State> afterware){
        afterwareList.remove(afterware);
    }

    public void subscribe(Subscriber<State> subscriber){
        subscribers.add(subscriber);
    }

    public void unsubscribe(Subscriber<State> subscriber){
        subscribers.remove(subscriber);
    }

    public void registerMiddleware(Middleware<Action,State> middleware){
        middlewares.add(middleware);
    }

    public void processNext(){

    }

    public void dispatch(Action action){

        actionStack.add(action);

        synchronized (this){

            List<NextDispatcher<Action>> nextDispatchers = new ArrayList<>();

            for(int i = 0; i < middlewares.size(); i++){
                final int o = i;
                nextDispatchers.add((newAction) -> {
                            if(o == middlewares.size()-1){


                                State newState = null;

                                for(Reducer<Action,State> reducer: reducers){

                                    newState = reducer.reduce(newAction,newState == null ? state : newState);

                                    if(newState == null){
                                        throw new RuntimeException("Reducer : "+reducer.getClass().getName()+" returned null state! This is a no-op!");
                                    }
                                }


                                notifySubscribers(newState,state);

                                for(Afterware<Action,State> afterware: afterwareList){
                                    afterware.listen(newAction,newState);
                                }

                            }else{
                                middlewares.get(o+1).dispatch(this,newAction,nextDispatchers.get(o+1));
                            }
                        }
                );
            }

            middlewares.get(0).dispatch(this,action,nextDispatchers.get(0));

        }


    }

    public void notifySubscribers(State newState,State oldState){

        state = newState;

        synchronized (this){
            for(Subscriber<State> subscriber : subscribers){
                if(subscriber.shouldUpdate(oldState,newState)){
                    subscriber.update(newState);
                }
            }
        }





    }

    public State getState(){
        return state;
    }


    public interface Subscriber<State>{
        public boolean shouldUpdate(State oldState, State newState);
        public void update(State newState);

    }

    public interface NextDispatcher<Action>{
        public void next(Action action);
    }

    public interface Afterware<Action,State>{
        public void listen(Action action,State state);
    }

    public interface Middleware<Action,State>{
        public void dispatch(Store<Action,State> store,Action action,NextDispatcher<Action> nextMiddleware);
    }

    public interface Reducer<Action,State>{
        public State reduce(Action action,State state);
    }

}

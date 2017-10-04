package cn.expopay.messageServer.util.akka.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActorFactory {

    private final Logger logger = LoggerFactory.getLogger(ActorFactory.class);

    private ActorSystem _system;
    //通过ActorSystem创建Actor
    private ActorRef master;


    public ActorFactory() {
        _system = ActorSystem.create("mqAkka");
        master = _system.actorOf(Props.create(MasterActor.class), "master");
    }

    private static class ActorFactoryHandler{
        private static ActorFactory af = new ActorFactory();
    }

    public static ActorRef getMasterActorRef(){
        return ActorFactoryHandler.af.master;
    }

    public static void destoryActorSystem(){
        ActorFactoryHandler.af._system.shutdown();
    }



}

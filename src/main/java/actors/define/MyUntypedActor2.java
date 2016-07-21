package actors.define;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.Option;

/**
 * Created by infosea on 2016-07-21.
 */
public class MyUntypedActor2 extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof String) {
            log.info("Received String message: {}", message);
            getSender().tell("this is a message from 2", getSelf());
        }
    }
}

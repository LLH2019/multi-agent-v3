package cloud;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Props;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import cloud.actor.BrainControlActor;
import base.model.bean.BasicCommon;
import cloud.global.GlobalActorRefName;
import cloud.global.GlobalAkkaPara;

import java.io.IOException;
import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ：LLH
 * @date ：Created in 2021/4/19 11:15
 * @description：初始启动类
 */
public class ActorBootstrapMain {
    private static Logger logger = Logger.getLogger(ActorBootstrapMain.class.getName());
    public static void main(String[] args) throws IOException {

        GlobalAkkaPara.taskNum = Integer.parseInt(args[0]);
        GlobalAkkaPara.rapid = Integer.parseInt(args[1]);
        GlobalAkkaPara.dataPath = args[2];
//        System.out.println(args[0]);

        logger.log(Level.INFO, "CloudBootstrapMain start...");
        ActorSystem<Void> system = GlobalAkkaPara.system;
        ActorRef<BasicCommon> brainControlActorRef = system.systemActorOf(BrainControlActor.create(),
                GlobalActorRefName.BRAIN_ACTOR, Props.empty());
        logger.log(Level.INFO, "init BrainControlActor...");
        GlobalAkkaPara.globalActorRefMap.put(GlobalActorRefName.BRAIN_ACTOR, brainControlActorRef);

//        ActorRef<BasicCommon> mongoDBActorRef = system.systemActorOf(MongoDBConnActor.create(),
//                GlobalActorRefName.MONGODB_CONN_ACTOR, Props.empty());
//        logger.log(Level.INFO, "init MongoDBConnActor...");
//        GlobalAkkaPara.globalActorRefMap.put(GlobalActorRefName.MONGODB_CONN_ACTOR, mongoDBActorRef);

//        ActorRef<BasicCommon> cloudKafkaConnectInActorRef = system.systemActorOf(CloudKafkaConnectInActor.create(),
//                GlobalActorRefName.CLOUD_KAFKA_CONNECT_IN_ACTOR, Props.empty());
//        logger.log(Level.INFO, "init CloudKafkaConnectInActor...");
//        GlobalAkkaPara.globalActorRefMap.put(GlobalActorRefName.CLOUD_KAFKA_CONNECT_IN_ACTOR, cloudKafkaConnectInActorRef);

//        httpClientConn(system);
//        CloudKafkaConsumer.init(system);
//        AkkaManagement.get(system).start();
//        ClusterBootstrap.get(system).start();
//        testCC3200(system);
    }


    private static void httpClientConn(ActorSystem<Void> system) throws IOException {
        final Http http = Http.get(system);

        HttpServer server = new HttpServer();
        final CompletionStage<ServerBinding> binding = http.newServerAt("192.168.123.131", 8080)
                .bind(server.createRoute());

        System.out.println("Server online at http://192.168.123.131:8080/\nPress RETURN to stop...");
        System.in.read(); // let it run until user presses return


        binding
                .thenCompose(ServerBinding::unbind) // trigger unbinding from the port
                .thenAccept(unbound -> system.terminate()); // and shutdown when done
    }
}

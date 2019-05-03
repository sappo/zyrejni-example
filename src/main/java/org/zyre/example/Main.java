package org.zyre.example;

import org.zeromq.czmq.Zframe;
import org.zeromq.czmq.Zmsg;
import org.zeromq.zyre.Zyre;

public class Main
{

    public static void main(String[] args) throws InterruptedException
    {
        Zyre peer1 = new Zyre("peer1");
        Zyre peer2 = new Zyre("peer2");

        peer1.start();
        peer2.start();

        peer1.join("CHAT");
        peer2.join("CHAT");
        Thread.sleep(250);  //  Give them time to connect to each other

        Zmsg msgToSend = new Zmsg();
        msgToSend.addstr("Hello Mate!");
        peer1.shout("CHAT", msgToSend);

        Zmsg peer2EnterMsg = peer1.recv();
        String cmd = peer2EnterMsg.popstr();
        String peerUuid = peer2EnterMsg.popstr();
        String peerName = peer2EnterMsg.popstr();
        System.out.println("MSG CMD:" + cmd);
        System.out.println("MSG Peer UUID:" + peerUuid);
        System.out.println("MSG Peer Name:" + peerName);
        Zmsg peer1EnterMsg = peer2.recv();

        Zmsg peer2JoinMsg = peer1.recv();
        cmd = peer2JoinMsg.popstr();
        peerUuid = peer2JoinMsg.popstr();
        peerName = peer2JoinMsg.popstr();
        String group = peer2JoinMsg.popstr();
        System.out.println("MSG CMD:" + cmd);
        System.out.println("MSG Peer UUID:" + peerUuid);
        System.out.println("MSG Peer Name:" + peerName);
        System.out.println("MSG Group:" + group);
        Zmsg peer1JoinMsg = peer2.recv();

        Zmsg peer1ShoutMsg = peer2.recv();
        cmd = peer1ShoutMsg.popstr();
        peerUuid = peer1ShoutMsg.popstr();
        peerName = peer1ShoutMsg.popstr();
        group = peer1ShoutMsg.popstr();
        String content = peer1ShoutMsg.popstr();
        System.out.println("MSG CMD:" + cmd);
        System.out.println("MSG Peer UUID:" + peerUuid);
        System.out.println("MSG Peer Name:" + peerName);
        System.out.println("MSG Group:" + group);
        System.out.println("MSG Content:" + content);

        Zmsg msgToSend2 = new Zmsg();
        msgToSend2.addstr("Hello back at you!");
        peer2.whisper(peerUuid, msgToSend2);

        Zmsg peer2WhisperMsg = peer1.recv();
        cmd = peer2WhisperMsg.popstr();
        peerUuid = peer2WhisperMsg.popstr();
        peerName = peer2WhisperMsg.popstr();
        content = peer2WhisperMsg.popstr();
        System.out.println("MSG CMD:" + cmd);
        System.out.println("MSG Peer UUID:" + peerUuid);
        System.out.println("MSG Peer Name:" + peerName);
        System.out.println("MSG Content:" + content);

        peer1.stop();
        peer2.stop();

        peer1.close();
        peer2.close();
    }

}

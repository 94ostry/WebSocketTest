package pl.postrowski;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.PongMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created by postrowski on 2016-04-19.
 */
@ServerEndpoint( "/websocket/foo" )
public class FooEndpoint
{
    //@OnMessage
    //public void onPongMessage( PongMessage pongMessage, Session session  )
    //{
        //System.out.println( " Pong message from " + session.getId() );
    //}

    @OnMessage
    public void onMessage( String message, Session session )
    {
        System.out.println( " Message " + message + " from " + session.getId() );

        if( message.equals( "DISCONNECT" ) )
        {
            try
            {
                session.close( new CloseReason( CloseReason.CloseCodes.UNEXPECTED_CONDITION, "BreakConnection" ) );
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

    @OnOpen
    public void onOpen( Session session )
    {
        System.out.println( "Connected " + session.getId() );
    }

    @OnClose
    public void onClose( CloseReason reason, Session session )
    {
        System.out.println( "Disconnected " + session.getId() + " reason : " + reason.getCloseCode() );
    }
}

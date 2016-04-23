package pl.postrowski;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.PongMessage;
import javax.websocket.Session;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by postrowski on 2016-04-19.
 */
@ClientEndpoint
public class FooClientEndpoint implements FooClientEndpointIf
{
    @OnMessage
    public void onPongMessage( PongMessage pongMessage )
    {
        final ByteBuffer applicationData = pongMessage.getApplicationData();
        final String pongText = new String( applicationData.array() );
        System.out.println( " pongMessage : " +  pongText );
    }

    @OnOpen
    public void onOpen(Session session) throws IOException
    {
        System.out.println( "Connected " + session.getId() );
    }

    @OnClose
    public void closing( CloseReason closeReason )
    {
        System.out.println( "Closing webSocket reason : " + closeReason.getCloseCode() );
    }

    @OnError
    public void error( Throwable ex )
    {
        System.err.println( ex );
    }

    @Override
    public void discoonect()
    {

    }
}

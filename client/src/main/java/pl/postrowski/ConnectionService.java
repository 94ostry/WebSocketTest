package pl.postrowski;

import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.glassfish.tyrus.core.TyrusEndpointWrapper;
import org.glassfish.tyrus.core.TyrusSession;

import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by postrowski on 2016-04-19.
 */
public class ConnectionService
{
    //private Session session;
    private Future<Session> sessionFuture;

    private Session getSession()
    {
        try
        {
            return sessionFuture.get();
        }
        catch( InterruptedException e )
        {
            e.printStackTrace();
        }
        catch( ExecutionException e )
        {
            e.printStackTrace();
        }

        throw new IllegalStateException( "Blad" );
    }

    public boolean connect( Object endpointImpl )
    {
        try
        {
            final ClientManager container = ClientManager.createClient();
            container.getProperties().put( ClientProperties.RECONNECT_HANDLER, new WebSocketReconnectHandler() );

            final URI uri = new URI( "ws://192.168.99.100:8080/server/websocket/foo" );
            //final URI uri = new URI( "ws://104.46.41.77:8080/server/websocket/foo" );

            //session = container.connectToServer( endpointImpl, uri );
            sessionFuture = container.asyncConnectToServer( endpointImpl, uri );

            final Session session = sessionFuture.get();
            final TyrusSession tyrusSession = (TyrusSession)session;
            tyrusSession.setHeartbeatInterval( 15000 );
            //System.out.println(((TyrusSession) session).getHeartbeatInterval() );

            return true;
        }
        catch( URISyntaxException e )
        {
            e.printStackTrace();
        }
        catch( DeploymentException e )
        {
            e.printStackTrace();
        }
        catch( InterruptedException e )
        {
            e.printStackTrace();
        }
        catch( ExecutionException e )
        {
            e.printStackTrace();
        }

        return false;
    }

    public void send( String message )
    {
        try
        {
            //session.getBasicRemote().sendText( message );
            getSession().getBasicRemote().sendText( message );
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
    }

    public void sendPing()
    {
        try
        {
            getSession().getBasicRemote().sendPing( ByteBuffer.wrap( "AlaMaKota".getBytes() ) );
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
    }

    private static class WebSocketReconnectHandler extends ClientManager.ReconnectHandler
    {

        private int counter = 0;

        @Override
        public boolean onDisconnect( CloseReason closeReason )
        {
            counter++;
            if( counter <= 3 )
            {
                System.out.println( "### Reconnecting... (reconnect count: " + counter + ")" );
                return true;
            }
            else
            {
                return false;
            }
        }

        @Override
        public boolean onConnectFailure( Exception exception )
        {
            counter++;
            if( counter <= 3 )
            {
                System.out.println(
                    "### Reconnecting... (reconnect count: " + counter + ") " + exception.getMessage() );

                // Thread.sleep(...) or something other "sleep-like" expression can be put here - you might want
                // to do it here to avoid potential DDoS when you don't limit number of reconnects.
                return true;
            }
            else
            {
                return false;
            }
        }

        @Override
        public long getDelay()
        {
            return 1L;
        }
    }
}

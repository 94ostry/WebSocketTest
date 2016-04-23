package pl.postrowski;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by postrowski on 2016-04-19.
 */
public class WebSocketApplication extends Application
{
    final ConnectionService connectionService = new ConnectionService();
    final FooClientEndpoint fooClientEndpoint = new FooClientEndpoint();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main( String[] args )
    {
        launch( args );
    }

    @Override
    public void start( Stage primaryStage ) throws Exception
    {
        final Parent root = FXMLLoader.load( getClass().getClassLoader().getResource( "sample.fxml" ) );


        final TextArea textAreaMessages = (TextArea)root.lookup( "#textAreaMessages" );

        final Button btnConnect = (Button)root.lookup( "#btnConnect" );
        btnConnect.setOnAction( event ->
        {
            final boolean connected = connectionService.connect( fooClientEndpoint );

            if( connected )
            {
                btnConnect.setDisable( true );
            }
        });

        final Button btnSend = (Button)root.lookup( "#btnSend" );
        btnSend.setOnAction( event ->
        {
            System.out.println( "send" );
            connectionService.send( LocalDateTime.now().toString() + " test" );
        } );

        final Button btnPing = (Button)root.lookup( "#btnPing" );
        btnPing.setOnAction( event ->
        {
            btnPing.setDisable( true );

            final Runnable pinger = () -> {
                System.out.println( "ping" );
                connectionService.sendPing();
            };

            final ScheduledFuture<?> scheduledFuture =
                scheduler.scheduleAtFixedRate( pinger, 0, 10, TimeUnit.SECONDS );

            //scheduledFuture.ca
        });


        final Button btnDisconnect = (Button)root.lookup( "#btnDisconnect" );
        btnDisconnect.setOnAction( event ->
        {
            System.out.println( "disconnect" );
            connectionService.send( "DISCONNECT" );
        });


        primaryStage.setTitle( "Hello World" );
        primaryStage.setScene( new Scene( root, 1200, 800 ) );
        primaryStage.show();
    }
}

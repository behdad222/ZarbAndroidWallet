package network.zarb.app;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import network.zarb.app.proto.ZarbGrpc;
import network.zarb.app.proto.ZarbProto;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendMessage();
    }

    public void sendMessage() {
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress("139.162.135.180", 9090)
                .usePlaintext()
                .build();

        ZarbGrpc.ZarbStub stub = ZarbGrpc.newStub(managedChannel);

        ZarbProto.AccountRequest accountRequest = ZarbProto.AccountRequest.newBuilder()
                .setAddress("zrb10vc8x2ve73hle08zaz7ud7jfh9f4fwjunn5kex")
                .setVerbosity(1)
                .build();

        stub.getAccount(accountRequest,
                new StreamObserver<ZarbProto.AccountResponse>() {
                    @Override
                    public void onNext(ZarbProto.AccountResponse value) {
                        Log.e(TAG, "GRPC Response: " + value.getJson());
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, "GRPC Error " + t.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "GRPC Completed");
                    }
                });
    }
}
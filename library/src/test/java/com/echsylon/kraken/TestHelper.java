package com.echsylon.kraken;

import android.content.Context;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.atlantis.Configuration;
import com.echsylon.atlantis.MockRequest;
import com.echsylon.atlantis.MockResponse;

import static org.mockito.Mockito.mock;

public class TestHelper {

    public static Kraken getKrakenInstance() {
        return getKrakenInstance(null, null);
    }

    public static Kraken getKrakenInstance(String key, String secret) {
        return new Kraken("http://localhost:8080", key, secret);
    }

    public static Atlantis startMockServer(String method, String url, String response) {
        Atlantis atlantis = new Atlantis(mock(Context.class),
                new Configuration.Builder()
                        .addRequest(new MockRequest.Builder()
                                .setMethod(method)
                                .setUrl(url)
                                .addResponse(new MockResponse.Builder()
                                        .addHeader("Content-Type", "application/json")
                                        .setStatus(200, "OK")
                                        .setBody(response)
                                        .build())
                                .build())
                        .build());

        atlantis.start();
        return atlantis;
    }

}

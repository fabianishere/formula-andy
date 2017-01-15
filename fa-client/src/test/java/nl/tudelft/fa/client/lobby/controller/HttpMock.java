package nl.tudelft.fa.client.lobby.controller;

import akka.NotUsed;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

import java.util.concurrent.CompletionStage;

public class HttpMock extends Http {
    private final Flow<HttpRequest, HttpResponse, NotUsed> flow;

    public HttpMock(Flow<HttpRequest, HttpResponse, NotUsed> flow) {
        super(null);
        this.flow = flow;
    }

    @Override
    public CompletionStage<HttpResponse> singleRequest(HttpRequest request, Materializer materializer) {
        return Source.single(request)
            .via(flow)
            .runWith(Sink.head(), materializer);
    }
}

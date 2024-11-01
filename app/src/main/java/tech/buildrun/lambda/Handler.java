

package tech.buildrun.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.buildrun.lambda.request.LoginRequest;
import tech.buildrun.lambda.request.LoginResponse;

import java.io.IOException;
import java.io.UncheckedIOException;

public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request,
                                                      Context context) {
        try {

            var loginRequest = mapper.readValue(request.getBody(), LoginRequest.class);

            boolean isAuthorized = loginRequest.username().equalsIgnoreCase("admin")
                    && loginRequest.password().equalsIgnoreCase("123");

            var loginResponse = new LoginResponse(isAuthorized);

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(mapper.writeValueAsString(loginResponse))
                    .withIsBase64Encoded(false);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void main(String[] args) {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        // request.setBody("{\"username\":\"admin\", \"password\":\"123\"}");
        
        // Teste sem um contexto real
        Context context = null;

        // Cria a instância do Handler e chama o método handleRequest
        Handler handler = new Handler();
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, null);

        // Exibe a resposta no console
        System.out.println("Resposta: " + response.getBody());
    }
}

package tech.buildrun.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Main <username> <password>");
            System.exit(1);
        }

        // Extrai os dados de username e password a partir dos argumentos passados no comando
        String username = args[0];
        String password = args[1];

        // Cria uma requisição com os dados reais
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        String body = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);
        request.setBody(body);

        // Cria um contexto (não precisamos de nada complexo aqui, o exemplo pode ser simplificado)
        Context context = new LambdaContext();

        // Cria uma instância do Handler
        Handler handler = new Handler();

        // Chama o método handleRequest com a requisição real
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);

        // Imprime a resposta
        System.out.println("Response: " + response.getBody());
    }

    // Classe Context simulada para fins de exemplo
    public static class LambdaContext implements Context {

        @Override
        public String getAwsRequestId() {
            return "sample-request-id";
        }

        @Override
        public String getLogGroupName() {
            return "/aws/lambda/sample-function";
        }

        @Override
        public String getLogStreamName() {
            return "log-stream";
        }

        @Override
        public String getFunctionName() {
            return "sample-function";
        }

        @Override
        public String getFunctionVersion() {
            return "$LATEST";
        }

        @Override
        public String getInvokedFunctionArn() {
            return "arn:aws:lambda:region:account-id:function:sample-function";
        }

        @Override
        public CognitoIdentity getCognitoIdentity() {
            return null;
        }

        @Override
        public ClientContext getClientContext() {
            return null;
        }

        @Override
        public int getRemainingTimeInMillis() {
            return 0;
        }

        @Override
        public int getMemoryLimitInMB() {
            return 0;
        }

        @Override
        public LambdaLogger getLogger() {
            return new LambdaLogger() {
                @Override
                public void log(String message) {
                    System.out.println(message);
                }
            };
        }
    }
}

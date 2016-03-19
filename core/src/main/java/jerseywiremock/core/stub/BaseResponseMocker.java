package jerseywiremock.core.stub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public abstract class BaseResponseMocker {
    protected final WireMockServer wireMockServer;
    protected final ObjectMapper objectMapper;
    protected final MappingBuilder mappingBuilder;
    protected final ResponseDefinitionBuilder responseDefinitionBuilder;

    public BaseResponseMocker(
            WireMockServer wireMockServer,
            ObjectMapper objectMapper,
            MappingBuilder mappingBuilder
    ) {
        this.wireMockServer = wireMockServer;
        this.objectMapper = objectMapper;
        this.mappingBuilder = mappingBuilder;

        responseDefinitionBuilder = aResponse().withHeader("Content-Type", "application/json");
    }

    public void stub() throws JsonProcessingException {
        amendResponseDefinition(responseDefinitionBuilder);

        wireMockServer.stubFor(mappingBuilder.willReturn(responseDefinitionBuilder));
    }

    protected abstract void amendResponseDefinition(ResponseDefinitionBuilder responseDefinitionBuilder) throws JsonProcessingException;
}
